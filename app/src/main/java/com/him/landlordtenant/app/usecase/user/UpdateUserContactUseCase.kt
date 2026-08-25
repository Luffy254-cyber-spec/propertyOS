package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserProfileData
import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Updates a user's contact information.
 */
class UpdateUserContactUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        email: String?,
        phoneNumber: String?
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanEmail = email?.trim()?.lowercase()
        val cleanPhone = phoneNumber
            ?.trim()
            ?.replace(" ", "")
            ?.replace("-", "")

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            val currentProfile = userRepository.getUserProfile(cleanUserId).getOrThrow()
            
            userRepository.updateUserProfile(
                userId = cleanUserId,
                profile = currentProfile.copy(
                    email = cleanEmail ?: currentProfile.email,
                    phoneNumber = cleanPhone ?: currentProfile.phoneNumber
                )
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}