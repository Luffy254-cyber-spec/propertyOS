package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.UserProfileData as AuthUserData
import javax.inject.Inject

/**
 * Retrieves the currently authenticated user.
 *
 * Used by:
 * - Application startup
 * - Session restoration
 * - Splash screen
 * - Dashboard initialization
 * - Role-based navigation
 * - Account/profile screens
 */
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<AuthUserData?> {
        return try {
            authRepository.getCurrentUser()
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}