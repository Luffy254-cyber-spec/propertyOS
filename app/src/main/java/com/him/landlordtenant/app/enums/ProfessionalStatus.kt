package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PROFESSIONAL STATUS
 * =============================================================
 *
 * Represents the lifecycle and availability of a verified
 * service provider on the platform.
 *
 * Examples:
 * - Plumber
 * - Electrician
 * - Carpenter
 * - Painter
 * - Cleaner
 * - Mason
 * - Appliance technician
 * - Security technician
 * - HVAC technician
 *
 * =============================================================
 */

enum class ProfessionalStatus(
    val displayName: String,
    val description: String,
    val canReceiveJobs: Boolean,
    val canAppearInSearch: Boolean,
    val requiresAction: Boolean
) {

    /**
     * Professional has registered but has not yet been verified.
     */
    PENDING_VERIFICATION(
        displayName = "Pending Verification",
        description = "Professional is awaiting identity and qualification verification.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = true
    ),

    /**
     * Identity and required credentials have been verified.
     */
    VERIFIED(
        displayName = "Verified",
        description = "Professional has successfully passed platform verification.",
        canReceiveJobs = true,
        canAppearInSearch = true,
        requiresAction = false
    ),

    /**
     * Professional is available to accept new jobs.
     */
    AVAILABLE(
        displayName = "Available",
        description = "Professional is available for new maintenance or service requests.",
        canReceiveJobs = true,
        canAppearInSearch = true,
        requiresAction = false
    ),

    /**
     * Professional is temporarily occupied but can still appear
     * in searches.
     */
    BUSY(
        displayName = "Busy",
        description = "Professional is currently busy with other work.",
        canReceiveJobs = false,
        canAppearInSearch = true,
        requiresAction = false
    ),

    /**
     * Professional is currently performing a job.
     */
    ON_JOB(
        displayName = "On Job",
        description = "Professional is currently working on an assigned job.",
        canReceiveJobs = false,
        canAppearInSearch = true,
        requiresAction = false
    ),

    /**
     * Professional is temporarily unavailable.
     */
    OFFLINE(
        displayName = "Offline",
        description = "Professional is currently offline or unavailable.",
        canReceiveJobs = false,
        canAppearInSearch = true,
        requiresAction = false
    ),

    /**
     * Professional account has been temporarily suspended.
     */
    SUSPENDED(
        displayName = "Suspended",
        description = "Professional account has been suspended by the platform.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = true
    ),

    /**
     * Verification/application was rejected.
     */
    REJECTED(
        displayName = "Rejected",
        description = "Professional application or verification was rejected.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = true
    ),

    /**
     * One or more required certifications have expired.
     */
    EXPIRED_CERTIFICATION(
        displayName = "Expired Certification",
        description = "One or more required professional certifications have expired.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = true
    ),

    /**
     * Professional has been permanently blocked.
     */
    BLACKLISTED(
        displayName = "Blacklisted",
        description = "Professional has been permanently restricted from the platform.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = false
    ),

    /**
     * Professional has voluntarily or administratively become
     * inactive.
     */
    INACTIVE(
        displayName = "Inactive",
        description = "Professional account is currently inactive.",
        canReceiveJobs = false,
        canAppearInSearch = false,
        requiresAction = false
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isVerified: Boolean
        get() =
            this == VERIFIED ||
                    this == AVAILABLE ||
                    this == BUSY ||
                    this == ON_JOB ||
                    this == OFFLINE

    val isAvailable: Boolean
        get() =
            this == AVAILABLE

    val isWorking: Boolean
        get() =
            this == ON_JOB

    val isTemporarilyUnavailable: Boolean
        get() =
            this == BUSY ||
                    this == ON_JOB ||
                    this == OFFLINE

    val isRestricted: Boolean
        get() =
            this == SUSPENDED ||
                    this == REJECTED ||
                    this == EXPIRED_CERTIFICATION ||
                    this == BLACKLISTED

    val needsVerification: Boolean
        get() =
            this == PENDING_VERIFICATION ||
                    this == EXPIRED_CERTIFICATION

    val canWork: Boolean
        get() =
            canReceiveJobs &&
                    isVerified

    /*
     * ---------------------------------------------------------
     * JOB HELPERS
     * ---------------------------------------------------------
     */

    /**
     * Can the professional receive a new job?
     */
    fun canAcceptJob(): Boolean =
        this == VERIFIED ||
                this == AVAILABLE

    /**
     * Can the professional be shown as a recommended
     * service provider?
     */
    fun canBeRecommended(): Boolean =
        this == VERIFIED ||
                this == AVAILABLE ||
                this == BUSY ||
                this == OFFLINE

    /**
     * Can the professional return to available status?
     */
    fun canBecomeAvailable(): Boolean =
        this == VERIFIED ||
                this == BUSY ||
                this == OFFLINE ||
                this == ON_JOB

    /**
     * Can the professional be suspended?
     */
    fun canSuspend(): Boolean =
        this != BLACKLISTED &&
                this != SUSPENDED &&
                this != REJECTED

    /**
     * Can the professional request verification again?
     */
    fun canRequestVerification(): Boolean =
        this == REJECTED ||
                this == EXPIRED_CERTIFICATION ||
                this == INACTIVE

    companion object {

        /**
         * Safely convert a backend string to ProfessionalStatus.
         */
        fun fromValue(value: String?): ProfessionalStatus? {
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
            default: ProfessionalStatus = PENDING_VERIFICATION
        ): ProfessionalStatus {
            return fromValue(value) ?: default
        }
    }
}