package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * TENANT MEMBERSHIP MODEL
 * =============================================================
 *
 * Represents the relationship between a tenant and a property.
 *
 * A tenant may:
 *
 * 1. Browse an apartment
 * 2. Request to join
 * 3. Accept apartment agreement
 * 4. Select a vacant house
 * 5. Pay required deposit/rent
 * 6. Become an active tenant
 * 7. Live in the house
 * 8. Give notice
 * 9. Vacate
 * 10. Complete final inspection
 *
 * =============================================================
 *
 * IMPORTANT
 *
 * User.kt represents the person.
 *
 * Tenant.kt represents tenant-specific profile information.
 *
 * TenantMembership.kt represents the TENANCY itself.
 *
 * =============================================================
 */

data class TenantMembership(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    /*
     * Tenant/user who owns this membership.
     */

    val tenantId: String = "",

    val tenantName: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String = "",

    val apartmentName: String? = null,

    val floorId: String? = null,

    val floorNumber: Int? = null,

    val houseId: String? = null,

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
     */

    val landlordId: String = "",

    val landlordName: String? = null,

    /*
     * ---------------------------------------------------------
     * MEMBERSHIP STATUS
     * ---------------------------------------------------------
     */

    val status:
    TenantMembershipStatus =
        TenantMembershipStatus.PENDING,

    /*
     * ---------------------------------------------------------
     * JOIN PROCESS
     * ---------------------------------------------------------
 */

    val joinRequest:
    TenantJoinRequest =
        TenantJoinRequest(),

    /*
     * ---------------------------------------------------------
     * AGREEMENT
     * ---------------------------------------------------------
 */

    val agreement:
    MembershipAgreement =
        MembershipAgreement(),

    /*
     * ---------------------------------------------------------
     * FINANCIAL INFORMATION
     * ---------------------------------------------------------
 */

    val financial:
    TenantFinancialSummary =
        TenantFinancialSummary(),

    /*
     * ---------------------------------------------------------
     * MOVE-IN
     * ---------------------------------------------------------
 */

    val moveIn:
    TenantMoveIn =
        TenantMoveIn(),

    /*
     * ---------------------------------------------------------
     * MOVE-OUT
     * ---------------------------------------------------------
 */

    val moveOut:
    TenantMoveOut =
        TenantMoveOut(),

    /*
     * ---------------------------------------------------------
     * CURRENT TENANCY
     * ---------------------------------------------------------
 */

    val tenancy:
    TenancyDetails =
        TenancyDetails(),

    /*
     * ---------------------------------------------------------
     * INSPECTION
     * ---------------------------------------------------------
 */

    val inspections:
    List<TenancyInspection> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * DOCUMENTS
     * ---------------------------------------------------------
 */

    val documents:
    List<TenancyDocument> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
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

    val isActive: Boolean
        get() =
            status ==
                    TenantMembershipStatus.ACTIVE

    val hasJoined: Boolean
        get() =
            status !=
                    TenantMembershipStatus.PENDING

    val hasAgreement: Boolean
        get() =
            agreement.accepted

    val hasPaidRequiredAmount: Boolean
        get() =
            financial.requiredAmountPaid

    val isVacating: Boolean
        get() =
            status ==
                    TenantMembershipStatus.NOTICE_GIVEN ||
                    status ==
                    TenantMembershipStatus.VACATING

    val isVacated: Boolean
        get() =
            status ==
                    TenantMembershipStatus.VACATED
}


/*
 * =============================================================
 * TENANT MEMBERSHIP STATUS
 * =============================================================
 */

enum class TenantMembershipStatus(

    val displayName: String

) {

    /*
     * Tenant has started the joining process.
     */

    PENDING(
        "Pending"
    ),

    /*
     * Tenant requested to join the apartment.
     */

    JOIN_REQUESTED(
        "Join Requested"
    ),

    /*
     * Agreement has been presented.
     */

    AGREEMENT_PENDING(
        "Agreement Pending"
    ),

    /*
     * Agreement accepted.
     */

    AGREEMENT_ACCEPTED(
        "Agreement Accepted"
    ),

    /*
     * Waiting for required payment.
     */

    PAYMENT_PENDING(
        "Payment Pending"
    ),

    /*
     * Payment completed.
     */

    PAYMENT_COMPLETED(
        "Payment Completed"
    ),

    /*
     * Tenant has been assigned a house.
     */

    HOUSE_ASSIGNED(
        "House Assigned"
    ),

    /*
     * Tenant has moved in.
     */

    ACTIVE(
        "Active Tenant"
    ),

    /*
     * Tenant gave notice.
     */

    NOTICE_GIVEN(
        "Notice Given"
    ),

    /*
     * Move-out process has started.
     */

    VACATING(
        "Vacating"
    ),

    /*
     * Tenant has left the house.
     */

    VACATED(
        "Vacated"
    ),

    /*
     * Membership was terminated.
     */

    TERMINATED(
        "Terminated"
    ),

    /*
     * Application/join request was rejected.
     */

    REJECTED(
        "Rejected"
    )
}


/*
 * =============================================================
 * TENANT JOIN REQUEST
 * =============================================================
 */

data class TenantJoinRequest(

    val requested: Boolean = false,

    val requestedAt: String? = null,

    val reviewed: Boolean = false,

    val reviewedAt: String? = null,

    val reviewedBy: String? = null,

    val approved: Boolean = false,

    val rejectionReason: String? = null
)


/*
 * =============================================================
 * MEMBERSHIP AGREEMENT
 * =============================================================
 *
 * Connects this tenancy with the legally accepted agreement.
 *
 * =============================================================
 */

