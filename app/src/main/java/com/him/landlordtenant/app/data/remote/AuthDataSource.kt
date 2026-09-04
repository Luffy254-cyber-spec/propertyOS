package com.him.landlordtenant.app.data.remote

import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun getUserId(): String? = firebaseAuth.currentUser?.uid

    suspend fun login(email: String, password: String): Result<AuthResult> = try {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(email: String, password: String): Result<AuthResult> = try {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signInWithGoogle(idToken: String): Result<AuthResult> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun startPhoneVerification(
        phoneNumber: String,
        activity: android.app.Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun signInWithPhone(verificationId: String, code: String): Result<AuthResult> = try {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val result = firebaseAuth.signInWithCredential(credential).await()
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun sendEmailVerification(): Result<Unit> = try {
        firebaseAuth.currentUser?.sendEmailVerification()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun reloadUser(): Result<Unit> = try {
        firebaseAuth.currentUser?.reload()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = try {
        firebaseAuth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateEmail(newEmail: String): Result<Unit> = try {
        // Use verifyBeforeUpdateEmail for security (sends verification to new email)
        firebaseAuth.currentUser?.verifyBeforeUpdateEmail(newEmail)?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateProfile(name: String?, photoUrl: String?): Result<Unit> = try {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(photoUrl?.let { android.net.Uri.parse(it) })
            .build()
        firebaseAuth.currentUser?.updateProfile(profileUpdates)?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
