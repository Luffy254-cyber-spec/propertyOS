package com.him.landlordtenant.app.ui.viewmodel.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.him.landlordtenant.app.data.model.User
import com.him.landlordtenant.app.data.model.UserRole
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.him.landlordtenant.app.util.AlertManager
import com.him.landlordtenant.app.util.BannerType
import com.him.landlordtenant.app.util.EmailService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val alertManager: AlertManager,
    private val emailService: EmailService
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var _verificationId: String? = null

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            Log.d(TAG, "Checking auth status for userId: $userId")
            viewModelScope.launch {
                try {
                    val result = withTimeout(5000) { userRepository.getUserById(userId) }
                    Log.d(TAG, "Current user loaded: ${result?.email}")
                    _currentUser.value = result

                    // Force verification check even on app resume/start
                    authRepository.reloadUser()
                    val isFirebaseVerified = authRepository.isEmailVerified().getOrDefault(false)
                    if (!isFirebaseVerified && result != null) {
                        Log.w(TAG, "User session exists but email is not verified. Redirecting...")
                        _authState.value = AuthState.RequiresEmailVerification(result.email ?: "")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load current user on start: ${e.message}", e)
                }
            }
        } else {
            Log.d(TAG, "No user ID found in session")
        }
    }

    fun login(email: String, password: String) {
        Log.i(TAG, "Attempting login for email: $email")
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(30000) { authRepository.login(email, password) }
                result.fold(
                    onSuccess = { userId ->
                        Log.i(TAG, "Firebase login successful. Fetching user profile...")
                        val user = try {
                            withTimeout(10000) { userRepository.getUserById(userId) }
                        } catch (e: Exception) { 
                            Log.e(TAG, "Profile fetch failed after login: ${e.message}")
                            null 
                        }
                        
                        _currentUser.value = user
                        
                        // Always refresh Firebase user before checking verification.
                        authRepository.reloadUser().onFailure { 
                            Log.e(TAG, "Failed to reload user during login: ${it.message}")
                        }

                        val isFirebaseVerified = authRepository.isEmailVerified().getOrDefault(false)
                        Log.d(TAG, "Login verification: Firebase=$isFirebaseVerified")
                        
                        // Send login alert
                        emailService.sendLoginAlert(email, android.os.Build.MODEL)

                        if (!isFirebaseVerified) {
                            Log.w(TAG, "Login successful but email is not verified")
                            
                            // Re-send verification email just in case they lost the old one
                            sendEmailVerification()
                            
                            _authState.value = AuthState.RequiresEmailVerification(email)
                        } else if (user?.phoneVerified == false && user?.phoneNumber != null) {
                            Log.w(TAG, "Login successful but phone not verified. Redirecting...")
                            _authState.value = AuthState.RequiresPhoneVerification(user.phoneNumber)
                        } else {
                            Log.i(TAG, "Email verified. Login can continue.")
                            
                            // Sync verification status to local DB if needed
                            if (user != null && !user.emailVerified) {
                                val updatedUser = user.copy(emailVerified = true, hasVerifiedContact = true)
                                userRepository.saveUser(updatedUser)
                                _currentUser.value = updatedUser
                            }

                            _authState.value = AuthState.Success
                            viewModelScope.launch {
                                alertManager.showBanner("Welcome back, ${user?.displayName ?: "User"}!", BannerType.SUCCESS)
                            }
                        }
                    },
                    onFailure = { error ->
                        val msg = error.message ?: "Login failed"
                        Log.e(TAG, "Firebase login failed: $msg", error)
                        _authState.value = AuthState.Error(msg)
                        viewModelScope.launch {
                            alertManager.showSnackbar(msg)
                        }
                    }
                )
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                Log.e(TAG, "Login timed out")
                _authState.value = AuthState.Error("Login timed out. Please check your internet connection.")
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected login error: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred during login")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        Log.i(TAG, "Starting Google Sign-In")
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(30000) { authRepository.signInWithGoogle(idToken) }
                result.fold(
                    onSuccess = { userId ->
                        Log.i(TAG, "Google Auth Success. Checking database for user: $userId")
                        val existingUser = try {
                            withTimeout(10000) { userRepository.getUserById(userId) }
                        } catch (e: Exception) { 
                            Log.e(TAG, "Failed to check existing user: ${e.message}")
                            null 
                        }

                        if (existingUser == null) {
                            Log.i(TAG, "Creating new user from Google credentials")
                            val firebaseUser = authRepository.getCurrentUser().getOrNull()
                            val newUser = User(
                                id = userId,
                                firebaseUid = userId,
                                email = firebaseUser?.email,
                                firstName = firebaseUser?.fullName ?: "User",
                                displayName = firebaseUser?.fullName ?: "User",
                                roles = emptyList(),
                                activeRole = null,
                                createdAt = Date().toString()
                            )
                            try {
                                withTimeout(15000) { userRepository.saveUser(newUser) }
                            } catch (e: Exception) {
                                Log.e(TAG, "Database save failed for Google user: ${e.message}")
                            }
                            _currentUser.value = newUser
                            sendEmailVerification()
                            _authState.value = AuthState.RequiresEmailVerification(newUser.email ?: "")
                        } else {
                            // Always refresh Firebase user before checking verification.
                            authRepository.reloadUser().onFailure {
                                Log.e(TAG, "Failed to reload user during Google login: ${it.message}")
                            }

                            val isFirebaseVerified = authRepository.isEmailVerified().getOrDefault(false)
                            Log.d(TAG, "Google login verification: Firebase=$isFirebaseVerified")
                            
                            _currentUser.value = existingUser
                            if (isFirebaseVerified) {
                                Log.i(TAG, "Email verified. Login can continue.")
                                
                                // Sync verification status to local DB if needed
                                if (!existingUser.emailVerified) {
                                    val updatedUser = existingUser.copy(emailVerified = true, hasVerifiedContact = true)
                                    userRepository.saveUser(updatedUser)
                                    _currentUser.value = updatedUser
                                }

                                _authState.value = AuthState.Success
                            } else {
                                Log.w(TAG, "Google user exists but email is not verified")
                                sendEmailVerification()
                                _authState.value = AuthState.RequiresEmailVerification(existingUser.email ?: "")
                            }
                        }
                        
                        _currentUser.value?.email?.let { 
                            emailService.sendLoginAlert(it, android.os.Build.MODEL)
                        }
                    },
                    onFailure = { error ->
                        val msg = if (error.message?.contains("cancelled", ignoreCase = true) == true) {
                            "Sign-in cancelled. Ensure your SHA-1 fingerprint is added to Firebase Console."
                        } else {
                            error.message ?: "Google Sign-In failed"
                        }
                        Log.e(TAG, "Google Sign-In Failure: $msg", error)
                        _authState.value = AuthState.Error(msg)
                    }
                )
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                Log.e(TAG, "Google Sign-In timed out")
                _authState.value = AuthState.Error("Google Sign-In timed out. Please try again.")
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected Google Sign-In error: ${e.message}", e)
                _authState.value = AuthState.Error("Google Sign-In failed: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        Log.i(TAG, "Registering new user: $email")
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(30000) { authRepository.register(name, email, password) }
                result.fold(
                    onSuccess = { userId ->
                        Log.i(TAG, "Firebase registration successful. ID: $userId")
                        val names = name.split(" ")
                        val firstName = names.firstOrNull() ?: ""
                        val lastName = if (names.size > 1) names.last() else ""
                        
                        val newUser = User(
                            id = userId,
                            firebaseUid = userId,
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            displayName = name,
                            roles = emptyList(),
                            activeRole = null,
                            createdAt = Date().toString()
                        )
                        
                        try {
                            val saveResult = withTimeout(15000) { userRepository.saveUser(newUser) }
                            saveResult.onFailure {
                                Log.e(TAG, "Failed to save user to DB after registration: ${it.message}")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Timeout saving user to DB: ${e.message}")
                        }
                        
                        _currentUser.value = newUser
                        Log.d(TAG, "Triggering initial verification email")
                        sendEmailVerification()
                        _authState.value = AuthState.RequiresEmailVerification(email)
                        
                        viewModelScope.launch {
                            alertManager.showAlert(
                                title = "Account Created",
                                message = "Your premium account has been created successfully. Please verify your email to continue."
                            )
                        }
                    },
                    onFailure = { error ->
                        val msg = error.message ?: "Registration failed"
                        Log.e(TAG, "Firebase registration failed: $msg", error)
                        _authState.value = AuthState.Error(msg)
                        viewModelScope.launch {
                            alertManager.showSnackbar(msg)
                        }
                    }
                )
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                Log.e(TAG, "Registration timed out")
                _authState.value = AuthState.Error("Registration timed out. Please check your internet connection.")
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected registration error: ${e.message}", e)
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun sendEmailVerification() {
        val user = _currentUser.value
        Log.i(TAG, "Sending verification email to: ${user?.email}")
        viewModelScope.launch {
            authRepository.sendEmailVerification().onSuccess {
                Log.d(TAG, "Firebase verification email sent successfully")
                alertManager.showSnackbar("Verification email sent")
            }.onFailure {
                Log.e(TAG, "Firebase failed to send verification email: ${it.message}", it)
                alertManager.showSnackbar("Failed to send verification email: ${it.message}")
            }
        }
    }

    fun checkEmailVerification() {
        Log.i(TAG, "Checking email verification status...")

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // IMPORTANT:
                // Refresh Firebase's local user object first.
                authRepository.reloadUser()
                    .onFailure { error ->
                        Log.e(
                            TAG,
                            "Failed to reload Firebase user: ${error.message}",
                            error
                        )

                        _authState.value =
                            AuthState.RequiresEmailVerification(
                                authRepository.getCurrentUserEmail() ?: ""
                            )

                        alertManager.showSnackbar(
                            "Could not refresh verification status. Please try again."
                        )

                        return@launch
                    }

                // Now Firebase should contain the latest verification status.
                val verified = authRepository
                    .isEmailVerified()
                    .getOrDefault(false)

                Log.i(TAG, "Firebase email verification status = $verified")

                if (!verified) {
                    Log.w(TAG, "Email is still not verified")

                    _authState.value =
                        AuthState.RequiresEmailVerification(
                            authRepository.getCurrentUserEmail() ?: ""
                        )

                    alertManager.showSnackbar(
                        "Email is not verified yet. Please open the verification email and click the verification link."
                    )

                    return@launch
                }

                // Firebase says the email IS verified.
                val userId = authRepository.getCurrentUserId()

                if (userId == null) {
                    Log.e(TAG, "No Firebase user ID found after verification")

                    _authState.value =
                        AuthState.Error("User session expired. Please log in again.")

                    return@launch
                }

                // Get propertyOS user profile.
                val user = userRepository.getUserById(userId)

                if (user != null) {

                    // Update BOTH application verification fields.
                    val updatedUser = user.copy(
                        emailVerified = true,
                        hasVerifiedContact = true
                    )

                    userRepository.saveUser(updatedUser)

                    _currentUser.value = updatedUser

                    Log.i(
                        TAG,
                        "propertyOS profile updated: emailVerified=true"
                    )
                }

                _authState.value = AuthState.Success

                alertManager.showBanner(
                    "Email verified successfully!",
                    BannerType.SUCCESS
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Unexpected email verification error: ${e.message}",
                    e
                )

                _authState.value =
                    AuthState.RequiresEmailVerification(
                        authRepository.getCurrentUserEmail() ?: ""
                    )

                alertManager.showSnackbar(
                    "Unable to check email verification: ${e.message}"
                )
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.sendPasswordReset(email).onSuccess {
                _authState.value = AuthState.Idle
                alertManager.showBanner("Password reset link sent to $email", BannerType.SUCCESS)
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Failed to send reset link")
            }
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.updateEmail(newEmail).onSuccess {
                _authState.value = AuthState.RequiresEmailVerification(newEmail)
                alertManager.showBanner("Verification link sent to $newEmail. Please verify to complete update.", BannerType.INFO)
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Failed to initiate email change")
            }
        }
    }

    fun updateUserRole(role: UserRole) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: run {
                Log.e(TAG, "No user ID found for role update")
                return@launch
            }
            _authState.value = AuthState.Loading
            userRepository.updateUserRole(userId, role).onSuccess {
                Log.i(TAG, "User role updated to $role in DB")
                val updatedUser = userRepository.getUserById(userId)
                if (updatedUser != null) {
                    Log.d(TAG, "Emitting updated user with activeRole: ${updatedUser.activeRole}")
                    _currentUser.value = updatedUser
                    _authState.value = AuthState.Success
                } else {
                    Log.e(TAG, "Failed to fetch updated user after role change")
                    _authState.value = AuthState.Error("Failed to load updated profile")
                }
            }.onFailure {
                Log.e(TAG, "Failed to update role in DB: ${it.message}")
                _authState.value = AuthState.Error(it.message ?: "Failed to update role")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentUser.value = null
            _authState.value = AuthState.Idle
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun prepareForRoleSwitch() {
        _authState.value = AuthState.Idle
        // We don't clear _currentUser because we need their ID to update the role
    }

    fun startPhoneAuth(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        authRepository.startPhoneVerification(
            phoneNumber = phoneNumber,
            activity = activity,
            onCodeSent = { id ->
                _verificationId = id
                _authState.value = AuthState.Idle // Ready for OTP input
            },
            onVerificationFailed = { e ->
                _authState.value = AuthState.Error(e.message ?: "Phone verification failed")
            }
        )
    }

    fun verifyOtp(otp: String) {
        val id = _verificationId ?: return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithPhone(id, otp)
            result.fold(
                onSuccess = { userId ->
                    val user = userRepository.getUserById(userId)
                    if (user == null) {
                        val newUser = User(id = userId, roles = emptyList(), activeRole = null)
                        userRepository.saveUser(newUser)
                        _currentUser.value = newUser
                    } else {
                        _currentUser.value = user
                    }
                    _authState.value = AuthState.Success
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Invalid OTP")
                }
            )
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class RequiresEmailVerification(val email: String) : AuthState()
    data class RequiresPhoneVerification(val phoneNumber: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
