package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Verifies a user's email address using a verification code/token.
 *
 * Used during:
 * - Account registration
 * - Email-change confirmation
 * - Security-sensitive account operations
 */
class VerifyEmailUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        userId: String,
        verificationCode: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanCode = verificationCode.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanCode.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Verification code is required."
                )
            )
        }

        if (cleanCode.length < 4) {
            return Result.failure(
                IllegalArgumentException(
                    "Invalid verification code."
                )
            )
        }

        return try {
            authRepository.verifyEmail(
                userId = cleanUserId,
                verificationCode = cleanCode
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}