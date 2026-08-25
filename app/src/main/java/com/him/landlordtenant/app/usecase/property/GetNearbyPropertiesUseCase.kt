package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Retrieves properties within a specified radius of a location.
 */
class GetNearbyPropertiesUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Result<List<PropertyListingData>> {

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

        if (radiusKm <= 0.0) {
            return Result.failure(
                IllegalArgumentException(
                    "Radius must be greater than zero."
                )
            )
        }

        return try {
            propertyRepository.getNearbyProperties(
                latitude = latitude,
                longitude = longitude,
                radiusKm = radiusKm
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}