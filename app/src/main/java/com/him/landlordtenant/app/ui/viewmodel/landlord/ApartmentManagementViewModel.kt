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

@HiltViewModel
class ApartmentManagementViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _apartment = MutableStateFlow<MarketplacePropertyListingData?>(null)
    val apartment: StateFlow<MarketplacePropertyListingData?> = _apartment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadApartment(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Try fetching from listing repository first ( Marketplace data )
            val listingResult = propertyListingRepository.getListing(id)
            if (listingResult.isSuccess) {
                _apartment.value = listingResult.getOrNull()
            } else {
                // If listing not found, try management repository and convert
                propertyRepository.getProperty(id).onSuccess { prop ->
                    _apartment.value = MarketplacePropertyListingData(
                        id = prop.id,
                        propertyId = prop.id,
                        ownerId = prop.ownerId,
                        title = prop.name,
                        description = prop.description ?: "",
                        totalUnits = prop.totalUnits,
                        availableUnits = prop.availableUnits,
                        monthlyRent = prop.startingRent,
                        location = ListingLocationData(
                            latitude = prop.latitude ?: 0.0,
                            longitude = prop.longitude ?: 0.0,
                            county = prop.county ?: "",
                            town = prop.town ?: "",
                            address = prop.address
                        ),
                        media = prop.media.map { ListingMediaData(fileUrl = it.url) }
                    )
                }.onFailure {
                    _error.value = "Property not found in any database."
                }
            }
            _isLoading.value = false
        }
    }
}
