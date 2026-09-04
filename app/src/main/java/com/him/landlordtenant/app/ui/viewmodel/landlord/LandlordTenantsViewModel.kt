package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.interfaces.TenantSummaryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordTenantsViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _tenants = MutableStateFlow<List<TenantSummaryData>>(emptyList())
    val tenants: StateFlow<List<TenantSummaryData>> = _tenants.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTenants()
    }

    fun loadTenants() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getTenants(userId).onSuccess {
                _tenants.value = it
            }
            _isLoading.value = false
        }
    }
}
