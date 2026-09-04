package com.him.landlordtenant.app.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.User
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.UserRepository
import com.him.landlordtenant.app.interfaces.UserProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val userData = userRepository.getUserById(userId)
            _user.value = userData
            _isLoading.value = false
        }
    }

    fun updateProfile(firstName: String, lastName: String, phoneNumber: String, username: String, bio: String, newEmail: String? = null) {
        val currentUser = _user.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            
            // If email is changing, initiate Firebase email update (sends verification to new email)
            if (newEmail != null && newEmail != currentUser.email) {
                authRepository.updateEmail(newEmail).onFailure {
                    // Log error but proceed with other profile changes? 
                    // Better to stop if email update is critical.
                }
            }

            val updatedUser = currentUser.copy(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                username = username,
                bio = bio,
                displayName = "$firstName $lastName"
            )
            userRepository.saveUser(updatedUser).onSuccess {
                _user.value = updatedUser
                _updateSuccess.value = true
            }
            _isLoading.value = false
        }
    }
}
