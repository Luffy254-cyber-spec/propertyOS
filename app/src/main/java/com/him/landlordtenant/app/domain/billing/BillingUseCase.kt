package com.him.landlordtenant.app.domain.billing

import com.him.landlordtenant.app.data.model.billing.*
import com.him.landlordtenant.app.interfaces.PropertyBillingRepository
import javax.inject.Inject

class BillingUseCase @Inject constructor(
    private val billingRepository: PropertyBillingRepository
) {
    /**
     * Tally water bill based on current and previous readings.
     */
    fun calculateMeteredCharge(
        previousReading: Double,
        currentReading: Double,
        ratePerUnit: Double
    ): Double {
        if (currentReading < previousReading) return 0.0
        return (currentReading - previousReading) * ratePerUnit
    }

    /**
     * Generate the next month's invoice by carrying forward unpaid balances
     * and copying recurring configurations.
     */
    suspend fun generateMonthlyInvoice(
        tenantId: String,
        unitId: String,
        month: Int,
        year: Int
    ): Result<Invoice> {
        return try {
            // 1. Get recurring configurations
            val configs = billingRepository.getFeeConfigurations(tenantId, unitId).getOrThrow()
            
            // 2. Get last month's outstanding balance (arrears)
            val arrears = billingRepository.getTotalOutstanding(tenantId).getOrThrow()
            
            // 3. Create invoice items from configs
            val items = configs.filter { it.isActive }.map { config ->
                InvoiceItem(
                    type = mapFeeType(config.feeType),
                    description = config.name,
                    unitRate = config.amount,
                    amount = config.amount,
                    isRecurring = true,
                    sourceId = config.id
                )
            }

            val subtotal = items.sumOf { it.amount }
            val totalDue = subtotal + arrears

            val invoice = Invoice(
                tenantId = tenantId,
                unitId = unitId,
                month = month,
                year = year,
                subtotal = subtotal,
                previousArrears = arrears,
                totalDue = totalDue,
                outstandingAmount = totalDue,
                status = BillingStatus.DRAFT
            )

            // 4. Save draft invoice
            val savedInvoiceId = billingRepository.createInvoice(invoice).getOrThrow()
            
            // 5. Save items
            items.forEach { billingRepository.addInvoiceItem(savedInvoiceId, it) }

            Result.success(invoice.copy(id = savedInvoiceId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapFeeType(type: FeeType): InvoiceItemType {
        return when (type) {
            FeeType.RENT -> InvoiceItemType.RENT
            FeeType.WATER -> InvoiceItemType.WATER
            FeeType.ELECTRICITY -> InvoiceItemType.ELECTRICITY
            FeeType.GARBAGE -> InvoiceItemType.GARBAGE
            FeeType.SERVICE_CHARGE -> InvoiceItemType.SERVICE_CHARGE
            else -> InvoiceItemType.OTHER
        }
    }
}
