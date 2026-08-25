package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Logs the currently authenticated user out of the application.
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return try {
            authRepository.logout()
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
