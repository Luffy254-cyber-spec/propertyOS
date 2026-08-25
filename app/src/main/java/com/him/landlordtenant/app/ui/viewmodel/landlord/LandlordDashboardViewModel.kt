package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.ui.screens.tenant.LandlordDashboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val propertyListingRepository: PropertyListingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LandlordDashboardUIState(
            businessName = "Landlord",
            email = "",
            totalProperties = 0,
            totalUnits = 0,
            totalRevenue = "KSh 0",
            occupancyRate = "0%"
        )
    )
    val uiState: StateFlow<LandlordDashboardUIState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val userName = authRepository.getCurrentUserName() ?: "Landlord"
            val email = authRepository.getCurrentUserEmail() ?: ""

            val listingsResult = propertyListingRepository.getListingsByOwner(userId)
            
            listingsResult.onSuccess { listings ->
                val totalProperties = listings.distinctBy { it.propertyId }.size
                val totalUnits = listings.size
                
                _uiState.value = LandlordDashboardUIState(
                    businessName = userName,
                    email = email,
                    totalProperties = totalProperties,
                    totalUnits = totalUnits,
                    totalRevenue = "KSh 0", // Would fetch from PaymentRepository
                    occupancyRate = "0%"
                )
            }
            _isRefreshing.value = false
        }
    }
}
