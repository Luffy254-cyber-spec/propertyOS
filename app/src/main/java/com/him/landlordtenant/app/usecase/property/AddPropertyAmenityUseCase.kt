package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.AmenityData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Adds an amenity to an existing property.
 */
class AddPropertyAmenityUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        amenityName: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanAmenity = amenityName.trim()

        if (cleanOwnerId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Owner ID is required."
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

        if (cleanAmenity.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Amenity name is required."
                )
            )
        }

        return try {
            propertyRepository.addAmenity(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                amenity = AmenityData(
                    id = "",
                    name = cleanAmenity,
                    icon = null
                )
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}