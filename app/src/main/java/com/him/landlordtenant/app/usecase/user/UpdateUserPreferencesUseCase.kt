package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Updates general application preferences for a user.
 */
class UpdateUserPreferencesUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        language: String? = null,
        currency: String? = null,
        theme: String? = null,
        timezone: String? = null,
        autoPlayVideos: Boolean? = null,
        showPropertyRecommendations: Boolean? = null
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanLanguage = language?.trim()
        val cleanCurrency = currency?.trim()?.uppercase()
        val cleanTheme = theme?.trim()?.lowercase()
        val cleanTimezone = timezone?.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("User ID is required.")
            )
        }

        if (cleanLanguage != null && cleanLanguage.length > 20) {
            return Result.failure(
                IllegalArgumentException("Invalid language.")
            )
        }

        if (
            cleanCurrency != null &&
            cleanCurrency.length != 3
        ) {
            return Result.failure(
                IllegalArgumentException("Invalid currency.")
            )
        }

        val supportedThemes = setOf(
            "light",
            "dark",
            "system"
        )

        if (
            cleanTheme != null &&
            cleanTheme !in supportedThemes
        ) {
            return Result.failure(
                IllegalArgumentException("Invalid theme.")
            )
        }

        return try {
            userRepository.updateUserPreferences(
                userId = cleanUserId,
                language = cleanLanguage,
                currency = cleanCurrency,
                theme = cleanTheme,
                timezone = cleanTimezone,
                autoPlayVideos = autoPlayVideos,
                showPropertyRecommendations =
                    showPropertyRecommendations
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}