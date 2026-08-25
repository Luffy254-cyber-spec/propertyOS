package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * TENANCY AGREEMENT (DOMAIN MODEL)
 * =============================================================
 *
 * Central model representing a legal contract between a
 * landlord and a tenant for a specific house unit.
 */
data class Agreement(
    val id: String = "",
    val referenceNumber: String = "",
    
    // Parties
    val apartmentId: String = "",
    val apartmentName: String = "",
    val houseId: String = "",
    val houseNumber: String = "",
    val tenantId: String = "",
    val tenantName: String = "",
    val landlordId: String = "",
    val landlordName: String = "",
    
    // Status and Versioning
    val status: AgreementStatus = AgreementStatus.DRAFT,
    val version: String = "1.0",
    val history: List<AgreementVersion> = emptyList(),
    
    // Financials
    val monthlyRent: Double = 0.0,
    val securityDeposit: Double = 0.0,
    val serviceCharge: Double = 0.0,
    val currency: String = "KES",
    
    // Dates
    val startDate: String = "",
    val endDate: String? = null,
    val noticePeriodDays: Int = 30,
    val durationMonths: Int = 12,
    
    // Content
    val sections: List<AgreementSection> = emptyList(),
    val fullContent: String = "",
    
    // Signatures
    val tenantSignature: DigitalSignature? = null,
    val landlordSignature: DigitalSignature? = null,
    
    // Metadata
    val createdAt: String = "",
    val updatedAt: String = "",
    val auditLog: List<AgreementAudit> = emptyList()
) {
    val isFullySigned: Boolean
        get() = tenantSignature != null && landlordSignature != null

    val isDraft: Boolean
        get() = status == AgreementStatus.DRAFT

    val isActive: Boolean
        get() = status == AgreementStatus.ACTIVE
}
