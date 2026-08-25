package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * TENANT MODEL
 * =============================================================
 *
 * Tenant-specific information.
 *
 * User.kt
 *     ↓
 * Authentication / identity
 *
 * Tenant.kt
 *     ↓
 * Rental / tenancy information
 *
 * A tenant can:
 *
 * - Search apartments
 * - Join an apartment
 * - Join a house
 * - Sign an agreement
 * - Pay deposit
 * - Pay rent
 * - Pay bills
 * - Submit maintenance requests
 * - Chat with landlord
 * - Join tenant group chat
 * - Request to vacate
 *
 * =============================================================
 */

data class Tenant(

    /*
     * ---------------------------------------------------------
     * IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String = "",

    /*
     * ---------------------------------------------------------
     * TENANT PROFILE
     * ---------------------------------------------------------
     */

    val occupation: String? = null,

    val employer: String? = null,

    val nationality: String? = null,

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val idType:
    TenantIdType = TenantIdType.NATIONAL_ID,

    val idNumber: String? = null,

    val idDocumentUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    val verificationStatus:
    IdentityVerificationStatus =
        IdentityVerificationStatus.NOT_VERIFIED,

    val verificationSubmittedAt: String? = null,

    val verificationCompletedAt: String? = null,

    val verificationNotes: String? = null,

    /*
     * ---------------------------------------------------------
     * CURRENT TENANCY
     * ---------------------------------------------------------
     */

    val currentApartmentId: String? = null,

    val currentFloorId: String? = null,

    val currentHouseId: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
     */

    val currentLandlordId: String? = null,

    val currentPropertyManagerId: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANCY STATUS
     * ---------------------------------------------------------
     */

    val tenancyStatus:
    TenancyStatus = TenancyStatus.NOT_RENTING,

    /*
     * ---------------------------------------------------------
     * LEASE INFORMATION
     * ---------------------------------------------------------
     */

    val leaseId: String? = null,

    val leaseStartDate: String? = null,

    val leaseEndDate: String? = null,

    val moveInDate: String? = null,

    val expectedMoveOutDate: String? = null,

    /*
     * ---------------------------------------------------------
     * RENT INFORMATION
     * ---------------------------------------------------------
     */

    val monthlyRent: Double = 0.0,

    val securityDeposit: Double = 0.0,

    val outstandingRent: Double = 0.0,

    val outstandingBills: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * RENT PAYMENT SETTINGS
     * ---------------------------------------------------------
     */

    val rentDueDay: Int = 1,

    val gracePeriodDays: Int = 0,

    val autoPaymentEnabled: Boolean = false,

    /*
     * ---------------------------------------------------------
     * OCCUPANTS
     * ---------------------------------------------------------
     */

    val numberOfOccupants: Int = 1,

    val occupants: List<TenantOccupant> = emptyList(),

    /*
     * ---------------------------------------------------------
     * EMERGENCY CONTACT
     * ---------------------------------------------------------
     */

    val emergencyContact:
    TenantEmergencyContact? = null,

    /*
     * ---------------------------------------------------------
     * CURRENT HOUSE INFORMATION
     * ---------------------------------------------------------
     */

    val houseConditionAtMoveIn:
    HouseCondition = HouseCondition.GOOD,

    val moveInInspectionCompleted: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TENANCY DOCUMENTS
     * ---------------------------------------------------------
     */

    val agreementId: String? = null,

    val signedAgreementUrl: String? = null,

    val otherDocumentUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * VACATING
     * ---------------------------------------------------------
     */

    val vacateRequestId: String? = null,

    val vacateRequestedAt: String? = null,

    val vacateNoticeDays: Int = 3,

    /*
     * ---------------------------------------------------------
     * COMMUNICATION
     * ---------------------------------------------------------
     */

    val tenantGroupChatId: String? = null,

    /*
     * ---------------------------------------------------------
     * FINANCIAL HISTORY
     * ---------------------------------------------------------
     */

    val totalRentPaid: Double = 0.0,

    val totalBillsPaid: Double = 0.0,

    val totalPayments: Int = 0,

    /*
     * ---------------------------------------------------------
     * TENANCY HISTORY
     * ---------------------------------------------------------
     */

    val previousTenancies:
    List<PreviousTenancy> = emptyList(),

    /*
     * ---------------------------------------------------------
     * CREATED / UPDATED
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether the tenant currently has a house.
     */

    val hasCurrentHouse: Boolean
        get() =
            !currentHouseId.isNullOrBlank()


    /**
     * Whether the tenant currently has an active tenancy.
     */

    val hasActiveTenancy: Boolean
        get() =
            tenancyStatus ==
                    TenancyStatus.ACTIVE


    /**
     * Total outstanding amount.
     */

    val totalOutstanding: Double
        get() =
            outstandingRent +
                    outstandingBills


    /**
     * Whether the tenant owes money.
     */

    val hasOutstandingBalance: Boolean
        get() =
            totalOutstanding > 0.0


    /**
     * Whether the tenant has completed identity verification.
     */

    val isVerified: Boolean
        get() =
            verificationStatus ==
                    IdentityVerificationStatus.VERIFIED


    /**
     * Whether a tenant can request to vacate.
     */

    val canRequestVacate: Boolean
        get() =
            tenancyStatus ==
                    TenancyStatus.ACTIVE


    /**
     * Whether the tenant has an emergency contact.
     */

    val hasEmergencyContact: Boolean
        get() =
            emergencyContact != null
}


