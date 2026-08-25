package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Deletes a property from the system.
 */
class DeletePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        confirmation: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanConfirmation = confirmation.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        if (!cleanConfirmation.equals("DELETE", ignoreCase = true)) {
            return Result.failure(
                IllegalArgumentException(
                    "Please type DELETE to confirm property deletion."
                )
            )
        }

        return try {
            propertyRepository.deleteProperty(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}