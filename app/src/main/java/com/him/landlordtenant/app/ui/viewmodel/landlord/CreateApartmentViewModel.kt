package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException

@HiltViewModel
class CreateApartmentViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository,
    private val authRepository: AuthRepository,
    private val agreementRepository: AgreementRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun createApartment(
        name: String,
        location: String,
        county: String,
        description: String,
        totalUnits: String,
        rules: String,
        proofUri: String?,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val userId = authRepository.getCurrentUserId() ?: run {
                    _error.value = "User not logged in"
                    return@launch
                }

                // 1. Upload proof image to Cloudinary if it exists
                var finalProofUrl: String? = null
                if (proofUri != null) {
                    documentRepository.uploadImage(proofUri).fold(
                        onSuccess = { finalProofUrl = it },
                        onFailure = { 
                            _error.value = "Failed to upload agreement proof: ${it.message}"
                            return@launch 
                        }
                    )
                }

                val propertyId = "prop_${System.currentTimeMillis()}"

                val listingData = CreatePropertyListingData(
                    propertyId = propertyId,
                    unitId = null,
                    ownerId = userId,
                    brokerId = null,
                    title = name,
                    description = description,
                    listingType = ListingType.RENT,
                    propertyType = PropertyType.APARTMENT,
                    monthlyRent = 0.0,
                    salePrice = null,
                    depositAmount = 0.0,
                    bedrooms = 0,
                    bathrooms = 0,
                    totalUnits = totalUnits.toIntOrNull() ?: 0,
                    availableUnits = totalUnits.toIntOrNull() ?: 0,
                    availableFrom = "Immediately",
                    amenities = emptyList(),
                    location = ListingLocationData(
                        latitude = 0.0,
                        longitude = 0.0,
                        county = county,
                        town = location,
                        estate = null,
                        address = null,
                        directions = null
                    )
                )

                // Add a 15-second timeout for the entire creation process
                withTimeout(15000) {
                    propertyListingRepository.createListing(userId, listingData).onSuccess { id ->
                        try {
                            // Create agreement with its own small timeout
                            withTimeout(5000) {
                                val agreementData = AgreementCreateData(
                                    landlordId = userId,
                                    tenantId = "", // Template
                                    propertyId = id,
                                    unitId = "GENERAL",
                                    startDate = "TBD",
                                    endDate = null,
                                    monthlyRent = 0.0,
                                    securityDeposit = 0.0,
                                    paymentDueDay = 5,
                                    noticePeriodDays = 30,
                                    rules = rules.split("\n").filter { it.isNotBlank() },
                                    agreementProofUrl = finalProofUrl
                                )
                                agreementRepository.createAgreement(userId, agreementData)
                            }
                        } catch (e: Exception) {
                            println("Agreement creation failed or timed out: ${e.message}")
                        }
                        onSuccess(id)
                    }.onFailure { e ->
                        _error.value = e.message ?: "Failed to create property"
                    }
                }
            } catch (e: TimeoutCancellationException) {
                _error.value = "Request timed out. Please check your internet connection."
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
