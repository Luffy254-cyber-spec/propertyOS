package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Removes a photo from a property.
 */
class RemovePropertyPhotoUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        photoId: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanPhotoId = photoId.trim()

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            propertyRepository.deleteMedia(
                ownerId = cleanOwnerId,
                propertyId = cleanPropertyId,
                mediaId = cleanPhotoId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}