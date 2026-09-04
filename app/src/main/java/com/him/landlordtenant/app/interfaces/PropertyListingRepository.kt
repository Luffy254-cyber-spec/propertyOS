package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * PROPERTY LISTING REPOSITORY
 * =============================================================
 *
 * Public property marketplace.
 *
 * Handles:
 *
 * - Vacant houses
 * - Available units
 * - Newly completed properties
 * - Property advertisements
 * - Property photos
 * - Property videos
 * - Pricing
 * - Amenities
 * - Location
 * - Map coordinates
 * - Search
 * - Filters
 * - Favorites
 * - Listing applications
 * - Broker listings
 * - Property promotion
 * - Listing analytics
 * - Listing status
 * - Listing expiration
 * - Viewing requests
 *
 * =============================================================
 */

interface PropertyListingRepository {

    /*
     * ---------------------------------------------------------
     * LISTINGS
     * ---------------------------------------------------------
     */

    suspend fun createListing(
        userId: String,
        listing: CreatePropertyListingData
    ): Result<String>

    suspend fun getListing(
        listingId: String
    ): Result<MarketplacePropertyListingData>

    fun observeListing(
        listingId: String
    ): Flow<Result<MarketplacePropertyListingData>>

    suspend fun updateListing(
        userId: String,
        listingId: String,
        update: UpdatePropertyListingData
    ): Result<Unit>

    suspend fun deleteListing(
        userId: String,
        listingId: String
    ): Result<Unit>

