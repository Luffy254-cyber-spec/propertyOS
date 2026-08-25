package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Publishes a property listing.
 */
class PublishPropertyUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.publishProperty(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}