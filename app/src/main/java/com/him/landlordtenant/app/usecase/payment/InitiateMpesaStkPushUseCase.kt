package com.him.landlordtenant.app.usecase.payment

import com.him.landlordtenant.app.interfaces.MpesaPaymentData
import com.him.landlordtenant.app.interfaces.PaymentRepository

class InitiateMpesaStkPushUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        userId: String,
        paymentId: String,
        phoneNumber: String
    ): Result<MpesaPaymentData> {
        return try {
            paymentRepository.initiateMpesaStkPush(userId, paymentId, phoneNumber)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