/*
 * =============================================================
 * TENANCY STATUS
 * =============================================================
 */

enum class TenancyStatus(

    val displayName: String

) {

    NOT_RENTING(
        "Not Renting"
    ),

    APPLICATION_PENDING(
        "Application Pending"
    ),

    APARTMENT_JOIN_PENDING(
        "Apartment Join Pending"
    ),

    HOUSE_SELECTION(
        "Selecting House"
    ),

    PAYMENT_PENDING(
        "Payment Pending"
    ),

    AGREEMENT_PENDING(
        "Agreement Pending"
    ),

    MOVE_IN_PENDING(
        "Move-In Pending"
    ),

    ACTIVE(
        "Active Tenant"
    ),

    VACATE_REQUESTED(
        "Vacate Requested"
    ),

    VACATING(
        "Vacating"
    ),

    MOVED_OUT(
        "Moved Out"
    ),

    TERMINATED(
        "Tenancy Terminated"
    )
}


/*
 * =============================================================
 * TENANT ID TYPE
 * =============================================================
 */

enum class TenantIdType(

    val displayName: String

) {

    NATIONAL_ID(
        "National ID"
    ),

    PASSPORT(
        "Passport"
    ),

    ALIEN_ID(
        "Alien ID"
    ),

    DRIVING_LICENSE(
        "Driving Licence"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * TENANT OCCUPANT
 * =============================================================
 *
 * Other people living in the tenant's house.
 *
 * Example:
 *
 * Main tenant
 *     ├── Spouse
 *     └── Child
 *
 * =============================================================
 */

data class TenantOccupant(

    val id: String = "",

    val name: String = "",

    val relationship:
    OccupantRelationship = OccupantRelationship.OTHER,

    val age: Int? = null,

    val phoneNumber: String? = null,

    val isMinor: Boolean = false,

    val isEmergencyContact: Boolean = false
)


/*
 * =============================================================
 * OCCUPANT RELATIONSHIP
 * =============================================================
 */

enum class OccupantRelationship(

    val displayName: String

) {

    SPOUSE(
        "Spouse"
    ),

    CHILD(
        "Child"
    ),

    PARENT(
        "Parent"
    ),

    SIBLING(
        "Sibling"
    ),

    RELATIVE(
        "Relative"
    ),

    FRIEND(
        "Friend"
    ),

    ROOMMATE(
        "Roommate"
    ),

    CARETAKER(
        "Caretaker"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * EMERGENCY CONTACT
 * =============================================================
 */

data class TenantEmergencyContact(

    val name: String = "",

    val relationship:
    EmergencyRelationship =
        EmergencyRelationship.OTHER,

    val phoneNumber: String = "",

    val alternativePhoneNumber: String? = null,

    val email: String? = null,

    val address: String? = null
)


/*
 * =============================================================
 * EMERGENCY RELATIONSHIP
 * =============================================================
 */

enum class EmergencyRelationship(

    val displayName: String

) {

    PARENT(
        "Parent"
    ),

    SPOUSE(
        "Spouse"
    ),

    SIBLING(
        "Sibling"
    ),

    RELATIVE(
        "Relative"
    ),

    FRIEND(
        "Friend"
    ),

    GUARDIAN(
        "Guardian"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * PREVIOUS TENANCY
 * =============================================================
 *
 * Keeps a history of previous rental properties.
 * =============================================================
 */

data class PreviousTenancy(

    val id: String = "",

    val apartmentId: String = "",

    val apartmentName: String = "",

    val houseId: String = "",

    val houseNumber: String = "",

    val landlordId: String = "",

    val landlordName: String = "",

    val startDate: String? = null,

    val endDate: String? = null,

    val monthlyRent: Double = 0.0,

    val reasonForLeaving: String? = null,

    val finalInspectionCompleted: Boolean = false,

    val outstandingBalance: Double = 0.0
)