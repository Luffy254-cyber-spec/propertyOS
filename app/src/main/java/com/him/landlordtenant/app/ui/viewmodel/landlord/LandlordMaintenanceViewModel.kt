package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.interfaces.LandlordMaintenanceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordMaintenanceViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _requests = MutableStateFlow<List<LandlordMaintenanceData>>(emptyList())
    val requests: StateFlow<List<LandlordMaintenanceData>> = _requests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRequests()
    }

    fun loadRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getMaintenanceRequests(userId).onSuccess {
                _requests.value = it
            }
            _isLoading.value = false
        }
    }
}
