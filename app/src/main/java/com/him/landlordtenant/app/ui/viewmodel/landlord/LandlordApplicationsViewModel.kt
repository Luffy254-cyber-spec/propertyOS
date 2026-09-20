package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.ApartmentApplicationData
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordApplicationsViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _applications = MutableStateFlow<List<ApartmentApplicationData>>(emptyList())
    val applications: StateFlow<List<ApartmentApplicationData>> = _applications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadApplications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            landlordRepository.getPendingApplications(userId).onSuccess {
                _applications.value = it
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun processApplication(applicationId: String, approve: Boolean, reason: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val status = if (approve) "APPROVED" else "DECLINED"
            
            landlordRepository.processApplication(userId, applicationId, status, reason).onSuccess {
                loadApplications()
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }
}
