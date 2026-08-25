package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MAINTENANCE STATUS
 * =============================================================
 *
 * Represents the complete lifecycle of a maintenance request.
 *
 * Flow:
 *
 * Tenant reports issue
 *        ↓
 *      PENDING
 *        ↓
 *    ASSIGNED
 *        ↓
 *    ACCEPTED
 *        ↓
 *     SCHEDULED
 *        ↓
 *     IN_PROGRESS
 *        ↓
 *     COMPLETED
 *        ↓
 * TENANT_CONFIRMATION
 *        ↓
 *      CLOSED
 *
 * Also supports:
 * - Rejection
 * - Cancellation
 * - Rescheduling
 * - Disputes
 * - Escalation
 * - Emergency requests
 *
 * =============================================================
 */

enum class MaintenanceStatus(
    val displayName: String,
    val description: String,
    val requiresAction: Boolean,
    val isActive: Boolean
) {

    /**
     * Tenant has submitted a maintenance request.
     */
    PENDING(
        displayName = "Pending",
        description = "Maintenance request has been submitted and is awaiting review.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Request has been reviewed and assigned to a professional.
     */
    ASSIGNED(
        displayName = "Assigned",
        description = "A technician or professional has been assigned.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Assigned professional accepted the job.
     */
    ACCEPTED(
        displayName = "Accepted",
        description = "Assigned professional accepted the maintenance request.",
        requiresAction = false,
        isActive = true
    ),

    /**
     * Visit/work has been scheduled.
     */
    SCHEDULED(
        displayName = "Scheduled",
        description = "Maintenance work has been scheduled.",
        requiresAction = false,
        isActive = true
    ),

    /**
     * Professional has started working.
     */
    IN_PROGRESS(
        displayName = "In Progress",
        description = "Maintenance work is currently being performed.",
        requiresAction = false,
        isActive = true
    ),

    /**
     * Technician reports that the work is finished.
     */
    COMPLETED(
        displayName = "Completed",
        description = "Professional has reported that the maintenance work is complete.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Tenant must confirm whether the work was satisfactory.
     */
    TENANT_CONFIRMATION(
        displayName = "Awaiting Tenant Confirmation",
        description = "Completed work is awaiting tenant confirmation.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Request has been successfully completed and confirmed.
     */
    CLOSED(
        displayName = "Closed",
        description = "Maintenance request has been completed and closed.",
        requiresAction = false,
        isActive = false
    ),

    /**
     * Assigned professional rejected the job.
     */
    REJECTED(
        displayName = "Rejected",
        description = "Maintenance request or assignment was rejected.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Request was cancelled.
     */
    CANCELLED(
        displayName = "Cancelled",
        description = "Maintenance request was cancelled.",
        requiresAction = false,
        isActive = false
    ),

    /**
     * Appointment/work needs to be scheduled again.
     */
    RESCHEDULE_REQUIRED(
        displayName = "Reschedule Required",
        description = "Maintenance appointment needs to be rescheduled.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Tenant disagrees that the work is complete.
     */
    DISPUTED(
        displayName = "Disputed",
        description = "Tenant or another authorized party disputed the completed work.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Request requires higher-level intervention.
     */
    ESCALATED(
        displayName = "Escalated",
        description = "Maintenance request has been escalated for further intervention.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Emergency maintenance request.
     */
    EMERGENCY(
        displayName = "Emergency",
        description = "Urgent maintenance issue requiring immediate attention.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Request is waiting for landlord/property manager approval.
     */
    AWAITING_APPROVAL(
        displayName = "Awaiting Approval",
        description = "Maintenance request requires authorization before work can proceed.",
        requiresAction = true,
        isActive = true
    ),

    /**
     * Work cannot continue because it is waiting for parts,
     * materials, funds or another dependency.
     */
    ON_HOLD(
        displayName = "On Hold",
        description = "Maintenance work is temporarily paused.",
        requiresAction = true,
        isActive = true
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isPending: Boolean
        get() =
            this == PENDING ||
                    this == AWAITING_APPROVAL

    val isAssigned: Boolean
        get() =
            this == ASSIGNED ||
                    this == ACCEPTED

    val isScheduled: Boolean
        get() =
            this == SCHEDULED

    val isBeingWorkedOn: Boolean
        get() =
            this == IN_PROGRESS

    val isCompleted: Boolean
        get() =
            this == COMPLETED ||
                    this == TENANT_CONFIRMATION ||
                    this == CLOSED

    val isClosed: Boolean
        get() =
            this == CLOSED ||
                    this == CANCELLED

    val needsAttention: Boolean
        get() =
            requiresAction ||
                    this == DISPUTED ||
                    this == ESCALATED ||
                    this == EMERGENCY ||
                    this == ON_HOLD

    val isUrgent: Boolean
        get() =
            this == EMERGENCY ||
                    this == ESCALATED

    /*
     * ---------------------------------------------------------
     * ACTION HELPERS
     * ---------------------------------------------------------
     */

    /**
     * Can a professional accept the assignment?
     */
    fun canAccept(): Boolean =
        this == ASSIGNED ||
                this == REJECTED

    /**
     * Can the job be scheduled?
     */
    fun canSchedule(): Boolean =
        this == ACCEPTED ||
                this == RESCHEDULE_REQUIRED

    /**
     * Can work begin?
     */
    fun canStartWork(): Boolean =
        this == SCHEDULED ||
                this == EMERGENCY

    /**
     * Can the professional mark the work complete?
     */
    fun canCompleteWork(): Boolean =
        this == IN_PROGRESS

    /**
     * Can the tenant confirm the work?
     */
    fun canTenantConfirm(): Boolean =
        this == COMPLETED ||
                this == TENANT_CONFIRMATION

    /**
     * Can the tenant dispute the completed work?
     */
    fun canDispute(): Boolean =
        this == COMPLETED ||
                this == TENANT_CONFIRMATION

    /**
     * Can the request be cancelled?
     */
    fun canCancel(): Boolean =
        this == PENDING ||
                this == ASSIGNED ||
                this == ACCEPTED ||
                this == SCHEDULED ||
                this == AWAITING_APPROVAL

    /**
     * Can the request be escalated?
     */
    fun canEscalate(): Boolean =
        this != CLOSED &&
                this != CANCELLED &&
                this != ESCALATED

    /**
     * Can the request be placed on hold?
     */
    fun canPlaceOnHold(): Boolean =
        this == ACCEPTED ||
                this == SCHEDULED ||
                this == IN_PROGRESS

    companion object {

        /**
         * Safely parse backend status.
         */
        fun fromValue(value: String?): MaintenanceStatus? {
            if (value.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            }
        }

        /**
         * Parse backend value with fallback.
         */
        fun fromValueOrDefault(
            value: String?,
            default: MaintenanceStatus = PENDING
        ): MaintenanceStatus {
            return fromValue(value) ?: default
        }
    }
}