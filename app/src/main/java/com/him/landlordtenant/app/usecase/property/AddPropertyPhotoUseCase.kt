package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Adds a photo to an existing property.
 */
class AddPropertyPhotoUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        filePath: String,
        isCoverPhoto: Boolean = false
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
            val result = propertyRepository.uploadImage(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                filePath = filePath
            )
            
            if (isCoverPhoto && result.isSuccess) {
                propertyRepository.setPrimaryImage(
                    ownerId = cleanOwnerId,
                    propertyId = cleanPropertyId,
                    mediaId = result.getOrThrow()
                )
            }
            
            result
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}