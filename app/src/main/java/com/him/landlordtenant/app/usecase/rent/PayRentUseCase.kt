package com.him.landlordtenant.app.usecase.rent

import com.him.landlordtenant.app.interfaces.CreatePaymentData
import com.him.landlordtenant.app.interfaces.PaymentRepository

class PayRentUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        tenantId: String,
        billId: String,
        amount: Double,
        method: String
    ): Result<String> {
        val paymentData = CreatePaymentData(
            tenantId = tenantId,
            landlordId = null, // Will be fetched from bill or property by repo
            propertyId = null,
            billId = billId,
            amount = amount,
            method = method,
            description = "Rent payment"
        )
        return try {
            val paymentId = paymentRepository.createPayment(tenantId, paymentData).getOrThrow()
            paymentRepository.initiatePayment(tenantId, paymentId).map { it.paymentId }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
