package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AgreementRepository
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ContractDigitalSignatureData
import com.him.landlordtenant.app.interfaces.SignatureMethod
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantAgreementViewModel @Inject constructor(
    private val agreementRepository: AgreementRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _agreement = MutableStateFlow<TenantAgreementUIModel?>(null)
    val agreement: StateFlow<TenantAgreementUIModel?> = _agreement.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadAgreement() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: run {
                _error.value = "User not logged in"
                _isLoading.value = false
                return@launch
            }

            // In a real app, we might need to find which agreement belongs to the tenant.
            // For now, let's try to get agreements by tenant.
            agreementRepository.getTenantAgreements(userId).onSuccess { agreements ->
                val latestAgreement = agreements.firstOrNull()
                if (latestAgreement != null) {
                    agreementRepository.getAgreement(latestAgreement.id).onSuccess { details ->
                        _agreement.value = TenantAgreementUIModel(
                            agreementId = details.id,
                            apartmentId = details.propertyId,
                            agreementVersion = "1.0",
                            apartmentName = details.propertyName,
                            houseNumber = details.unitName,
                            floorNumber = "0",
                            landlordName = details.landlordName,
                            tenantName = details.tenantName,
                            createdDate = "N/A",
                            effectiveDate = details.startDate,
                            monthlyRent = details.monthlyRent,
                            deposit = details.securityDeposit,
                            noticePeriodDays = details.noticePeriodDays,
                            agreementContent = details.rules.joinToString("\n"),
                            agreementProofUrl = details.agreementProofUrl,
                            isAlreadyAccepted = details.status == "TENANT_ACCEPTED" || details.tenantSigned,
                            acceptedDate = if (details.tenantSigned) "Recently" else null
                        )
                    }.onFailure {
                        _error.value = "Failed to load agreement details: ${it.message}"
                    }
                } else {
                    _error.value = "No agreement found for this tenant"
                }
            }.onFailure {
                _error.value = "Failed to fetch agreements: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun acceptAgreement(uiModel: TenantAgreementUIModel) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            _isLoading.value = true
            
            // 1. Sign the agreement
            val signature = ContractDigitalSignatureData(
                signerId = userId,
                signerName = uiModel.tenantName,
                signatureImageUrl = null,
                signedAt = System.currentTimeMillis().toString(),
                ipAddress = "0.0.0.0", // Mock
                deviceInfo = android.os.Build.MODEL,
                signatureMethod = SignatureMethod.VERIFIED_ELECTRONIC_SIGNATURE
            )
            
            agreementRepository.signAsTenant(userId, uiModel.agreementId, signature).onSuccess {
                // 2. Update status
                agreementRepository.acceptInvitation(userId, uiModel.agreementId).onSuccess {
                    loadAgreement() // Refresh
                }.onFailure {
                    _error.value = "Failed to update agreement status: ${it.message}"
                }
            }.onFailure {
                _error.value = "Failed to sign agreement: ${it.message}"
            }
            _isLoading.value = false
        }
    }
}
