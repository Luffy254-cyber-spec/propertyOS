package com.him.landlordtenant.app.usecase.payment

import com.him.landlordtenant.app.interfaces.PaymentRepository
import com.him.landlordtenant.app.interfaces.PaymentSummaryData

class GetPaymentSummaryUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(userId: String): Result<PaymentSummaryData> {
        return try {
            paymentRepository.getPaymentSummary(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
