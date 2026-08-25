package com.him.landlordtenant.app.ui.viewmodel.auth

import android.app.Activity
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
            viewModelScope.launch {
                try {
                    val result = withTimeout(5000) { userRepository.getUserById(userId) }
                    _currentUser.value = result
                } catch (e: Exception) {
                    // Ignore background check failure
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(15000) { authRepository.login(email, password) }
                result.fold(
                    onSuccess = { userId ->
                        val user = try {
                            withTimeout(5000) { userRepository.getUserById(userId) }
                        } catch (e: Exception) { null }
                        
                        _currentUser.value = user
                        
                        // Send login alert
                        emailService.sendLoginAlert(email, android.os.Build.MODEL)

                        if (user?.emailVerified == false) {
                            _authState.value = AuthState.RequiresEmailVerification(email)
                        } else if (user?.phoneVerified == false) {
                            _authState.value = AuthState.RequiresPhoneVerification(user.phoneNumber ?: "")
                        } else {
                            _authState.value = AuthState.Success
                            viewModelScope.launch {
                                alertManager.showBanner("Welcome back, ${user?.displayName ?: "User"}!", BannerType.SUCCESS)
                            }
                        }
                    },
                    onFailure = { error ->
                        val msg = error.message ?: "Login failed"
                        _authState.value = AuthState.Error(msg)
                        viewModelScope.launch {
                            alertManager.showSnackbar(msg)
                        }
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login timed out. Please check your connection.")
            }
        }
    }

    fun signInWithGoogle(idToken: String, role: UserRole = UserRole.TENANT) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(15000) { authRepository.signInWithGoogle(idToken) }
                result.fold(
                    onSuccess = { userId ->
                        val existingUser = try {
                            withTimeout(5000) { userRepository.getUserById(userId) }
                        } catch (e: Exception) { null }

                        if (existingUser == null) {
                            val firebaseUser = authRepository.getCurrentUser().getOrNull()
                            val newUser = User(
                                id = userId,
                                firebaseUid = userId,
                                email = firebaseUser?.email,
                                firstName = firebaseUser?.fullName ?: "User",
                                displayName = firebaseUser?.fullName ?: "User",
                                roles = listOf(role),
                                activeRole = role,
                                createdAt = Date().toString()
                            )
                            try {
                                withTimeout(10000) { userRepository.saveUser(newUser) }
                            } catch (e: Exception) {
                                // Even if save fails, we can proceed if auth succeeded
                            }
                            _currentUser.value = newUser
                            _authState.value = AuthState.RequiresEmailVerification(newUser.email ?: "")
                        } else {
                            _currentUser.value = existingUser
                            _authState.value = AuthState.Success
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
                        _authState.value = AuthState.Error(msg)
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Google Sign-In timed out.")
            }
        }
    }

    fun register(name: String, email: String, password: String, role: UserRole = UserRole.LANDLORD) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = withTimeout(15000) { authRepository.register(name, email, password) }
                result.fold(
                    onSuccess = { userId ->
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
                            roles = listOf(role),
                            activeRole = role,
                            createdAt = Date().toString()
                        )
                        
                        try {
                            val saveResult = withTimeout(10000) { userRepository.saveUser(newUser) }
                            saveResult.onFailure {
                                // Log but proceed? User exists in Auth anyway.
                            }
                        } catch (e: Exception) {
                            // Timeout saving, but Auth succeeded.
                        }
                        
                        _currentUser.value = newUser
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
                        _authState.value = AuthState.Error(msg)
                        viewModelScope.launch {
                            alertManager.showSnackbar(msg)
                        }
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration timed out.")
            }
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            authRepository.sendEmailVerification()
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            authRepository.isEmailVerified().onSuccess { verified ->
                if (verified) {
                    _authState.value = AuthState.Success
                }
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
                        val newUser = User(id = userId, roles = listOf(UserRole.TENANT), activeRole = UserRole.TENANT) // Default role
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
