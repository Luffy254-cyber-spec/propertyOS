package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Adds a video to an existing property.
 */
class AddPropertyVideoUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        filePath: String
    ): Result<String> {

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

        if (filePath.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "File path is required."
                )
            )
        }

        return try {
            propertyRepository.uploadVideo(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                filePath = filePath
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}