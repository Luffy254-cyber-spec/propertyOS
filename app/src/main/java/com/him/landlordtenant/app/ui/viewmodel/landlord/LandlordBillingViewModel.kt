package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.ui.screens.tenant.LandlordBillingUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordBillingViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _billingState = MutableStateFlow(
        LandlordBillingUIState(
            totalOutstanding = "KSh 0",
            thisMonthCollection = "KSh 0",
            arrears = "KSh 0",
            collectedAmount = "KSh 0",
            pendingAmount = "KSh 0",
            overdueAmount = "KSh 0"
        )
    )
    val billingState: StateFlow<LandlordBillingUIState> = _billingState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBillingData()
    }

    fun loadBillingData() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getRentCollectionSummary(userId).onSuccess { summary ->
                _billingState.value = LandlordBillingUIState(
                    totalOutstanding = "KSh ${summary.outstandingAmount}",
                    thisMonthCollection = "KSh ${summary.collectedAmount}",
                    arrears = "KSh ${summary.outstandingAmount}", // Assuming arrears is same as outstanding for now
                    collectedAmount = "KSh ${summary.collectedAmount}",
                    pendingAmount = "KSh ${summary.expectedAmount - summary.collectedAmount}",
                    overdueAmount = "KSh ${summary.outstandingAmount}"
                )
            }
            _isLoading.value = false
        }
    }
}
