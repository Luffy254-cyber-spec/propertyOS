package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PROPERTY STATUS
 * =============================================================
 *
 * Represents the lifecycle/status of a property or property
 * listing within the platform.
 *
 * Used by:
 * - Landlords
 * - Property managers
 * - Caretakers
 * - Brokers
 * - Tenants
 * - Admins
 *
 * =============================================================
 */

enum class PropertyStatus(
    val displayName: String,
    val description: String,
    val isVisibleToTenants: Boolean,
    val canBeAdvertised: Boolean,
    val canReceiveApplications: Boolean
) {

    /**
     * Property/listing is still being prepared.
     */
    DRAFT(
        displayName = "Draft",
        description = "Property has been created but is not ready for publishing.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property is waiting for verification.
     */
    PENDING_VERIFICATION(
        displayName = "Pending Verification",
        description = "Property is awaiting verification or administrative approval.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property is publicly available.
     */
    AVAILABLE(
        displayName = "Available",
        description = "Property is available for viewing, applications or tenancy.",
        isVisibleToTenants = true,
        canBeAdvertised = true,
        canReceiveApplications = true
    ),

    /**
     * Someone has temporarily reserved the property.
     */
    RESERVED(
        displayName = "Reserved",
        description = "Property has been reserved by a prospective tenant.",
        isVisibleToTenants = true,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property is currently occupied.
     */
    OCCUPIED(
        displayName = "Occupied",
        description = "Property currently has an active tenant.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Some units are occupied while others remain available.
     */
    PARTIALLY_OCCUPIED(
        displayName = "Partially Occupied",
        description = "Some units are occupied while other units are available.",
        isVisibleToTenants = true,
        canBeAdvertised = true,
        canReceiveApplications = true
    ),

    /**
     * Property temporarily unavailable because of repairs.
     */
    MAINTENANCE(
        displayName = "Under Maintenance",
        description = "Property is temporarily unavailable due to maintenance.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property is undergoing major improvements.
     */
    RENOVATION(
        displayName = "Under Renovation",
        description = "Property is undergoing renovation or construction work.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property has been temporarily disabled.
     */
    SUSPENDED(
        displayName = "Suspended",
        description = "Property has been suspended from the platform.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property exists but is hidden from public listings.
     */
    HIDDEN(
        displayName = "Hidden",
        description = "Property is hidden from public searches.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Property has been sold.
     */
    SOLD(
        displayName = "Sold",
        description = "Property has been sold and is no longer available for rental.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    ),

    /**
     * Historical property/listing that is no longer active.
     */
    ARCHIVED(
        displayName = "Archived",
        description = "Property has been archived and is no longer active.",
        isVisibleToTenants = false,
        canBeAdvertised = false,
        canReceiveApplications = false
    );

    /*
     * ---------------------------------------------------------
     * STATE HELPERS
     * ---------------------------------------------------------
     */

    val isActive: Boolean
        get() =
            this == AVAILABLE ||
                    this == PARTIALLY_OCCUPIED ||
                    this == RESERVED ||
                    this == OCCUPIED

    val isAvailable: Boolean
        get() =
            this == AVAILABLE ||
                    this == PARTIALLY_OCCUPIED

    val isUnavailable: Boolean
        get() =
            this == MAINTENANCE ||
                    this == RENOVATION ||
                    this == SUSPENDED ||
                    this == HIDDEN ||
                    this == SOLD ||
                    this == ARCHIVED

    val requiresVerification: Boolean
        get() = this == PENDING_VERIFICATION

    val isPublic: Boolean
        get() = isVisibleToTenants

    /*
     * ---------------------------------------------------------
     * TRANSITION HELPERS
     * ---------------------------------------------------------
     */

    /**
     * Determines whether the property can be published.
     */
    fun canPublish(): Boolean {
        return this == DRAFT ||
                this == PENDING_VERIFICATION ||
                this == HIDDEN
    }

    /**
     * Determines whether the property can accept
     * tenant applications.
     */
    fun canAcceptApplications(): Boolean {
        return canReceiveApplications
    }

    /**
     * Determines whether the property can be reserved.
     */
    fun canReserve(): Boolean {
        return this == AVAILABLE ||
                this == PARTIALLY_OCCUPIED
    }

    /**
     * Determines whether the property can be marked occupied.
     */
    fun canMarkOccupied(): Boolean {
        return this == RESERVED ||
                this == AVAILABLE
    }

    /**
     * Determines whether the property can be archived.
     */
    fun canArchive(): Boolean {
        return this != ARCHIVED
    }

    companion object {

        /**
         * Safely convert a String into PropertyStatus.
         */
        fun fromValue(value: String?): PropertyStatus? {
            if (value.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            }
        }

        /**
         * Convert a String into PropertyStatus with
         * a safe fallback.
         */
        fun fromValueOrDefault(
            value: String?,
            default: PropertyStatus = DRAFT
        ): PropertyStatus {
            return fromValue(value) ?: default
        }
    }
}