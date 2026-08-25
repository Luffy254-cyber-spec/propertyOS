package com.him.landlordtenant.app.usecase.payment

import com.him.landlordtenant.app.interfaces.PaymentRepository
import com.him.landlordtenant.app.interfaces.PaymentVerificationData

class VerifyPaymentUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: String): Result<PaymentVerificationData> {
        return try {
            paymentRepository.verifyPayment(paymentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
