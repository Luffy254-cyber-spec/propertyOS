package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
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
            val userId = authRepository.getCurrentUserId() ?: return@launch
            Log.d("MyApartmentsVM", "Reliable loading for: $userId")
            
            propertyRepository.getPropertiesByOwner(userId).onSuccess { props ->
                _apartments.value = props.map { prop ->
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
            }
            _isLoading.value = false
        }
    }
}
