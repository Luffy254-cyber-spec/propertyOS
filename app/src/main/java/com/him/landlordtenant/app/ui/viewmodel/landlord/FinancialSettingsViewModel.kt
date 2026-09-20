package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig
import com.him.landlordtenant.app.interfaces.PropertyBillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FinancialSettingsViewModel @Inject constructor(
    private val billingRepository: PropertyBillingRepository,
    private val firebaseDataSource: com.him.landlordtenant.app.data.remote.FirebaseDataSource
) : ViewModel() {

    private val _config = MutableStateFlow<PaymentChannelConfig?>(null)
    val config: StateFlow<PaymentChannelConfig?> = _config.asStateFlow()

    private val _waterRate = MutableStateFlow(100.0)
    val waterRate: StateFlow<Double> = _waterRate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun loadConfig(propertyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            billingRepository.getPaymentConfig(propertyId).onSuccess {
                _config.value = it ?: PaymentChannelConfig(propertyId = propertyId)
            }
            
            // Load Water Rate
            try {
                val snapshot = firebaseDataSource.getReference("landlord_settings/$propertyId/water_rate").get().await()
                _waterRate.value = snapshot.getValue(Double::class.java) ?: 100.0
            } catch (e: Exception) {
                _waterRate.value = 100.0
            }
            
            _isLoading.value = false
        }
    }

    fun saveConfig(config: PaymentChannelConfig, waterRate: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val paymentResult = billingRepository.savePaymentConfig(config)
            
            // Save Water Rate
            firebaseDataSource.getReference("landlord_settings/${config.propertyId}/water_rate").setValue(waterRate)
            _waterRate.value = waterRate

            if (paymentResult.isSuccess) {
                _saveSuccess.value = true
            }
            _isLoading.value = false
        }
    }

    fun resetSuccess() {
        _saveSuccess.value = false
    }
}
