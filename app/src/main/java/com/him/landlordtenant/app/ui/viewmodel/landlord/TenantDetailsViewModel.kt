package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantDetailsViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _tenant = MutableStateFlow<TenantDashboardUIState?>(null)
    val tenant: StateFlow<TenantDashboardUIState?> = _tenant.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTenant(tenantId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getTenants(userId).onSuccess { list ->
                val data = list.find { it.id == tenantId }
                if (data != null) {
                    _tenant.value = TenantDashboardUIState(
                        tenantName = data.name,
                        apartmentName = data.propertyName,
                        apartmentId = data.propertyId,
                        houseNumber = data.unitName,
                        floorNumber = "N/A",
                        houseType = "N/A",
                        location = "N/A",
                        landlordId = userId,
                        landlordName = "Me",
                        landlordPhone = "0700000000",
                        monthlyRent = 0.0, // Should be fetched from unit
                        waterBill = 0.0,
                        garbageFee = 0.0,
                        serviceCharge = 0.0,
                        outstandingAmount = data.rentBalance,
                        totalDue = data.rentBalance,
                        dueDate = "N/A",
                        nextPaymentDate = "N/A",
                        rentStatus = if (data.rentBalance <= 0) TenantRentStatus.PAID else TenantRentStatus.OVERDUE,
                        occupancyStatus = TenantOccupancyStatus.ACTIVE
                    )
                }
            }
            _isLoading.value = false
        }
    }
}
