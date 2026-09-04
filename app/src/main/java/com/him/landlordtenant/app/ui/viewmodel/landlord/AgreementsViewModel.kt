package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AgreementRepository
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreementsViewModel @Inject constructor(
    private val agreementRepository: AgreementRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _agreements = MutableStateFlow<List<TenantAgreementUIModel>>(emptyList())
    val agreements: StateFlow<List<TenantAgreementUIModel>> = _agreements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentAgreement = MutableStateFlow<TenantAgreementUIModel?>(null)
    val currentAgreement: StateFlow<TenantAgreementUIModel?> = _currentAgreement.asStateFlow()

    fun loadAgreements() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            agreementRepository.getLandlordAgreements(userId).onSuccess { list ->
                _agreements.value = list.map { item ->
                    TenantAgreementUIModel(
                        agreementId = item.id,
                        agreementVersion = "1.0",
                        apartmentName = item.propertyName,
                        houseNumber = item.unitName,
                        floorNumber = "0",
                        landlordName = item.landlordName,
                        tenantName = item.tenantName ?: "Pending",
                        createdDate = "N/A",
                        effectiveDate = item.startDate,
                        monthlyRent = item.monthlyRent,
                        deposit = item.monthlyRent, // Fallback to 1 month rent
                        noticePeriodDays = 30,
                        agreementContent = "Agreement content for ${item.id}",
                        isAlreadyAccepted = item.status == "ACTIVE"
                    )
                }
            }
            _isLoading.value = false
        }
    }

    fun loadAgreement(agreementId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            agreementRepository.getAgreement(agreementId).onSuccess { item ->
                _currentAgreement.value = TenantAgreementUIModel(
                    agreementId = item.id,
                    agreementVersion = "1.0",
                    apartmentName = item.propertyName,
                    houseNumber = item.unitName,
                    floorNumber = "0",
                    landlordName = item.landlordName,
                    tenantName = item.tenantName,
                    createdDate = item.createdAt,
                    effectiveDate = item.startDate,
                    monthlyRent = item.monthlyRent,
                    deposit = item.securityDeposit,
                    noticePeriodDays = item.noticePeriodDays,
                    agreementContent = item.rules.joinToString("\n"),
                    isAlreadyAccepted = item.status == "ACTIVE"
                )
            }
            _isLoading.value = false
        }
    }
}
