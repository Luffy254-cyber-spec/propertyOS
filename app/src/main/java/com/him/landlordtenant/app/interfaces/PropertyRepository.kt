package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * PROPERTY REPOSITORY
 * =============================================================
 *
 * Handles property discovery, listings, units, media, location,
 * amenities, availability, favorites, sharing and viewings.
 *
 * This repository can serve:
 *
 * - Tenants searching for houses
 * - Guests browsing properties
 * - Landlords advertising properties
 * - Brokers managing listings
 * - Property managers managing units
 *
 * =============================================================
 */

interface PropertyRepository {

    /*
     * ---------------------------------------------------------
     * PROPERTY DISCOVERY
     * ---------------------------------------------------------
     */

    /**
     * Get publicly available properties.
     */
    suspend fun getAvailableProperties(
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Observe available properties.
     */
    fun observeAvailableProperties(): Flow<Result<List<PropertyListingData>>>

    /**
     * Search properties using free-text search.
     */
    suspend fun searchProperties(
        query: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Search by county.
     */
    suspend fun getPropertiesByCounty(
        county: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Search by town.
     */
    suspend fun getPropertiesByTown(
        town: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Find properties within a geographical radius.
     */
    suspend fun getNearbyProperties(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Result<List<PropertyListingData>>

    /**
     * Get featured properties.
     */
    suspend fun getFeaturedProperties(
        limit: Int = 10
    ): Result<List<PropertyListingData>>

    /**
     * Get recently listed properties.
     */
    suspend fun getRecentlyListedProperties(
        limit: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Get recently completed properties.
     */
    suspend fun getRecentlyCompletedProperties(
        limit: Int = 20
    ): Result<List<PropertyListingData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY DETAILS
     * ---------------------------------------------------------
     */

    /**
     * Get complete property details.
     */
    suspend fun getProperty(
        propertyId: String
    ): Result<PropertyDetailsData>

    /**
     * Observe a property's changes.
     */
    fun observeProperty(
        propertyId: String
    ): Flow<Result<PropertyDetailsData>>

    /**
     * Get similar properties.
     */
    suspend fun getSimilarProperties(
        propertyId: String,
        limit: Int = 10
    ): Result<List<PropertyListingData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY CREATION / MANAGEMENT
     * ---------------------------------------------------------
     */

    /**
     * Create a new property.
     */
    suspend fun createProperty(
        ownerId: String,
        property: PropertyCreateData
    ): Result<String>

    /**
     * Update property information.
     */
    suspend fun updateProperty(
        ownerId: String,
        propertyId: String,
        property: PropertyCreateData
    ): Result<Unit>

    /**
     * Delete a property.
     */
    suspend fun deleteProperty(
        ownerId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Publish a property listing.
     */
    suspend fun publishProperty(
        ownerId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Unpublish a property listing.
     */
    suspend fun unpublishProperty(
        ownerId: String,
        propertyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY STATUS
     * ---------------------------------------------------------
     */

    /**
     * Change property availability/status.
     */
    suspend fun updatePropertyStatus(
        ownerId: String,
        propertyId: String,
        status: String
    ): Result<Unit>

    /**
     * Mark property as available.
     */
    suspend fun markAsAvailable(
        ownerId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Mark property as occupied.
     */
    suspend fun markAsOccupied(
        ownerId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Mark property as under construction.
     */
    suspend fun markAsUnderConstruction(
        ownerId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Mark property as temporarily unavailable.
     */
    suspend fun markAsUnavailable(
        ownerId: String,
        propertyId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * UNITS
     * ---------------------------------------------------------
     */

    /**
     * Create a property unit.
     */
    suspend fun createUnit(
        ownerId: String,
        propertyId: String,
        unit: UnitCreateData
    ): Result<String>

    /**
     * Update unit information.
     */
    suspend fun updateUnit(
        ownerId: String,
        propertyId: String,
        unitId: String,
        unit: UnitCreateData
    ): Result<Unit>

    /**
     * Delete a unit.
     */
    suspend fun deleteUnit(
        ownerId: String,
        propertyId: String,
        unitId: String
    ): Result<Unit>

    /**
     * Get all units.
     */
    suspend fun getUnits(
        propertyId: String
    ): Result<List<PropertyUnitData>>

    /**
     * Observe units in real time.
     */
    fun observeUnits(
        propertyId: String
    ): Flow<Result<List<PropertyUnitData>>>

    /**
     * Get only vacant units.
     */
    suspend fun getVacantUnits(
        propertyId: String
    ): Result<List<PropertyUnitData>>

    /**
     * Get only occupied units.
     */
    suspend fun getOccupiedUnits(
        propertyId: String
    ): Result<List<PropertyUnitData>>


    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    /**
     * Upload property image.
     */
    suspend fun uploadImage(
        ownerId: String,
        propertyId: String,
        filePath: String
    ): Result<String>

    /**
     * Upload property video.
     */
    suspend fun uploadVideo(
        ownerId: String,
        propertyId: String,
        filePath: String
    ): Result<String>

    /**
     * Delete property media.
     */
    suspend fun deleteMedia(
        ownerId: String,
        propertyId: String,
        mediaId: String
    ): Result<Unit>

    /**
     * Reorder property media.
     */
    suspend fun reorderMedia(
        ownerId: String,
        propertyId: String,
        mediaIds: List<String>
    ): Result<Unit>

    /**
     * Set the primary property image.
     */
    suspend fun setPrimaryImage(
        ownerId: String,
        propertyId: String,
        mediaId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * LOCATION / MAPS
     * ---------------------------------------------------------
     */

    /**
     * Update property coordinates.
     */
    suspend fun updateLocation(
        ownerId: String,
        propertyId: String,
        latitude: Double,
        longitude: Double
    ): Result<Unit>

    /**
     * Get property location.
     */
    suspend fun getLocation(
        propertyId: String
    ): Result<PropertyLocationData>

    /**
     * Search properties around a map point.
     */
    suspend fun searchWithinBounds(
        northEastLatitude: Double,
        northEastLongitude: Double,
        southWestLatitude: Double,
        southWestLongitude: Double
    ): Result<List<PropertyListingData>>

    /**
     * Get directions metadata for a property.
     */
    suspend fun getDirections(
        propertyId: String,
        fromLatitude: Double,
        fromLongitude: Double
    ): Result<DirectionsData>


    /*
     * ---------------------------------------------------------
     * AMENITIES
     * ---------------------------------------------------------
     */

    /**
     * Get property amenities.
     */
    suspend fun getAmenities(
        propertyId: String
    ): Result<List<AmenityData>>

    /**
     * Add an amenity.
     */
    suspend fun addAmenity(
        ownerId: String,
        propertyId: String,
        amenity: AmenityData
    ): Result<Unit>

    /**
     * Remove an amenity.
     */
    suspend fun removeAmenity(
        ownerId: String,
        propertyId: String,
        amenityId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SEARCH FILTERS
     * ---------------------------------------------------------
     */

    /**
     * Advanced property search.
     */
    suspend fun filterProperties(
        filter: PropertyFilterData,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>>

    /**
     * Get available property types.
     */
    suspend fun getPropertyTypes(): Result<List<String>>

    /**
     * Get available counties.
     */
    suspend fun getCounties(): Result<List<String>>

    /**
     * Get towns for a county.
     */
    suspend fun getTowns(
        county: String
    ): Result<List<String>>

    /**
     * Get available amenities for filtering.
     */
    suspend fun getAvailableAmenities(): Result<List<AmenityData>>


    /*
     * ---------------------------------------------------------
     * FAVORITES
     * ---------------------------------------------------------
     */

    /**
     * Save property to user's favorites.
     */
    suspend fun saveProperty(
        userId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Remove property from favorites.
     */
    suspend fun removeSavedProperty(
        userId: String,
        propertyId: String
    ): Result<Unit>

    /**
     * Check whether property is saved.
     */
    suspend fun isPropertySaved(
        userId: String,
        propertyId: String
    ): Result<Boolean>

    /**
     * Get saved properties.
     */
    suspend fun getSavedProperties(
        userId: String
    ): Result<List<PropertyListingData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY SHARING
     * ---------------------------------------------------------
     */

    /**
     * Record a property share.
     */
    suspend fun recordShare(
        propertyId: String,
        userId: String?
    ): Result<Unit>

    /**
     * Generate/share property link.
     */
    suspend fun generateShareLink(
        propertyId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * VIEWINGS
     * ---------------------------------------------------------
     */

    /**
     * Request a property viewing.
     */
    suspend fun requestViewing(
        requesterId: String,
        propertyId: String,
        request: PropertyViewingRequestData
    ): Result<String>

    /**
     * Get viewing requests for a property.
     */
    suspend fun getPropertyViewings(
        propertyId: String
    ): Result<List<PropertyViewingData>>

    /**
     * Get viewings requested by a user.
     */
    suspend fun getUserViewings(
        userId: String
    ): Result<List<PropertyViewingData>>

    /**
     * Cancel a viewing request.
     */
    suspend fun cancelViewing(
        requesterId: String,
        viewingId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY REPORTING
     * ---------------------------------------------------------
     */

    /**
     * Report an incorrect or suspicious listing.
     */
    suspend fun reportProperty(
        userId: String,
        propertyId: String,
        report: PropertyReportData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    /**
     * Record a property view.
     */
    suspend fun recordPropertyView(
        propertyId: String,
        userId: String?
    ): Result<Unit>

    /**
     * Get listing analytics.
     */
    suspend fun getPropertyAnalytics(
        ownerId: String,
        propertyId: String
    ): Result<PropertyAnalyticsData>
}


/*
 * =============================================================
 * PROPERTY DATA CONTRACTS
 * =============================================================
 */

data class PropertyCreateData(
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
    val amenities: List<String> = emptyList()
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
    val availableUnits: Int,
    val primaryImageUrl: String?,
    val verified: Boolean,
    val featured: Boolean
)

data class PropertyDetailsData(
    val id: String,
    val name: String,
    val description: String?,
    val propertyType: String,
    val address: String,
    val county: String?,
    val town: String?,
    val latitude: Double?,
    val longitude: Double?,
    val startingRent: Double,
    val totalUnits: Int,
    val availableUnits: Int,
    val occupiedUnits: Int,
    val status: String,
    val verified: Boolean,
    val ownerName: String?,
    val ownerVerified: Boolean,
    val media: List<PropertyMediaData>,
    val amenities: List<AmenityData>,
    val units: List<PropertyUnitData>
)

data class PropertyUnitData(
    val id: String,
    val name: String,
    val floor: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val sizeSquareMeters: Double?,
    val monthlyRent: Double,
    val deposit: Double?,
    val furnished: Boolean,
    val available: Boolean,
    val tenantId: String?
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
    val id: String,
    val url: String,
    val type: PropertyMediaType,
    val title: String?,
    val isPrimary: Boolean,
    val order: Int
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
    val id: String,
    val name: String,
    val icon: String?
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
    val id: String,
    val propertyId: String,
    val propertyName: String,
    val requesterId: String,
    val requesterName: String,
    val requestedDate: String,
    val requestedTime: String,
    val status: String
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