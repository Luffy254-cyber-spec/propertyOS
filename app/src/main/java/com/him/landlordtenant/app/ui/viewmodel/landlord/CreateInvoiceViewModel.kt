package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.billing.*
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.data.model.House
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class CreateInvoiceViewModel @Inject constructor(
    private val billingRepository: PropertyBillingRepository,
    private val firebaseDataSource: FirebaseDataSource,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _house = MutableStateFlow<House?>(null)
    val house: StateFlow<House?> = _house.asStateFlow()

    private val _lastWaterReading = MutableStateFlow<MeterReading?>(null)
    val lastWaterReading: StateFlow<MeterReading?> = _lastWaterReading.asStateFlow()

    private val _waterRate = MutableStateFlow(100.0) // Default rate
    val waterRate: StateFlow<Double> = _waterRate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadData(apartmentId: String, floorId: String, houseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load House
                val hSnapshot = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId").get().await()
                val houseData = hSnapshot.getValue(House::class.java)
                _house.value = houseData

                // Load Water Rate from Settings
                val rateSnapshot = firebaseDataSource.getReference("landlord_settings/$apartmentId/water_rate").get().await()
                _waterRate.value = rateSnapshot.getValue(Double::class.java) ?: 100.0

                // Load Last Reading
                billingRepository.getLatestMeterReading("water_$houseId").onSuccess {
                    _lastWaterReading.value = it
                }.onFailure {
                    // Fallback to initial reading if no bills exist yet
                    _lastWaterReading.value = MeterReading(
                        currentReading = houseData?.initialWaterReading ?: 0.0,
                        meterId = "water_$houseId"
                    )
                }
                
                // If it succeeded but returned null, also fallback
                if (_lastWaterReading.value == null) {
                    _lastWaterReading.value = MeterReading(
                        currentReading = houseData?.initialWaterReading ?: 0.0,
                        meterId = "water_$houseId"
                    )
                }
            } catch (e: Exception) {
                Log.e("CreateInvoiceVM", "Error loading data", e)
            }
            _isLoading.value = false
        }
    }

    fun saveWaterRate(apartmentId: String, rate: Double) {
        viewModelScope.launch {
            firebaseDataSource.getReference("landlord_settings/$apartmentId/water_rate").setValue(rate)
            _waterRate.value = rate
        }
    }

    fun generateInvoice(
        house: House,
        waterUnits: Double,
        otherCharges: List<InvoiceItem>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tenantId = house.tenantId ?: throw Exception("No tenant assigned to this unit")
                
                val invoiceId = "INV_${System.currentTimeMillis()}"
                val totalRent = house.monthlyRent
                val totalWater = waterUnits * _waterRate.value
                val totalOthers = otherCharges.sumOf { it.amount }
                
                val invoice = Invoice(
                    id = invoiceId,
                    tenantId = tenantId,
                    unitId = house.id,
                    invoiceNumber = System.currentTimeMillis().toString().takeLast(6),
                    totalDue = totalRent + totalWater + totalOthers,
                    outstandingAmount = totalRent + totalWater + totalOthers,
                    status = BillingStatus.PENDING_PAYMENT,
                    issueDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L) // 7 days from now
                )

                billingRepository.createInvoice(invoice).onSuccess {
                    // Add items
                    billingRepository.addInvoiceItem(invoiceId, InvoiceItem(type = InvoiceItemType.RENT, description = "Monthly Rent", amount = totalRent))
                    if (totalWater > 0) {
                        billingRepository.addInvoiceItem(invoiceId, InvoiceItem(type = InvoiceItemType.WATER, description = "Water Consumption ($waterUnits units)", amount = totalWater))
                    }
                    otherCharges.forEach { 
                        billingRepository.addInvoiceItem(invoiceId, it.copy(invoiceId = invoiceId))
                    }
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("CreateInvoiceVM", "Error generating invoice", e)
            }
            _isLoading.value = false
        }
    }
}
