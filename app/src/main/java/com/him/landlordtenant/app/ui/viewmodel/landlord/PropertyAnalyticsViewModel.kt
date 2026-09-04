package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PropertyAnalyticsData
import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.interfaces.PropertyDetailsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyAnalyticsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _analytics = MutableStateFlow<PropertyAnalyticsData?>(null)
    val analytics: StateFlow<PropertyAnalyticsData?> = _analytics.asStateFlow()

    private val _property = MutableStateFlow<PropertyDetailsData?>(null)
    val property: StateFlow<PropertyDetailsData?> = _property.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadData(propertyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            propertyRepository.getProperty(propertyId).onSuccess {
                _property.value = it
            }
            
            propertyRepository.getPropertyAnalytics(userId, propertyId).onSuccess {
                _analytics.value = it
            }
            _isLoading.value = false
        }
    }
}
