package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyLocationData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Retrieves the map/location information for a property.
 */
class GetPropertyLocationUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        propertyId: String
    ): Result<PropertyLocationData> {

        val cleanPropertyId = propertyId.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.getLocation(
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}