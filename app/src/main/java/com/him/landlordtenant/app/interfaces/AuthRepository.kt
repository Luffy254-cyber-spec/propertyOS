package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * AUTH REPOSITORY
 * =============================================================
 */

interface AuthRepository {

    fun observeAuthenticationState(): Flow<Boolean>

    fun getCurrentUserId(): String?

    fun getCurrentUserEmail(): String?

    fun getCurrentUserName(): String?

    suspend fun getCurrentUser(): Result<UserProfileData?>

    suspend fun register(name: String, email: String, password: String): Result<String>

    suspend fun login(email: String, password: String): Result<String>

    suspend fun signInWithGoogle(idToken: String): Result<String>

    suspend fun logout(): Result<Unit>

    suspend fun sendPasswordReset(email: String): Result<Unit>

    suspend fun sendEmailVerification(): Result<Unit>

    suspend fun isEmailVerified(): Result<Boolean>

    suspend fun refreshSession(): Result<UserProfileData>

    suspend fun deleteAccount(): Result<Unit>

    suspend fun updateDisplayName(name: String): Result<Unit>

    suspend fun updateEmail(email: String): Result<Unit>

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

    suspend fun reAuthenticate(password: String): Result<Unit>

    suspend fun enableBiometric(userId: String): Result<Unit>

    suspend fun disableBiometric(userId: String): Result<Unit>

    suspend fun forgotPassword(identifier: String): Result<Unit>

    suspend fun resetPassword(identifier: String, resetToken: String, newPassword: String): Result<Unit>

    suspend fun verifyEmail(userId: String, verificationCode: String): Result<Unit>

    suspend fun verifyPhone(userId: String, verificationCode: String): Result<Unit>

    fun startPhoneVerification(
        phoneNumber: String,
        activity: android.app.Activity,
        onCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    )

    suspend fun signInWithPhone(verificationId: String, code: String): Result<String>
}
