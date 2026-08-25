package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Handles user authentication.
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {

        val cleanEmail = email.trim()

        if (cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Email address is required."))
        }

        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password is required."))
        }

        return try {
            authRepository.login(
                email = cleanEmail,
                password = password
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
