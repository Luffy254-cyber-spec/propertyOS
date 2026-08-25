package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * SEARCH REPOSITORY
 * =============================================================
 *
 * Centralized search and discovery engine.
 *
 * Supports:
 *
 * - Property search
 * - Tenant search
 * - Landlord search
 * - Broker/agent search
 * - Technician search
 * - Service-provider search
 * - Location/radius search
 * - Advanced filters
 * - Sorting
 * - Search history
 * - Trending searches
 * - Saved searches
 * - Search suggestions
 * - Recommendations
 * - Nearby services
 * - Verified-only search
 *
 * =============================================================
 */

interface SearchRepository {

    /*
     * ---------------------------------------------------------
     * GLOBAL SEARCH
     * ---------------------------------------------------------
     */

    suspend fun globalSearch(
        userId: String?,
        query: String,
        filters: GlobalSearchFilters = GlobalSearchFilters()
    ): Result<GlobalSearchResult>

    suspend fun search(
        request: SearchRequest
    ): Result<SearchResult>


    /*
     * ---------------------------------------------------------
     * PROPERTY SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchProperties(
        query: String,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>>

    suspend fun searchPropertiesNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>>

    suspend fun searchPropertiesByCounty(
        county: String,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>>

    suspend fun searchPropertiesByTown(
        town: String,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>>

    suspend fun searchPropertiesByEstate(
        estate: String,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>>


    /*
     * ---------------------------------------------------------
     * LANDLORD SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchLandlords(
        query: String,
        filters: UserSearchFilters = UserSearchFilters()
    ): Result<List<SearchUserData>>

    suspend fun getVerifiedLandlords(
        location: String? = null
    ): Result<List<SearchUserData>>


    /*
     * ---------------------------------------------------------
     * BROKER / AGENT SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchBrokers(
        query: String,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>>

    suspend fun searchBrokersNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>>


    /*
     * ---------------------------------------------------------
     * TECHNICIAN / PROFESSIONAL SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchProfessionals(
        query: String,
        type: ProfessionalSearchType?,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>>

    suspend fun searchProfessionalsNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
        type: ProfessionalSearchType? = null,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>>

    suspend fun searchTechnicians(
        query: String,
        specialization: ProfessionalSearchType?,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>>


    /*
     * ---------------------------------------------------------
     * SEARCH SUGGESTIONS
     * ---------------------------------------------------------
     */

    suspend fun getSearchSuggestions(
        query: String,
        type: SearchSuggestionType? = null,
        limit: Int = 10
    ): Result<List<SearchSuggestionData>>

    suspend fun getLocationSuggestions(
        query: String,
        limit: Int = 10
    ): Result<List<LocationSuggestionData>>

    suspend fun getPopularSearches(
        location: String? = null,
        limit: Int = 10
    ): Result<List<PopularSearchData>>


    /*
     * ---------------------------------------------------------
     * SEARCH HISTORY
     * ---------------------------------------------------------
     */

    suspend fun saveSearchHistory(
        userId: String,
        search: SearchHistoryData
    ): Result<String>

    suspend fun getSearchHistory(
        userId: String,
        limit: Int = 20
    ): Result<List<SearchHistoryData>>

    suspend fun deleteSearchHistory(
        userId: String,
        historyId: String
    ): Result<Unit>

