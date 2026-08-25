package com.him.landlordtenant.app.data.dto.finance

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * PAYMENT TRANSACTION DTO
 * =============================================================
 *
 * Represents the low-level transaction between the application
 * and an external payment provider.
 *
 * PaymentDto
 *      ↓
 * Business payment record
 *
 * PaymentTransactionDto
 *      ↓
 * Provider transaction / webhook / reconciliation record
 *
 * ReceiptDto
 *      ↓
 * Proof of payment
 *
 * =============================================================
 */

@Serializable
data class PaymentTransactionDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val transactionReference: String = "",

    val paymentId: String? = null,

    val billId: String? = null,

    val tenantId: String? = null,

    /*
     * ---------------------------------------------------------
     * PROVIDER
     * ---------------------------------------------------------
     */

    val provider: String = "MPESA",

    val providerTransactionId: String? = null,

    val providerReference: String? = null,

    val providerReceiptNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT METHOD
     * ---------------------------------------------------------
 */

    val paymentMethod: String = "MPESA",

    val accountReference: String? = null,

    val phoneNumberMasked: String? = null,

    val bankReference: String? = null,

    /*
     * ---------------------------------------------------------
     * AMOUNT
     * ---------------------------------------------------------
 */

    val amount: Double = 0.0,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * TRANSACTION STATUS
     * ---------------------------------------------------------
 */

    val status: String = "PENDING",

    val resultCode: String? = null,

    val resultMessage: String? = null,

    /*
     * ---------------------------------------------------------
     * CALLBACK / WEBHOOK
     * ---------------------------------------------------------
 */

    val callbackReceived: Boolean = false,

    val callbackReceivedAt: String? = null,

    val webhookEventId: String? = null,

    val webhookVerified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * RECONCILIATION
     * ---------------------------------------------------------
 */

    val reconciliationStatus: String = "PENDING",

    val reconciled: Boolean = false,

    val reconciledAt: String? = null,

    val reconciliationReference: String? = null,

    /*
     * ---------------------------------------------------------
     * RETRY
     * ---------------------------------------------------------
 */

    val retryCount: Int = 0,

    val lastRetryAt: String? = null,

    val nextRetryAt: String? = null,

    val maxRetries: Int = 3,

    /*
     * ---------------------------------------------------------
     * FAILURE
     * ---------------------------------------------------------
 */

    val failureCode: String? = null,

    val failureReason: String? = null,

    /*
     * ---------------------------------------------------------
     * REFUND
     * ---------------------------------------------------------
 */

    val refundRequested: Boolean = false,

    val refundTransactionId: String? = null,

    val refundAmount: Double = 0.0,

    val refundStatus: String? = null,

    /*
     * ---------------------------------------------------------
     * SECURITY / VERIFICATION
     * ---------------------------------------------------------
 */

    val signatureVerified: Boolean = false,

    val duplicateDetected: Boolean = false,

    val suspicious: Boolean = false,

    /*
     * ---------------------------------------------------------
     * RAW PROVIDER REFERENCE
     * ---------------------------------------------------------
 */

    val providerRequestId: String? = null,

    val providerResponseId: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val initiatedAt: String? = null,

    val completedAt: String? = null,

    val failedAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null
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
            status.uppercase() in setOf(
                "PENDING",
                "PROCESSING"
            )

    val isFailed: Boolean
        get() =
            status.uppercase() in setOf(
                "FAILED",
                "CANCELLED"
            )

    val canRetry: Boolean
        get() =
            isFailed &&
                    retryCount < maxRetries

    val isReconciled: Boolean
        get() =
            reconciled &&
                    reconciliationStatus.uppercase() == "RECONCILED"

    val requiresAttention: Boolean
        get() =
            suspicious ||
                    duplicateDetected ||
                    (!isSuccessful && !isPending && !isFailed)

    val hasRefund: Boolean
        get() =
            refundAmount > 0.0 ||
                    refundRequested
}