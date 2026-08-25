package com.him.landlordtenant.app.usecase.listing

import com.him.landlordtenant.app.interfaces.MarketplacePropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyListingRepository

class GetLatestListingsUseCase(
    private val propertyListingRepository: PropertyListingRepository
) {
    suspend operator fun invoke(limit: Int = 20): Result<List<MarketplacePropertyListingData>> {
        return try {
            propertyListingRepository.getLatestListings(limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
