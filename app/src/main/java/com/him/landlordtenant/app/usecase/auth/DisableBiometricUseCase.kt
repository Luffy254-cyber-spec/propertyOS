package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Disables biometric authentication for the user's account/device.
 *
 * The actual biometric prompt is handled by the Android
 * presentation/security layer. This use case only updates
 * the application's biometric authentication state.
 */
class DisableBiometricUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        userId: String
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
            authRepository.disableBiometric(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}