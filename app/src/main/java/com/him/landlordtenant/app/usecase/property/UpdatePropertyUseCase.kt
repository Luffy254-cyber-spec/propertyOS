package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyCreateData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Updates an existing property.
 */
class UpdatePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        propertyData: PropertyCreateData
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()

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

        return try {
            propertyRepository.updateProperty(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                property = propertyData
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}