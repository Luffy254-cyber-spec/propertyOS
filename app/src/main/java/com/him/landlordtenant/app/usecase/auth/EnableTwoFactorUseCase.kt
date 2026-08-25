package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.SecurityRepository
import com.him.landlordtenant.app.interfaces.TwoFactorMethod

/**
 * Starts the process of enabling two-factor authentication (2FA).
 *
 * This use case requests the backend to prepare 2FA for the user.
 *
 * The backend may return:
 * - A setup identifier
 * - A QR-code payload
 * - An authenticator secret
 * - Recovery codes
 *
 * The secret itself should be handled securely and should not
 * be logged.
 */
class EnableTwoFactorUseCase(
    private val securityRepository: SecurityRepository
) {

    suspend operator fun invoke(
        userId: String,
        method: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanMethod = method.trim().uppercase()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanMethod.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Two-factor authentication method is required."
                )
            )
        }

        val twoFactorMethod = try {
            TwoFactorMethod.valueOf(cleanMethod)
        } catch (e: IllegalArgumentException) {
            return Result.failure(
                IllegalArgumentException(
                    "Unsupported two-factor authentication method."
                )
            )
        }

        return try {
            securityRepository.enableTwoFactor(
                userId = cleanUserId,
                method = twoFactorMethod
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}