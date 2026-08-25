package com.him.landlordtenant.app.usecase.listing

import com.him.landlordtenant.app.interfaces.CreateListingViewingData
import com.him.landlordtenant.app.interfaces.PropertyListingRepository

class RequestListingViewingUseCase(
    private val propertyListingRepository: PropertyListingRepository
) {
    suspend operator fun invoke(
        userId: String,
        viewingData: CreateListingViewingData
    ): Result<String> {
        return try {
            propertyListingRepository.requestViewing(userId, viewingData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
