package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Changes the password of an already authenticated user.
 *
 * Unlike ResetPasswordUseCase, this operation requires the
 * user's current password.
 */
class ChangePasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        userId: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (currentPassword.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Current password is required."
                )
            )
        }

        if (newPassword.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "New password is required."
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

        if (currentPassword == newPassword) {
            return Result.failure(
                IllegalArgumentException(
                    "New password must be different from the current password."
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
            authRepository.changePassword(
                currentPassword = currentPassword,
                newPassword = newPassword
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}