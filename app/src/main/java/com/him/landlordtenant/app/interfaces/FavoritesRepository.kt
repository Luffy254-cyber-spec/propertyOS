package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * FAVORITES REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Favorite properties
 * - Saved listings
 * - Watchlists
 * - Property collections
 * - Property comparison
 * - Personal notes
 * - Price monitoring
 * - Availability monitoring
 * - Listing alerts
 * - Recently viewed properties
 * - Removed/expired listing handling
 *
 * =============================================================
 */

interface FavoritesRepository {

    /*
     * ---------------------------------------------------------
     * FAVORITES
     * ---------------------------------------------------------
     */

    suspend fun addFavorite(
        userId: String,
        listingId: String
    ): Result<String>

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
    ): Result<List<FavoritePropertyData>>

    fun observeFavorites(
        userId: String
    ): Flow<Result<List<FavoritePropertyData>>>


    /*
     * ---------------------------------------------------------
     * WATCHLIST
     * ---------------------------------------------------------
     */

    suspend fun addToWatchlist(
        userId: String,
        listingId: String
    ): Result<String>

    suspend fun removeFromWatchlist(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getWatchlist(
        userId: String
    ): Result<List<FavoritePropertyData>>

    suspend fun isWatching(
        userId: String,
        listingId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * COLLECTIONS
     * ---------------------------------------------------------
     */

    suspend fun createCollection(
        userId: String,
        collection: CreatePropertyCollectionData
    ): Result<String>

    suspend fun updateCollection(
        userId: String,
        collectionId: String,
        update: UpdatePropertyCollectionData
    ): Result<Unit>

    suspend fun deleteCollection(
        userId: String,
        collectionId: String
    ): Result<Unit>

    suspend fun getCollections(
        userId: String
    ): Result<List<PropertyCollectionData>>

    suspend fun addListingToCollection(
        userId: String,
        collectionId: String,
        listingId: String
    ): Result<Unit>

    suspend fun removeListingFromCollection(
        userId: String,
        collectionId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getCollectionListings(
        userId: String,
        collectionId: String
    ): Result<List<FavoritePropertyData>>


    /*
     * ---------------------------------------------------------
     * COMPARISON
     * ---------------------------------------------------------
     */

    suspend fun addToComparison(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun removeFromComparison(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getComparisonListings(
        userId: String
    ): Result<List<FavoritePropertyData>>

    suspend fun clearComparison(
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
     */

    suspend fun addNote(
        userId: String,
        listingId: String,
        note: String
    ): Result<String>

    suspend fun updateNote(
        userId: String,
        noteId: String,
        note: String
    ): Result<Unit>

    suspend fun deleteNote(
        userId: String,
        noteId: String
    ): Result<Unit>

    suspend fun getNotes(
        userId: String,
        listingId: String
    ): Result<List<PropertyFavoriteNoteData>>


    /*
     * ---------------------------------------------------------
     * PRICE ALERTS
     * ---------------------------------------------------------
     */

    suspend fun enablePriceAlert(
        userId: String,
        listingId: String,
        targetPrice: Double?
    ): Result<Unit>

    suspend fun disablePriceAlert(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getPriceAlerts(
        userId: String
    ): Result<List<PropertyPriceAlertData>>


    /*
     * ---------------------------------------------------------
     * AVAILABILITY ALERTS
     * ---------------------------------------------------------
     */

    suspend fun enableAvailabilityAlert(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun disableAvailabilityAlert(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getAvailabilityAlerts(
        userId: String
    ): Result<List<PropertyAvailabilityAlertData>>


    /*
     * ---------------------------------------------------------
     * LISTING CHANGES
     * ---------------------------------------------------------
     */

    suspend fun getListingChanges(
        userId: String,
        listingId: String
    ): Result<List<PropertyListingChangeData>>

    suspend fun recordListingChange(
        listingId: String,
        change: PropertyListingChangeData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RECENTLY VIEWED
     * ---------------------------------------------------------
     */

    suspend fun recordRecentlyViewed(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getRecentlyViewed(
        userId: String,
        limit: Int = 20
    ): Result<List<FavoritePropertyData>>

    suspend fun clearRecentlyViewed(
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SHARE
     * ---------------------------------------------------------
     */

    suspend fun recordShare(
        userId: String,
        listingId: String
    ): Result<Unit>

    suspend fun getSharedListings(
        userId: String
    ): Result<List<FavoritePropertyData>>


    /*
     * ---------------------------------------------------------
     * ALERT PROCESSING
     * ---------------------------------------------------------
     */

    suspend fun processPriceAlerts(): Result<Int>

    suspend fun processAvailabilityAlerts(): Result<Int>


    /*
     * ---------------------------------------------------------
     * CLEANUP
     * ---------------------------------------------------------
     */

    suspend fun removeExpiredListings(
        userId: String
    ): Result<Int>

    suspend fun restoreAvailableListing(
        userId: String,
        listingId: String
    ): Result<Unit>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class FavoritePropertyData(
    val favoriteId: String,
    val listingId: String,
    val propertyId: String,
    val title: String,
    val primaryImageUrl: String?,
    val monthlyRent: Double?,
    val salePrice: Double?,
    val bedrooms: Int,
    val bathrooms: Int,
    val location: String,
    val verified: Boolean,
    val listingStatus: String,
    val savedAt: String,
    val lastViewedAt: String?
)

data class CreatePropertyCollectionData(
    val name: String,
    val description: String?,
    val icon: String?
)

data class UpdatePropertyCollectionData(
    val name: String?,
    val description: String?,
    val icon: String?
)

data class PropertyCollectionData(
    val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val icon: String?,
    val listingCount: Int,
    val createdAt: String
)

data class PropertyFavoriteNoteData(
    val id: String,
    val userId: String,
    val listingId: String,
    val note: String,
    val createdAt: String,
    val updatedAt: String
)

data class PropertyPriceAlertData(
    val id: String,
    val userId: String,
    val listingId: String,
    val currentPrice: Double?,
    val targetPrice: Double?,
    val enabled: Boolean,
    val lastNotifiedAt: String?
)

data class PropertyAvailabilityAlertData(
    val id: String,
    val userId: String,
    val listingId: String,
    val enabled: Boolean,
    val lastNotifiedAt: String?
)

data class PropertyListingChangeData(
    val id: String = "",
    val listingId: String,
    val changeType: ListingChangeType,
    val oldValue: String?,
    val newValue: String?,
    val changedAt: String
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ListingChangeType {

    PRICE_CHANGED,

    RENT_CHANGED,

    STATUS_CHANGED,

    AVAILABILITY_CHANGED,

    DESCRIPTION_CHANGED,

    AMENITIES_CHANGED,

    MEDIA_CHANGED,

    LOCATION_CHANGED
}