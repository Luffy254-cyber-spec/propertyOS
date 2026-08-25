package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * AGREEMENT STATUS
 * =============================================================
 */
enum class AgreementStatus(
    val displayName: String,
    val description: String
) {
    DRAFT(
        "Draft",
        "The agreement is still being drafted and is not yet visible to the tenant."
    ),

    PENDING_TENANT_SIGNATURE(
        "Pending Tenant",
        "The agreement is ready and waiting for the tenant to review and sign."
    ),

    PENDING_LANDLORD_SIGNATURE(
        "Pending Landlord",
        "The tenant has signed, and the agreement is waiting for the landlord's final signature."
    ),

    ACTIVE(
        "Active",
        "The agreement is fully signed and legally binding."
    ),

    TERMINATED(
        "Terminated",
        "The agreement has been ended by either party before its expiry date."
    ),

    EXPIRED(
        "Expired",
        "The agreement's duration has ended."
    ),

    REJECTED(
        "Rejected",
        "The tenant declined the terms of the agreement."
    ),

    ARCHIVED(
        "Archived",
        "The agreement is no longer relevant and has been moved to records."
    );

    val isSigned: Boolean
        get() = this == ACTIVE || this == TERMINATED || this == EXPIRED

    val canBeSigned: Boolean
        get() = this == PENDING_TENANT_SIGNATURE || this == PENDING_LANDLORD_SIGNATURE

    val isEditable: Boolean
        get() = this == DRAFT
}
