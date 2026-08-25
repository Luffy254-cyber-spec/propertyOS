package com.him.landlordtenant.app.usecase.rent

import com.him.landlordtenant.app.interfaces.PaymentDetailsData
import com.him.landlordtenant.app.interfaces.PaymentRepository

class GetRentPaymentHistoryUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(tenantId: String): Result<List<PaymentDetailsData>> {
        return try {
            paymentRepository.getTenantPayments(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
