package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Updates the geographical location of a property.
 */
class UpdatePropertyLocationUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        latitude: Double,
        longitude: Double
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

        if (latitude !in -90.0..90.0) {
            return Result.failure(
                IllegalArgumentException(
                    "Latitude must be between -90 and 90."
                )
            )
        }

        if (longitude !in -180.0..180.0) {
            return Result.failure(
                IllegalArgumentException(
                    "Longitude must be between -180 and 180."
                )
            )
        }

        return try {
            propertyRepository.updateLocation(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                latitude = latitude,
                longitude = longitude
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}