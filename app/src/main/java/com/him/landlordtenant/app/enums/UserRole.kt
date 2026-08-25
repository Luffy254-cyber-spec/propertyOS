package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * USER ROLE
 * =============================================================
 *
 * Defines the major roles supported by the property-management
 * platform.
 *
 * This enum should be used throughout:
 *
 * - Authentication
 * - Authorization
 * - Navigation
 * - Dashboard selection
 * - Messaging
 * - Property management
 * - Finance
 * - Maintenance
 * - Administration
 *
 * =============================================================
 */

enum class UserRole(

    val displayName: String,

    val description: String,

    val dashboardRoute: String,

    val canManageProperties: Boolean,

    val canManageTenants: Boolean,

    val canManageFinances: Boolean,

    val canManageMaintenance: Boolean,

    val canManageProfessionals: Boolean,

    val canAdvertiseProperties: Boolean,

    val canApproveUsers: Boolean,

    val canAccessAdminTools: Boolean
) {

    /**
     * Tenant / occupant.
     */
    TENANT(
        displayName = "Tenant",
        description = "A person renting or occupying a property.",
        dashboardRoute = "tenant/dashboard",

        canManageProperties = false,
        canManageTenants = false,
        canManageFinances = false,
        canManageMaintenance = true,
        canManageProfessionals = false,
        canAdvertiseProperties = false,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Property owner.
     */
    LANDLORD(
        displayName = "Landlord",
        description = "A property owner who manages properties and tenants.",
        dashboardRoute = "landlord/dashboard",

        canManageProperties = true,
        canManageTenants = true,
        canManageFinances = true,
        canManageMaintenance = true,
        canManageProfessionals = true,
        canAdvertiseProperties = true,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Person responsible for day-to-day property operations.
     */
    CARETAKER(
        displayName = "Caretaker",
        description = "A person responsible for daily property operations.",
        dashboardRoute = "caretaker/dashboard",

        canManageProperties = true,
        canManageTenants = true,
        canManageFinances = true,
        canManageMaintenance = true,
        canManageProfessionals = true,
        canAdvertiseProperties = false,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Property intermediary / letting agent.
     */
    BROKER(
        displayName = "Broker",
        description = "A property broker or letting agent.",
        dashboardRoute = "broker/dashboard",

        canManageProperties = false,
        canManageTenants = true,
        canManageFinances = false,
        canManageMaintenance = false,
        canManageProfessionals = false,
        canAdvertiseProperties = true,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Technician or service provider.
     *
     * Examples:
     * - Plumber
     * - Electrician
     * - Carpenter
     * - Cleaner
     * - Painter
     * - Security technician
     */
    PROFESSIONAL(
        displayName = "Professional",
        description = "A verified technician or property service provider.",
        dashboardRoute = "professional/dashboard",

        canManageProperties = false,
        canManageTenants = false,
        canManageFinances = false,
        canManageMaintenance = true,
        canManageProfessionals = false,
        canAdvertiseProperties = false,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Property manager.
     *
     * Can manage multiple properties on behalf of landlords.
     */
    PROPERTY_MANAGER(
        displayName = "Property Manager",
        description = "A person or company managing properties for owners.",
        dashboardRoute = "manager/dashboard",

        canManageProperties = true,
        canManageTenants = true,
        canManageFinances = true,
        canManageMaintenance = true,
        canManageProfessionals = true,
        canAdvertiseProperties = true,
        canApproveUsers = false,
        canAccessAdminTools = false
    ),

    /**
     * Platform administrator.
     */
    ADMIN(
        displayName = "Administrator",
        description = "Platform administrator with system-wide access.",
        dashboardRoute = "admin/dashboard",

        canManageProperties = true,
        canManageTenants = true,
        canManageFinances = true,
        canManageMaintenance = true,
        canManageProfessionals = true,
        canAdvertiseProperties = true,
        canApproveUsers = true,
        canAccessAdminTools = true
    ),

    /**
     * Customer-support staff.
     */
    SUPPORT(
        displayName = "Support",
        description = "Customer-support staff assisting platform users.",
        dashboardRoute = "support/dashboard",

        canManageProperties = false,
        canManageTenants = true,
        canManageFinances = false,
        canManageMaintenance = false,
        canManageProfessionals = false,
        canAdvertiseProperties = false,
        canApproveUsers = false,
        canAccessAdminTools = false
    );

    /**
     * ---------------------------------------------------------
     * PERMISSION HELPERS
     * ---------------------------------------------------------
     */

    val canManageAnything: Boolean
        get() =
            canManageProperties ||
                    canManageTenants ||
                    canManageFinances ||
                    canManageMaintenance ||
                    canManageProfessionals

    val isStaff: Boolean
        get() =
            this == ADMIN ||
                    this == SUPPORT ||
                    this == PROPERTY_MANAGER ||
                    this == CARETAKER

    val isPropertyOwner: Boolean
        get() = this == LANDLORD

    val isPropertyResident: Boolean
        get() = this == TENANT

    val isServiceProvider: Boolean
        get() = this == PROFESSIONAL

    val isAgent: Boolean
        get() = this == BROKER

    val isAdministrator: Boolean
        get() = this == ADMIN

    val isManagementRole: Boolean
        get() =
            this == LANDLORD ||
                    this == PROPERTY_MANAGER ||
                    this == CARETAKER

    /**
     * ---------------------------------------------------------
     * ROUTING
     * ---------------------------------------------------------
     */

    fun dashboard(): String = dashboardRoute

    companion object {

        /**
         * Safely convert a string into a UserRole.
         *
         * Unknown values return null instead of crashing.
         */
        fun fromValue(value: String?): UserRole? {
            if (value.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            }
        }

        /**
         * Convert an unknown role into a safe default.
         */
        fun fromValueOrDefault(
            value: String?,
            default: UserRole = TENANT
        ): UserRole {
            return fromValue(value) ?: default
        }
    }
}