package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyManagementViewModel @Inject constructor(
    private val propertyListingRepository: PropertyListingRepository,
    private val propertyRepository: PropertyRepository,
    private val authRepository: AuthRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _property = MutableStateFlow<PropertyDetailsData?>(null)
    val property: StateFlow<PropertyDetailsData?> = _property.asStateFlow()

    private val _isNameAvailable = MutableStateFlow<Boolean?>(null)
    val isNameAvailable: StateFlow<Boolean?> = _isNameAvailable.asStateFlow()

    private var originalName: String? = null

    fun loadProperty(propertyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            propertyRepository.getProperty(propertyId).onSuccess {
                _property.value = it
                originalName = it.name
                _isNameAvailable.value = true // Originally true since it's the current name
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun validatePropertyName(name: String) {
        if (name.isBlank()) {
            _isNameAvailable.value = null
            return
        }
        if (name == originalName) {
            _isNameAvailable.value = true
            return
        }
        viewModelScope.launch {
            propertyRepository.isPropertyNameTaken(name).onSuccess { taken ->
                _isNameAvailable.value = !taken
            }.onFailure {
                _isNameAvailable.value = null
            }
        }
    }

    fun updateBasicInfo(apartmentId: String, name: String, description: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val update = UpdatePropertyListingData(title = name, description = description)
            propertyListingRepository.updateListing(userId, apartmentId, update).onSuccess {
                onSuccess()
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun updateLocation(apartmentId: String, latitude: Double, longitude: Double, address: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            propertyRepository.updateLocation(userId, apartmentId, latitude, longitude).onSuccess {
                onSuccess()
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun uploadImages(apartmentId: String, imageUris: List<Uri>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepository.getCurrentUserId() ?: return@launch
                val uploadDeferred = imageUris.map { uri ->
                    async { documentRepository.uploadImage(uri.toString()) }
                }
                val results = uploadDeferred.awaitAll()
                val successfulUrls = results.mapNotNull { it.getOrNull() }
                
                // For now, we just simulate the success after uploads
                if (successfulUrls.isNotEmpty()) {
                    onSuccess()
                } else {
                    _error.value = "No images were successfully uploaded"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }
}
