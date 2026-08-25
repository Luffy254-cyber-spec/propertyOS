package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Adds a property to the current user's favorites.
 */
class AddPropertyToFavoritesUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        userId: String,
        propertyId: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanPropertyId = propertyId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.saveProperty(
                userId = cleanUserId,
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}