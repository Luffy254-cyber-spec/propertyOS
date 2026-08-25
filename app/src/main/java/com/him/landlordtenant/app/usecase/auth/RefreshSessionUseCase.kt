package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.UserProfileData as AuthUserData

/**
 * Refreshes the currently authenticated user's session.
 *
 * Used when:
 * - Access token has expired
 * - Application starts
 * - Application returns from background
 * - Refresh token needs to be exchanged
 * - Session needs to be validated
 */
class RefreshSessionUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<AuthUserData> {
        return try {
            authRepository.refreshSession()
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}