data class MembershipAgreement(

    val agreementId: String? = null,

    val agreementVersion: Int? = null,

    val accepted: Boolean = false,

    val acceptedAt: String? = null,

    val acceptedByTenant: Boolean = false,

    val acceptedByLandlord: Boolean = false,

    val agreementHash: String? = null,

    val immutable: Boolean = true
)


/*
 * =============================================================
 * TENANT FINANCIAL SUMMARY
 * =============================================================
 */

data class TenantFinancialSummary(

    /*
     * Required amount before moving in.
     *
     * Example:
     *
     * Deposit       = KES 15,000
     * First rent    = KES 15,000
     * Other charges = KES 2,000
     *
     * Total         = KES 32,000
     */

    val requiredAmount: Double = 0.0,

    val requiredAmountPaid: Boolean = false,

    val amountPaidBeforeMoveIn: Double = 0.0,

    /*
     * Security deposit.
     */

    val securityDeposit: Double = 0.0,

    val securityDepositPaid: Double = 0.0,

    /*
     * Current monthly rent.
     */

    val monthlyRent: Double = 0.0,

    /*
     * Current outstanding balance.
     */

    val outstandingBalance: Double = 0.0,

    /*
     * Total amount paid during tenancy.
     */

    val totalPaid: Double = 0.0,

    /*
     * Currency.
     */

    val currency: String = "KES"
)


/*
 * =============================================================
 * MOVE-IN INFORMATION
 * =============================================================
 */

data class TenantMoveIn(

    val scheduled: Boolean = false,

    val scheduledDate: String? = null,

    val actualDate: String? = null,

    val completed: Boolean = false,

    val completedBy: String? = null,

    val notes: String? = null
)


/*
 * =============================================================
 * MOVE-OUT INFORMATION
 * =============================================================
 */

data class TenantMoveOut(

    /*
     * Notice period required.
     */

    val noticePeriodDays: Int = 3,

    /*
     * Tenant submitted notice.
     */

    val noticeGiven: Boolean = false,

    val noticeDate: String? = null,

    /*
     * Expected move-out date.
     */

    val expectedMoveOutDate: String? = null,

    /*
     * Actual date tenant left.
     */

    val actualMoveOutDate: String? = null,

    /*
     * Whether tenant has physically left.
     */

    val completed: Boolean = false,

    /*
     * Reason for leaving.
     */

    val reason:
    MoveOutReason? = null,

    val notes: String? = null
)


/*
 * =============================================================
 * MOVE-OUT REASON
 * =============================================================
 */

enum class MoveOutReason(

    val displayName: String

) {

    RELOCATING(
        "Relocating"
    ),

    JOB_CHANGE(
        "Job Change"
    ),

    FAMILY(
        "Family Reasons"
    ),

    FINANCIAL(
        "Financial Reasons"
    ),

    FOUND_ANOTHER_PROPERTY(
        "Found Another Property"
    ),

    LANDLORD_REQUEST(
        "Landlord Request"
    ),

    AGREEMENT_EXPIRED(
        "Agreement Expired"
    ),

    PROPERTY_SALE(
        "Property Sale"
    ),

    MAINTENANCE(
        "Property Maintenance"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * TENANCY DETAILS
 * =============================================================
 */

data class TenancyDetails(

    /*
     * Start of tenancy.
     */

    val startDate: String? = null,

    /*
     * End of tenancy if fixed-term.
     */

    val endDate: String? = null,

    /*
     * Whether agreement is fixed-term.
     */

    val fixedTerm: Boolean = false,

    /*
     * Rent payment day.
     *
     * Example:
     *
     * 28
     *
     * means rent is due on the 28th.
     */

    val rentDueDay: Int = 28,

    /*
     * Automatic reminders.
     */

    val rentReminderEnabled: Boolean = true,

    /*
     * Whether tenant currently occupies the house.
     */

    val occupying: Boolean = false
)


/*
 * =============================================================
 * TENANCY INSPECTION
 * =============================================================
 */

data class TenancyInspection(

    val id: String = "",

    val type:
    InspectionType =
        InspectionType.MOVE_IN,

    val inspectionDate: String? = null,

    val conductedBy: String? = null,

    val condition:
    PropertyCondition =
        PropertyCondition.GOOD,

    val notes: String? = null,

    val imageUrls:
    List<String> =
        emptyList(),

    val completed: Boolean = false
)


/*
 * =============================================================
 * INSPECTION TYPE
 * =============================================================
 */

enum class InspectionType {

    MOVE_IN,

    ROUTINE,

    MAINTENANCE,

    MOVE_OUT,

    FINAL
}


/*
 * =============================================================
 * PROPERTY CONDITION
 * =============================================================
 */

enum class PropertyCondition(

    val displayName: String

) {

    EXCELLENT(
        "Excellent"
    ),

    GOOD(
        "Good"
    ),

    FAIR(
        "Fair"
    ),

    NEEDS_REPAIR(
        "Needs Repair"
    ),

    NEEDS_PAINTING(
        "Needs Painting"
    ),

    SERIOUS_DAMAGE(
        "Serious Damage"
    )
}


/*
 * =============================================================
 * TENANCY DOCUMENT
 * =============================================================
 */

data class TenancyDocument(

    val id: String = "",

    val name: String = "",

    val type:
    TenancyDocumentType =
        TenancyDocumentType.OTHER,

    val documentUrl: String? = null,

    val uploadedBy: String? = null,

    val uploadedAt: String? = null
)


/*
 * =============================================================
 * TENANCY DOCUMENT TYPE
 * =============================================================
 */

enum class TenancyDocumentType {

    AGREEMENT,

    ID_DOCUMENT,

    PAYMENT_RECEIPT,

    MOVE_IN_INSPECTION,

    MOVE_OUT_INSPECTION,

    NOTICE_TO_VACATE,

    INVENTORY,

    OTHER
}