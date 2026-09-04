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

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import android.util.Log
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.ui.screens.tenant.LandlordActivityUIModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class CreateApartmentViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository,
    private val propertyRepository: PropertyRepository,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val agreementRepository: AgreementRepository,
    private val documentRepository: DocumentRepository,
    private val activityRepository: ActivityRepository,
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isNameAvailable = MutableStateFlow<Boolean?>(null)
    val isNameAvailable: StateFlow<Boolean?> = _isNameAvailable.asStateFlow()

    private val _isCheckingName = MutableStateFlow(false)
    val isCheckingName: StateFlow<Boolean> = _isCheckingName.asStateFlow()

    private var validationJob: Job? = null

    fun setError(message: String) {
        _error.value = message
    }

    fun validatePropertyName(name: String) {
        validationJob?.cancel()
        if (name.isBlank()) {
            _isNameAvailable.value = null
            return
        }
        
        validationJob = viewModelScope.launch {
            _isCheckingName.value = true
            delay(600) // Debounce for 600ms
            val result = propertyRepository.isPropertyNameTaken(name)
            result.onSuccess { taken ->
                _isNameAvailable.value = !taken
                if (taken) {
                    _error.value = "Property name '$name' is already taken."
                } else if (_error.value?.contains("already taken") == true) {
                    _error.value = null
                }
            }.onFailure {
                _isNameAvailable.value = null
            }
            _isCheckingName.value = false
        }
    }

    fun createApartment(
        name: String,
        propertyType: String,
        startingRent: Double,
        location: String,
        county: String,
        description: String,
        totalUnits: String,
        amenities: List<String>,
        propertyImages: List<String>,
        rules: String,
        proofUri: String?,
        latitude: Double,
        longitude: Double,
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

                // 1. Upload property images in parallel
                val finalPropertyImages = mutableListOf<String>()
                if (propertyImages.isNotEmpty()) {
                    val imageUploadDeferred = propertyImages.map { uri ->
                        viewModelScope.async {
                            documentRepository.uploadImage(uri)
                        }
                    }
                    
                    val imageUploadResults = imageUploadDeferred.awaitAll()
                    
                    imageUploadResults.forEachIndexed { index, result ->
                        result.onSuccess {
                            finalPropertyImages.add(it)
                        }.onFailure {
                            val errorMsg = "Property image ${index + 1} upload failed: ${it.message}"
                            Log.e("CreateApartmentVM", errorMsg)
                            // Continue if some images fail? For now, we fail fast
                            _error.value = errorMsg
                            _isLoading.value = false
                            return@launch
                        }
                    }
                }

                // 2. Upload proof image to Cloudinary if it exists
                var finalProofUrl: String? = null
                if (proofUri != null) {
                    documentRepository.uploadImage(proofUri).onSuccess {
                        finalProofUrl = it
                    }.onFailure {
                        _error.value = "Failed to upload agreement proof: ${it.message}"
                        _isLoading.value = false
                        return@launch
                    }
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
                    propertyType = try { PropertyType.valueOf(propertyType.uppercase()) } catch(e: Exception) { PropertyType.APARTMENT },
                    monthlyRent = startingRent,
                    salePrice = null,
                    depositAmount = startingRent, // Default to 1 month
                    bedrooms = 0,
                    bathrooms = 0,
                    totalUnits = totalUnits.toIntOrNull() ?: 0,
                    availableUnits = totalUnits.toIntOrNull() ?: 0,
                    availableFrom = "Immediately",
                    amenities = amenities.mapNotNull { 
                        try { ListingAmenity.valueOf(it.uppercase()) } catch(e: Exception) { null }
                    },
                    media = finalPropertyImages.map { ListingMediaData(fileUrl = it, type = ListingMediaType.IMAGE) },
                    location = ListingLocationData(
                        latitude = latitude,
                        longitude = longitude,
                        county = county,
                        town = location,
                        estate = null,
                        address = location,
                        directions = null
                    )
                )

                // Add a 15-second timeout for the entire creation process
                withTimeout(15000) {
                    Log.d("CreateApartmentVM", "Attempting to create listing for property: $propertyId")
                    // Create in marketplace listings
                    val listingResult = propertyListingRepository.createListing(userId, listingData)
                    
                    Log.d("CreateApartmentVM", "Attempting to create management property record")
                    // Also create in management properties
                    val propertyCreateData = PropertyCreateData(
                        id = propertyId,
                        name = name,
                        description = description,
                        propertyType = propertyType,
                        address = location,
                        county = county,
                        town = location,
                        latitude = latitude,
                        longitude = longitude,
                        totalUnits = totalUnits.toIntOrNull() ?: 0,
                        startingRent = startingRent,
                        amenities = amenities,
                        media = finalPropertyImages.map { PropertyMediaData(url = it) }
                    )
                    val managementResult = propertyRepository.createProperty(userId, propertyCreateData)

                    if (listingResult.isSuccess || managementResult.isSuccess) {
                        val id = listingResult.getOrNull() ?: managementResult.getOrThrow()
                        
                        // Add images to listing/property
                        // propertyListingRepository.addPhotos(id, finalPropertyImages)

                        // Automatically create community groups
                        try {
                            chatRepository.getOrCreateCommunityConversation(id, userId, name)
                            chatRepository.getOrCreateTenantGroup(id, userId, name)
                            
                            // Log Dashboard Activity (Successful)
                            activityRepository.logActivity(
                                userId = userId,
                                activity = CreateActivityData(
                                    title = "Property Created",
                                    subtitle = "You added $name to your portfolio",
                                    type = "PROPERTY",
                                    status = "SUCCESS"
                                )
                            )
                        } catch (e: Exception) {
                            println("Community group or activity creation failed: ${e.message}")
                        }

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
                                    monthlyRent = startingRent,
                                    securityDeposit = startingRent,
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
                    } else {
                        val errorMsg = listingResult.exceptionOrNull()?.message ?: managementResult.exceptionOrNull()?.message ?: "Failed to create property"
                        _error.value = errorMsg
                        
                        // Log Failure Activity
                        activityRepository.logActivity(
                            userId = userId,
                            activity = CreateActivityData(
                                title = "Property Creation Failed",
                                subtitle = "Error: $errorMsg",
                                type = "PROPERTY",
                                status = "FAILURE"
                            )
                        )
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
