package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Removes an amenity from an existing property.
 */
class RemovePropertyAmenityUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        amenityId: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanAmenityId = amenityId.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.removeAmenity(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                amenityId = cleanAmenityId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}