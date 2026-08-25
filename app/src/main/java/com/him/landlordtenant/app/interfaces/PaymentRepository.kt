package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow
import com.him.landlordtenant.app.enums.PaymentStatus

interface PaymentRepository {

    suspend fun createPayment(
        userId: String,
        payment: CreatePaymentData
    ): Result<String>

    suspend fun initiatePayment(
        userId: String,
        paymentId: String
    ): Result<PaymentInitiationData>

    suspend fun retryPayment(
        userId: String,
        paymentId: String
    ): Result<PaymentInitiationData>

    suspend fun getPayment(
        paymentId: String
    ): Result<PaymentDetailsData>

    fun observePayment(
        paymentId: String
    ): Flow<Result<PaymentDetailsData>>

    suspend fun getTenantPayments(
        tenantId: String
    ): Result<List<PaymentDetailsData>>

    suspend fun getLandlordPayments(
        landlordId: String
    ): Result<List<PaymentDetailsData>>

    suspend fun initiateMpesaStkPush(
        userId: String,
        paymentId: String,
        phoneNumber: String
    ): Result<MpesaPaymentData>

    suspend fun verifyPayment(
        paymentId: String
    ): Result<PaymentVerificationData>

    suspend fun applyPaymentToBill(
        paymentId: String,
        billId: String,
        amount: Double
    ): Result<Unit>

    suspend fun generateReceipt(
        paymentId: String
    ): Result<String>

    suspend fun getPaymentSummary(
        userId: String
    ): Result<PaymentSummaryData>
}

data class CreatePaymentData(
    val tenantId: String,
    val landlordId: String?,
    val propertyId: String?,
    val billId: String?,
    val amount: Double,
    val currency: String = "KES",
    val method: String,
    val description: String?
)

data class PaymentInitiationData(
    val paymentId: String,
    val provider: String,
    val checkoutUrl: String?,
    val reference: String?,
    val expiresAt: String?
)

data class PaymentDetailsData(
    val id: String,
    val tenantId: String,
    val tenantName: String?,
    val landlordId: String?,
    val propertyId: String?,
    val billId: String?,
    val amount: Double,
    val currency: String,
    val method: String,
    val provider: String,
    val status: PaymentStatus,
    val transactionReference: String?,
    val providerReference: String?,
    val createdAt: String,
    val completedAt: String?
)

data class MpesaPaymentData(
    val paymentId: String,
    val checkoutRequestId: String?,
    val merchantRequestId: String?,
    val phoneNumber: String,
    val amount: Double,
    val status: PaymentStatus,
    val mpesaReceiptNumber: String?
)

data class PaymentVerificationData(
    val paymentId: String,
    val verified: Boolean,
    val providerReference: String?,
    val amount: Double,
    val verifiedAt: String?
)

data class PaymentSummaryData(
    val totalPayments: Int,
    val totalAmount: Double,
    val successfulPayments: Int,
    val failedPayments: Int,
    val pendingPayments: Int
)
