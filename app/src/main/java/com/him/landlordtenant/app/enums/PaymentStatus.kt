package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PAYMENT STATUS
 * =============================================================
 *
 * Represents the complete lifecycle of a payment.
 *
 * Used for:
 * - Rent payments
 * - Utility payments
 * - Deposits
 * - Service charges
 * - Maintenance payments
 * - M-Pesa
 * - Bank transfers
 * - Card payments
 * - Refunds
 * - Payment reconciliation
 *
 * =============================================================
 */

enum class PaymentStatus(
    val displayName: String,
    val description: String,
    val isSuccessful: Boolean,
    val requiresAction: Boolean
) {

    /**
     * Payment record has been created but payment has not
     * started.
     */
    CREATED(
        displayName = "Created",
        description = "Payment has been created and is awaiting initiation.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Payment request has been sent to the payment provider.
     */
    INITIATED(
        displayName = "Initiated",
        description = "Payment has been initiated with the payment provider.",
        isSuccessful = false,
        requiresAction = false
    ),

    /**
     * Payment provider is processing the transaction.
     */
    PROCESSING(
        displayName = "Processing",
        description = "Payment is currently being processed.",
        isSuccessful = false,
        requiresAction = false
    ),

    /**
     * Payment was successfully completed.
     */
    SUCCESS(
        displayName = "Successful",
        description = "Payment was successfully completed.",
        isSuccessful = true,
        requiresAction = false
    ),

    /**
     * Payment was confirmed by the provider but still needs
     * internal reconciliation.
     */
    PENDING_RECONCILIATION(
        displayName = "Pending Reconciliation",
        description = "Payment succeeded but is awaiting reconciliation.",
        isSuccessful = true,
        requiresAction = true
    ),

    /**
     * Payment has been fully reconciled and allocated.
     */
    RECONCILED(
        displayName = "Reconciled",
        description = "Payment has been verified and reconciled successfully.",
        isSuccessful = true,
        requiresAction = false
    ),

    /**
     * Payment failed.
     */
    FAILED(
        displayName = "Failed",
        description = "Payment failed to complete.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Payment was cancelled before completion.
     */
    CANCELLED(
        displayName = "Cancelled",
        description = "Payment was cancelled before completion.",
        isSuccessful = false,
        requiresAction = false
    ),

    /**
     * Payment request expired.
     */
    EXPIRED(
        displayName = "Expired",
        description = "Payment request expired before completion.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Payment was rejected by the provider.
     */
    REJECTED(
        displayName = "Rejected",
        description = "Payment was rejected by the payment provider.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Payment was reversed by the provider.
     */
    REVERSED(
        displayName = "Reversed",
        description = "Previously completed payment was reversed.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Refund has been requested.
     */
    REFUND_REQUESTED(
        displayName = "Refund Requested",
        description = "A refund has been requested for the payment.",
        isSuccessful = true,
        requiresAction = true
    ),

    /**
     * Refund is currently being processed.
     */
    REFUND_PROCESSING(
        displayName = "Refund Processing",
        description = "Payment refund is being processed.",
        isSuccessful = true,
        requiresAction = false
    ),

    /**
     * Payment has been refunded.
     */
    REFUNDED(
        displayName = "Refunded",
        description = "Payment has been fully refunded.",
        isSuccessful = false,
        requiresAction = false
    ),

    /**
     * Payment was partially refunded.
     */
    PARTIALLY_REFUNDED(
        displayName = "Partially Refunded",
        description = "Part of the payment has been refunded.",
        isSuccessful = true,
        requiresAction = false
    ),

    /**
     * Payment was flagged for investigation.
     */
    FLAGGED(
        displayName = "Flagged",
        description = "Payment has been flagged for review.",
        isSuccessful = false,
        requiresAction = true
    ),

    /**
     * Payment was identified as a duplicate.
     */
    DUPLICATE(
        displayName = "Duplicate",
        description = "Payment appears to be a duplicate transaction.",
        isSuccessful = false,
        requiresAction = true
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isPending: Boolean
        get() =
            this == CREATED ||
                    this == INITIATED ||
                    this == PROCESSING ||
                    this == PENDING_RECONCILIATION ||
                    this == REFUND_REQUESTED ||
                    this == REFUND_PROCESSING

    val isFailed: Boolean
        get() =
            this == FAILED ||
                    this == REJECTED ||
                    this == EXPIRED

    val isCancelled: Boolean
        get() =
            this == CANCELLED

    val isRefunded: Boolean
        get() =
            this == REFUNDED ||
                    this == PARTIALLY_REFUNDED

    val isReversed: Boolean
        get() =
            this == REVERSED

    val needsReview: Boolean
        get() =
            this == FLAGGED ||
                    this == DUPLICATE ||
                    this == REVERSED ||
                    this == PENDING_RECONCILIATION

    val canRetry: Boolean
        get() =
            this == FAILED ||
                    this == REJECTED ||
                    this == EXPIRED

    val canRefund: Boolean
        get() =
            this == SUCCESS ||
                    this == RECONCILED ||
                    this == PARTIALLY_REFUNDED

    val isTerminal: Boolean
        get() =
            this == RECONCILED ||
                    this == CANCELLED ||
                    this == REFUNDED ||
                    this == DUPLICATE

    /*
     * ---------------------------------------------------------
     * TRANSITION HELPERS
     * ---------------------------------------------------------
     */

    /**
     * Determines whether the payment can be marked successful.
     */
    fun canComplete(): Boolean =
        this == INITIATED ||
                this == PROCESSING

    /**
     * Determines whether the payment can enter reconciliation.
     */
    fun canReconcile(): Boolean =
        this == SUCCESS ||
                this == PENDING_RECONCILIATION

    /**
     * Determines whether a refund can be requested.
     */
    fun canRequestRefund(): Boolean =
        this == SUCCESS ||
                this == RECONCILED ||
                this == PARTIALLY_REFUNDED

    /**
     * Determines whether the payment can be cancelled.
     */
    fun canCancel(): Boolean =
        this == CREATED ||
                this == INITIATED ||
                this == PROCESSING

    /**
     * Determines whether the transaction can be retried.
     */
    fun canRetryPayment(): Boolean =
        canRetry

    companion object {

        /**
         * Safely convert a backend value into PaymentStatus.
         */
        fun fromValue(value: String?): PaymentStatus? {
            if (value.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            }
        }

        /**
         * Convert a backend value with a safe fallback.
         */
        fun fromValueOrDefault(
            value: String?,
            default: PaymentStatus = CREATED
        ): PaymentStatus {
            return fromValue(value) ?: default
        }
    }
}