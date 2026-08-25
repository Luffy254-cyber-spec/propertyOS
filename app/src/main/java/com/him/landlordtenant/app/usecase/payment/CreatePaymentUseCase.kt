package com.him.landlordtenant.app.usecase.payment

import com.him.landlordtenant.app.interfaces.CreatePaymentData
import com.him.landlordtenant.app.interfaces.PaymentRepository

class CreatePaymentUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        userId: String,
        paymentData: CreatePaymentData
    ): Result<String> {
        return try {
            paymentRepository.createPayment(userId, paymentData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
