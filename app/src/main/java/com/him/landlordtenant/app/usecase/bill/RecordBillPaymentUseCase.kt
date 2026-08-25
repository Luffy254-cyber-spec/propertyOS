package com.him.landlordtenant.app.usecase.bill

import com.him.landlordtenant.app.interfaces.BillPaymentData
import com.him.landlordtenant.app.interfaces.BillRepository

class RecordBillPaymentUseCase(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(
        userId: String,
        billId: String,
        payment: BillPaymentData
    ): Result<String> {
        return try {
            billRepository.recordPayment(userId, billId, payment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
