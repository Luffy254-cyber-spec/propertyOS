package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Removes a video from an existing property.
 */
class RemovePropertyVideoUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        videoId: String
    ): Result<Unit> {

        val cleanOwnerId = ownerId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanVideoId = videoId.trim()

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
                mediaId = cleanVideoId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}