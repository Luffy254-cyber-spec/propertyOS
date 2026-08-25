package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PropertyListingRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : PropertyListingRepository {

    override suspend fun getListingsByOwner(ownerId: String): Result<List<MarketplacePropertyListingData>> = try {
        val snapshot = firestoreDataSource.collection("listings")
            .whereEqualTo("ownerId", ownerId)
            .get()
            .await()
        Result.success(snapshot.toObjects(MarketplacePropertyListingData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createListing(userId: String, listing: CreatePropertyListingData): Result<String> = try {
        val docRef = firestoreDataSource.collection("listings").document()
        val id = docRef.id
        val listingWithId = MarketplacePropertyListingData(
            id = id,
            propertyId = listing.propertyId,
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
            media = emptyList(),
            status = ListingStatus.PUBLISHED,
            verified = false,
            featured = false,
            views = 0,
            favorites = 0,
            createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        )
        // Add a 10-second timeout for the Firestore write
        kotlinx.coroutines.withTimeout(10000) {
            docRef.set(listingWithId).await()
        }
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getListing(listingId: String): Result<MarketplacePropertyListingData> = try {
        val snapshot = firestoreDataSource.collection("listings").document(listingId).get().await()
        val listing = snapshot.toObject(MarketplacePropertyListingData::class.java)
        if (listing != null) Result.success(listing) else Result.failure(Exception("Listing not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun searchListings(query: String, filters: ListingSearchFilters): Result<List<MarketplacePropertyListingData>> = try {
        val snapshot = firestoreDataSource.collection("listings")
            .whereEqualTo("status", ListingStatus.PUBLISHED.name)
            .get()
            .await()
        val all = snapshot.toObjects(MarketplacePropertyListingData::class.java)
        val filtered = all.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true) ||
            it.location.town.contains(query, ignoreCase = true)
        }
        Result.success(filtered)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeListing(listingId: String): Flow<Result<MarketplacePropertyListingData>> = flow {
        try {
            val snapshot = firestoreDataSource.collection("listings").document(listingId).get().await()
            val listing = snapshot.toObject(MarketplacePropertyListingData::class.java)
            if (listing != null) emit(Result.success(listing)) else emit(Result.failure(Exception("Not found")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateListing(userId: String, listingId: String, update: UpdatePropertyListingData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteListing(userId: String, listingId: String): Result<Unit> = Result.failure(NotImplementedError())
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
    override suspend fun getFeaturedListings(limit: Int): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getLatestListings(limit: Int): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getRecentlyCompletedProperties(limit: Int): Result<List<MarketplacePropertyListingData>> = Result.failure(NotImplementedError())
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
