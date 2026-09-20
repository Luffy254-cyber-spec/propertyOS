package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LandlordTenantDetailsUIState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val apartmentName: String = "",
    val apartmentId: String = "",
    val unitName: String = "",
    val unitId: String = "",
    val monthlyRent: Double = 0.0,
    val rentBalance: Double = 0.0,
    val tenancyStatus: String = "Active",
    val profilePhotoUrl: String? = null,
    val joinDate: String = "N/A"
)

@HiltViewModel
class TenantDetailsViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _tenant = MutableStateFlow<LandlordTenantDetailsUIState?>(null)
    val tenant: StateFlow<LandlordTenantDetailsUIState?> = _tenant.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadTenant(tenantId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val userId = authRepository.getCurrentUserId() ?: return@launch
                
                // 1. Get Tenant Summary from Landlord Path
                val summaryResult = landlordRepository.getTenant(userId, tenantId)
                val summary = summaryResult.getOrNull() ?: throw Exception("Tenant record not found in your directory")
                
                // 2. Get Full User Profile for Phone/Email
                val userProfile = userRepository.getUserById(tenantId)
                
                // 3. Get Unit Details for Monthly Rent
                var monthlyRent = 0.0
                if (summary.propertyId.isNotEmpty()) {
                    val unitsResult = propertyRepository.getUnits(summary.propertyId)
                    unitsResult.getOrNull()?.find { it.id == summary.unitId || it.name == summary.unitName }?.let {
                        monthlyRent = it.monthlyRent
                    }
                }

                _tenant.value = LandlordTenantDetailsUIState(
                    id = summary.id,
                    name = summary.name,
                    email = userProfile?.email ?: "",
                    phoneNumber = summary.phoneNumber ?: userProfile?.phoneNumber ?: "Not available",
                    apartmentName = summary.propertyName,
                    apartmentId = summary.propertyId,
                    unitName = summary.unitName,
                    unitId = summary.unitId,
                    monthlyRent = monthlyRent,
                    rentBalance = summary.rentBalance,
                    tenancyStatus = summary.tenancyStatus,
                    profilePhotoUrl = userProfile?.profileImageUrl
                )
            } catch (e: Exception) {
                Log.e("TenantDetailsVM", "Error loading tenant", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
