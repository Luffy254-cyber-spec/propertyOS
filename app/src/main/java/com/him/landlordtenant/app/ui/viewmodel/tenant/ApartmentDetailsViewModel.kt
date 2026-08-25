package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApartmentDetailsViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository,
    private val tenantRepository: com.him.landlordtenant.app.interfaces.TenantRepository,
    private val authRepository: com.him.landlordtenant.app.interfaces.AuthRepository,
    private val userRepository: com.him.landlordtenant.app.interfaces.UserRepository
) : ViewModel() {

    private val _apartment = MutableStateFlow<TenantApartmentUIModel?>(null)
    val apartment: StateFlow<TenantApartmentUIModel?> = _apartment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _joinSuccess = MutableStateFlow(false)
    val joinSuccess: StateFlow<Boolean> = _joinSuccess.asStateFlow()

    fun loadApartment(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            propertyListingRepository.getListing(id).onSuccess { listing ->
                val landlord = userRepository.getUserById(listing.ownerId)
                
                _apartment.value = TenantApartmentUIModel(
                    id = listing.id,
                    name = listing.title,
                    county = listing.location.county,
                    location = listing.location.town,
                    description = listing.description,
                    availableUnits = listing.availableUnits,
                    totalUnits = listing.totalUnits,
                    startingRent = listing.monthlyRent ?: 0.0,
                    highestRent = listing.monthlyRent ?: 0.0,
                    rating = 4.8,
                    verified = listing.verified,
                    distanceKm = 0.0,
                    houseTypes = emptyList(),
                    images = listing.media.map { it.fileUrl },
                    landlordName = landlord?.fullName ?: "Landlord",
                    landlordPhone = landlord?.phoneNumber ?: "N/A"
                )
            }.onFailure {
                // Handle error
            }
            _isLoading.value = false
        }
    }

    fun joinApartment(apartmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            tenantRepository.joinApartment(userId, apartmentId).onSuccess {
                _joinSuccess.value = true
            }
            _isLoading.value = false
        }
    }
}

