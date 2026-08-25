package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Updates user privacy and visibility preferences.
 *
 * These settings control how a user's non-sensitive profile
 * information is exposed to other users.
 *
 * Server-side authorization must still be enforced by the backend.
 */
class UpdatePrivacySettingsUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        profileVisible: Boolean? = null,
        phoneVisible: Boolean? = null,
        emailVisible: Boolean? = null,
        showOnlineStatus: Boolean? = null,
        allowDirectMessages: Boolean? = null,
        allowPropertyRecommendations: Boolean? = null,
        allowLocationSharing: Boolean? = null,
        showInPublicDirectory: Boolean? = null
    ): Result<Unit> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            userRepository.updatePrivacySettings(
                userId = cleanUserId,
                profileVisible = profileVisible,
                phoneVisible = phoneVisible,
                emailVisible = emailVisible,
                showOnlineStatus = showOnlineStatus,
                allowDirectMessages = allowDirectMessages,
                allowPropertyRecommendations =
                    allowPropertyRecommendations,
                allowLocationSharing = allowLocationSharing,
                showInPublicDirectory = showInPublicDirectory
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}