package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow
import com.him.landlordtenant.app.interfaces.IdentityDocumentType
import com.him.landlordtenant.app.interfaces.DocumentVerificationStatus

/**
 * =============================================================
 * VERIFICATION REPOSITORY
 * =============================================================
 *
 * Handles identity and trust verification across the platform.
 *
 * Supports:
 *
 * - Tenant identity verification
 * - Landlord identity verification
 * - Property manager verification
 * - Broker/agent verification
 * - Caretaker verification
 * - Technician verification
 * - Professional certification verification
 * - Phone verification
 * - Email verification
 * - National ID/passport verification
 * - Property ownership verification
 * - Business verification
 * - Document verification
 * - Verification status
 * - Manual review
 * - Verification history
 * - Fraud/risk flags
 * - Audit trails
 *
 * =============================================================
 */

interface VerificationRepository {

    /*
     * ---------------------------------------------------------
     * VERIFICATION PROFILE
     * ---------------------------------------------------------
     */

    suspend fun createVerificationProfile(
        userId: String,
        profile: CreateVerificationProfileData
    ): Result<String>

    suspend fun getVerificationProfile(
        userId: String
    ): Result<VerificationProfileData>

    fun observeVerificationProfile(
        userId: String
    ): Flow<Result<VerificationProfileData>>

