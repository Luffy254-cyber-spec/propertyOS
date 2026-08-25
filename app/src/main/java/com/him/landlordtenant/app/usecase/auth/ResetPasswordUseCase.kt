package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Completes the password recovery process.
 *
 * The recovery token/code must have been issued by the backend
 * through the forgot-password flow.
 */
class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        identifier: String,
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {

        val cleanIdentifier = identifier.trim()
        val cleanToken = resetToken.trim()

        if (cleanIdentifier.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Email address or phone number is required."
                )
            )
        }

        if (cleanToken.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Password reset code is required."
                )
            )
        }

        if (newPassword.length < 8) {
            return Result.failure(
                IllegalArgumentException(
                    "Password must contain at least 8 characters."
                )
            )
        }

        if (newPassword != confirmPassword) {
            return Result.failure(
                IllegalArgumentException(
                    "Passwords do not match."
                )
            )
        }

        if (!newPassword.any { it.isUpperCase() }) {
            return Result.failure(
                IllegalArgumentException(
                    "Password must contain at least one uppercase letter."
                )
            )
        }

        if (!newPassword.any { it.isLowerCase() }) {
            return Result.failure(
                IllegalArgumentException(
                    "Password must contain at least one lowercase letter."
                )
            )
        }

        if (!newPassword.any { it.isDigit() }) {
            return Result.failure(
                IllegalArgumentException(
                    "Password must contain at least one number."
                )
            )
        }

        return try {
            authRepository.resetPassword(
                identifier = cleanIdentifier,
                resetToken = cleanToken,
                newPassword = newPassword
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}