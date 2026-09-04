package com.him.landlordtenant.app.ui.viewmodel.tenant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantJoiningViewModel @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val authRepository: AuthRepository,
    private val documentRepository: DocumentRepository,
    private val agreementRepository: AgreementRepository,
    private val chatRepository: ChatRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun joinApartment(
        apartmentId: String,
        signature: String,
        idUri: Uri?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val userId = authRepository.getCurrentUserId() ?: throw Exception("User not logged in")
                
                // 1. Upload ID Image
                var idUrl: String? = null
                if (idUri != null) {
                    val uploadResult = documentRepository.uploadImage(idUri.toString())
                    uploadResult.onSuccess { url ->
                        idUrl = url
                    }.onFailure {
                        throw Exception("Failed to upload National ID: ${it.message}")
                    }
                } else {
                    throw Exception("National ID photo is required")
                }

                // 2. Perform Join & Sign
                tenantRepository.joinApartment(userId, apartmentId, signature, idUrl).onSuccess {
                    // Create an agreement record or update existing one
                    val agreementId = "AGR_${userId}_${apartmentId}"
                    val digitalSignature = ContractDigitalSignatureData(
                        signerId = userId,
                        signerName = signature,
                        signatureImageUrl = null,
                        signedAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date()),
                        ipAddress = "0.0.0.0", // Placeholder
                        deviceInfo = "Android App",
                        signatureMethod = SignatureMethod.TYPED
                    )
                    agreementRepository.signAsTenant(userId, agreementId, digitalSignature)
                    
                    // 3. Add to community groups
                    viewModelScope.launch {
                        chatRepository.addTenantToCommunityGroup(userId, apartmentId)
                    }
                    
                    // Log Success Activity
                    viewModelScope.launch {
                        activityRepository.logActivity(
                            userId = userId,
                            activity = CreateActivityData(
                                title = "Joined Property",
                                subtitle = "You have successfully joined the apartment community.",
                                type = "TENANT",
                                status = "SUCCESS"
                            )
                        )
                    }
                    
                    onSuccess()
                }.onFailure {
                    // Log Failure Activity
                    viewModelScope.launch {
                        activityRepository.logActivity(
                            userId = userId,
                            activity = CreateActivityData(
                                title = "Join Property Failed",
                                subtitle = it.message ?: "Unknown error occurred",
                                type = "TENANT",
                                status = "FAILURE"
                            )
                        )
                    }
                    throw it
                }
                
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
