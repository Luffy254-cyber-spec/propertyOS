package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getAvailableProperties(page: Int = 1, pageSize: Int = 20): Result<List<PropertyListingData>>
    fun observeAvailableProperties(): Flow<Result<List<PropertyListingData>>>
    suspend fun searchProperties(query: String, page: Int = 1, pageSize: Int = 20): Result<List<PropertyListingData>>
    suspend fun getPropertiesByCounty(county: String, page: Int = 1, pageSize: Int = 20): Result<List<PropertyListingData>>
    suspend fun getPropertiesByTown(town: String, page: Int = 1, pageSize: Int = 20): Result<List<PropertyListingData>>
    suspend fun getNearbyProperties(latitude: Double, longitude: Double, radiusKm: Double = 10.0): Result<List<PropertyListingData>>
    suspend fun getFeaturedProperties(limit: Int = 10): Result<List<PropertyListingData>>
    suspend fun getRecentlyListedProperties(limit: Int = 20): Result<List<PropertyListingData>>
    suspend fun getRecentlyCompletedProperties(limit: Int = 20): Result<List<PropertyListingData>>

    suspend fun getProperty(propertyId: String): Result<PropertyDetailsData>
    suspend fun getPropertiesByOwner(ownerId: String): Result<List<PropertyDetailsData>>
    suspend fun isPropertyNameTaken(name: String): Result<Boolean>
    fun observeProperty(propertyId: String): Flow<Result<PropertyDetailsData>>
    suspend fun getSimilarProperties(propertyId: String, limit: Int = 10): Result<List<PropertyListingData>>

    suspend fun createProperty(ownerId: String, property: PropertyCreateData): Result<String>
    suspend fun updateProperty(ownerId: String, propertyId: String, property: PropertyCreateData): Result<Unit>
    suspend fun deleteProperty(ownerId: String, propertyId: String): Result<Unit>
    suspend fun publishProperty(ownerId: String, propertyId: String): Result<Unit>
    suspend fun unpublishProperty(ownerId: String, propertyId: String): Result<Unit>

    suspend fun updatePropertyStatus(ownerId: String, propertyId: String, status: String): Result<Unit>
    suspend fun markAsAvailable(ownerId: String, propertyId: String): Result<Unit>
    suspend fun markAsOccupied(ownerId: String, propertyId: String): Result<Unit>
    suspend fun markAsUnderConstruction(ownerId: String, propertyId: String): Result<Unit>
    suspend fun markAsUnavailable(ownerId: String, propertyId: String, reason: String?): Result<Unit>

    suspend fun updateHouseStatus(apartmentId: String, floorId: String, houseId: String, status: com.him.landlordtenant.app.data.model.HouseStatus): Result<Unit>

    suspend fun createUnit(ownerId: String, propertyId: String, unit: UnitCreateData): Result<String>
    suspend fun updateUnit(ownerId: String, propertyId: String, unitId: String, unit: UnitCreateData): Result<Unit>
    suspend fun deleteUnit(ownerId: String, propertyId: String, unitId: String): Result<Unit>
    suspend fun getUnits(propertyId: String): Result<List<PropertyUnitData>>
    fun observeUnits(propertyId: String): Flow<Result<List<PropertyUnitData>>>
    suspend fun getVacantUnits(propertyId: String): Result<List<PropertyUnitData>>
    suspend fun getOccupiedUnits(propertyId: String): Result<List<PropertyUnitData>>

    suspend fun uploadImage(ownerId: String, propertyId: String, filePath: String): Result<String>
    suspend fun uploadVideo(ownerId: String, propertyId: String, filePath: String): Result<String>
    suspend fun deleteMedia(ownerId: String, propertyId: String, mediaId: String): Result<Unit>
    suspend fun reorderMedia(ownerId: String, propertyId: String, mediaIds: List<String>): Result<Unit>
    suspend fun setPrimaryImage(ownerId: String, propertyId: String, mediaId: String): Result<Unit>

    suspend fun updateLocation(ownerId: String, propertyId: String, latitude: Double, longitude: Double): Result<Unit>
    suspend fun getLocation(propertyId: String): Result<PropertyLocationData>
    suspend fun searchWithinBounds(northEastLatitude: Double, northEastLongitude: Double, southWestLatitude: Double, southWestLongitude: Double): Result<List<PropertyListingData>>
    suspend fun getDirections(propertyId: String, fromLatitude: Double, fromLongitude: Double): Result<DirectionsData>

    suspend fun getAmenities(propertyId: String): Result<List<AmenityData>>
    suspend fun addAmenity(ownerId: String, propertyId: String, amenity: AmenityData): Result<Unit>
    suspend fun removeAmenity(ownerId: String, propertyId: String, amenityId: String): Result<Unit>

    suspend fun filterProperties(filter: PropertyFilterData, page: Int = 1, pageSize: Int = 20): Result<List<PropertyListingData>>
    suspend fun getPropertyTypes(): Result<List<String>>
    suspend fun getCounties(): Result<List<String>>
    suspend fun getTowns(county: String): Result<List<String>>
    suspend fun getAvailableAmenities(): Result<List<AmenityData>>

    suspend fun saveProperty(userId: String, propertyId: String): Result<Unit>
    suspend fun removeSavedProperty(userId: String, propertyId: String): Result<Unit>
    suspend fun isPropertySaved(userId: String, propertyId: String): Result<Boolean>
    suspend fun getSavedProperties(userId: String): Result<List<PropertyListingData>>

    suspend fun recordShare(propertyId: String, userId: String?): Result<Unit>
    suspend fun generateShareLink(propertyId: String): Result<String>

    suspend fun requestViewing(requesterId: String, propertyId: String, request: PropertyViewingRequestData): Result<String>
    suspend fun getPropertyViewings(propertyId: String): Result<List<PropertyViewingData>>
    suspend fun getLandlordViewings(landlordId: String): Result<List<PropertyViewingData>>
    suspend fun getUserViewings(userId: String): Result<List<PropertyViewingData>>
    suspend fun cancelViewing(requesterId: String, viewingId: String, reason: String?): Result<Unit>

    suspend fun reportProperty(userId: String, propertyId: String, report: PropertyReportData): Result<String>

    suspend fun recordPropertyView(propertyId: String, userId: String?): Result<Unit>
    suspend fun getPropertyAnalytics(ownerId: String, propertyId: String): Result<PropertyAnalyticsData>
}

