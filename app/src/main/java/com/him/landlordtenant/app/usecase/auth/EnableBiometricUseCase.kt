package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Enables biometric authentication for a user.
 *
 * The actual biometric prompt must be handled by the Android
 * presentation/security layer. This use case only controls the
 * application's biometric preference/state.
 *
 * Supported device mechanisms may include:
 * - Fingerprint
 * - Face authentication
 * - Device biometric authentication
 */
class EnableBiometricUseCase(
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
            authRepository.enableBiometric(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}