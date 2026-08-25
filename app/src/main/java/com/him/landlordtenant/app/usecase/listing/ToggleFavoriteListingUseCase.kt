package com.him.landlordtenant.app.usecase.listing

import com.him.landlordtenant.app.interfaces.PropertyListingRepository

class ToggleFavoriteListingUseCase(
    private val propertyListingRepository: PropertyListingRepository
) {
    suspend operator fun invoke(userId: String, listingId: String): Result<Unit> {
        return try {
            val isFav = propertyListingRepository.isFavorite(userId, listingId).getOrDefault(false)
            if (isFav) {
                propertyListingRepository.removeFavorite(userId, listingId)
            } else {
                propertyListingRepository.addFavorite(userId, listingId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
