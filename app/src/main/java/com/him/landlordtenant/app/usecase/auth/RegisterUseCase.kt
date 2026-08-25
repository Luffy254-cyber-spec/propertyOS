package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.utils.ValidationUtils

/**
 * Handles creation of a new application account.
 */
class RegisterUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        acceptTerms: Boolean
    ): Result<String> {

        val cleanName = name.trim()
        val cleanEmail = email.trim().lowercase()

        if (cleanName.isBlank()) {
            return Result.failure(IllegalArgumentException("Name is required."))
        }

        if (!ValidationUtils.isValidEmail(cleanEmail)) {
            return Result.failure(IllegalArgumentException("Please provide a valid email address."))
        }

        if (!ValidationUtils.isValidPassword(password)) {
            return Result.failure(IllegalArgumentException("Password must contain at least 8 characters."))
        }

        if (password != confirmPassword) {
            return Result.failure(IllegalArgumentException("Passwords do not match."))
        }

        if (!acceptTerms) {
            return Result.failure(IllegalArgumentException("You must accept the terms and conditions."))
        }

        return try {
            authRepository.register(
                name = cleanName,
                email = cleanEmail,
                password = password
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
