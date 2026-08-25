package com.him.landlordtenant.app.data.dto.agreement

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * AGREEMENT DTO
 * =============================================================
 *
 * Remote/API representation of a tenant-landlord agreement.
 *
 * Supports:
 * - New tenancy agreements
 * - Digital signing
 * - Witnesses
 * - Rent and deposit terms
 * - House rules
 * - Renewal
 * - Termination
 * - Agreement documents
 * - Tenant/landlord acceptance
 *
 * =============================================================
 */

@Serializable
data class AgreementDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    val version: Int = 1,

    /*
     * ---------------------------------------------------------
     * PARTIES
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val landlordId: String = "",

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val houseId: String = "",

    /*
     * ---------------------------------------------------------
     * AGREEMENT TYPE
     * ---------------------------------------------------------
     */

    val agreementType: String = "TENANCY",

    val tenancyType: String = "MONTHLY",

    /*
     * ---------------------------------------------------------
     * TENANCY PERIOD
     * ---------------------------------------------------------
 */

    val startDate: String? = null,

    val endDate: String? = null,

    val renewalDate: String? = null,

    val noticePeriodDays: Int = 30,

    /*
     * ---------------------------------------------------------
     * RENT
     * ---------------------------------------------------------
 */

    val monthlyRent: Double = 0.0,

    val serviceCharge: Double = 0.0,

    val utilityCharge: Double = 0.0,

    val currency: String = "KES",

    val rentDueDay: Int = 1,

    val rentDueGracePeriodDays: Int = 0,

    /*
     * ---------------------------------------------------------
     * DEPOSIT
     * ---------------------------------------------------------
 */

    val securityDeposit: Double = 0.0,

    val depositPaid: Double = 0.0,

    val depositBalance: Double = 0.0,

    val depositRefundable: Boolean = true,

    /*
     * ---------------------------------------------------------
     * PAYMENT TERMS
     * ---------------------------------------------------------
 */

    val acceptedPaymentMethods: List<String> = emptyList(),

    val latePaymentPenaltyEnabled: Boolean = false,

    val latePaymentPenaltyType: String? = null,

    val latePaymentPenaltyValue: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * PROPERTY RULES
     * ---------------------------------------------------------
 */

    val houseRules: List<String> = emptyList(),

    val petPolicy: String? = null,

    val smokingPolicy: String? = null,

    val sublettingAllowed: Boolean = false,

    val commercialUseAllowed: Boolean = false,

    val guestPolicy: String? = null,

    /*
     * ---------------------------------------------------------
     * UTILITIES
     * ---------------------------------------------------------
 */

    val electricityResponsibility: String? = null,

    val waterResponsibility: String? = null,

    val internetResponsibility: String? = null,

    val garbageResponsibility: String? = null,

    val otherUtilityTerms: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
 */

    val tenantMaintenanceResponsibilities: List<String> = emptyList(),

    val landlordMaintenanceResponsibilities: List<String> = emptyList(),

    val emergencyMaintenanceContactId: String? = null,

    /*
     * ---------------------------------------------------------
     * AGREEMENT TERMS
     * ---------------------------------------------------------
 */

    val termsAndConditions: List<String> = emptyList(),

    val specialConditions: List<String> = emptyList(),

    val terminationConditions: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * DOCUMENT
     * ---------------------------------------------------------
 */

    val documentUrl: String? = null,

    val documentStoragePath: String? = null,

    val documentHash: String? = null,

    val attachmentIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * SIGNING
     * ---------------------------------------------------------
 */

    val tenantSignatureUrl: String? = null,

    val landlordSignatureUrl: String? = null,

    val witnessSignatureUrls: List<String> = emptyList(),

    val tenantSignedAt: String? = null,

    val landlordSignedAt: String? = null,

    val fullySignedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * WITNESSES
     * ---------------------------------------------------------
 */

    val witnessIds: List<String> = emptyList(),

    val witnessNames: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * ACCEPTANCE
     * ---------------------------------------------------------
 */

    val tenantAccepted: Boolean = false,

    val landlordAccepted: Boolean = false,

    val termsAccepted: Boolean = false,

    /*
     * ---------------------------------------------------------
     * AGREEMENT STATUS
     * ---------------------------------------------------------
 */

    val status: String = "DRAFT",

    val rejectionReason: String? = null,

    val cancellationReason: String? = null,

    /*
     * ---------------------------------------------------------
     * RENEWAL
     * ---------------------------------------------------------
 */

    val autoRenewalEnabled: Boolean = false,

    val renewalCount: Int = 0,

    val previousAgreementId: String? = null,

    val renewedAgreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * TERMINATION
     * ---------------------------------------------------------
 */

    val terminationRequestedBy: String? = null,

    val terminationRequestedAt: String? = null,

    val terminationEffectiveDate: String? = null,

    val terminationReason: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verified: Boolean = false,

    val verifiedBy: String? = null,

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val sentAt: String? = null,

    val lastViewedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val totalMonthlyCharge: Double
        get() =
            monthlyRent +
                    serviceCharge +
                    utilityCharge

    val isFullyAccepted: Boolean
        get() =
            tenantAccepted &&
                    landlordAccepted &&
                    termsAccepted

    val isFullySigned: Boolean
        get() =
            !tenantSignedAt.isNullOrBlank() &&
                    !landlordSignedAt.isNullOrBlank()

    val isActive: Boolean
        get() =
            status.uppercase() == "ACTIVE"

    val isExpired: Boolean
        get() =
            status.uppercase() == "EXPIRED"

    val isTerminated: Boolean
        get() =
            status.uppercase() == "TERMINATED"

    val requiresTenantSignature: Boolean
        get() =
            tenantAccepted &&
                    tenantSignedAt.isNullOrBlank()

    val requiresLandlordSignature: Boolean
        get() =
            landlordAccepted &&
                    landlordSignedAt.isNullOrBlank()
}