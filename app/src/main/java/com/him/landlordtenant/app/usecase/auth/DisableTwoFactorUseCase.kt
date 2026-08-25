package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.SecurityRepository

/**
 * Disables two-factor authentication for an authenticated user.
 *
 * For security, the caller should normally require a recent
 * authentication or a valid 2FA verification before allowing
 * this operation.
 */
class DisableTwoFactorUseCase(
    private val securityRepository: SecurityRepository
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

        if (cleanCode.length < 4 || cleanCode.length > 8) {
            return Result.failure(
                IllegalArgumentException(
                    "Invalid verification code."
                )
            )
        }

        return try {
            securityRepository.disableTwoFactor(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}