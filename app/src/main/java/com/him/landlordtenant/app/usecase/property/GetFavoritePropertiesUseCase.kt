package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Retrieves properties saved by a user.
 */
class GetFavoritePropertiesUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Result<List<PropertyListingData>> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            propertyRepository.getSavedProperties(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}