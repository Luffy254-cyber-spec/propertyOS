package com.him.landlordtenant.app.data.dto.finance

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * PAYMENT DTO
 * =============================================================
 *
 * Represents a payment made toward a bill, rent, deposit,
 * maintenance charge or another property-related charge.
 *
 * PaymentDto = business-level payment record.
 *
 * PaymentTransactionDto = individual payment-provider/
 * transaction-level record.
 *
 * ReceiptDto = generated proof of payment.
 *
 * =============================================================
 */

@Serializable
data class PaymentDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    val receiptNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PARTIES
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val landlordId: String? = null,

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val houseId: String? = null,

    val agreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * BILL REFERENCES
     * ---------------------------------------------------------
     */

    val billId: String? = null,

    val billReferenceNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT DETAILS
     * ---------------------------------------------------------
     */

    val paymentType: String = "RENT",

    val description: String? = null,

    val amount: Double = 0.0,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * PAYMENT METHOD
     * ---------------------------------------------------------
     */

    val paymentMethod: String = "MPESA",

    val paymentProvider: String? = null,

    val paymentAccountReference: String? = null,

    /*
     * ---------------------------------------------------------
     * TRANSACTION
     * ---------------------------------------------------------
     */

    val transactionId: String? = null,

    val providerTransactionId: String? = null,

    val providerReference: String? = null,

    val transactionDate: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT STATUS
     * ---------------------------------------------------------
 */

    val status: String = "PENDING",

    val failureReason: String? = null,

    val failureCode: String? = null,

    /*
     * ---------------------------------------------------------
     * ALLOCATION
     * ---------------------------------------------------------
 */

    val allocatedAmount: Double = 0.0,

    val unallocatedAmount: Double = 0.0,

    val allocatedToBill: Boolean = false,

    /*
     * ---------------------------------------------------------
     * PARTIAL PAYMENT
     * ---------------------------------------------------------
 */

    val partialPayment: Boolean = false,

    val remainingBillBalance: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * REFUND
     * ---------------------------------------------------------
 */

    val refundable: Boolean = false,

    val refunded: Boolean = false,

    val refundedAmount: Double = 0.0,

    val refundTransactionId: String? = null,

    val refundedAt: String? = null,

    val refundReason: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verified: Boolean = false,

    val verifiedBy: String? = null,

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * RECEIPT
     * ---------------------------------------------------------
 */

    val receiptGenerated: Boolean = false,

    val receiptId: String? = null,

    val receiptUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * NOTIFICATIONS
     * ---------------------------------------------------------
 */

    val tenantNotified: Boolean = false,

    val landlordNotified: Boolean = false,

    val notificationSentAt: String? = null,

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

    val completedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isSuccessful: Boolean
        get() =
            status.uppercase() in setOf(
                "SUCCESS",
                "COMPLETED",
                "PAID"
            )

    val isPending: Boolean
        get() =
            status.uppercase() == "PENDING"

    val isFailed: Boolean
        get() =
            status.uppercase() in setOf(
                "FAILED",
                "CANCELLED"
            )

    val isRefunded: Boolean
        get() =
            refunded &&
                    refundedAmount > 0.0

    val netAmount: Double
        get() =
            (amount - refundedAmount)
                .coerceAtLeast(0.0)

    val hasReceipt: Boolean
        get() =
            receiptGenerated &&
                    !receiptId.isNullOrBlank()

    val isUnallocated: Boolean
        get() =
            unallocatedAmount > 0.0
}