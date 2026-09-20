package com.him.landlordtenant.app.ui.viewmodel.guest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
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
            propertyRepository.getAvailableProperties(1, 50).onSuccess { listings ->
                _apartments.value = listings.map { listing ->
                    TenantApartmentUIModel(
                        id = listing.id,
                        name = listing.name,
                        county = listing.county ?: "",
                        location = listing.location,
                        description = "", 
                        availableUnits = listing.availableUnits,
                        totalUnits = listing.totalUnits,
                        startingRent = listing.startingRent,
                        highestRent = listing.startingRent,
                        rating = 4.5,
                        verified = listing.verified,
                        distanceKm = 0.0,
                        houseTypes = emptyList(),
                        images = listOfNotNull(listing.primaryImageUrl)
                    )
                }
            }
            _isLoading.value = false
        }
    }
}
