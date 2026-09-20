package com.him.landlordtenant.app.ui.viewmodel.tenant

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    private val _propertyAgreement = MutableStateFlow<TenantAgreementUIModel?>(null)
    val propertyAgreement: StateFlow<TenantAgreementUIModel?> = _propertyAgreement.asStateFlow()

    fun loadPropertyAgreement(propertyId: String, houseNumber: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Search all agreements for this property and find the one with unitId = "GENERAL"
                val allAgreementsRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("agreements")
                val snapshot = allAgreementsRef.orderByChild("propertyId").equalTo(propertyId).get().await()
                
                val agreementData = snapshot.children.mapNotNull { 
                    it.getValue(AgreementDetailsData::class.java) 
                }.find { it.unitId == "GENERAL" }

                if (agreementData != null) {
                    val rules = agreementData.getRulesList()
                    val content = if (houseNumber != null && houseNumber != "GENERAL") {
                        "UNIT SPECIFIC NOTICE: This application and subsequent agreement is for Unit $houseNumber.\n\n" + rules.joinToString("\n")
                    } else {
                        rules.joinToString("\n")
                    }

                    _propertyAgreement.value = TenantAgreementUIModel(
                        agreementId = agreementData.id,
                        apartmentId = propertyId,
                        agreementVersion = "1.0",
                        apartmentName = agreementData.propertyName,
                        houseNumber = houseNumber ?: "GENERAL",
                        floorNumber = "0",
                        landlordId = agreementData.landlordId,
                        landlordName = agreementData.landlordName,
                        tenantName = "",
                        createdDate = "Just now",
                        effectiveDate = agreementData.startDate,
                        monthlyRent = agreementData.monthlyRent,
                        deposit = agreementData.securityDeposit,
                        noticePeriodDays = agreementData.noticePeriodDays,
                        agreementContent = content,
                        agreementProofUrl = agreementData.agreementProofUrl
                    )
                }
            } catch (e: Exception) {
                Log.e("TenantJoiningVM", "Failed to load agreement: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun joinApartment(
        apartmentId: String,
        signature: String,
        idUri: Uri?,
        houseId: String? = null,
        houseNumber: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val userId = authRepository.getCurrentUserId() ?: throw Exception("User not logged in")
                
                // Fetch full user profile for application
                val userResult = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("users/$userId").get().await()
                val user = userResult.getValue(com.him.landlordtenant.app.data.model.User::class.java) ?: throw Exception("User profile not found")

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

                // 2. Perform Apply
                val propertyRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("listings/$apartmentId").get().await()
                val propertyName = propertyRef.child("title").getValue(String::class.java) ?: "Apartment"
                val landlordId = propertyRef.child("ownerId").getValue(String::class.java) ?: ""

                if (landlordId.isEmpty()) throw Exception("Landlord info missing")

                val applicationId = "APP_${userId}_${System.currentTimeMillis()}"
                val application = ApartmentApplicationData(
                    id = applicationId,
                    tenantId = userId,
                    tenantName = user.fullName,
                    tenantEmail = user.email ?: "",
                    tenantPhone = user.phoneNumber ?: "",
                    apartmentId = apartmentId,
                    apartmentName = propertyName,
                    houseId = houseId ?: "",
                    houseNumber = houseNumber ?: "",
                    landlordId = landlordId,
                    status = "PENDING",
                    signature = signature,
                    nationalIdUrl = idUrl ?: "",
                    appliedAt = System.currentTimeMillis()
                )

                // Save to Firebase
                val db = com.google.firebase.database.FirebaseDatabase.getInstance()
                db.getReference("applications/$landlordId/$applicationId").setValue(application).await()
                db.getReference("tenant_applications/$userId/$applicationId").setValue(application).await()

                // Log Success Activity
                activityRepository.logActivity(
                    userId = userId,
                    activity = CreateActivityData(
                        title = "Application Submitted",
                        subtitle = "Applied to $propertyName ${if (!houseNumber.isNullOrEmpty()) "Unit $houseNumber" else ""}",
                        type = "TENANT",
                        status = "SUCCESS"
                    )
                )
                
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
