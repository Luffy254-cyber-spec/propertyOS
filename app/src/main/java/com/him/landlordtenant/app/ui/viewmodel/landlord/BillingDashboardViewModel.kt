package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PaymentRepository
import com.him.landlordtenant.app.ui.screens.tenant.LandlordBillingUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LandlordBillingUIState(
            totalOutstanding = "KSh 0",
            thisMonthCollection = "KSh 0",
            arrears = "KSh 0",
            collectedAmount = "KSh 0",
            pendingAmount = "KSh 0",
            overdueAmount = "KSh 0"
        )
    )
    val uiState: StateFlow<LandlordBillingUIState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            paymentRepository.getLandlordPayments(userId).onSuccess { payments ->
                val successful = payments.filter { it.status == com.him.landlordtenant.app.enums.PaymentStatus.SUCCESS || it.status == com.him.landlordtenant.app.enums.PaymentStatus.RECONCILED }
                val pending = payments.filter { it.status == com.him.landlordtenant.app.enums.PaymentStatus.INITIATED || it.status == com.him.landlordtenant.app.enums.PaymentStatus.PROCESSING }
                
                val totalCollected = successful.sumOf { it.amount }
                val totalPending = pending.sumOf { it.amount }
                
                _uiState.value = LandlordBillingUIState(
                    totalOutstanding = "KSh ${String.format("%,.0f", totalPending)}",
                    thisMonthCollection = "KSh ${String.format("%,.0f", totalCollected)}", // Simplified
                    arrears = "KSh 0",
                    collectedAmount = "KSh ${String.format("%,.0f", totalCollected)}",
                    pendingAmount = "KSh ${String.format("%,.0f", totalPending)}",
                    overdueAmount = "KSh 0"
                )
            }
            _isLoading.value = false
        }
    }
}
