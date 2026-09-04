package com.him.landlordtenant.app.interfaces.repository.impl

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.him.landlordtenant.app.data.remote.AuthDataSource
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.UserProfileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override fun observeAuthenticationState(): Flow<Boolean> = flow {
        emit(authDataSource.getCurrentUser() != null)
    }

    override fun getCurrentUserId(): String? = authDataSource.getUserId()

    override fun getCurrentUserEmail(): String? = authDataSource.getCurrentUser()?.email

    override fun getCurrentUserName(): String? = authDataSource.getCurrentUser()?.displayName

    override suspend fun getCurrentUser(): Result<UserProfileData?> {
        val user = authDataSource.getCurrentUser() ?: return Result.success(null)
        return Result.success(
            UserProfileData(
                id = user.uid,
                fullName = user.displayName ?: "",
                email = user.email ?: "",
                phoneNumber = user.phoneNumber,
                profileImageUrl = user.photoUrl?.toString(),
                roles = emptyList(), // This should ideally be fetched from UserRepository
                activeRole = "",
                isVerified = user.isEmailVerified
            )
        )
    }

    override suspend fun register(name: String, email: String, password: String): Result<String> {
        val result = authDataSource.register(email, password)
        return result.map { authResult ->
            val userId = authResult.user?.uid ?: ""
            // Sync name to Firebase Auth profile
            authDataSource.updateProfile(name, null)
            userId
        }
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return authDataSource.login(email, password).map { it.user?.uid ?: "" }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        return authDataSource.signInWithGoogle(idToken).map { it.user?.uid ?: "" }
    }

    override suspend fun logout(): Result<Unit> {
        authDataSource.signOut()
        return Result.success(Unit)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = authDataSource.sendPasswordReset(email)
    override suspend fun sendEmailVerification(): Result<Unit> = authDataSource.sendEmailVerification()
    override suspend fun isEmailVerified(): Result<Boolean> = Result.success(authDataSource.getCurrentUser()?.isEmailVerified ?: false)
    override suspend fun reloadUser(): Result<Unit> = authDataSource.reloadUser()
    override suspend fun refreshSession(): Result<UserProfileData> = Result.failure(NotImplementedError())
    override suspend fun deleteAccount(): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun updateDisplayName(name: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun updateEmail(email: String): Result<Unit> = authDataSource.updateEmail(email)
    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun reAuthenticate(password: String): Result<Unit> = Result.failure(NotImplementedError())

    override suspend fun enableBiometric(userId: String): Result<Unit> = Result.success(Unit)
    override suspend fun disableBiometric(userId: String): Result<Unit> = Result.success(Unit)
    override suspend fun forgotPassword(identifier: String): Result<Unit> = authDataSource.sendPasswordReset(identifier)
    override suspend fun resetPassword(identifier: String, resetToken: String, newPassword: String): Result<Unit> = Result.success(Unit)
    override suspend fun verifyEmail(userId: String, verificationCode: String): Result<Unit> = Result.success(Unit)
    override suspend fun verifyPhone(userId: String, verificationCode: String): Result<Unit> = Result.success(Unit)

    override fun startPhoneVerification(
        phoneNumber: String,
        activity: android.app.Activity,
        onCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-verification or instant validation
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                onCodeSent(verificationId)
            }
        }
        authDataSource.startPhoneVerification(phoneNumber, activity, callbacks)
    }

    override suspend fun signInWithPhone(verificationId: String, code: String): Result<String> {
        return authDataSource.signInWithPhone(verificationId, code).map { it.user?.uid ?: "" }
    }
}
