package com.him.landlordtenant.app.usecase.listing

import com.him.landlordtenant.app.interfaces.ListingSearchFilters
import com.him.landlordtenant.app.interfaces.MarketplacePropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyListingRepository

class SearchMarketplaceUseCase(
    private val propertyListingRepository: PropertyListingRepository
) {
    suspend operator fun invoke(
        query: String,
        filters: ListingSearchFilters = ListingSearchFilters()
    ): Result<List<MarketplacePropertyListingData>> {
        return try {
            propertyListingRepository.searchListings(query, filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
