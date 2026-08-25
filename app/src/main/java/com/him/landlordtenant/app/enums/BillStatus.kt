package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * BILL STATUS
 * =============================================================
 *
 * Represents the lifecycle of a rent, utility, service charge,
 * deposit or other property-related bill.
 *
 * =============================================================
 */

enum class BillStatus(
    val displayName: String,
    val description: String,
    val isPayable: Boolean,
    val requiresAction: Boolean
) {

    /**
     * Bill is being prepared.
     */
    DRAFT(
        displayName = "Draft",
        description = "Bill is being prepared and has not been issued.",
        isPayable = false,
        requiresAction = false
    ),

    /**
     * Bill has been issued to the tenant.
     */
    ISSUED(
        displayName = "Issued",
        description = "Bill has been issued and is awaiting payment.",
        isPayable = true,
        requiresAction = true
    ),

    /**
     * Bill is approaching its due date.
     */
    DUE_SOON(
        displayName = "Due Soon",
        description = "Bill is approaching its payment due date.",
        isPayable = true,
        requiresAction = true
    ),

    /**
     * Bill has passed its due date without being fully paid.
     */
    OVERDUE(
        displayName = "Overdue",
        description = "Bill has passed its due date and has an outstanding balance.",
        isPayable = true,
        requiresAction = true
    ),

    /**
     * Tenant has made a payment but the bill is not fully settled.
     */
    PARTIALLY_PAID(
        displayName = "Partially Paid",
        description = "A payment has been received but the bill still has a balance.",
        isPayable = true,
        requiresAction = true
    ),

    /**
     * Bill has been completely paid.
     */
    PAID(
        displayName = "Paid",
        description = "Bill has been fully paid.",
        isPayable = false,
        requiresAction = false
    ),

    /**
     * Payment is currently being processed.
     */
    PAYMENT_PROCESSING(
        displayName = "Payment Processing",
        description = "A payment has been initiated and is being processed.",
        isPayable = true,
        requiresAction = false
    ),

    /**
     * Tenant or landlord has disputed the bill.
     */
    DISPUTED(
        displayName = "Disputed",
        description = "Bill has an active dispute requiring review.",
        isPayable = false,
        requiresAction = true
    ),

    /**
     * Bill has been temporarily put on hold.
     */
    ON_HOLD(
        displayName = "On Hold",
        description = "Bill has temporarily been placed on hold.",
        isPayable = false,
        requiresAction = true
    ),

    /**
     * Bill was cancelled.
     */
    CANCELLED(
        displayName = "Cancelled",
        description = "Bill has been cancelled and is no longer payable.",
        isPayable = false,
        requiresAction = false
    ),

    /**
     * Bill was waived by an authorized user.
     */
    WAIVED(
        displayName = "Waived",
        description = "Outstanding amount has been waived.",
        isPayable = false,
        requiresAction = false
    ),

    /**
     * Bill has been written off.
     */
    WRITTEN_OFF(
        displayName = "Written Off",
        description = "Outstanding amount has been formally written off.",
        isPayable = false,
        requiresAction = false
    ),

    /**
     * Historical bill retained for records.
     */
    ARCHIVED(
        displayName = "Archived",
        description = "Bill has been archived for historical purposes.",
        isPayable = false,
        requiresAction = false
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isOutstanding: Boolean
        get() =
            this == ISSUED ||
                    this == DUE_SOON ||
                    this == OVERDUE ||
                    this == PARTIALLY_PAID

    val isFullySettled: Boolean
        get() =
            this == PAID ||
                    this == WAIVED ||
                    this == WRITTEN_OFF

    val isClosed: Boolean
        get() =
            this == PAID ||
                    this == CANCELLED ||
                    this == WAIVED ||
                    this == WRITTEN_OFF ||
                    this == ARCHIVED

    val isOverdue: Boolean
        get() = this == OVERDUE

    val isPartiallyPaid: Boolean
        get() = this == PARTIALLY_PAID

    val canReceivePayment: Boolean
        get() =
            this == ISSUED ||
                    this == DUE_SOON ||
                    this == OVERDUE ||
                    this == PARTIALLY_PAID

    val needsAttention: Boolean
        get() =
            this == OVERDUE ||
                    this == PARTIALLY_PAID ||
                    this == DISPUTED ||
                    this == ON_HOLD

    /*
     * ---------------------------------------------------------
     * TRANSITIONS
     * ---------------------------------------------------------
     */

    /**
     * Can a tenant make a payment?
     */
    fun canPay(): Boolean =
        canReceivePayment

    /**
     * Can another payment be added to the bill?
     */
    fun canAcceptAdditionalPayment(): Boolean =
        this == PARTIALLY_PAID ||
                this == ISSUED ||
                this == DUE_SOON ||
                this == OVERDUE

    /**
     * Can the bill be disputed?
     */
    fun canDispute(): Boolean =
        this == ISSUED ||
                this == DUE_SOON ||
                this == OVERDUE ||
                this == PARTIALLY_PAID

    /**
     * Can the bill be cancelled?
     */
    fun canCancel(): Boolean =
        this == DRAFT ||
                this == ISSUED ||
                this == DUE_SOON

    /**
     * Can the bill be archived?
     */
    fun canArchive(): Boolean =
        isClosed

    companion object {

        /**
         * Safely convert a backend string into BillStatus.
         */
        fun fromValue(value: String?): BillStatus? {
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
            default: BillStatus = DRAFT
        ): BillStatus {
            return fromValue(value) ?: default
        }
    }
}