package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyDetailsData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Retrieves the current availability information for a property.
 */
class GetPropertyAvailabilityUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        propertyId: String
    ): Result<PropertyDetailsData> {

        val cleanPropertyId = propertyId.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.getProperty(
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}