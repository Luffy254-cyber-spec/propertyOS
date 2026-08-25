package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * USER MODEL
 * =============================================================
 *
 * Represents the authenticated identity of a person using the
 * application.
 *
 * A User can be:
 *
 *     TENANT
 *     LANDLORD
 *     PROPERTY_MANAGER
 *     CARETAKER
 *     BROKER
 *     TECHNICIAN
 *     ADMIN
 *
 * Authentication and authorization should be based on this model.
 *
 * =============================================================
 */

data class User(

    /*
     * ---------------------------------------------------------
     * IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val firebaseUid: String? = null,

    val email: String? = null,

    val phoneNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PERSONAL INFORMATION
     * ---------------------------------------------------------
     */

    val firstName: String = "",

    val middleName: String? = null,

    val lastName: String = "",

    val displayName: String = "",

    val profileImageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * ACCOUNT ROLES
     * ---------------------------------------------------------
     */

    val roles: List<UserRole> = listOf(UserRole.TENANT),

    val activeRole: UserRole = UserRole.TENANT,

    /*
     * ---------------------------------------------------------
     * AUTHENTICATION PROVIDER
     * ---------------------------------------------------------
     *
     * Supports:
     *
     * - Email/password
     * - Google
     * - Phone
     *
     * Additional providers can be added later.
     *
     * ---------------------------------------------------------
     */

    val authProvider:
    AuthProvider = AuthProvider.EMAIL,

    /*
     * ---------------------------------------------------------
     * ACCOUNT STATUS
     * ---------------------------------------------------------
     */

    val accountStatus:
    AccountStatus = AccountStatus.ACTIVE,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    val emailVerified: Boolean = false,

    val phoneVerified: Boolean = false,

    val identityVerificationStatus:
    IdentityVerificationStatus =
        IdentityVerificationStatus.NOT_VERIFIED,

    /*
     * ---------------------------------------------------------
     * USERNAME
     * ---------------------------------------------------------
     */

    val username: String? = null,
    
    val bio: String? = null,

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
     */

    val county: String? = null,

    val town: String? = null,

    val country: String = "Kenya",

    /*
     * ---------------------------------------------------------
     * PREFERENCES
     * ---------------------------------------------------------
     */

    val preferences:
    UserPreferences = UserPreferences(),

    /*
     * ---------------------------------------------------------
     * NOTIFICATION SETTINGS
     * ---------------------------------------------------------
     */

    val notificationSettings:
    NotificationSettings = NotificationSettings(),

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    val twoFactorEnabled: Boolean = false,

    val lastLoginAt: String? = null,

    val lastLoginIp: String? = null,

    /*
     * ---------------------------------------------------------
     * LOGIN INFORMATION
     * ---------------------------------------------------------
     */

    val loginCount: Long = 0,

    /*
     * ---------------------------------------------------------
     * LANDLORD-SPECIFIC INFORMATION
     * ---------------------------------------------------------
     */

    val landlordProfileId: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANT-SPECIFIC INFORMATION
     * ---------------------------------------------------------
     */

    val tenantProfileId: String? = null,

    /*
     * ---------------------------------------------------------
     * MANAGEMENT INFORMATION
     * ---------------------------------------------------------
     */

    val managedApartmentIds:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
     */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val deletedAt: String? = null

) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Full name of the user.
     */

    val fullName: String
        get() {

            return listOf(
                firstName,
                middleName,
                lastName
            )
                .filter {
                    !it.isNullOrBlank()
                }
                .joinToString(" ")
        }


    /**
     * Whether the user is a tenant.
     */

    val isTenant: Boolean
        get() =
            roles.contains(UserRole.TENANT)


    /**
     * Whether the user is a landlord.
     */

    val isLandlord: Boolean
        get() =
            roles.contains(UserRole.LANDLORD)


    /**
     * Whether the user is a property manager.
     */

    val isPropertyManager: Boolean
        get() =
            roles.contains(UserRole.PROPERTY_MANAGER)


    /**
     * Whether the account is allowed to log in.
     */

    val canLogin: Boolean
        get() =

            accountStatus == AccountStatus.ACTIVE ||
                    accountStatus == AccountStatus.PENDING_VERIFICATION


    /**
     * Whether identity verification has been completed.
     */

    val isIdentityVerified: Boolean
        get() =
            identityVerificationStatus ==
                    IdentityVerificationStatus.VERIFIED


    /**
     * Whether the account has a verified contact method.
     */

    val hasVerifiedContact: Boolean
        get() =
            emailVerified ||
                    phoneVerified
}


/*
 * =============================================================
 * USER ROLE
 * =============================================================
 *
 * Determines what the user is allowed to do in the application.
 *
 * =============================================================
 */

enum class UserRole(

    val displayName: String

) {

    TENANT(
        "Tenant"
    ),

    LANDLORD(
        "Landlord"
    ),

    PROPERTY_MANAGER(
        "Property Manager"
    ),

    CARETAKER(
        "Caretaker"
    ),

    BROKER(
        "Broker"
    ),

    TECHNICIAN(
        "Technician"
    ),

    ADMIN(
        "Administrator"
    )
}


/*
 * =============================================================
 * AUTHENTICATION PROVIDER
 * =============================================================
 */

enum class AuthProvider(

    val displayName: String

) {

    EMAIL(
        "Email & Password"
    ),

    GOOGLE(
        "Google"
    ),

    PHONE(
        "Phone Number"
    ),

    APPLE(
        "Apple"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * ACCOUNT STATUS
 * =============================================================
 */

enum class AccountStatus(

    val displayName: String

) {

    ACTIVE(
        "Active"
    ),

    PENDING_VERIFICATION(
        "Pending Verification"
    ),

    SUSPENDED(
        "Suspended"
    ),

    LOCKED(
        "Locked"
    ),

    DISABLED(
        "Disabled"
    ),

    DELETED(
        "Deleted"
    )
}


/*
 * =============================================================
 * IDENTITY VERIFICATION STATUS
 * =============================================================
 */

enum class IdentityVerificationStatus(

    val displayName: String

) {

    NOT_VERIFIED(
        "Not Verified"
    ),

    PENDING(
        "Verification Pending"
    ),

    VERIFIED(
        "Verified"
    ),

    REJECTED(
        "Verification Rejected"
    ),

    EXPIRED(
        "Verification Expired"
    )
}


/*
 * =============================================================
 * USER PREFERENCES
 * =============================================================
 */

data class UserPreferences(

    val language: String = "English",

    val currency: String = "KES",

    val darkMode: Boolean = false,

    val biometricLogin: Boolean = false,

    val locationEnabled: Boolean = true,

    val marketingNotifications: Boolean = false
)


/*
 * =============================================================
 * NOTIFICATION SETTINGS
 * =============================================================
 */

data class NotificationSettings(

    val pushNotifications: Boolean = true,

    val rentReminders: Boolean = true,

    val billReminders: Boolean = true,

    val paymentNotifications: Boolean = true,

    val maintenanceNotifications: Boolean = true,

    val agreementNotifications: Boolean = true,

    val messageNotifications: Boolean = true,

    val emergencyNotifications: Boolean = true,

    val promotionalNotifications: Boolean = false
)