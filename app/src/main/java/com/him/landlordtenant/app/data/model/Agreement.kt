package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * TENANCY AGREEMENT MODEL
 * =============================================================
 */

data class Agreement(
    val id: String = "",
    val apartmentId: String = "",
    val houseId: String = "",
    val tenantId: String = "",
    val landlordId: String = "",
    val version: String = "1.0",
    val status: AgreementStatus = AgreementStatus.DRAFT,
    val content: String = "",
    val monthlyRent: Double = 0.0,
    val securityDeposit: Double = 0.0,
    val startDate: String = "",
    val endDate: String? = null,
    val noticePeriodDays: Int = 30,
    val terms: List<AgreementTerm> = emptyList(),
    val signatures: List<DigitalSignature> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

data class AgreementTerm(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val mandatory: Boolean = true
)

data class DigitalSignature(
    val userId: String = "",
    val userName: String = "",
    val timestamp: String = "",
    val ipAddress: String? = null,
    val signatureHash: String = ""
)

enum class AgreementStatus(val displayName: String) {
    DRAFT("Draft"),
    PENDING_TENANT_SIGNATURE("Pending Tenant Signature"),
    PENDING_LANDLORD_SIGNATURE("Pending Landlord Signature"),
    ACTIVE("Active"),
    TERMINATED("Terminated"),
    EXPIRED("Expired")
}
