package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentUIState
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentReceiptUIModel
import com.him.landlordtenant.app.enums.PaymentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TenantPaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val authRepository: AuthRepository,
    private val activityRepository: ActivityRepository,
    private val billingRepository: PropertyBillingRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentProcessState>(PaymentProcessState.Idle)
    val paymentState: StateFlow<PaymentProcessState> = _paymentState.asStateFlow()

    private val _paymentConfig = MutableStateFlow<com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig?>(null)
    val paymentConfig: StateFlow<com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig?> = _paymentConfig.asStateFlow()

    fun loadPaymentConfig(propertyId: String) {
        viewModelScope.launch {
            billingRepository.getPaymentConfig(propertyId).onSuccess {
                _paymentConfig.value = it
            }
        }
    }

    fun payWithMpesaStkPush(paymentData: TenantPaymentUIState, phoneNumber: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentProcessState.Processing
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            // 1. Create a Payment Record first
            val createPaymentData = CreatePaymentData(
                tenantId = userId,
                landlordId = null, // Will be fetched from property if needed, but let's assume propertyId is enough
                propertyId = paymentData.apartmentId,
                billId = null,
                amount = paymentData.total,
                method = "MPESA_STK",
                description = "Rent Payment for ${paymentData.houseNumber}",
                externalReference = paymentData.houseNumber
            )
            
            val paymentResult = paymentRepository.createPayment(userId, createPaymentData)
            paymentResult.onSuccess { paymentId ->
                // 2. Initiate STK Push
                val stkResult = paymentRepository.initiateMpesaStkPush(userId, paymentId, phoneNumber)
                stkResult.onSuccess { mpesaData ->
                    if (mpesaData.status == PaymentStatus.INITIATED) {
                        _paymentState.value = PaymentProcessState.StkSent
                        
                        // Start polling for payment status
                        pollForPaymentStatus(paymentId, paymentData, phoneNumber)

                        activityRepository.logActivity(userId, CreateActivityData(
                            title = "M-Pesa Payment Initiated",
                            subtitle = "KSh ${paymentData.total} to Paybill",
                            type = "PAYMENT",
                            status = "PENDING"
                        ))
                    } else {
                        _paymentState.value = PaymentProcessState.Error("STK Push failed to initiate")
                    }
                }.onFailure {
                    _paymentState.value = PaymentProcessState.Error(it.message ?: "Network error during STK Push")
                }
            }.onFailure {
                _paymentState.value = PaymentProcessState.Error("Failed to create payment record")
            }
        }
    }

    fun payWithCard(paymentData: TenantPaymentUIState) {
        viewModelScope.launch {
            _paymentState.value = PaymentProcessState.Processing
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            val createPaymentData = CreatePaymentData(
                tenantId = userId,
                landlordId = null,
                propertyId = paymentData.apartmentId,
                billId = null,
                amount = paymentData.total,
                method = "CARD",
                description = "Rent Payment: ${paymentData.houseNumber}"
            )
            
            val paymentResult = paymentRepository.createPayment(userId, createPaymentData)
            paymentResult.onSuccess { paymentId ->
                val cardResult = paymentRepository.initiateCardPayment(userId, paymentId)
                cardResult.onSuccess { intentData ->
                    _paymentState.value = PaymentProcessState.CardIntentCreated(intentData)
                }.onFailure {
                    _paymentState.value = PaymentProcessState.Error("Failed to initiate card payment: ${it.message}")
                }
            }.onFailure {
                _paymentState.value = PaymentProcessState.Error("Failed to create payment record")
            }
        }
    }

    fun completePayment(paymentData: TenantPaymentUIState, phoneNumber: String) {
        // Fallback or manual override (Wait for actual status first)
        viewModelScope.launch {
            _paymentState.value = PaymentProcessState.Processing
            delay(2000)
            // Note: transactionId in UI State is actually the internal payment ID
            paymentRepository.verifyPayment(paymentData.transactionId).onSuccess { verification ->
                if (verification.verified) {
                     _paymentState.value = PaymentProcessState.Success(
                        createReceipt(paymentData, phoneNumber, verification.providerReference ?: "N/A")
                    )
                } else {
                    _paymentState.value = PaymentProcessState.Error("Payment not yet confirmed. Please try again in a moment.")
                    delay(3000)
                    _paymentState.value = PaymentProcessState.StkSent
                }
            }
        }
    }

    private fun pollForPaymentStatus(paymentId: String, paymentData: TenantPaymentUIState, phoneNumber: String) {
        viewModelScope.launch {
            var attempts = 0
            val maxAttempts = 12 // Poll for 60 seconds (5s intervals)
            
            while (attempts < maxAttempts) {
                delay(5000) // Wait 5 seconds
                attempts++
                
                paymentRepository.verifyPayment(paymentId).onSuccess { verification ->
                    if (verification.verified) {
                        _paymentState.value = PaymentProcessState.Success(
                            createReceipt(paymentData, phoneNumber, verification.providerReference ?: "MP-${System.currentTimeMillis().toString().takeLast(8)}")
                        )
                        return@launch
                    }
                }
                
                // If we're still in StkSent state, continue polling
                if (_paymentState.value !is PaymentProcessState.StkSent) return@launch
            }
            
            // If we timed out, we stay in StkSent but allow manual check
        }
    }

    private fun createReceipt(paymentData: TenantPaymentUIState, phoneNumber: String, transactionId: String): TenantPaymentReceiptUIModel {
        return TenantPaymentReceiptUIModel(
            receiptNumber = "RCP-${System.currentTimeMillis()}",
            transactionId = transactionId,
            amount = paymentData.total,
            paymentMethod = "M-Pesa STK",
            phoneNumber = phoneNumber,
            date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
            apartmentName = paymentData.apartmentName.ifEmpty { "propertyOS Managed" },
            houseNumber = paymentData.houseNumber
        )
    }
}

sealed class PaymentProcessState {
    object Idle : PaymentProcessState()
    object Processing : PaymentProcessState()
    object StkSent : PaymentProcessState()
    data class CardIntentCreated(val data: CardPaymentIntentData) : PaymentProcessState()
    data class Success(val receipt: TenantPaymentReceiptUIModel) : PaymentProcessState()
    data class Error(val message: String) : PaymentProcessState()
}