    suspend fun clearSearchHistory(
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SAVED SEARCHES
     * ---------------------------------------------------------
     */

    suspend fun saveSearch(
        userId: String,
        search: CreateSearchData
    ): Result<String>

    suspend fun updateSavedSearch(
        userId: String,
        searchId: String,
        update: UpdateSearchData
    ): Result<Unit>

    suspend fun deleteSavedSearch(
        userId: String,
        searchId: String
    ): Result<Unit>

    suspend fun getSavedSearches(
        userId: String
    ): Result<List<SavedSearchResultData>>


    /*
     * ---------------------------------------------------------
     * RECOMMENDATIONS
     * ---------------------------------------------------------
     */

    suspend fun getRecommendedProperties(
        userId: String,
        limit: Int = 20
    ): Result<List<SearchPropertyData>>

    suspend fun getRecommendedProfessionals(
        userId: String,
        type: ProfessionalSearchType?,
        limit: Int = 20
    ): Result<List<SearchProfessionalData>>

    suspend fun getRecommendedBrokers(
        userId: String,
        limit: Int = 20
    ): Result<List<SearchProfessionalData>>


    /*
     * ---------------------------------------------------------
     * NEARBY SERVICES
     * ---------------------------------------------------------
     */

    suspend fun getNearbyServices(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
        serviceType: ProfessionalSearchType? = null
    ): Result<List<SearchProfessionalData>>


    /*
     * ---------------------------------------------------------
     * TRENDING
     * ---------------------------------------------------------
     */

    suspend fun getTrendingProperties(
        location: String? = null,
        limit: Int = 20
    ): Result<List<SearchPropertyData>>

    suspend fun getTrendingLocations(
        limit: Int = 20
    ): Result<List<LocationSuggestionData>>


    /*
     * ---------------------------------------------------------
     * SEARCH ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun recordSearch(
        userId: String?,
        query: String,
        type: SearchType
    ): Result<Unit>

    suspend fun recordSearchClick(
        userId: String?,
        resultId: String,
        type: SearchResultType
    ): Result<Unit>

    suspend fun getSearchAnalytics(
        startDate: String,
        endDate: String
    ): Result<SearchAnalyticsData>


    /*
     * ---------------------------------------------------------
     * FILTER METADATA
     * ---------------------------------------------------------
     */

    suspend fun getAvailablePropertyTypes(): Result<List<String>>

    suspend fun getAvailableAmenities(): Result<List<String>>

    suspend fun getAvailableProfessionalTypes(): Result<List<String>>

    suspend fun getAvailableLocations(): Result<List<LocationSuggestionData>>
}


/*
 * =============================================================
 * REQUEST / FILTER DATA
 * =============================================================
 */

data class SearchRequest(
    val userId: String?,
    val query: String,
    val type: SearchType,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radiusKm: Double? = null,
    val page: Int = 1,
    val pageSize: Int = 20,
    val sort: SearchSort = SearchSort.RELEVANCE
)

data class GlobalSearchFilters(
    val verifiedOnly: Boolean = false,
    val availableOnly: Boolean = true,
    val location: String? = null
)

data class PropertySearchFilters(
    val minRent: Double? = null,
    val maxRent: Double? = null,
    val minSalePrice: Double? = null,
    val maxSalePrice: Double? = null,
    val minBedrooms: Int? = null,
    val maxBedrooms: Int? = null,
    val minBathrooms: Int? = null,
    val maxBathrooms: Int? = null,
    val propertyTypes: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val county: String? = null,
    val town: String? = null,
    val estate: String? = null,
    val verifiedOnly: Boolean = false,
    val furnishedOnly: Boolean = false,
    val availableOnly: Boolean = true,
    val sort: SearchSort = SearchSort.RELEVANCE
)

data class UserSearchFilters(
    val verifiedOnly: Boolean = false,
    val role: SearchUserRole? = null,
    val location: String? = null
)

data class ProfessionalSearchFilters(
    val verifiedOnly: Boolean = false,
    val certificationRequired: Boolean = false,
    val minimumRating: Double? = null,
    val minimumExperienceYears: Int? = null,
    val location: String? = null,
    val availableNow: Boolean = false,
    val sort: SearchSort = SearchSort.RELEVANCE
)

data class CreateSearchData(
    val name: String,
    val query: String,
    val type: SearchType,
    val filters: String?,
    val notifyOnNewResults: Boolean = true
)

data class UpdateSearchData(
    val name: String?,
    val query: String?,
    val filters: String?,
    val notifyOnNewResults: Boolean?
)


/*
 * =============================================================
 * SEARCH RESULTS
 * =============================================================
 */

data class SearchResult(
    val query: String,
    val type: SearchType,
    val totalResults: Int,
    val page: Int,
    val pageSize: Int,
    val results: List<SearchResultItem>
)

data class GlobalSearchResult(
    val query: String,
    val properties: List<SearchPropertyData>,
    val professionals: List<SearchProfessionalData>,
    val landlords: List<SearchUserData>,
    val brokers: List<SearchProfessionalData>,
    val totalResults: Int
)

data class SearchResultItem(
    val id: String,
    val type: SearchResultType,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val verified: Boolean,
    val distanceKm: Double?
)

data class SearchPropertyData(
    val id: String,
    val propertyId: String,
    val title: String,
    val propertyType: String,
    val monthlyRent: Double?,
    val salePrice: Double?,
    val bedrooms: Int,
    val bathrooms: Int,
    val imageUrl: String?,
    val county: String,
    val town: String,
    val estate: String?,
    val latitude: Double?,
    val longitude: Double?,
    val verified: Boolean,
    val featured: Boolean,
    val rating: Double?,
    val distanceKm: Double?
)

data class SearchUserData(
    val id: String,
    val name: String,
    val role: SearchUserRole,
    val profileImageUrl: String?,
    val phoneNumber: String?,
    val location: String?,
    val verified: Boolean,
    val rating: Double?
)

data class SearchProfessionalData(
    val id: String,
    val name: String,
    val profession: ProfessionalSearchType,
    val profileImageUrl: String?,
    val phoneNumber: String?,
    val location: String?,
    val verified: Boolean,
    val certified: Boolean,
    val yearsOfExperience: Int?,
    val rating: Double?,
    val reviewCount: Int,
    val distanceKm: Double?,
    val availableNow: Boolean
)

data class SearchSuggestionData(
    val text: String,
    val type: SearchSuggestionType,
    val subtitle: String?
)

data class LocationSuggestionData(
    val name: String,
    val county: String?,
    val country: String,
    val latitude: Double?,
    val longitude: Double?
)

data class PopularSearchData(
    val query: String,
    val searchCount: Int,
    val location: String?
)

data class SearchHistoryData(
    val id: String = "",
    val userId: String,
    val query: String,
    val type: SearchType,
    val filters: String?,
    val searchedAt: String
)

data class SavedSearchResultData(
    val id: String,
    val userId: String,
    val name: String,
    val query: String,
    val type: SearchType,
    val filters: String?,
    val notifyOnNewResults: Boolean,
    val createdAt: String
)

data class SearchAnalyticsData(
    val totalSearches: Int,
    val uniqueUsers: Int,
    val topQueries: List<PopularSearchData>,
    val topLocations: List<LocationSuggestionData>,
    val mostClickedResultTypes: Map<SearchResultType, Int>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class SearchType {

    GLOBAL,

    PROPERTY,

    LANDLORD,

    TENANT,

    BROKER,

    PROFESSIONAL,

    SERVICE
}

enum class SearchResultType {

    PROPERTY,

    TENANT,

    LANDLORD,

    BROKER,

    TECHNICIAN,

    PROFESSIONAL
}

enum class SearchUserRole {

    TENANT,

    LANDLORD,

    PROPERTY_MANAGER,

    BROKER,

    CARETAKER,

    TECHNICIAN,

    CONTRACTOR
}

enum class ProfessionalSearchType {

    PLUMBER,

    ELECTRICIAN,

    CARPENTER,

    MASON,

    PAINTER,

    HVAC_TECHNICIAN,

    CLEANER,

    SECURITY,

    PEST_CONTROL,

    SURVEYOR,

    VALUER,

    ARCHITECT,

    ENGINEER,

    LAWYER,

    AGENT,

    MOVING_SERVICE,

    OTHER
}

enum class SearchSuggestionType {

    PROPERTY,

    LOCATION,

    ESTATE,

    TOWN,

    COUNTY,

    PROFESSIONAL,

    BROKER,

    RECENT
}

enum class SearchSort {

    RELEVANCE,

    NEWEST,

    LOWEST_PRICE,

    HIGHEST_PRICE,

    RATING,

    MOST_VIEWED,

    MOST_FAVORITED,

    DISTANCE,

    EXPERIENCE
}