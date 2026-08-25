package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.interfaces.MarketplacePropertyListingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApartmentManagementViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository
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
            propertyListingRepository.getListing(id).onSuccess {
                _apartment.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load apartment"
            }
            _isLoading.value = false
        }
    }
}
