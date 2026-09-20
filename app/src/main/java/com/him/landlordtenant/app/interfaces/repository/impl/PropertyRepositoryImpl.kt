package com.him.landlordtenant.app.interfaces.repository.impl

import android.util.Log
import com.him.landlordtenant.app.data.dao.ApartmentDao
import com.him.landlordtenant.app.data.dao.HouseDao
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.util.NetworkHelper
import com.google.firebase.functions.FirebaseFunctions
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
    private val firebaseFunctions: FirebaseFunctions,
    private val networkHelper: NetworkHelper
) : PropertyRepository {

    override suspend fun getProperty(propertyId: String): Result<PropertyDetailsData> = try {
        if (!networkHelper.isNetworkAvailable()) {
            Result.failure(Exception("Offline: Property not available in cache"))
        } else {
            // Try RTDB first as it's the primary sync source for current management
            val rtdbRef = firebaseDataSource.getReference("properties/$propertyId")
            val snapshot = rtdbRef.get().await()
            val property = snapshot.getValue(PropertyDetailsData::class.java)
            
            if (property != null) {
                Result.success(property)
            } else {
                // Fallback to Firestore
                firestoreDataSource.getData("properties", propertyId, PropertyDetailsData::class.java)
                    .map { it ?: throw Exception("Property not found in any source") }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPropertiesByOwner(ownerId: String): Result<List<PropertyDetailsData>> = try {
        Log.d("PropertyRepo", "Independent Fetch: Landlord properties for $ownerId")
        
        // Use a dedicated, non-indexed path for 100% reliability
        val ref = firebaseDataSource.getReference("landlord_properties/$ownerId")
        val snapshot = ref.get().await()
        
        val properties = snapshot.children.mapNotNull { child ->
            try {
                child.getValue(PropertyDetailsData::class.java)?.let { prop ->
                    if (prop.id.isEmpty()) prop.copy(id = child.key ?: "") else prop
                }
            } catch (e: Exception) {
                Log.e("PropertyRepo", "Mapping error for property ${child.key}: ${e.message}")
                null
            }
        }
        
        Log.d("PropertyRepo", "Independent Fetch found ${properties.size} properties")
        Result.success(properties)
    } catch (e: Exception) {
        Log.e("PropertyRepo", "Independent Fetch fatal error", e)
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

        // 1. Save to Public Properties (Secondary)
        firebaseDataSource.writeData("properties/$id", details)
        
        // 2. Save to Landlord Specific Collection (Primary for Dashboard)
        // This path is 100% reliable as it uses the ownerId as a key
        val rtdbResult = firebaseDataSource.writeData("landlord_properties/$ownerId/$id", details)
        
        // 3. Save to Firestore (Sync/Backup)
        firestoreDataSource.saveData("properties", id, details)
        
        if (rtdbResult.isSuccess) {
            Result.success(id)
        } else {
            Result.failure(rtdbResult.exceptionOrNull() ?: Exception("Failed to write to Realtime Database"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAvailableProperties(page: Int, pageSize: Int): Result<List<PropertyListingData>> = try {
        Log.d("PropertyRepo", "Fetching available properties")
        
        val rtdbList = try {
            val snapshot = firebaseDataSource.getReference("listings").get().await()
            snapshot.children.mapNotNull { child ->
                try {
                    child.getValue(MarketplacePropertyListingData::class.java)?.let { listing ->
                        if (listing.id.isEmpty()) listing.copy(id = child.key ?: "") else listing
                    }
                } catch (e: Exception) {
                    null
                }
            }.filter { it.status == ListingStatus.PUBLISHED }
        } catch (ex: Exception) {
            Log.e("PropertyRepo", "RTDB available properties fetch error: ${ex.message}")
            null
        }

        val firestoreList = try {
            val snapshot = firestoreDataSource.collection("listings")
                .whereEqualTo("status", ListingStatus.PUBLISHED.name)
                .get()
                .await()
            snapshot.toObjects(MarketplacePropertyListingData::class.java).mapIndexed { index, listing ->
                if (listing.id.isEmpty()) listing.copy(id = snapshot.documents[index].id) else listing
            }
        } catch (ex: Exception) {
            Log.e("PropertyRepo", "Firestore available properties fetch error: ${ex.message}")
            null
        }

        val combined = mutableListOf<MarketplacePropertyListingData>()
        rtdbList?.let { combined.addAll(it) }
        firestoreList?.let { combined.addAll(it) }

        val unique = combined.distinctBy { it.id.ifEmpty { it.title } }

        val listings = unique.map { listing ->
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
                    totalUnits = listing.totalUnits,
                    availableUnits = listing.availableUnits,
                    primaryImageUrl = listing.media.firstOrNull()?.fileUrl,
                    verified = listing.verified,
                    featured = listing.featured
                )
            }
        Log.d("PropertyRepo", "Returning ${listings.size} unique available properties")
        Result.success(listings)
    } catch (e: Exception) {
        Log.e("PropertyRepo", "getAvailableProperties general failure", e)
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
    override suspend fun updateProperty(ownerId: String, propertyId: String, property: PropertyCreateData): Result<Unit> = try {
        val updates = mutableMapOf<String, Any>()
        updates["name"] = property.name
        property.description?.let { updates["description"] = it }
        updates["address"] = property.address
        property.county?.let { updates["county"] = it }
        property.town?.let { updates["town"] = it }
        property.latitude?.let { updates["latitude"] = it }
        property.longitude?.let { updates["longitude"] = it }
        updates["totalUnits"] = property.totalUnits
        updates["startingRent"] = property.startingRent
        
        if (property.media.isNotEmpty()) {
            updates["media"] = property.media
        }
        
        if (property.amenities.isNotEmpty()) {
            updates["amenities"] = property.amenities.map { AmenityData(id = it, name = it) }
        }

        firebaseDataSource.updateData("properties/$propertyId", updates)
        firebaseDataSource.updateData("landlord_properties/$ownerId/$propertyId", updates)
        firestoreDataSource.saveData("properties", propertyId, updates)
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
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
    override suspend fun getUnits(propertyId: String): Result<List<PropertyUnitData>> = try {
        val snapshot = firebaseDataSource.getReference("properties/$propertyId/units").get().await()
        val list = snapshot.children.mapNotNull { it.getValue(PropertyUnitData::class.java) }
        Result.success(list)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override fun observeUnits(propertyId: String): Flow<Result<List<PropertyUnitData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getVacantUnits(propertyId: String): Result<List<PropertyUnitData>> = Result.failure(NotImplementedError())
    override suspend fun getOccupiedUnits(propertyId: String): Result<List<PropertyUnitData>> = Result.failure(NotImplementedError())
    override suspend fun uploadImage(ownerId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun uploadVideo(ownerId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun deleteMedia(ownerId: String, propertyId: String, mediaId: String): Result<Unit> = try {
        val data = hashMapOf(
            "publicId" to mediaId,
            "resourceType" to "image"
        )
        firebaseFunctions.getHttpsCallable("deleteCloudinaryMedia").call(data).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
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

    override suspend fun updateHouseStatus(
        apartmentId: String,
        floorId: String,
        houseId: String,
        status: com.him.landlordtenant.app.data.model.HouseStatus
    ): Result<Unit> = try {
        Log.d("PropertyRepo", "Updating house $houseId to $status")
        
        // 1. Update in Landlord Units (Reliable Source)
        val landlordUnitRef = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
        landlordUnitRef.child("status").setValue(status.name).await()
        
        // 2. Update in Public Properties
        val publicUnitRef = firebaseDataSource.getReference("properties/$apartmentId/units/$houseId")
        publicUnitRef.child("status").setValue(status.name).await()
        
        // 3. Re-calculate available units for the apartment
        val unitsSnapshot = firebaseDataSource.getReference("landlord_units/$apartmentId").get().await()
        var totalAvailable = 0
        unitsSnapshot.children.forEach { fSnapshot ->
            fSnapshot.children.forEach { uSnapshot ->
                val unitStatus = uSnapshot.child("status").getValue(String::class.java)
                if (unitStatus == com.him.landlordtenant.app.data.model.HouseStatus.VACANT.name) {
                    totalAvailable++
                }
            }
        }
        
        // Update total available in public property and landlord property
        val propSnapshot = firestoreDataSource.collection("properties").document(apartmentId).get().await()
        val ownerId = propSnapshot.getString("ownerId")
        if (ownerId != null) {
            firebaseDataSource.getReference("landlord_properties/$ownerId/$apartmentId/availableUnits").setValue(totalAvailable)
        }
        firebaseDataSource.getReference("properties/$apartmentId/availableUnits").setValue(totalAvailable)
        firebaseDataSource.getReference("listings/$apartmentId/availableUnits").setValue(totalAvailable)

        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("PropertyRepo", "Failed to update house status", e)
        Result.failure(e)
    }
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

    override suspend fun getLandlordViewings(landlordId: String): Result<List<PropertyViewingData>> = try {
        val snapshot = firestoreDataSource.collection("viewing_requests")
            .whereEqualTo("ownerId", landlordId)
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
