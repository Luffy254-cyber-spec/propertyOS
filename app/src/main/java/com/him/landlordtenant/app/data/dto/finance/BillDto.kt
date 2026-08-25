package com.him.landlordtenant.app.data.dto.finance

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * BILL DTO
 * =============================================================
 *
 * Represents a financial charge issued to a tenant.
 *
 * Supports:
 * - Monthly rent
 * - Water
 * - Electricity
 * - Service charge
 * - Garbage
 * - Internet
 * - Parking
 * - Maintenance charges
 * - Penalties
 * - Other custom charges
 *
 * Also supports arrears tracking, partial payments, recurring
 * billing, reminders and automatic status calculation.
 *
 * =============================================================
 */

@Serializable
data class BillDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * TENANT / PROPERTY REFERENCES
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val landlordId: String = "",

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val houseId: String? = null,

    val agreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * BILL INFORMATION
     * ---------------------------------------------------------
     */

    val title: String = "",

    val description: String? = null,

    val billType: String = "RENT",

    val category: String = "RENT",

    /*
     * ---------------------------------------------------------
     * BILLING PERIOD
     * ---------------------------------------------------------
     */

    val billingPeriodStart: String? = null,

    val billingPeriodEnd: String? = null,

    val billingMonth: Int? = null,

    val billingYear: Int? = null,

    /*
     * ---------------------------------------------------------
     * AMOUNTS
     * ---------------------------------------------------------
     */

    val amount: Double = 0.0,

    val discountAmount: Double = 0.0,

    val penaltyAmount: Double = 0.0,

    val previousArrears: Double = 0.0,

    val totalAmount: Double = 0.0,

    val amountPaid: Double = 0.0,

    val balance: Double = 0.0,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * DUE DATE
     * ---------------------------------------------------------
     */

    val issuedDate: String? = null,

    val dueDate: String? = null,

    val gracePeriodDays: Int = 0,

    /*
     * ---------------------------------------------------------
     * PAYMENT STATUS
     * ---------------------------------------------------------
 */

    val paymentStatus: String = "UNPAID",

    val status: String = "PENDING",

    /*
     * ---------------------------------------------------------
     * PAYMENT
     * ---------------------------------------------------------
 */

    val lastPaymentId: String? = null,

    val lastPaymentDate: String? = null,

    val lastPaymentAmount: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * PARTIAL PAYMENT
     * ---------------------------------------------------------
 */

    val partialPaymentAllowed: Boolean = true,

    val minimumPaymentAmount: Double? = null,

    /*
     * ---------------------------------------------------------
     * RECURRING BILLING
     * ---------------------------------------------------------
 */

    val recurring: Boolean = false,

    val recurringFrequency: String? = null,

    val recurringDay: Int? = null,

    val recurringEndDate: String? = null,

    val parentBillId: String? = null,

    /*
     * ---------------------------------------------------------
     * PENALTIES
     * ---------------------------------------------------------
 */

    val penaltyEnabled: Boolean = false,

    val penaltyType: String? = null,

    val penaltyValue: Double = 0.0,

    val penaltyApplied: Boolean = false,

    val penaltyAppliedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * METER BILLING
     * ---------------------------------------------------------
 */

    val meterId: String? = null,

    val previousMeterReading: Double? = null,

    val currentMeterReading: Double? = null,

    val consumption: Double? = null,

    val unitRate: Double? = null,

    val meterUnit: String? = null,

    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
 */

    val reminderEnabled: Boolean = true,

    val reminderDaysBeforeDue: List<Int> = emptyList(),

    val reminderSent: Boolean = false,

    val overdueReminderSent: Boolean = false,

    /*
     * ---------------------------------------------------------
     * COLLECTION
     * ---------------------------------------------------------
 */

    val collectionStatus: String = "NORMAL",

    val collectionAttempts: Int = 0,

    val lastCollectionAttemptAt: String? = null,

    /*
     * ---------------------------------------------------------
     * RECEIPT
     * ---------------------------------------------------------
 */

    val receiptId: String? = null,

    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
 */

    val notes: String? = null,

    val internalNotes: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val paidAt: String? = null,

    val cancelledAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val calculatedTotal: Double
        get() =
            amount +
                    penaltyAmount +
                    previousArrears -
                    discountAmount

    val calculatedBalance: Double
        get() =
            (totalAmount - amountPaid)
                .coerceAtLeast(0.0)

    val hasArrears: Boolean
        get() =
            previousArrears > 0.0 ||
                    balance > 0.0

    val isPaid: Boolean
        get() =
            paymentStatus.uppercase() == "PAID" ||
                    balance <= 0.0

    val isPartiallyPaid: Boolean
        get() =
            amountPaid > 0.0 &&
                    balance > 0.0

    val isOverdue: Boolean
        get() =
            status.uppercase() == "OVERDUE"

    val hasPenalty: Boolean
        get() =
            penaltyAmount > 0.0

    val isRecurringBill: Boolean
        get() = recurring
}