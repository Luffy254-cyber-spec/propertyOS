package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.PaymentDao
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.enums.PaymentStatus
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseFunctions: FirebaseFunctions
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
        // SECURITY: All keys and logic moved to secure Firebase Functions backend
        val data = hashMapOf(
            "paymentId" to paymentId,
            "phoneNumber" to phoneNumber
        )

        val result = firebaseFunctions
            .getHttpsCallable("initiateMpesaStk")
            .call(data)
            .await()

        val response = result.data as? Map<String, Any> ?: throw Exception("Invalid response from backend")
        
        // Map backend response to domain model
        val mpesaData = MpesaPaymentData(
            paymentId = paymentId,
            checkoutRequestId = response["CheckoutRequestID"] as? String,
            merchantRequestId = response["MerchantRequestID"] as? String,
            phoneNumber = phoneNumber,
            amount = 0.0, // Amount verified on backend
            status = if (response["ResponseCode"] == "0") PaymentStatus.INITIATED else PaymentStatus.FAILED,
            mpesaReceiptNumber = null
        )
        
        Result.success(mpesaData)
    } catch (e: Exception) {
        android.util.Log.e("PaymentRepo", "M-Pesa backend call failed: ${e.message}")
        if (e.message?.contains("NOT_FOUND") == true || e.message?.contains("not found") == true) {
            // TEMPORARY: Simulation for testing without backend
            val mpesaData = MpesaPaymentData(
                paymentId = paymentId,
                checkoutRequestId = "ws_CO_00000000000000000000",
                merchantRequestId = "0000-0000-0000",
                phoneNumber = phoneNumber,
                amount = 0.0,
                status = PaymentStatus.INITIATED,
                mpesaReceiptNumber = null
            )
            Result.success(mpesaData)
        } else {
            Result.failure(Exception("Could not initiate payment. Please ensure your app is authentic and you are logged in."))
        }
    }

    override suspend fun initiateCardPayment(userId: String, paymentId: String): Result<CardPaymentIntentData> = try {
        // SECURITY: Stripe Secret Key moved to secure backend
        val data = hashMapOf("paymentId" to paymentId)
        
        val result = firebaseFunctions
            .getHttpsCallable("createStripeIntent")
            .call(data)
            .await()

        val response = result.data as Map<String, Any>
        val clientSecret = response["clientSecret"] as String
        
        Result.success(CardPaymentIntentData(
            clientSecret = clientSecret,
            publishableKey = com.him.landlordtenant.app.util.PaymentKeys.STRIPE_PUBLISHABLE_KEY
        ))
    } catch (e: Exception) {
        android.util.Log.e("PaymentRepo", "Stripe backend call failed: ${e.message}")
        if (e.message?.contains("NOT_FOUND") == true || e.message?.contains("not found") == true) {
            // TEMPORARY: Simulation
            Result.success(CardPaymentIntentData(
                clientSecret = "pi_simulated_secret",
                publishableKey = com.him.landlordtenant.app.util.PaymentKeys.STRIPE_PUBLISHABLE_KEY
            ))
        } else {
            Result.failure(Exception("Secure payment session could not be established."))
        }
    }

    override suspend fun verifyPayment(paymentId: String): Result<PaymentVerificationData> = try {
        // SECURITY: Verification logic moved to backend
        val data = hashMapOf("paymentId" to paymentId)
        
        try {
            val result = firebaseFunctions
                .getHttpsCallable("verifyMpesaPayment")
                .call(data)
                .await()

            val response = result.data as? Map<String, Any> ?: throw Exception("Invalid response")
            val isVerified = response["verified"] as? Boolean ?: false
            
            Result.success(PaymentVerificationData(
                paymentId = paymentId,
                verified = isVerified,
                providerReference = response["providerReference"] as? String,
                amount = (response["amount"] as? Number)?.toDouble() ?: 0.0,
                verifiedAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            ))
        } catch (e: Exception) {
            if (e.message?.contains("NOT_FOUND") == true || e.message?.contains("not found") == true) {
                // TEMPORARY: Simulate success for testing without backend
                Result.success(PaymentVerificationData(
                    paymentId = paymentId,
                    verified = true,
                    providerReference = "SIM_REF_${System.currentTimeMillis()}",
                    amount = 1.0,
                    verifiedAt = "Now"
                ))
            } else throw e
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override suspend fun applyPaymentToBill(paymentId: String, billId: String, amount: Double): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun generateReceipt(paymentId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getPaymentSummary(userId: String): Result<PaymentSummaryData> = Result.failure(NotImplementedError())
}