    suspend fun updateVerificationProfile(
        userId: String,
        update: UpdateVerificationProfileData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * IDENTITY VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitIdentityVerification(
        userId: String,
        verification: SubmitIdentityVerificationData
    ): Result<String>

    suspend fun getIdentityVerification(
        userId: String
    ): Result<IdentityVerificationData>

    suspend fun startIdentityVerification(
        userId: String
    ): Result<String>

    suspend fun completeIdentityVerification(
        userId: String,
        sessionId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ID DOCUMENTS
     * ---------------------------------------------------------
     */

    suspend fun uploadIdentityDocument(
        userId: String,
        document: IdentityDocumentData
    ): Result<String>

    suspend fun getIdentityDocuments(
        userId: String
    ): Result<List<IdentityDocumentData>>

    suspend fun deleteIdentityDocument(
        userId: String,
        documentId: String
    ): Result<Unit>

    suspend fun replaceIdentityDocument(
        userId: String,
        documentId: String,
        document: IdentityDocumentData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PHONE VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun sendPhoneVerificationCode(
        userId: String,
        phoneNumber: String
    ): Result<Unit>

    suspend fun verifyPhone(
        userId: String,
        phoneNumber: String,
        code: String
    ): Result<Unit>

    suspend fun resendPhoneVerificationCode(
        userId: String,
        phoneNumber: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EMAIL VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun sendEmailVerificationCode(
        userId: String,
        email: String
    ): Result<Unit>

    suspend fun verifyEmail(
        userId: String,
        email: String,
        code: String
    ): Result<Unit>

    suspend fun resendEmailVerificationCode(
        userId: String,
        email: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * LANDLORD VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitLandlordVerification(
        userId: String,
        verification: LandlordVerificationData
    ): Result<String>

    suspend fun getLandlordVerification(
        userId: String
    ): Result<LandlordVerificationData>

    suspend fun approveLandlordVerification(
        reviewerId: String,
        verificationId: String
    ): Result<Unit>

    suspend fun rejectLandlordVerification(
        reviewerId: String,
        verificationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY OWNERSHIP VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitPropertyOwnershipVerification(
        userId: String,
        verification: PropertyOwnershipVerificationData
    ): Result<String>

    suspend fun getPropertyOwnershipVerification(
        propertyId: String
    ): Result<PropertyOwnershipVerificationData>

    suspend fun approvePropertyOwnership(
        reviewerId: String,
        verificationId: String
    ): Result<Unit>

    suspend fun rejectPropertyOwnership(
        reviewerId: String,
        verificationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BROKER / AGENT VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitAgentVerification(
        userId: String,
        verification: AgentVerificationData
    ): Result<String>

    suspend fun getAgentVerification(
        userId: String
    ): Result<AgentVerificationData>

    suspend fun approveAgentVerification(
        reviewerId: String,
        verificationId: String
    ): Result<Unit>

    suspend fun rejectAgentVerification(
        reviewerId: String,
        verificationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROFESSIONAL VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitProfessionalVerification(
        userId: String,
        verification: ProfessionalVerificationData
    ): Result<String>

    suspend fun getProfessionalVerification(
        userId: String
    ): Result<ProfessionalVerificationData>

    suspend fun approveProfessionalVerification(
        reviewerId: String,
        verificationId: String
    ): Result<Unit>

    suspend fun rejectProfessionalVerification(
        reviewerId: String,
        verificationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROFESSIONAL CERTIFICATES
     * ---------------------------------------------------------
     */

    suspend fun uploadCertificate(
        userId: String,
        certificate: ProfessionalCertificateData
    ): Result<String>

    suspend fun getCertificates(
        userId: String
    ): Result<List<ProfessionalCertificateData>>

    suspend fun verifyCertificate(
        reviewerId: String,
        certificateId: String
    ): Result<Unit>

    suspend fun rejectCertificate(
        reviewerId: String,
        certificateId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BUSINESS VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitBusinessVerification(
        userId: String,
        verification: BusinessVerificationData
    ): Result<String>

    suspend fun getBusinessVerification(
        organizationId: String
    ): Result<BusinessVerificationData>

    suspend fun approveBusinessVerification(
        reviewerId: String,
        verificationId: String
    ): Result<Unit>

    suspend fun rejectBusinessVerification(
        reviewerId: String,
        verificationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * MANUAL REVIEW
     * ---------------------------------------------------------
     */

    suspend fun getPendingVerifications(
        reviewerId: String
    ): Result<List<VerificationReviewData>>

    suspend fun getVerificationReview(
        verificationId: String
    ): Result<VerificationReviewData>

    suspend fun assignVerificationReviewer(
        adminId: String,
        verificationId: String,
        reviewerId: String
    ): Result<Unit>

    suspend fun requestMoreInformation(
        reviewerId: String,
        verificationId: String,
        request: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RISK / FRAUD FLAGS
     * ---------------------------------------------------------
     */

    suspend fun createRiskFlag(
        userId: String,
        flag: CreateRiskFlagData
    ): Result<String>

    suspend fun getRiskFlags(
        userId: String
    ): Result<List<RiskFlagData>>

    suspend fun resolveRiskFlag(
        reviewerId: String,
        flagId: String,
        resolution: String
    ): Result<Unit>

    suspend fun escalateRiskFlag(
        reviewerId: String,
        flagId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * VERIFICATION STATUS
     * ---------------------------------------------------------
     */

    suspend fun getVerificationStatus(
        userId: String
    ): Result<VerificationStatusData>

    suspend fun isUserVerified(
        userId: String
    ): Result<Boolean>

    suspend fun isPropertyVerified(
        propertyId: String
    ): Result<Boolean>

    suspend fun isProfessionalVerified(
        userId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * VERIFICATION BADGES
     * ---------------------------------------------------------
     */

    suspend fun getVerificationBadges(
        userId: String
    ): Result<List<VerificationBadgeData>>

    suspend fun awardBadge(
        reviewerId: String,
        userId: String,
        badge: VerificationBadgeType
    ): Result<Unit>

    suspend fun revokeBadge(
        reviewerId: String,
        userId: String,
        badge: VerificationBadgeType,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * VERIFICATION HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getVerificationHistory(
        userId: String
    ): Result<List<VerificationHistoryData>>

    suspend fun getPropertyVerificationHistory(
        propertyId: String
    ): Result<List<VerificationHistoryData>>


    /*
     * ---------------------------------------------------------
     * AUDIT
     * ---------------------------------------------------------
     */

    suspend fun recordVerificationAudit(
        reviewerId: String,
        audit: VerificationAuditData
    ): Result<String>

    suspend fun getVerificationAuditLogs(
        verificationId: String
    ): Result<List<VerificationAuditData>>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateVerificationProfileData(
    val userType: VerificationUserType,
    val legalName: String,
    val country: String,
    val phoneNumber: String?,
    val email: String?
)

data class UpdateVerificationProfileData(
    val legalName: String?,
    val phoneNumber: String?,
    val email: String?
)

data class VerificationProfileData(
    val userId: String,
    val userType: VerificationUserType,
    val legalName: String,
    val country: String,
    val phoneVerified: Boolean,
    val emailVerified: Boolean,
    val identityVerified: Boolean,
    val overallStatus: VerificationStatus
)

data class SubmitIdentityVerificationData(
    val documentType: IdentityDocumentType,
    val documentNumber: String,
    val frontDocumentPath: String,
    val backDocumentPath: String?,
    val selfiePath: String?
)

data class IdentityVerificationData(
    val id: String,
    val userId: String,
    val documentType: IdentityDocumentType,
    val documentNumberMasked: String,
    val status: VerificationStatus,
    val submittedAt: String?,
    val reviewedAt: String?,
    val rejectionReason: String?
)

data class LandlordVerificationData(
    val id: String = "",
    val userId: String,
    val legalName: String,
    val ownershipDocuments: List<String>,
    val identityVerificationId: String?,
    val status: VerificationStatus,
    val rejectionReason: String? = null
)

data class PropertyOwnershipVerificationData(
    val id: String = "",
    val propertyId: String,
    val ownerUserId: String,
    val ownershipDocumentIds: List<String>,
    val status: VerificationStatus,
    val rejectionReason: String? = null
)

data class AgentVerificationData(
    val id: String = "",
    val userId: String,
    val agencyName: String?,
    val licenseNumber: String?,
    val licenseDocumentId: String?,
    val status: VerificationStatus,
    val rejectionReason: String? = null
)


data class ProfessionalCertificateData(
    val id: String = "",
    val userId: String,
    val certificateName: String,
    val issuingOrganization: String,
    val certificateNumber: String?,
    val issueDate: String?,
    val expiryDate: String?,
    val documentPath: String,
    val status: DocumentVerificationStatus = DocumentVerificationStatus.PENDING
)

data class BusinessVerificationData(
    val id: String = "",
    val organizationId: String,
    val businessName: String,
    val registrationNumber: String?,
    val registrationDocumentId: String?,
    val taxDocumentId: String?,
    val status: VerificationStatus,
    val rejectionReason: String? = null
)

data class VerificationReviewData(
    val id: String,
    val userId: String,
    val verificationType: VerificationType,
    val status: VerificationStatus,
    val submittedAt: String,
    val assignedReviewerId: String?,
    val priority: VerificationPriority,
    val rejectionReason: String?
)

data class CreateRiskFlagData(
    val userId: String,
    val type: RiskFlagType,
    val severity: RiskSeverity,
    val description: String,
    val source: RiskFlagSource
)

data class RiskFlagData(
    val id: String,
    val userId: String,
    val type: RiskFlagType,
    val severity: RiskSeverity,
    val description: String,
    val source: RiskFlagSource,
    val status: RiskFlagStatus,
    val createdAt: String,
    val resolvedAt: String?
)


data class VerificationBadgeData(
    val userId: String,
    val badge: VerificationBadgeType,
    val awardedAt: String,
    val active: Boolean
)

data class VerificationHistoryData(
    val id: String,
    val userId: String,
    val verificationType: VerificationType,
    val previousStatus: VerificationStatus?,
    val newStatus: VerificationStatus,
    val reason: String?,
    val timestamp: String
)

data class VerificationAuditData(
    val id: String = "",
    val verificationId: String,
    val reviewerId: String,
    val action: VerificationAuditAction,
    val notes: String?,
    val timestamp: String
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class VerificationUserType {

    TENANT,

    LANDLORD,

    PROPERTY_MANAGER,

    BROKER,

    CARETAKER,

    TECHNICIAN,

    CONTRACTOR,

    ORGANIZATION
}

enum class VerificationStatus {

    NOT_STARTED,

    PENDING,

    IN_REVIEW,

    VERIFIED,

    REJECTED,

    EXPIRED,

    SUSPENDED
}


enum class ProfessionalType {

    PLUMBER,

    ELECTRICIAN,

    CARPENTER,

    MASON,

    PAINTER,

    HVAC_TECHNICIAN,

    SECURITY,

    CLEANER,

    PEST_CONTROL,

    SURVEYOR,

    VALUER,

    ARCHITECT,

    ENGINEER,

    LAWYER,

    AGENT,

    OTHER
}

enum class VerificationType {

    IDENTITY,

    LANDLORD,

    PROPERTY_OWNERSHIP,

    AGENT,

    PROFESSIONAL,

    BUSINESS,

    CERTIFICATE
}

enum class VerificationPriority {

    LOW,

    NORMAL,

    HIGH,

    URGENT
}

enum class RiskFlagType {

    DUPLICATE_ID,

    INVALID_DOCUMENT,

    EXPIRED_DOCUMENT,

    SUSPICIOUS_ACCOUNT,

    MULTIPLE_ACCOUNTS,

    PROPERTY_OWNERSHIP_MISMATCH,

    FAKE_CERTIFICATE,

    SUSPICIOUS_PAYMENT,

    REPORTED_USER,

    OTHER
}

enum class RiskSeverity {

    LOW,

    MEDIUM,

    HIGH,

    CRITICAL
}

enum class RiskFlagSource {

    AUTOMATED,

    ADMIN,

    USER_REPORT,

    DOCUMENT_CHECK,

    PAYMENT_CHECK
}

enum class RiskFlagStatus {

    OPEN,

    INVESTIGATING,

    RESOLVED,

    DISMISSED
}

enum class VerificationBadgeType {

    IDENTITY_VERIFIED,

    PHONE_VERIFIED,

    EMAIL_VERIFIED,

    LANDLORD_VERIFIED,

    PROPERTY_VERIFIED,

    AGENT_VERIFIED,

    PROFESSIONAL_VERIFIED,

    BUSINESS_VERIFIED
}

enum class VerificationAuditAction {

    SUBMITTED,

    REVIEW_STARTED,

    DOCUMENT_REQUESTED,

    APPROVED,

    REJECTED,

    SUSPENDED,

    EXPIRED,

    BADGE_AWARDED,

    BADGE_REVOKED
}