data class PropertyCreateData(
    val id: String? = null,
    val name: String,
    val description: String?,
    val propertyType: String,
    val address: String,
    val county: String?,
    val town: String?,
    val latitude: Double?,
    val longitude: Double?,
    val totalUnits: Int,
    val startingRent: Double,
    val amenities: List<String> = emptyList(),
    val media: List<PropertyMediaData> = emptyList()
)

data class PropertyListingData(
    val id: String,
    val name: String,
    val propertyType: String,
    val location: String,
    val county: String?,
    val town: String?,
    val latitude: Double?,
    val longitude: Double?,
    val startingRent: Double,
    val totalUnits: Int,
    val availableUnits: Int,
    val primaryImageUrl: String?,
    val verified: Boolean,
    val featured: Boolean
)

data class PropertyDetailsData(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val description: String? = null,
    val propertyType: String = "",
    val address: String = "",
    val county: String? = null,
    val town: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val startingRent: Double = 0.0,
    val totalUnits: Int = 0,
    val availableUnits: Int = 0,
    val occupiedUnits: Int = 0,
    val status: String = "",
    val verified: Boolean = false,
    val ownerName: String? = null,
    val ownerVerified: Boolean = false,

    @get:com.google.firebase.database.Exclude
    val media: List<PropertyMediaData> = emptyList(),
    @set:com.google.firebase.database.PropertyName("media")
    @get:com.google.firebase.database.PropertyName("media")
    var mediaRaw: Any? = null,

    @get:com.google.firebase.database.Exclude
    val amenities: List<AmenityData> = emptyList(),
    @set:com.google.firebase.database.PropertyName("amenities")
    @get:com.google.firebase.database.PropertyName("amenities")
    var amenitiesRaw: Any? = null,

    @get:com.google.firebase.database.Exclude
    val units: List<PropertyUnitData> = emptyList(),
    @set:com.google.firebase.database.PropertyName("units")
    @get:com.google.firebase.database.PropertyName("units")
    var unitsRaw: Any? = null,

    @get:com.google.firebase.database.Exclude
    val floors: List<PropertyFloorData> = emptyList(),
    @set:com.google.firebase.database.PropertyName("floors")
    @get:com.google.firebase.database.PropertyName("floors")
    var floorsRaw: Any? = null
) {
    fun getMediaList(): List<PropertyMediaData> {
        val raw = mediaRaw ?: media
        return when (raw) {
            is List<*> -> raw.filterIsInstance<PropertyMediaData>()
            is Map<*, *> -> raw.values.mapNotNull { 
                if (it is Map<*, *>) {
                    try {
                        PropertyMediaData(
                            id = it["id"] as? String ?: "",
                            url = it["url"] as? String ?: "",
                            type = try { PropertyMediaType.valueOf(it["type"] as? String ?: "IMAGE") } catch(e: Exception) { PropertyMediaType.IMAGE }
                        )
                    } catch (e: Exception) { null }
                } else null
            }
            else -> emptyList()
        }
    }

    fun getAmenitiesList(): List<AmenityData> {
        val raw = amenitiesRaw ?: amenities
        return when (raw) {
            is List<*> -> raw.filterIsInstance<AmenityData>()
            is Map<*, *> -> raw.values.mapNotNull { 
                if (it is Map<*, *>) {
                    AmenityData(
                        id = it["id"] as? String ?: "",
                        name = it["name"] as? String ?: ""
                    )
                } else null
            }
            else -> emptyList()
        }
    }

    fun getUnitsList(): List<PropertyUnitData> {
        val raw = unitsRaw ?: units
        return when (raw) {
            is List<*> -> raw.filterIsInstance<PropertyUnitData>()
            is Map<*, *> -> raw.values.mapNotNull { if (it is PropertyUnitData) it else null } // Simplified
            else -> emptyList()
        }
    }
}

