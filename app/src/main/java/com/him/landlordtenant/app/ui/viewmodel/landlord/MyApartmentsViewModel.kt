package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyApartmentsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val propertyListingRepository: PropertyListingRepository
) : ViewModel() {

    private val _apartments = MutableStateFlow<List<TenantApartmentUIModel>>(emptyList())
    val apartments: StateFlow<List<TenantApartmentUIModel>> = _apartments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadApartments()
    }

    fun loadApartments() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            propertyListingRepository.getListingsByOwner(userId).onSuccess { listings ->
                _apartments.value = listings.map { listing ->
                    TenantApartmentUIModel(
                        id = listing.id,
                        name = listing.title,
                        county = listing.location.county,
                        location = listing.location.town,
                        description = listing.description,
                        availableUnits = listing.availableUnits,
                        totalUnits = listing.totalUnits,
                        startingRent = listing.monthlyRent ?: 0.0,
                        highestRent = listing.monthlyRent ?: 0.0,
                        rating = 4.5,
                        verified = listing.verified,
                        distanceKm = 0.0,
                        houseTypes = emptyList(),
                        images = listing.media.map { it.fileUrl },
                        latitude = listing.location.latitude,
                        longitude = listing.location.longitude
                    )
                }
            }
            _isLoading.value = false
        }
    }
}
