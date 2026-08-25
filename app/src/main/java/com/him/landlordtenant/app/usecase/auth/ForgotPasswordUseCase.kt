package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Starts the password recovery process.
 *
 * The identifier can be:
 * - Email address
 * - Phone number
 *
 * The backend is responsible for determining whether the
 * account exists and sending the appropriate recovery method.
 */
class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        identifier: String
    ): Result<Unit> {

        val cleanIdentifier = identifier.trim()

        if (cleanIdentifier.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Email address or phone number is required."
                )
            )
        }

        return try {
            authRepository.forgotPassword(
                identifier = cleanIdentifier
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}