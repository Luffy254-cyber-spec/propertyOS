package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.billing.*
import com.him.landlordtenant.app.domain.billing.BillingUseCase
import com.him.landlordtenant.app.interfaces.PropertyBillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingUseCase: BillingUseCase,
    private val billingRepository: PropertyBillingRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _meterReadingResult = MutableStateFlow<Double?>(null)
    val meterReadingResult: StateFlow<Double?> = _meterReadingResult.asStateFlow()

    fun saveMeterReading(
        tenantId: String,
        unitId: String,
        meterId: String,
        previousReading: Double,
        currentReading: Double,
        rate: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val amount = billingUseCase.calculateMeteredCharge(previousReading, currentReading, rate)
            
            val reading = MeterReading(
                meterId = meterId,
                tenantId = tenantId,
                unitId = unitId,
                previousReading = previousReading,
                currentReading = currentReading,
                consumption = currentReading - previousReading,
                ratePerUnit = rate,
                calculatedAmount = amount,
                status = ReadingStatus.VERIFIED
            )

            billingRepository.saveMeterReading(reading).onSuccess {
                _meterReadingResult.value = amount
                // Optionally trigger invoice update logic here
            }
            _isLoading.value = false
        }
    }

    fun generateMonthlyInvoice(tenantId: String, unitId: String, month: Int, year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            billingUseCase.generateMonthlyInvoice(tenantId, unitId, month, year)
            _isLoading.value = false
        }
    }
}