    /**
     * Check if a listing title is already taken.
     */
    suspend fun isTitleTaken(
        title: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * PUBLISHING
     * ---------------------------------------------------------
     */

    suspend fun publishListing(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun unpublishListing(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun pauseListing(
        userId: String,
        listingId: String,
        reason: String?
    ): Result<Unit>

    suspend fun resumeListing(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun markAsRented(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun markAsUnavailable(
        userId: String,
        listingId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchListings(
        query: String,
        filters: ListingSearchFilters = ListingSearchFilters()
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun searchByLocation(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
        filters: ListingSearchFilters = ListingSearchFilters()
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun searchByCounty(
        county: String,
        filters: ListingSearchFilters = ListingSearchFilters()
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun searchByTown(
        town: String,
        filters: ListingSearchFilters = ListingSearchFilters()
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getNearbyListings(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Result<List<MarketplacePropertyListingData>>


    /*
     * ---------------------------------------------------------
     * DISCOVERY
     * ---------------------------------------------------------
     */

    suspend fun getFeaturedListings(
        limit: Int = 20
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getLatestListings(
        limit: Int = 20
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getRecentlyCompletedProperties(
        limit: Int = 20
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getAvailableUnits(
        propertyId: String
    ): Result<List<PropertyUnitListingData>>

    suspend fun getListingsByOwner(
        ownerId: String
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getListingsByBroker(
        brokerId: String
    ): Result<List<MarketplacePropertyListingData>>

    suspend fun getListingsByProperty(
        propertyId: String
    ): Result<List<MarketplacePropertyListingData>>


    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    suspend fun addPhoto(
        userId: String,
        listingId: String,
        photo: ListingMediaData
    ): Result<String>

    suspend fun addVideo(
        userId: String,
        listingId: String,
        video: ListingMediaData
    ): Result<String>

    suspend fun removeMedia(
        userId: String,
        listingId: String,
        mediaId: String
    ): Result<Unit>

    suspend fun reorderMedia(
        userId: String,
        listingId: String,
        mediaIds: List<String>
    ): Result<Unit>

    suspend fun setPrimaryMedia(
        userId: String,
        listingId: String,
        mediaId: String
    ): Result<Unit>

    suspend fun getListingMedia(
        listingId: String
    ): Result<List<ListingMediaData>>


    /*
     * ---------------------------------------------------------
     * AMENITIES
     * ---------------------------------------------------------
     */

    suspend fun addAmenity(
        userId: String,
        listingId: String,
        amenity: ListingAmenity
    ): Result<Unit>

    suspend fun removeAmenity(
        userId: String,
        listingId: String,
        amenity: ListingAmenity
    ): Result<Unit>

    suspend fun getAmenities(
        listingId: String
    ): Result<List<ListingAmenity>>


    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
     */

    suspend fun updateLocation(
        userId: String,
        listingId: String,
        location: ListingLocationData
    ): Result<Unit>

    suspend fun getLocation(
        listingId: String
    ): Result<ListingLocationData>

    suspend fun updateDirections(
        userId: String,
        listingId: String,
        directions: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * FAVORITES
     * ---------------------------------------------------------
     */

    suspend fun addFavorite(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun removeFavorite(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun isFavorite(
        userId: String,
        listingId: String
    ): Result<Boolean>

    suspend fun getFavorites(
        userId: String
    ): Result<List<MarketplacePropertyListingData>>


    /*
     * ---------------------------------------------------------
     * SAVED SEARCHES
     * ---------------------------------------------------------
     */

    suspend fun createSavedSearch(
        userId: String,
        search: CreateSavedSearchData
    ): Result<String>

    suspend fun updateSavedSearch(
        userId: String,
        searchId: String,
        update: UpdateSavedSearchData
    ): Result<Unit>

    suspend fun deleteSavedSearch(
        userId: String,
        searchId: String
    ): Result<Unit>

    suspend fun getSavedSearches(
        userId: String
    ): Result<List<SavedSearchData>>


    /*
     * ---------------------------------------------------------
     * LISTING INQUIRIES
     * ---------------------------------------------------------
     */

    suspend fun createInquiry(
        userId: String,
        inquiry: CreateListingInquiryData
    ): Result<String>

    suspend fun getListingInquiries(
        listingId: String,
        userId: String
    ): Result<List<ListingInquiryData>>

    suspend fun respondToInquiry(
        userId: String,
        inquiryId: String,
        response: String
    ): Result<Unit>

    suspend fun closeInquiry(
        userId: String,
        inquiryId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY VIEWING
     * ---------------------------------------------------------
     */

    suspend fun requestViewing(
        userId: String,
        viewing: CreateListingViewingData
    ): Result<String>

    suspend fun getListingViewings(
        listingId: String
    ): Result<List<ListingViewingData>>

    suspend fun cancelViewing(
        userId: String,
        viewingId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BROKER
     * ---------------------------------------------------------
     */

    suspend fun assignBroker(
        userId: String,
        listingId: String,
        brokerId: String
    ): Result<Unit>

    suspend fun removeBroker(
        userId: String,
        listingId: String,
        brokerId: String
    ): Result<Unit>

    suspend fun getListingBrokers(
        listingId: String
    ): Result<List<ListingBrokerData>>


    /*
     * ---------------------------------------------------------
     * PROMOTION
     * ---------------------------------------------------------
     */

    suspend fun promoteListing(
        userId: String,
        listingId: String,
        promotion: CreateListingPromotionData
    ): Result<String>

    suspend fun cancelPromotion(
        userId: String,
        promotionId: String
    ): Result<Unit>

    suspend fun getActivePromotions(
        listingId: String
    ): Result<List<ListingPromotionData>>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun recordListingView(
        listingId: String,
        userId: String?
    ): Result<Unit>

    suspend fun recordListingShare(
        listingId: String,
        userId: String?
    ): Result<Unit>

    suspend fun getListingAnalytics(
        listingId: String
    ): Result<MarketplaceListingAnalyticsData>


    /*
     * ---------------------------------------------------------
     * REPORTING
     * ---------------------------------------------------------
     */

    suspend fun reportListing(
        userId: String,
        listingId: String,
        reason: ListingReportReason,
        description: String?
    ): Result<String>

    suspend fun getListingReports(
        listingId: String
    ): Result<List<ListingReportData>>


    /*
     * ---------------------------------------------------------
     * ADMIN MODERATION
     * ---------------------------------------------------------
     */

    suspend fun approveListing(
        adminId: String,
        listingId: String
    ): Result<Unit>

    suspend fun rejectListing(
        adminId: String,
        listingId: String,
        reason: String
    ): Result<Unit>

    suspend fun featureListing(
        adminId: String,
        listingId: String
    ): Result<Unit>

    suspend fun removeFeaturedStatus(
        adminId: String,
        listingId: String
    ): Result<Unit>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreatePropertyListingData(
    val propertyId: String = "",
    val unitId: String? = null,
    val ownerId: String = "",
    val brokerId: String? = null,
    val title: String = "",
    val description: String = "",
    val listingType: ListingType = ListingType.RENT,
    val propertyType: PropertyType = PropertyType.APARTMENT,
    val monthlyRent: Double? = null,
    val salePrice: Double? = null,
    val depositAmount: Double? = null,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val totalUnits: Int = 0,
    val availableUnits: Int = 0,
    val availableFrom: String = "",
    val amenities: List<ListingAmenity> = emptyList(),
    val media: List<ListingMediaData> = emptyList(),
    val location: ListingLocationData = ListingLocationData()
)

data class UpdatePropertyListingData(
    val title: String? = null,
    val description: String? = null,
    val monthlyRent: Double? = null,
    val salePrice: Double? = null,
    val depositAmount: Double? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val availableFrom: String? = null,
    val status: ListingStatus? = null
)

data class MarketplacePropertyListingData(
    val id: String = "",
    val propertyId: String = "",
    val unitId: String? = null,
    val ownerId: String = "",
    val brokerId: String? = null,
    val title: String = "",
    val description: String = "",
    val listingType: ListingType = ListingType.RENT,
    val propertyType: PropertyType = PropertyType.APARTMENT,
    val monthlyRent: Double? = null,
    val salePrice: Double? = null,
    val depositAmount: Double? = null,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val totalUnits: Int = 0,
    val availableUnits: Int = 0,
    val availableFrom: String = "",
    val amenities: List<ListingAmenity> = emptyList(),
    val location: ListingLocationData = ListingLocationData(),
    val media: List<ListingMediaData> = emptyList(),
    val status: ListingStatus = ListingStatus.PUBLISHED,
    val verified: Boolean = false,
    val featured: Boolean = false,
    val views: Int = 0,
    val favorites: Int = 0,
    val createdAt: String = ""
)

data class PropertyUnitListingData(
    val unitId: String = "",
    val unitNumber: String = "",
    val floor: String? = null,
    val monthlyRent: Double? = null,
    val salePrice: Double? = null,
    val depositAmount: Double? = null,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val available: Boolean = true
)

data class ListingMediaData(
    val id: String = "",
    val type: ListingMediaType = ListingMediaType.IMAGE,
    val fileUrl: String = "",
    val thumbnailUrl: String? = null,
    val caption: String? = null,
    val order: Int = 0,
    val isPrimary: Boolean = false
)

data class ListingLocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val county: String = "",
    val town: String = "",
    val estate: String? = null,
    val address: String? = null,
    val directions: String? = null
)

data class ListingSearchFilters(
    val listingType: ListingType? = null,
    val propertyType: PropertyType? = null,
    val minRent: Double? = null,
    val maxRent: Double? = null,
    val minBedrooms: Int? = null,
    val maxBedrooms: Int? = null,
    val minBathrooms: Int? = null,
    val maxBathrooms: Int? = null,
    val amenities: List<ListingAmenity> = emptyList(),
    val verifiedOnly: Boolean = false,
    val featuredOnly: Boolean = false,
    val availableOnly: Boolean = true,
    val sortBy: ListingSort = ListingSort.NEWEST
)

data class CreateSavedSearchData(
    val name: String,
    val query: String?,
    val filters: ListingSearchFilters,
    val notifyWhenMatched: Boolean = true
)

data class UpdateSavedSearchData(
    val name: String?,
    val query: String?,
    val filters: ListingSearchFilters?,
    val notifyWhenMatched: Boolean?
)

data class SavedSearchData(
    val id: String,
    val userId: String,
    val name: String,
    val query: String?,
    val filters: ListingSearchFilters,
    val notifyWhenMatched: Boolean,
    val createdAt: String
)

data class CreateListingInquiryData(
    val listingId: String,
    val message: String,
    val phoneNumber: String?
)

data class ListingInquiryData(
    val id: String,
    val listingId: String,
    val senderId: String,
    val message: String,
    val phoneNumber: String?,
    val response: String?,
    val status: InquiryStatus,
    val createdAt: String
)

data class CreateListingViewingData(
    val listingId: String,
    val preferredDate: String,
    val preferredTime: String,
    val message: String?
)

data class ListingViewingData(
    val id: String,
    val listingId: String,
    val requesterId: String,
    val date: String,
    val time: String,
    val status: ListingViewingStatus,
    val message: String?
)

data class ListingBrokerData(
    val brokerId: String,
    val brokerName: String,
    val verified: Boolean,
    val phoneNumber: String?,
    val assignedAt: String
)

data class CreateListingPromotionData(
    val type: PromotionType,
    val startDate: String,
    val endDate: String
)

data class ListingPromotionData(
    val id: String,
    val listingId: String,
    val type: PromotionType,
    val startDate: String,
    val endDate: String,
    val active: Boolean
)

data class MarketplaceListingAnalyticsData(
    val listingId: String,
    val totalViews: Int,
    val uniqueViews: Int,
    val favorites: Int,
    val inquiries: Int,
    val viewingRequests: Int,
    val shares: Int,
    val conversionRate: Double
)

data class ListingReportData(
    val id: String,
    val listingId: String,
    val reporterId: String,
    val reason: ListingReportReason,
    val description: String?,
    val status: ListingReportStatus,
    val createdAt: String
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ListingType {

    RENT,

    SALE,

    RENT_TO_OWN,

    SHORT_TERM
}

enum class PropertyType {

    APARTMENT,

    HOUSE,

    BEDSITTER,

    STUDIO,

    ONE_BEDROOM,

    TWO_BEDROOM,

    THREE_BEDROOM,

    FOUR_BEDROOM,

    FIVE_PLUS_BEDROOM,

    TOWNHOUSE,

    VILLA,

    COMMERCIAL,

    OFFICE,

    SHOP,

    LAND,

    OTHER
}

enum class ListingStatus {

    DRAFT,

    PENDING_APPROVAL,

    PUBLISHED,

    PAUSED,

    RESERVED,

    RENTED,

    SOLD,

    UNAVAILABLE,

    EXPIRED,

    REJECTED
}

enum class ListingMediaType {

    IMAGE,

    VIDEO,

    VIRTUAL_TOUR
}

enum class ListingAmenity {

    PARKING,

    SECURITY,

    CCTV,

    GATED_COMPOUND,

    WATER,

    BOREHOLE,

    BACKUP_WATER,

    ELECTRICITY,

    BACKUP_GENERATOR,

    SOLAR,

    WIFI,

    FIBER,

    BALCONY,

    GARDEN,

    SWIMMING_POOL,

    GYM,

    ELEVATOR,

    PLAYGROUND,

    LAUNDRY,

    SERVANT_QUARTERS,

    PET_FRIENDLY,

    FURNISHED,

    HOT_SHOWER,

    DSTV,

    INTERNET,

    OWN_COMPOUND,

    NEAR_PUBLIC_TRANSPORT
}

enum class ListingSort {

    NEWEST,

    OLDEST,

    LOWEST_PRICE,

    HIGHEST_PRICE,

    MOST_VIEWED,

    MOST_FAVORITED,

    NEAREST
}

enum class InquiryStatus {

    NEW,

    RESPONDED,

    CONVERTED,

    CLOSED
}

enum class ListingViewingStatus {

    REQUESTED,

    CONFIRMED,

    RESCHEDULED,

    COMPLETED,

    CANCELLED,

    NO_SHOW
}

enum class PromotionType {

    FEATURED,

    TOP_SEARCH,

    HOMEPAGE,

    PREMIUM,

    URGENT
}

enum class ListingReportReason {

    FAKE_PROPERTY,

    WRONG_PRICE,

    PROPERTY_NOT_AVAILABLE,

    FRAUD,

    DUPLICATE_LISTING,

    MISLEADING_PHOTOS,

    WRONG_LOCATION,

    SCAM,

    OTHER
}

enum class ListingReportStatus {

    PENDING,

    REVIEWING,

    RESOLVED,

    DISMISSED
}