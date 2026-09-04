package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.PaymentDao
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.network.PaymentAPI
import com.him.landlordtenant.app.network.dto.MpesaRequestDto
import com.him.landlordtenant.app.enums.PaymentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val paymentAPI: PaymentAPI
) : PaymentRepository {

    override suspend fun createPayment(userId: String, payment: CreatePaymentData): Result<String> {
        val id = firestoreDataSource.collection("payments").document().id
        return firestoreDataSource.saveData("payments", id, payment).map { id }
    }

    override suspend fun getPayment(paymentId: String): Result<PaymentDetailsData> {
        return firestoreDataSource.getData("payments", paymentId, PaymentDetailsData::class.java)
            .map { it ?: throw Exception("Payment not found") }
    }

    override fun observePayment(paymentId: String): Flow<Result<PaymentDetailsData>> = flow {
        emit(getPayment(paymentId))
    }

    override suspend fun getTenantPayments(tenantId: String): Result<List<PaymentDetailsData>> = try {
        val snapshot = firestoreDataSource.collection("payments").whereEqualTo("tenantId", tenantId).get().await()
        Result.success(snapshot.toObjects(PaymentDetailsData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLandlordPayments(landlordId: String): Result<List<PaymentDetailsData>> = try {
        val snapshot = firestoreDataSource.collection("payments").whereEqualTo("landlordId", landlordId).get().await()
        Result.success(snapshot.toObjects(PaymentDetailsData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun initiatePayment(userId: String, paymentId: String): Result<PaymentInitiationData> = Result.failure(NotImplementedError())
    override suspend fun retryPayment(userId: String, paymentId: String): Result<PaymentInitiationData> = Result.failure(NotImplementedError())

    override suspend fun initiateMpesaStkPush(userId: String, paymentId: String, phoneNumber: String): Result<MpesaPaymentData> = try {
        val payment = getPayment(paymentId).getOrThrow()
        
        val request = MpesaRequestDto(
            phoneNumber = phoneNumber,
            amount = payment.amount,
            accountReference = payment.id.take(12),
            transactionDesc = "PropertyOS Rent Payment"
        )
        
        val response = paymentAPI.initiateMpesaStkPush(request)
        
        val mpesaData = MpesaPaymentData(
            paymentId = paymentId,
            checkoutRequestId = response.CheckoutRequestID,
            merchantRequestId = response.MerchantRequestID,
            phoneNumber = phoneNumber,
            amount = payment.amount,
            status = PaymentStatus.INITIATED,
            mpesaReceiptNumber = null
        )
        
        Result.success(mpesaData)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun verifyPayment(paymentId: String): Result<PaymentVerificationData> = Result.failure(NotImplementedError())
    override suspend fun applyPaymentToBill(paymentId: String, billId: String, amount: Double): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun generateReceipt(paymentId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getPaymentSummary(userId: String): Result<PaymentSummaryData> = Result.failure(NotImplementedError())
}
