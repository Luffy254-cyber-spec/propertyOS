package com.him.landlordtenant.app.data.model.billing

import java.util.Date

/**
 * =============================================================
 * RECURRING FEE CONFIGURATION
 * =============================================================
 */
data class FeeConfiguration(
    val id: String = "",
    val propertyId: String = "",
    val unitId: String = "",
    val tenantId: String = "",
    val feeType: FeeType = FeeType.RENT,
    val name: String = "",
    val amount: Double = 0.0,
    val billingFrequency: BillingFrequency = BillingFrequency.MONTHLY,
    val isRecurring: Boolean = true,
    val isActive: Boolean = true,
    val effectiveFrom: Long = System.currentTimeMillis(),
    val effectiveTo: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class FeeType {
    RENT, WATER, ELECTRICITY, GARBAGE, SERVICE_CHARGE, PARKING, SECURITY, MANAGEMENT, OTHER_RECURRING
}

enum class BillingFrequency {
    MONTHLY, WEEKLY, YEARLY, CUSTOM
}

/**
 * =============================================================
 * METERED UTILITIES
 * =============================================================
 */
data class Meter(
    val id: String = "",
    val unitId: String = "",
    val meterType: MeterType = MeterType.WATER,
    val meterNumber: String = "",
    val unitOfMeasure: String = "Units",
    val ratePerUnit: Double = 0.0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class MeterType {
    WATER, ELECTRICITY, GAS, OTHER
}

data class MeterReading(
    val id: String = "",
    val meterId: String = "",
    val billingPeriodId: String = "",
    val tenantId: String = "",
    val unitId: String = "",
    val previousReading: Double = 0.0,
    val currentReading: Double = 0.0,
    val consumption: Double = 0.0,
    val ratePerUnit: Double = 0.0,
    val calculatedAmount: Double = 0.0,
    val readingDate: Long = System.currentTimeMillis(),
    val status: ReadingStatus = ReadingStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReadingStatus {
    DRAFT, VERIFIED, REJECTED
}

/**
 * =============================================================
 * INVOICING
 * =============================================================
 */
data class BillingPeriod(
    val id: String = "",
    val tenantId: String = "",
    val unitId: String = "",
    val month: Int = 1,
    val year: Int = 2026,
    val status: BillingStatus = BillingStatus.DRAFT,
    val subtotal: Double = 0.0,
    val previousArrears: Double = 0.0,
    val totalDue: Double = 0.0,
    val amountPaid: Double = 0.0,
    val balance: Double = 0.0,
    val dueDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class BillingStatus {
    DRAFT, PENDING_READING, GENERATED, PENDING_PAYMENT, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
}

data class Invoice(
    val id: String = "",
    val tenantId: String = "",
    val leaseId: String = "",
    val unitId: String = "",
    val billingPeriodId: String = "",
    val invoiceNumber: String = "",
    val month: Int = 1,
    val year: Int = 2026,
    val issueDate: Long = System.currentTimeMillis(),
    val dueDate: Long = System.currentTimeMillis(),
    val subtotal: Double = 0.0,
    val previousArrears: Double = 0.0,
    val additionalCharges: Double = 0.0,
    val discounts: Double = 0.0,
    val totalDue: Double = 0.0,
    val amountPaid: Double = 0.0,
    val outstandingAmount: Double = 0.0,
    val status: BillingStatus = BillingStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class InvoiceItem(
    val id: String = "",
    val invoiceId: String = "",
    val type: InvoiceItemType = InvoiceItemType.RENT,
    val description: String = "",
    val quantity: Double = 1.0,
    val unitRate: Double = 0.0,
    val amount: Double = 0.0,
    val isRecurring: Boolean = true,
    val sourceId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class InvoiceItemType {
    RENT, WATER, ELECTRICITY, GARBAGE, SERVICE_CHARGE, PARKING, SECURITY, MANAGEMENT, REPAIR, DAMAGE, PENALTY, OTHER
}

/**
 * =============================================================
 * PAYMENTS & RECEIPTS
 * =============================================================
 */
data class Payment(
    val id: String = "",
    val tenantId: String = "",
    val invoiceId: String = "",
    val paymentReference: String = "",
    val providerTransactionId: String = "",
    val amount: Double = 0.0,
    val currency: String = "KES",
    val paymentMethod: PaymentMethod = PaymentMethod.MPESA,
    val status: PaymentStatus = PaymentStatus.INITIATED,
    val initiatedAt: Long = System.currentTimeMillis(),
    val verifiedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PaymentMethod {
    MPESA, CASH, BANK_TRANSFER, CARD
}

enum class PaymentStatus {
    INITIATED, PENDING, SUCCESS, FAILED, CANCELLED, VERIFICATION_PENDING, VERIFIED, REVERSED
}

data class PaymentAllocation(
    val id: String = "",
    val paymentId: String = "",
    val invoiceId: String = "",
    val amountAllocated: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

data class Receipt(
    val id: String = "",
    val receiptNumber: String = "",
    val paymentId: String = "",
    val tenantId: String = "",
    val invoiceId: String = "",
    val amount: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.MPESA,
    val providerTransactionId: String = "",
    val paymentDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * =============================================================
 * PAYMENT CHANNEL CONFIGURATION
 * =============================================================
 */
data class PaymentChannelConfig(
    val propertyId: String = "",
    val landlordId: String = "",
    
    // M-Pesa
    val mpesaShortCode: String? = null, // Paybill or Till
    val mpesaType: MpesaType = MpesaType.PAYBILL,
    val mpesaConsumerKey: String? = null,
    val mpesaConsumerSecret: String? = null,
    val mpesaPasskey: String? = null,
    
    // Airtel Money
    val airtelClientId: String? = null,
    val airtelClientSecret: String? = null,
    val airtelMerchantCode: String? = null,
    
    // Card (Visa/Mastercard)
    val cardEnabled: Boolean = false,
    val stripePublishableKey: String? = null,
    val stripeSecretKey: String? = null,
    val stripePayoutCardNumber: String? = null,
    
    // Cash
    val cashEnabled: Boolean = true,
    
    val updatedAt: Long = System.currentTimeMillis()
)

enum class MpesaType {
    PAYBILL, TILL, BUY_GOODS
}