data class PropertyFloorData(
    val id: String = "",
    val number: Int = 0,
    val name: String = "",
    val unitCount: Int = 0,
    val createdAt: Long = 0L
)

data class PropertyUnitData(
    val id: String = "",
    val name: String = "",
    val floor: String? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val sizeSquareMeters: Double? = null,
    val monthlyRent: Double = 0.0,
    val deposit: Double? = null,
    val furnished: Boolean = false,
    val available: Boolean = true,
    val tenantId: String? = null
)

data class UnitCreateData(
    val name: String,
    val floor: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val sizeSquareMeters: Double?,
    val monthlyRent: Double,
    val deposit: Double?,
    val furnished: Boolean
)

data class PropertyMediaData(
    val id: String = "",
    val url: String = "",
    val type: PropertyMediaType = PropertyMediaType.IMAGE,
    val title: String? = null,
    val isPrimary: Boolean = false,
    val order: Int = 0
)

enum class PropertyMediaType {
    IMAGE,
    VIDEO
}

data class PropertyLocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val county: String?,
    val town: String?
)

data class DirectionsData(
    val distanceKm: Double,
    val estimatedMinutes: Int,
    val routeUrl: String?
)

data class AmenityData(
    val id: String = "",
    val name: String = "",
    val icon: String? = null
)

data class PropertyFilterData(
    val query: String? = null,
    val county: String? = null,
    val town: String? = null,
    val propertyType: String? = null,
    val minRent: Double? = null,
    val maxRent: Double? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val furnished: Boolean? = null,
    val amenities: List<String> = emptyList(),
    val verifiedOnly: Boolean = false,
    val availableOnly: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radiusKm: Double? = null
)

data class PropertyViewingRequestData(
    val preferredDate: String,
    val preferredTime: String,
    val alternativeDate: String?,
    val alternativeTime: String?,
    val message: String?
)

data class PropertyViewingData(
    val id: String = "",
    val propertyId: String = "",
    val propertyName: String = "",
    val ownerId: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val requestedDate: String = "",
    val requestedTime: String = "",
    val status: String = "Pending"
)

data class PropertyReportData(
    val reason: String,
    val description: String?
)

data class PropertyAnalyticsData(
    val totalViews: Long,
    val uniqueViews: Long,
    val totalShares: Long,
    val totalViewingRequests: Long,
    val favoriteCount: Long,
    val conversionRate: Double
)
