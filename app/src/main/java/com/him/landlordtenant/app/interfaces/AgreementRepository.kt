package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * AGREEMENT REPOSITORY
 * =============================================================
 *
 * Handles the complete digital tenancy agreement lifecycle.
 *
 * Supports:
 * - Agreement templates
 * - Draft agreements
 * - Tenant invitations
 * - Landlord signatures
 * - Tenant signatures
 * - Agreement activation
 * - Renewals
 * - Expiry tracking
 * - Termination
 * - Agreement documents
 * - Agreement history
 * - Rules and clauses
 * - Digital audit trail
 *
 * =============================================================
 */

interface AgreementRepository {

    /*
     * ---------------------------------------------------------
     * AGREEMENT CREATION
     * ---------------------------------------------------------
     */

    /**
     * Create a new agreement.
     */
    suspend fun createAgreement(
        createdBy: String,
        agreement: AgreementCreateData
    ): Result<String>

    /**
     * Get an agreement by ID.
     */
    suspend fun getAgreement(
        agreementId: String
    ): Result<AgreementDetailsData>

    /**
     * Observe agreement changes in real time.
     */
    fun observeAgreement(
        agreementId: String
    ): Flow<Result<AgreementDetailsData>>

    /**
     * Update an agreement draft.
     *
     * Only editable agreements should be accepted by the
     * implementation.
     */
    suspend fun updateAgreement(
        userId: String,
        agreementId: String,
        agreement: AgreementCreateData
    ): Result<Unit>

