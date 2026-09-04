package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.*
import com.google.firebase.firestore.toObjects
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PropertyListingRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : PropertyListingRepository {

    override suspend fun getListingsByOwner(ownerId: String): Result<List<MarketplacePropertyListingData>> = try {
        Log.d("PropertyListingRepo", "Fetching listings for owner: $ownerId")
        
        // Fetch from RTDB
        val rtdbList = try {
            val rtdbRef = firebaseDataSource.getReference("listings")
            val snapshot = rtdbRef.orderByChild("ownerId").equalTo(ownerId).get().await()
            Log.d("PropertyListingRepo", "RTDB found ${snapshot.childrenCount} listings")
            snapshot.children.mapNotNull { child ->
                try {
                    child.getValue(MarketplacePropertyListingData::class.java)
                } catch (e: Exception) {
                    Log.e("PropertyListingRepo", "Failed to map RTDB listing ${child.key}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("PropertyListingRepo", "RTDB listings fetch failed: ${e.message}")
            null
        }
        
        // Fetch from Firestore
        val firestoreList = try {
            val snapshot = firestoreDataSource.collection("listings").whereEqualTo("ownerId", ownerId).get().await()
            Log.d("PropertyListingRepo", "Firestore found ${snapshot.size()} listings")
            snapshot.toObjects(MarketplacePropertyListingData::class.java)
        } catch (e: Exception) {
            Log.e("PropertyListingRepo", "Firestore listings fetch failed: ${e.message}")
            null
        }
        
        val combined = mutableListOf<MarketplacePropertyListingData>()
        rtdbList?.let { combined.addAll(it) }
        firestoreList?.let { combined.addAll(it) }
        
        val unique = combined.distinctBy { it.id.ifEmpty { it.title } }
        Log.d("PropertyListingRepo", "Returning ${unique.size} unique listings")
        
        if (rtdbList == null && firestoreList == null) {
            Result.failure(Exception("Failed to fetch listings from all sources"))
        } else {
            Result.success(unique)
        }
    } catch (e: Exception) {
        Log.e("PropertyListingRepo", "getListingsByOwner general failure", e)
        Result.failure(e)
    }

    override suspend fun createListing(userId: String, listing: CreatePropertyListingData): Result<String> = try {
        val id = listing.propertyId ?: firestoreDataSource.collection("listings").document().id
        val listingWithId = MarketplacePropertyListingData(
            id = id,
            propertyId = id, // Ensure propertyId is same as id
            unitId = listing.unitId,
            ownerId = userId,
            brokerId = listing.brokerId,
            title = listing.title,
            description = listing.description,
            listingType = listing.listingType,
            propertyType = listing.propertyType,
            monthlyRent = listing.monthlyRent,
            salePrice = listing.salePrice,
            depositAmount = listing.depositAmount,
            bedrooms = listing.bedrooms,
            bathrooms = listing.bathrooms,
            totalUnits = listing.totalUnits,
            availableUnits = listing.availableUnits,
            availableFrom = listing.availableFrom,
            amenities = listing.amenities,
            location = listing.location,
            media = listing.media,
            status = ListingStatus.PUBLISHED,
            verified = false,
            featured = false,
            views = 0,
            favorites = 0,
            createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        )

        // Try Firestore first as requested if RTDB is an issue
        val firestoreResult = firestoreDataSource.saveData("listings", id, listingWithId)
        
        // Also try Realtime Database
        val rtdbResult = try {
            firebaseDataSource.writeData("listings/$id", listingWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
        
        if (firestoreResult.isSuccess || rtdbResult.isSuccess) {
            Result.success(id)
        } else {
            Result.failure(firestoreResult.exceptionOrNull() ?: Exception("Failed to save to both Firestore and RTDB"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getListing(listingId: String): Result<MarketplacePropertyListingData> = try {
        // Try Realtime Database first
        val snapshot = firebaseDataSource.getReference("listings/$listingId").get().await()
        val listing = snapshot.getValue(MarketplacePropertyListingData::class.java)
        
        if (listing != null) {
            Result.success(listing)
        } else {
            // Fallback to Firestore
            val firestoreSnapshot = firestoreDataSource.collection("listings").document(listingId).get().await()
            val firestoreListing = firestoreSnapshot.toObject(MarketplacePropertyListingData::class.java)
            if (firestoreListing != null) Result.success(firestoreListing) else Result.failure(Exception("Listing not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun searchListings(query: String, filters: ListingSearchFilters): Result<List<MarketplacePropertyListingData>> = try {
        // Try Realtime Database
        val snapshot = firebaseDataSource.getReference("listings")
            .get()
            .await()
        
        val all = snapshot.children.mapNotNull { it.getValue(MarketplacePropertyListingData::class.java) }
        val filtered = all.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true) ||
            it.location.town.contains(query, ignoreCase = true)
        }
        
        if (filtered.isNotEmpty()) {
            Result.success(filtered)
        } else {
            // Fallback to Firestore
            val firestoreSnapshot = firestoreDataSource.collection("listings")
                .whereEqualTo("status", ListingStatus.PUBLISHED.name)
                .get()
                .await()
            val firestoreAll = firestoreSnapshot.toObjects(MarketplacePropertyListingData::class.java)
            val firestoreFiltered = firestoreAll.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true) ||
                it.location.town.contains(query, ignoreCase = true)
            }
            Result.success(firestoreFiltered)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeListing(listingId: String): Flow<Result<MarketplacePropertyListingData>> = flow {
        try {
            // Check Realtime Database first
            val snapshot = firebaseDataSource.getReference("listings/$listingId").get().await()
            val listing = snapshot.getValue(MarketplacePropertyListingData::class.java)
            
            if (listing != null) {
                emit(Result.success(listing))
            } else {
                // Fallback to Firestore
                val firestoreSnapshot = firestoreDataSource.collection("listings").document(listingId).get().await()
                val firestoreListing = firestoreSnapshot.toObject(MarketplacePropertyListingData::class.java)
                if (firestoreListing != null) emit(Result.success(firestoreListing)) else emit(Result.failure(Exception("Not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateListing(userId: String, listingId: String, update: UpdatePropertyListingData): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("listings/$listingId")
        val snapshot = ref.get().await()
        val current = snapshot.getValue(MarketplacePropertyListingData::class.java) ?: throw Exception("Listing not found")
        
        if (current.ownerId != userId) throw Exception("Unauthorized")
        
        val updated = current.copy(
            title = update.title ?: current.title,
            description = update.description ?: current.description,
            monthlyRent = update.monthlyRent ?: current.monthlyRent,
            status = update.status ?: current.status
        )
        
        firebaseDataSource.writeData("listings/$listingId", updated)
        firestoreDataSource.saveData("listings", listingId, updated)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun deleteListing(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())

    override suspend fun isTitleTaken(title: String): Result<Boolean> = try {
        val snapshot = firestoreDataSource.collection("listings")
            .whereEqualTo("title", title)
            .limit(1)
            .get()
            .await()
        if (!snapshot.isEmpty) {
            Result.success(true)
        } else {
            // Fallback to RTDB
            val rtdbRef = firebaseDataSource.getReference("listings")
            val rtdbSnapshot = rtdbRef.orderByChild("title").equalTo(title).limitToFirst(1).get().await()
            Result.success(rtdbSnapshot.exists())
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun publishListing(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun unpublishListing(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun pauseListing(userId: String, listingId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun resumeListing(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsRented(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsUnavailable(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun searchByLocation(latitude: Double, longitude: Double, radiusKm: Double, filters: ListingSearchFilters): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun searchByCounty(county: String, filters: ListingSearchFilters): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun searchByTown(town: String, filters: ListingSearchFilters): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getNearbyListings(latitude: Double, longitude: Double, radiusKm: Double): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getFeaturedListings(limit: Int): Result<List<MarketplacePropertyListingData>> = try {
        val snapshot = firebaseDataSource.getReference("listings")
            .orderByChild("createdAt")
            .limitToLast(limit * 2) // Fetch more to allow for filtering
            .get()
            .await()
        
        val listings = snapshot.children.mapNotNull { it.getValue(MarketplacePropertyListingData::class.java) }
            .filter { it.status == ListingStatus.PUBLISHED }
            .sortedByDescending { it.createdAt }
            .take(limit)
            
        Result.success(listings)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLatestListings(limit: Int): Result<List<MarketplacePropertyListingData>> = getFeaturedListings(limit)

    override suspend fun getRecentlyCompletedProperties(limit: Int): Result<List<MarketplacePropertyListingData>> = getFeaturedListings(limit)
    override suspend fun getAvailableUnits(propertyId: String): Result<List<PropertyUnitListingData>> = Result.failure(NotImplementedError())
    override suspend fun getListingsByBroker(brokerId: String): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getListingsByProperty(propertyId: String): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun addPhoto(userId: String, listingId: String, photo: ListingMediaData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun addVideo(userId: String, listingId: String, video: ListingMediaData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun removeMedia(userId: String, listingId: String, mediaId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun reorderMedia(userId: String, listingId: String, mediaIds: List<String>): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun setPrimaryMedia(userId: String, listingId: String, mediaId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getListingMedia(listingId: String): Result<List<ListingMediaData>> = Result.failure(NotImplementedError())
    override suspend fun addAmenity(userId: String, listingId: String, amenity: ListingAmenity): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeAmenity(userId: String, listingId: String, amenity: ListingAmenity): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getAmenities(listingId: String): Result<List<ListingAmenity>> = Result.failure(NotImplementedError())
    override suspend fun updateLocation(userId: String, listingId: String, location: ListingLocationData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getLocation(listingId: String): Result<ListingLocationData> = Result.failure(NotImplementedError())
    override suspend fun updateDirections(userId: String, listingId: String, directions: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun addFavorite(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeFavorite(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun isFavorite(userId: String, listingId: String): Result<Boolean> = Result.failure(NotImplementedError())
    override suspend fun getFavorites(userId: String): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun createSavedSearch(userId: String, search: CreateSavedSearchData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateSavedSearch(userId: String, searchId: String, update: UpdateSavedSearchData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteSavedSearch(userId: String, searchId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getSavedSearches(userId: String): Result<List<SavedSearchData>> = Result.failure(NotImplementedError())
    override suspend fun createInquiry(userId: String, inquiry: CreateListingInquiryData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getListingInquiries(listingId: String, userId: String): Result<List<ListingInquiryData>> = Result.failure(NotImplementedError())
    override suspend fun respondToInquiry(userId: String, inquiryId: String, response: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun closeInquiry(userId: String, inquiryId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun requestViewing(userId: String, viewing: CreateListingViewingData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getListingViewings(listingId: String): Result<List<ListingViewingData>> = Result.failure(NotImplementedError())
    override suspend fun cancelViewing(userId: String, viewingId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun assignBroker(userId: String, listingId: String, brokerId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeBroker(userId: String, listingId: String, brokerId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getListingBrokers(listingId: String): Result<List<ListingBrokerData>> = Result.failure(NotImplementedError())
    override suspend fun promoteListing(userId: String, listingId: String, promotion: CreateListingPromotionData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun cancelPromotion(userId: String, promotionId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getActivePromotions(listingId: String): Result<List<ListingPromotionData>> = Result.failure(NotImplementedError())
    override suspend fun recordListingView(listingId: String, userId: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun recordListingShare(listingId: String, userId: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getListingAnalytics(listingId: String): Result<MarketplaceListingAnalyticsData> = Result.failure(NotImplementedError())
    override suspend fun reportListing(userId: String, listingId: String, reason: ListingReportReason, description: String?): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getListingReports(listingId: String): Result<List<ListingReportData>> = Result.failure(NotImplementedError())
    override suspend fun approveListing(adminId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rejectListing(adminId: String, listingId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun featureListing(adminId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeFeaturedStatus(adminId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
}
