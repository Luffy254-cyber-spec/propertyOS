package com.him.landlordtenant.app.interfaces.repository.impl

import android.util.Log
import com.him.landlordtenant.app.data.dao.ApartmentDao
import com.him.landlordtenant.app.data.dao.HouseDao
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PropertyRepositoryImpl @Inject constructor(
    private val apartmentDao: ApartmentDao,
    private val houseDao: HouseDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkHelper: NetworkHelper
) : PropertyRepository {

    override suspend fun getProperty(propertyId: String): Result<PropertyDetailsData> {
        if (!networkHelper.isNetworkAvailable()) {
            // Try local cache (e.g. from HouseDao or ApartmentDao if applicable)
            // For now, if offline and not in cache, return error
            return Result.failure(Exception("Offline: Property not available in cache"))
        }
        return firestoreDataSource.getData("properties", propertyId, PropertyDetailsData::class.java)
            .map { it ?: throw Exception("Property not found") }
    }

    override suspend fun getPropertiesByOwner(ownerId: String): Result<List<PropertyDetailsData>> = try {
        Log.d("PropertyRepo", "Fetching properties for owner: $ownerId")
        
        // Fetch from Firestore
        val firestoreList = try {
            val snapshot = firestoreDataSource.collection("properties")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()
            Log.d("PropertyRepo", "Firestore found ${snapshot.size()} properties")
            snapshot.toObjects(PropertyDetailsData::class.java).mapIndexed { index, prop ->
                // Ensure ID is present if not mapped
                if (prop.id.isEmpty()) prop.copy(id = snapshot.documents[index].id) else prop
            }
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Firestore properties fetch failed: ${e.message}")
            null 
        }

        // Fetch from RTDB
        val rtdbList = try {
            val rtdbRef = firebaseDataSource.getReference("properties")
            // Attempt query by ownerId
            val snapshot = rtdbRef.orderByChild("ownerId").equalTo(ownerId).get().await()
            Log.d("PropertyRepo", "RTDB found ${snapshot.childrenCount} properties")
            
            if (snapshot.exists()) {
                snapshot.children.mapNotNull { child ->
                    try {
                        child.getValue(PropertyDetailsData::class.java)?.let { prop ->
                            if (prop.id.isEmpty()) prop.copy(id = child.key ?: "") else prop
                        }
                    } catch (e: Exception) {
                        Log.e("PropertyRepo", "Failed to map RTDB property ${child.key}: ${e.message}")
                        null
                    }
                }
            } else {
                // If query returns nothing, double check with a manual filter as a fallback (resilience)
                val allSnapshot = rtdbRef.get().await()
                allSnapshot.children.mapNotNull { child ->
                    val pOwnerId = child.child("ownerId").getValue(String::class.java)
                    if (pOwnerId == ownerId) {
                        child.getValue(PropertyDetailsData::class.java)?.let { prop ->
                            if (prop.id.isEmpty()) prop.copy(id = child.key ?: "") else prop
                        }
                    } else null
                }
            }
        } catch (e: Exception) {
            Log.e("PropertyRepo", "RTDB properties fetch failed: ${e.message}")
            null
        }

        val combined = mutableListOf<PropertyDetailsData>()
        firestoreList?.let { combined.addAll(it) }
        rtdbList?.let { combined.addAll(it) }
        
        val unique = combined.distinctBy { it.id.ifEmpty { it.name } }
        Log.d("PropertyRepo", "Returning ${unique.size} unique properties for dashboard")
        
        if (firestoreList == null && rtdbList == null) {
            Result.failure(Exception("Critical: Failed to fetch properties from all database sources"))
        } else {
            Result.success(unique)
        }
    } catch (e: Exception) {
        Log.e("PropertyRepo", "getPropertiesByOwner fatal error", e)
        Result.failure(e)
    }

    override suspend fun isPropertyNameTaken(name: String): Result<Boolean> = try {
        val snapshot = firestoreDataSource.collection("properties")
            .whereEqualTo("name", name)
            .limit(1)
            .get()
            .await()
        Result.success(!snapshot.isEmpty)
    } catch (e: Exception) {
        // Fallback to RTDB if needed
        try {
            val rtdbRef = firebaseDataSource.getReference("properties")
            val rtdbSnapshot = rtdbRef.orderByChild("name").equalTo(name).limitToFirst(1).get().await()
            Result.success(rtdbSnapshot.exists())
        } catch (rtdbEx: Exception) {
            Result.failure(e)
        }
    }

    override fun observeProperty(propertyId: String): Flow<Result<PropertyDetailsData>> = flow {
        emit(getProperty(propertyId))
    }

    override suspend fun createProperty(ownerId: String, property: PropertyCreateData): Result<String> = try {
        val id = property.id ?: firestoreDataSource.collection("properties").document().id
        
        // Create a model object to ensure perfect mapping
        val details = PropertyDetailsData(
            id = id,
            ownerId = ownerId,
            name = property.name,
            description = property.description,
            propertyType = property.propertyType,
            address = property.address,
            county = property.county,
            town = property.town,
            latitude = property.latitude,
            longitude = property.longitude,
            startingRent = property.startingRent,
            totalUnits = property.totalUnits,
            availableUnits = property.totalUnits,
            occupiedUnits = 0,
            status = "AVAILABLE",
            verified = false,
            media = property.media.map { it.copy(id = it.id.ifEmpty { java.util.UUID.randomUUID().toString() }) },
            amenities = property.amenities.map { AmenityData(id = it, name = it) }
        )

        // Save to Firestore (Primary)
        val firestoreResult = firestoreDataSource.saveData("properties", id, details)
        
        // Save to RTDB (Secondary)
        var rtdbSuccess = false
        try {
            Log.d("PropertyRepo", "Writing property to RTDB: properties/$id")
            val rtdbResult = firebaseDataSource.writeData("properties/$id", details)
            if (rtdbResult.isSuccess) {
                Log.d("PropertyRepo", "RTDB property save successful")
                rtdbSuccess = true
            } else {
                Log.e("PropertyRepo", "RTDB property save failed: ${rtdbResult.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            Log.e("PropertyRepo", "RTDB property save exception: ${e.message}")
        }
        
        if (firestoreResult.isSuccess || rtdbSuccess) Result.success(id) 
        else Result.failure(firestoreResult.exceptionOrNull() ?: Exception("Failed to save property to both sources"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAvailableProperties(page: Int, pageSize: Int): Result<List<PropertyListingData>> = try {
        val snapshot = firebaseDataSource.getReference("listings")
            .get()
            .await()
        
        val listings = snapshot.children.mapNotNull { it.getValue(MarketplacePropertyListingData::class.java) }
            .filter { it.status == ListingStatus.PUBLISHED }
            .map { listing ->
                PropertyListingData(
                    id = listing.id,
                    name = listing.title,
                    propertyType = listing.propertyType.name,
                    location = listing.location.town,
                    county = listing.location.county,
                    town = listing.location.town,
                    latitude = listing.location.latitude,
                    longitude = listing.location.longitude,
                    startingRent = listing.monthlyRent ?: 0.0,
                    availableUnits = listing.availableUnits,
                    primaryImageUrl = listing.media.firstOrNull()?.fileUrl,
                    verified = listing.verified,
                    featured = listing.featured
                )
            }
        Result.success(listings)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeAvailableProperties(): Flow<Result<List<PropertyListingData>>> = flow { 
        emit(getAvailableProperties(1, 50))
    }

    override suspend fun getFeaturedProperties(limit: Int): Result<List<PropertyListingData>> = try {
        getAvailableProperties(1, limit).map { it.filter { p -> p.featured } }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun searchProperties(query: String, page: Int, pageSize: Int): Result<List<PropertyListingData>> = try {
        getAvailableProperties(page, pageSize).map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) || (it.town?.contains(query, ignoreCase = true) ?: false) }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPropertiesByCounty(county: String, page: Int, pageSize: Int): Result<List<PropertyListingData>> = try {
        getAvailableProperties(page, pageSize).map { it.filter { p -> p.county == county } }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPropertiesByTown(town: String, page: Int, pageSize: Int): Result<List<PropertyListingData>> = try {
        getAvailableProperties(page, pageSize).map { it.filter { p -> p.town == town } }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getNearbyProperties(latitude: Double, longitude: Double, radiusKm: Double): Result<List<PropertyListingData>> = try {
        getAvailableProperties(1, 100) // Search nearby implementation would go here
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getRecentlyListedProperties(limit: Int): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getRecentlyCompletedProperties(limit: Int): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getSimilarProperties(propertyId: String, limit: Int): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun updateProperty(ownerId: String, propertyId: String, property: PropertyCreateData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteProperty(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun publishProperty(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun unpublishProperty(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun updatePropertyStatus(ownerId: String, propertyId: String, status: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsAvailable(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsOccupied(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsUnderConstruction(ownerId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAsUnavailable(ownerId: String, propertyId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun createUnit(ownerId: String, propertyId: String, unit: UnitCreateData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateUnit(ownerId: String, propertyId: String, unitId: String, unit: UnitCreateData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteUnit(ownerId: String, propertyId: String, unitId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getUnits(propertyId: String): Result<List<PropertyUnitData>> = Result.failure(NotImplementedError())
    override fun observeUnits(propertyId: String): Flow<Result<List<PropertyUnitData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getVacantUnits(propertyId: String): Result<List<PropertyUnitData>> = Result.failure(NotImplementedError())
    override suspend fun getOccupiedUnits(propertyId: String): Result<List<PropertyUnitData>> = Result.failure(NotImplementedError())
    override suspend fun uploadImage(ownerId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun uploadVideo(ownerId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun deleteMedia(ownerId: String, propertyId: String, mediaId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun reorderMedia(ownerId: String, propertyId: String, mediaIds: List<String>): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun setPrimaryImage(ownerId: String, propertyId: String, mediaId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun updateLocation(ownerId: String, propertyId: String, latitude: Double, longitude: Double): Result<Unit> = try {
        val updates = mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )
        firebaseDataSource.getReference("properties/$propertyId").updateChildren(updates).await()
        firebaseDataSource.getReference("listings/$propertyId/location").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getLocation(propertyId: String): Result<PropertyLocationData> = Result.failure(NotImplementedError())
    override suspend fun searchWithinBounds(northEastLatitude: Double, northEastLongitude: Double, southWestLatitude: Double, southWestLongitude: Double): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getDirections(propertyId: String, fromLatitude: Double, fromLongitude: Double): Result<DirectionsData> = Result.failure(NotImplementedError())
    override suspend fun getAmenities(propertyId: String): Result<List<AmenityData>> = Result.failure(NotImplementedError())
    override suspend fun addAmenity(ownerId: String, propertyId: String, amenity: AmenityData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeAmenity(ownerId: String, propertyId: String, amenityId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun filterProperties(filter: PropertyFilterData, page: Int, pageSize: Int): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun getPropertyTypes(): Result<List<String>> = Result.failure(NotImplementedError())
    override suspend fun getCounties(): Result<List<String>> = Result.failure(NotImplementedError())
    override suspend fun getTowns(county: String): Result<List<String>> = Result.failure(NotImplementedError())
    override suspend fun getAvailableAmenities(): Result<List<AmenityData>> = Result.failure(NotImplementedError())
    override suspend fun saveProperty(userId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeSavedProperty(userId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun isPropertySaved(userId: String, propertyId: String): Result<Boolean> = Result.failure(NotImplementedError())
    override suspend fun getSavedProperties(userId: String): Result<List<PropertyListingData>> = Result.failure(NotImplementedError())
    override suspend fun recordShare(propertyId: String, userId: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun generateShareLink(propertyId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun requestViewing(requesterId: String, propertyId: String, request: PropertyViewingRequestData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getPropertyViewings(propertyId: String): Result<List<PropertyViewingData>> = try {
        val snapshot = firestoreDataSource.collection("viewing_requests")
            .whereEqualTo("propertyId", propertyId)
            .get()
            .await()
        Result.success(snapshot.toObjects(PropertyViewingData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserViewings(userId: String): Result<List<PropertyViewingData>> = try {
        val snapshot = firestoreDataSource.collection("viewing_requests")
            .whereEqualTo("requesterId", userId)
            .get()
            .await()
        Result.success(snapshot.toObjects(PropertyViewingData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun cancelViewing(requesterId: String, viewingId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun reportProperty(userId: String, propertyId: String, report: PropertyReportData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun recordPropertyView(propertyId: String, userId: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getPropertyAnalytics(ownerId: String, propertyId: String): Result<PropertyAnalyticsData> = try {
        // Fetch views from RTDB or indexed collection
        val viewsSnapshot = firebaseDataSource.getReference("analytics/properties/$propertyId/views").get().await()
        val totalViews = viewsSnapshot.childrenCount
        
        val viewingRequests = firestoreDataSource.collection("viewing_requests")
            .whereEqualTo("propertyId", propertyId)
            .get()
            .await()
            
        Result.success(PropertyAnalyticsData(
            totalViews = totalViews,
            uniqueViews = totalViews, // Placeholder for real unique logic
            totalShares = 0,
            totalViewingRequests = viewingRequests.size().toLong(),
            favoriteCount = 0,
            conversionRate = if (totalViews > 0) viewingRequests.size().toDouble() / totalViews * 100 else 0.0
        ))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
