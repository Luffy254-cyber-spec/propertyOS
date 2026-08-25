package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Updates the authenticated user's profile photo.
 *
 * The actual image compression, upload and storage should be
 * handled by the data/repository layer.
 */
class UpdateUserPhotoUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        imagePath: String
    ): Result<String> {

        val cleanUserId = userId.trim()
        val cleanImagePath = imagePath.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanImagePath.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Profile image is required."
                )
            )
        }

        return try {
            userRepository.updateUserPhoto(
                userId = cleanUserId,
                imagePath = cleanImagePath
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}