    /**
     * Delete an agreement draft.
     */
    suspend fun deleteDraft(
        userId: String,
        agreementId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AGREEMENT LISTS
     * ---------------------------------------------------------
     */

    /**
     * Get agreements belonging to a user.
     */
    suspend fun getUserAgreements(
        userId: String
    ): Result<List<ContractAgreementSummaryData>>

    /**
     * Observe agreements belonging to a user.
     */
    fun observeUserAgreements(
        userId: String
    ): Flow<Result<List<AgreementSummaryData>>>

    /**
     * Get agreements for a property.
     */
    suspend fun getPropertyAgreements(
        propertyId: String
    ): Result<List<AgreementSummaryData>>

    /**
     * Get agreements for a particular tenant.
     */
    suspend fun getTenantAgreements(
        tenantId: String
    ): Result<List<AgreementSummaryData>>

    /**
     * Get agreements belonging to a landlord.
     */
    suspend fun getLandlordAgreements(
        landlordId: String
    ): Result<List<AgreementSummaryData>>


    /*
     * ---------------------------------------------------------
     * TEMPLATES
     * ---------------------------------------------------------
     */

    /**
     * Get available agreement templates.
     */
    suspend fun getTemplates(): Result<List<AgreementTemplateData>>

    /**
     * Get a specific template.
     */
    suspend fun getTemplate(
        templateId: String
    ): Result<AgreementTemplateData>

    /**
     * Create an agreement from a template.
     */
    suspend fun createFromTemplate(
        createdBy: String,
        templateId: String,
        agreement: AgreementCreateData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * CLAUSES
     * ---------------------------------------------------------
     */

    /**
     * Get clauses attached to an agreement.
     */
    suspend fun getClauses(
        agreementId: String
    ): Result<List<AgreementClauseData>>

    /**
     * Add a custom clause.
     */
    suspend fun addClause(
        userId: String,
        agreementId: String,
        clause: AgreementClauseData
    ): Result<String>

    /**
     * Update a clause.
     */
    suspend fun updateClause(
        userId: String,
        agreementId: String,
        clauseId: String,
        clause: AgreementClauseData
    ): Result<Unit>

    /**
     * Remove a clause.
     */
    suspend fun removeClause(
        userId: String,
        agreementId: String,
        clauseId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TENANT INVITATION
     * ---------------------------------------------------------
     */

    /**
     * Invite tenant to an agreement.
     */
    suspend fun inviteTenant(
        landlordId: String,
        agreementId: String,
        tenantId: String
    ): Result<Unit>

    /**
     * Accept agreement invitation.
     */
    suspend fun acceptInvitation(
        tenantId: String,
        agreementId: String
    ): Result<Unit>

    /**
     * Reject agreement invitation.
     */
    suspend fun rejectInvitation(
        tenantId: String,
        agreementId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SIGNATURES
     * ---------------------------------------------------------
     */

    /**
     * Sign agreement as tenant.
     */
    suspend fun signAsTenant(
        tenantId: String,
        agreementId: String,
        signature: ContractDigitalSignatureData
    ): Result<Unit>

    /**
     * Sign agreement as landlord.
     */
    suspend fun signAsLandlord(
        landlordId: String,
        agreementId: String,
        signature: ContractDigitalSignatureData
    ): Result<Unit>

    /**
     * Check signature status.
     */
    suspend fun getSignatureStatus(
        agreementId: String
    ): Result<AgreementSignatureStatusData>

    /**
     * Withdraw a pending signature.
     */
    suspend fun withdrawSignatureRequest(
        userId: String,
        agreementId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPROVAL / ACTIVATION
     * ---------------------------------------------------------
     */

    /**
     * Approve a fully signed agreement.
     */
    suspend fun approveAgreement(
        landlordId: String,
        agreementId: String
    ): Result<Unit>

    /**
     * Activate the agreement.
     */
    suspend fun activateAgreement(
        agreementId: String
    ): Result<Unit>

    /**
     * Check whether agreement can become active.
     */
    suspend fun canActivateAgreement(
        agreementId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * DOCUMENT GENERATION
     * ---------------------------------------------------------
     */

    /**
     * Generate a PDF agreement.
     */
    suspend fun generatePdf(
        agreementId: String
    ): Result<String>

    /**
     * Get the generated document.
     */
    suspend fun getAgreementDocument(
        agreementId: String
    ): Result<AgreementDocumentData?>

    /**
     * Regenerate the agreement document.
     */
    suspend fun regenerateDocument(
        agreementId: String
    ): Result<String>

    /**
     * Download/share URL for the agreement.
     */
    suspend fun getDocumentUrl(
        agreementId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * RENEWAL
     * ---------------------------------------------------------
     */

    /**
     * Request agreement renewal.
     */
    suspend fun requestRenewal(
        requesterId: String,
        agreementId: String
    ): Result<String>

    /**
     * Approve renewal.
     */
    suspend fun approveRenewal(
        landlordId: String,
        agreementId: String
    ): Result<Unit>

    /**
     * Reject renewal.
     */
    suspend fun rejectRenewal(
        landlordId: String,
        agreementId: String,
        reason: String?
    ): Result<Unit>

    /**
     * Create a new agreement from an existing one.
     */
    suspend fun renewAgreement(
        userId: String,
        agreementId: String,
        renewal: AgreementRenewalData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * EXPIRY
     * ---------------------------------------------------------
     */

    /**
     * Get agreements approaching expiry.
     */
    suspend fun getExpiringAgreements(
        userId: String,
        days: Int = 30
    ): Result<List<AgreementSummaryData>>

    /**
     * Get expired agreements.
     */
    suspend fun getExpiredAgreements(
        userId: String
    ): Result<List<AgreementSummaryData>>

    /**
     * Mark expired agreements.
     *
     * Usually called by a scheduled backend worker.
     */
    suspend fun processExpiredAgreements(): Result<Int>


    /*
     * ---------------------------------------------------------
     * TERMINATION
     * ---------------------------------------------------------
     */

    /**
     * Request agreement termination.
     */
    suspend fun requestTermination(
        requesterId: String,
        agreementId: String,
        request: AgreementTerminationData
    ): Result<Unit>

    /**
     * Approve termination.
     */
    suspend fun approveTermination(
        landlordId: String,
        agreementId: String
    ): Result<Unit>

    /**
     * Reject termination request.
     */
    suspend fun rejectTermination(
        landlordId: String,
        agreementId: String,
        reason: String?
    ): Result<Unit>

    /**
     * Immediately terminate an agreement where permitted.
     */
    suspend fun terminateAgreement(
        userId: String,
        agreementId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AUDIT TRAIL
     * ---------------------------------------------------------
     */

    /**
     * Get the complete agreement audit history.
     */
    suspend fun getAuditTrail(
        agreementId: String
    ): Result<List<AgreementAuditData>>

    /**
     * Observe agreement audit events.
     */
    fun observeAuditTrail(
        agreementId: String
    ): Flow<Result<List<AgreementAuditData>>>


    /*
     * ---------------------------------------------------------
     * AUTOMATION
     * ---------------------------------------------------------
     */

    /**
     * Find agreements requiring attention.
     */
    suspend fun getAgreementsRequiringAction(
        userId: String
    ): Result<List<AgreementSummaryData>>

    /**
     * Send expiry reminders.
     */
    suspend fun sendExpiryReminders(
        daysBeforeExpiry: Int
    ): Result<Int>

    /**
     * Send pending-signature reminders.
     */
    suspend fun sendSignatureReminders(): Result<Int>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class AgreementCreateData(
    val landlordId: String,
    val tenantId: String,
    val propertyId: String,
    val unitId: String,

    val startDate: String,
    val endDate: String?,

    val monthlyRent: Double,
    val securityDeposit: Double,

    val paymentDueDay: Int,

    val noticePeriodDays: Int,

    val utilitiesIncluded: List<String> = emptyList(),

    val rules: List<String> = emptyList(),
    val customClauses: List<AgreementClauseData> = emptyList(),
    val agreementProofUrl: String? = null
)

data class ContractAgreementSummaryData(
    val id: String,
    val propertyId: String,
    val propertyName: String,
    val unitId: String,
    val unitName: String,
    val tenantId: String?,
    val tenantName: String?,
    val landlordId: String,
    val landlordName: String,
    val startDate: String,
    val endDate: String?,
    val monthlyRent: Double,
    val status: String
)

data class AgreementDetailsData(
    val id: String,
    val landlordId: String,
    val landlordName: String,
    val tenantId: String,
    val tenantName: String,
    val propertyId: String,
    val propertyName: String,
    val unitId: String,
    val unitName: String,

    val startDate: String,
    val endDate: String?,

    val monthlyRent: Double,
    val securityDeposit: Double,
    val paymentDueDay: Int,
    val noticePeriodDays: Int,

    val utilitiesIncluded: List<String>,
    val rules: List<String>,
    val clauses: List<AgreementClauseData>,

    val status: String,

    val tenantSigned: Boolean,
    val landlordSigned: Boolean,

    val createdAt: String,
    val updatedAt: String
)

data class AgreementTemplateData(
    val id: String,
    val name: String,
    val description: String?,
    val version: String,
    val clauses: List<AgreementClauseData>,
    val defaultNoticePeriodDays: Int,
    val active: Boolean
)

data class AgreementClauseData(
    val id: String = "",
    val title: String,
    val content: String,
    val required: Boolean = false,
    val order: Int = 0
)

data class ContractDigitalSignatureData(
    val signerId: String,
    val signerName: String,
    val signatureImageUrl: String?,
    val signedAt: String,
    val ipAddress: String?,
    val deviceInfo: String?,
    val signatureMethod: SignatureMethod
)

enum class SignatureMethod {
    DRAWN,
    TYPED,
    UPLOADED,
    VERIFIED_ELECTRONIC_SIGNATURE
}

data class AgreementSignatureStatusData(
    val tenantSigned: Boolean,
    val landlordSigned: Boolean,
    val tenantSignedAt: String?,
    val landlordSignedAt: String?,
    val fullySigned: Boolean
)

data class AgreementDocumentData(
    val id: String,
    val agreementId: String,
    val fileName: String,
    val url: String,
    val version: Int,
    val generatedAt: String
)

data class AgreementRenewalData(
    val newStartDate: String,
    val newEndDate: String?,
    val newMonthlyRent: Double,
    val newSecurityDeposit: Double?,
    val newNoticePeriodDays: Int?,
    val reason: String?
)

data class AgreementTerminationData(
    val reason: String,
    val requestedEndDate: String,
    val notes: String?
)

data class AgreementAuditData(
    val id: String,
    val agreementId: String,
    val actorId: String,
    val actorName: String,
    val action: String,
    val description: String,
    val timestamp: String
)