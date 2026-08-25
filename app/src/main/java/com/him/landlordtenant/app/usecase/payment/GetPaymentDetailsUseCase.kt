package com.him.landlordtenant.app.usecase.payment

import com.him.landlordtenant.app.interfaces.PaymentDetailsData
import com.him.landlordtenant.app.interfaces.PaymentRepository

class GetPaymentDetailsUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: String): Result<PaymentDetailsData> {
        return try {
            paymentRepository.getPayment(paymentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
