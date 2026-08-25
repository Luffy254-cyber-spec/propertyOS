package com.him.landlordtenant.app.usecase.listing

import com.him.landlordtenant.app.interfaces.CreatePropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyListingRepository

class CreateListingUseCase(
    private val propertyListingRepository: PropertyListingRepository
) {
    suspend operator fun invoke(
        userId: String,
        listingData: CreatePropertyListingData
    ): Result<String> {
        return try {
            propertyListingRepository.createListing(userId, listingData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
