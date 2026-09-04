package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyApartmentsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val propertyListingRepository: PropertyListingRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _apartments = MutableStateFlow<List<TenantApartmentUIModel>>(emptyList())
    val apartments: StateFlow<List<TenantApartmentUIModel>> = _apartments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadApartments()
    }

    fun refresh() {
        loadApartments()
    }

    fun loadApartments() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: run {
                Log.e("MyApartmentsVM", "No user ID found")
                _isLoading.value = false
                return@launch
            }
            Log.d("MyApartmentsVM", "Loading properties for user: $userId")
            
            val listingsResult = propertyListingRepository.getListingsByOwner(userId)
            val managementResult = propertyRepository.getPropertiesByOwner(userId)
            
            val propertiesFromListings = listingsResult.getOrDefault(emptyList()).map { listing ->
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
                    longitude = listing.location.longitude,
                    totalRevenue = "KSh 0" 
                )
            }
            
            val managementProperties = managementResult.getOrDefault(emptyList()).map { prop ->
                TenantApartmentUIModel(
                    id = prop.id,
                    name = prop.name,
                    county = prop.county ?: "",
                    location = prop.town ?: "",
                    description = prop.description ?: "",
                    availableUnits = prop.availableUnits,
                    totalUnits = prop.totalUnits,
                    startingRent = prop.startingRent,
                    highestRent = prop.startingRent,
                    rating = 4.5,
                    verified = prop.verified,
                    distanceKm = 0.0,
                    houseTypes = emptyList(),
                    images = prop.media.map { it.url },
                    latitude = prop.latitude ?: 0.0,
                    longitude = prop.longitude ?: 0.0,
                    totalRevenue = "KSh 0"
                )
            }
            
            val combined = (propertiesFromListings + managementProperties).distinctBy { it.id.ifEmpty { it.name } }
            Log.d("MyApartmentsVM", "Total unique properties: ${combined.size}")
            _apartments.value = combined
            _isLoading.value = false
        }
    }
}
