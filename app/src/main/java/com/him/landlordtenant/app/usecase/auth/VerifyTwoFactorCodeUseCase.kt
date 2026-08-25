package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.SecurityRepository

/**
 * Verifies a two-factor authentication code.
 *
 * Used for:
 * - Login verification
 * - Completing 2FA setup
 * - Step-up authentication
 * - Sensitive account operations
 */
class VerifyTwoFactorCodeUseCase(
    private val securityRepository: SecurityRepository
) {

    suspend operator fun invoke(
        userId: String,
        verificationCode: String,
        purpose: String = "LOGIN"
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanCode = verificationCode.trim()
        val cleanPurpose = purpose.trim().uppercase()

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

        if (!cleanCode.all { it.isDigit() }) {
            return Result.failure(
                IllegalArgumentException(
                    "Verification code must contain only numbers."
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

        if (cleanPurpose.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Verification purpose is required."
                )
            )
        }

        return try {
            securityRepository.markTwoFactorVerified(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}