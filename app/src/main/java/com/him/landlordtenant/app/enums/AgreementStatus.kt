package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * AGREEMENT STATUS
 * =============================================================
 *
 * Represents the complete lifecycle of a tenant-landlord
 * agreement.
 *
 * Used for:
 * - Tenancy agreements
 * - Lease agreements
 * - Renewals
 * - Electronic signatures
 * - Agreement expiry
 * - Termination
 * - Disputes
 * - Agreement history
 *
 * =============================================================
 */

enum class AgreementStatus(
    val displayName: String,
    val description: String,
    val isActive: Boolean,
    val requiresAction: Boolean
) {

    /**
     * Agreement is being prepared.
     */
    DRAFT(
        displayName = "Draft",
        description = "Agreement is being prepared and has not been submitted.",
        isActive = false,
        requiresAction = false
    ),

    /**
     * Agreement has been sent to the tenant.
     */
    PENDING_TENANT_SIGNATURE(
        displayName = "Pending Tenant Signature",
        description = "Agreement is waiting for the tenant to sign.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement has been sent to the landlord.
     */
    PENDING_LANDLORD_SIGNATURE(
        displayName = "Pending Landlord Signature",
        description = "Agreement is waiting for the landlord to sign.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Both parties have signed.
     */
    SIGNED(
        displayName = "Signed",
        description = "Agreement has been signed by all required parties.",
        isActive = false,
        requiresAction = false
    ),

    /**
     * Agreement is awaiting final verification/activation.
     */
    PENDING_APPROVAL(
        displayName = "Pending Approval",
        description = "Agreement has been signed and is awaiting approval.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement is currently governing the tenancy.
     */
    ACTIVE(
        displayName = "Active",
        description = "Agreement is currently active.",
        isActive = true,
        requiresAction = false
    ),

    /**
     * Agreement is approaching its end date.
     */
    EXPIRING_SOON(
        displayName = "Expiring Soon",
        description = "Agreement is active but approaching its expiry date.",
        isActive = true,
        requiresAction = true
    ),

    /**
     * Agreement has reached its end date.
     */
    EXPIRED(
        displayName = "Expired",
        description = "Agreement has passed its expiry date.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement is being renewed.
     */
    RENEWAL_PENDING(
        displayName = "Renewal Pending",
        description = "A renewal has been initiated but is not yet complete.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement was renewed successfully.
     */
    RENEWED(
        displayName = "Renewed",
        description = "Agreement was successfully renewed.",
        isActive = false,
        requiresAction = false
    ),

    /**
     * Agreement has been terminated before its planned end date.
     */
    TERMINATED(
        displayName = "Terminated",
        description = "Agreement was terminated before or at its end.",
        isActive = false,
        requiresAction = false
    ),

    /**
     * Agreement is currently involved in a dispute.
     */
    DISPUTED(
        displayName = "Disputed",
        description = "Agreement is subject to an active dispute.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement was rejected.
     */
    REJECTED(
        displayName = "Rejected",
        description = "Agreement was rejected by a required party.",
        isActive = false,
        requiresAction = true
    ),

    /**
     * Agreement was cancelled before becoming active.
     */
    CANCELLED(
        displayName = "Cancelled",
        description = "Agreement was cancelled before activation.",
        isActive = false,
        requiresAction = false
    ),

    /**
     * Agreement has been archived.
     */
    ARCHIVED(
        displayName = "Archived",
        description = "Agreement is retained for historical records.",
        isActive = false,
        requiresAction = false
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isPending: Boolean
        get() =
            this == PENDING_TENANT_SIGNATURE ||
                    this == PENDING_LANDLORD_SIGNATURE ||
                    this == PENDING_APPROVAL ||
                    this == RENEWAL_PENDING

    val isSigned: Boolean
        get() =
            this == SIGNED ||
                    this == ACTIVE ||
                    this == EXPIRING_SOON ||
                    this == RENEWED ||
                    this == EXPIRED ||
                    this == TERMINATED

    val isCompleted: Boolean
        get() =
            this == SIGNED ||
                    this == ACTIVE ||
                    this == EXPIRING_SOON ||
                    this == RENEWED ||
                    this == EXPIRED ||
                    this == TERMINATED

    val isTerminated: Boolean
        get() =
            this == TERMINATED ||
                    this == CANCELLED

    val requiresSignature: Boolean
        get() =
            this == PENDING_TENANT_SIGNATURE ||
                    this == PENDING_LANDLORD_SIGNATURE

    val canBeRenewed: Boolean
        get() =
            this == ACTIVE ||
                    this == EXPIRING_SOON ||
                    this == EXPIRED

    val canBeCancelled: Boolean
        get() =
            this == DRAFT ||
                    this == PENDING_TENANT_SIGNATURE ||
                    this == PENDING_LANDLORD_SIGNATURE ||
                    this == PENDING_APPROVAL

    val isFinal: Boolean
        get() =
            this == REJECTED ||
                    this == CANCELLED ||
                    this == TERMINATED ||
                    this == ARCHIVED

    /*
     * ---------------------------------------------------------
     * TRANSITION HELPERS
     * ---------------------------------------------------------
     */

    /**
     * Determines whether a tenant can sign.
     */
    fun canTenantSign(): Boolean =
        this == PENDING_TENANT_SIGNATURE

    /**
     * Determines whether a landlord can sign.
     */
    fun canLandlordSign(): Boolean =
        this == PENDING_LANDLORD_SIGNATURE

    /**
     * Determines whether the agreement can become active.
     */
    fun canActivate(): Boolean =
        this == SIGNED ||
                this == PENDING_APPROVAL

    /**
     * Determines whether the agreement can enter dispute.
     */
    fun canDispute(): Boolean =
        this == ACTIVE ||
                this == EXPIRING_SOON

    /**
     * Determines whether the agreement can be terminated.
     */
    fun canTerminate(): Boolean =
        this == ACTIVE ||
                this == EXPIRING_SOON ||
                this == DISPUTED

    companion object {

        /**
         * Safely parse a status received from the backend.
         */
        fun fromValue(value: String?): AgreementStatus? {
            if (value.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            }
        }

        /**
         * Parse status with a safe fallback.
         */
        fun fromValueOrDefault(
            value: String?,
            default: AgreementStatus = DRAFT
        ): AgreementStatus {
            return fromValue(value) ?: default
        }
    }
}