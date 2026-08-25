package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * COMPLIANCE REPOSITORY
 * =============================================================
 *
 * Handles compliance requirements for:
 *
 * - Properties
 * - Units
 * - Landlords
 * - Tenants
 * - Staff
 * - Brokers
 * - Technicians
 * - Contractors
 * - Organizations
 *
 * Includes:
 *
 * - Licenses
 * - Certifications
 * - Insurance
 * - Identification
 * - Inspections
 * - Safety requirements
 * - Expiry tracking
 * - Compliance scoring
 * - Compliance checks
 * - Required documents
 * - Verification
 * - Alerts
 * - Escalations
 * - Audit history
 *
 * =============================================================
 */

interface ComplianceRepository {

    /*
     * ---------------------------------------------------------
     * REQUIREMENTS
     * ---------------------------------------------------------
     */

    suspend fun createRequirement(
        actorId: String,
        requirement: CreateComplianceRequirementData
    ): Result<String>

    suspend fun getRequirement(
        requirementId: String
    ): Result<ComplianceRequirementData>

    suspend fun getRequirements(
        scope: ComplianceScope
    ): Result<List<ComplianceRequirementData>>

    fun observeRequirements(
        scope: ComplianceScope
    ): Flow<Result<List<ComplianceRequirementData>>>

    suspend fun updateRequirement(
        actorId: String,
        requirementId: String,
        update: UpdateComplianceRequirementData
    ): Result<Unit>

    suspend fun deleteRequirement(
        actorId: String,
        requirementId: String
    ): Result<Unit>

    suspend fun enableRequirement(
        actorId: String,
        requirementId: String
    ): Result<Unit>

    suspend fun disableRequirement(
        actorId: String,
        requirementId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * COMPLIANCE RECORDS
     * ---------------------------------------------------------
     */

    suspend fun createRecord(
        actorId: String,
        record: CreateComplianceRecordData
    ): Result<String>

    suspend fun getRecord(
        recordId: String
    ): Result<ComplianceRecordData>

    suspend fun getRecords(
        scope: ComplianceScope
    ): Result<List<ComplianceRecordData>>

    fun observeRecords(
        scope: ComplianceScope
    ): Flow<Result<List<ComplianceRecordData>>>

    suspend fun updateRecord(
        actorId: String,
        recordId: String,
        update: UpdateComplianceRecordData
    ): Result<Unit>

    suspend fun deleteRecord(
        actorId: String,
        recordId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DOCUMENTS
     * ---------------------------------------------------------
     */

    suspend fun attachDocument(
        actorId: String,
        recordId: String,
        documentId: String
    ): Result<Unit>

    suspend fun detachDocument(
        actorId: String,
        recordId: String,
        documentId: String
    ): Result<Unit>

    suspend fun getRequiredDocuments(
        requirementId: String
    ): Result<List<ComplianceDocumentRequirementData>>

    suspend fun getMissingDocuments(
        scope: ComplianceScope
    ): Result<List<MissingComplianceDocumentData>>


    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitForVerification(
        actorId: String,
        recordId: String
    ): Result<Unit>

    suspend fun verifyRecord(
        verifierId: String,
        recordId: String,
        verification: ComplianceVerificationData
    ): Result<Unit>

    suspend fun rejectRecord(
        verifierId: String,
        recordId: String,
        reason: String
    ): Result<Unit>

    suspend fun requestCorrection(
        verifierId: String,
        recordId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EXPIRY
     * ---------------------------------------------------------
     */

    suspend fun getExpiringRecords(
        scope: ComplianceScope,
        withinDays: Int = 30
    ): Result<List<ComplianceRecordData>>

    suspend fun getExpiredRecords(
        scope: ComplianceScope
    ): Result<List<ComplianceRecordData>>

    suspend fun getExpirySummary(
        scope: ComplianceScope
    ): Result<ComplianceExpirySummaryData>


    /*
     * ---------------------------------------------------------
     * COMPLIANCE CHECK
     * ---------------------------------------------------------
     */

    suspend fun runComplianceCheck(
        actorId: String,
        scope: ComplianceScope
    ): Result<ComplianceCheckResultData>

    suspend fun checkEntity(
        actorId: String,
        entityType: ComplianceEntityType,
        entityId: String
    ): Result<ComplianceCheckResultData>

    suspend fun getLatestComplianceCheck(
        scope: ComplianceScope
    ): Result<ComplianceCheckResultData>


    /*
     * ---------------------------------------------------------
     * COMPLIANCE SCORE
     * ---------------------------------------------------------
     */

    suspend fun calculateComplianceScore(
        scope: ComplianceScope
    ): Result<ComplianceScoreData>

    suspend fun getComplianceScoreTrend(
        scope: ComplianceScope,
        months: Int = 12
    ): Result<List<ComplianceScoreDataPoint>>


    /*
     * ---------------------------------------------------------
     * INSPECTIONS
     * ---------------------------------------------------------
     */

    suspend fun createInspection(
        actorId: String,
        inspection: CreateComplianceInspectionData
    ): Result<String>

    suspend fun getInspection(
        inspectionId: String
    ): Result<ComplianceInspectionData>

    suspend fun getInspections(
        scope: ComplianceScope
    ): Result<List<ComplianceInspectionData>>

    suspend fun scheduleInspection(
        actorId: String,
        inspectionId: String,
        scheduledAt: String
    ): Result<Unit>

    suspend fun completeInspection(
        actorId: String,
        inspectionId: String,
        result: InspectionResultData
    ): Result<Unit>

    suspend fun cancelInspection(
        actorId: String,
        inspectionId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DEFICIENCIES
     * ---------------------------------------------------------
     */

    suspend fun createDeficiency(
        actorId: String,
        deficiency: CreateComplianceDeficiencyData
    ): Result<String>

    suspend fun getDeficiencies(
        scope: ComplianceScope
    ): Result<List<ComplianceDeficiencyData>>

    suspend fun resolveDeficiency(
        actorId: String,
        deficiencyId: String,
        resolution: String
    ): Result<Unit>

    suspend fun escalateDeficiency(
        actorId: String,
        deficiencyId: String,
        escalationLevel: Int
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * INSURANCE
     * ---------------------------------------------------------
     */

    suspend fun createInsuranceRecord(
        actorId: String,
        insurance: CreateInsuranceComplianceData
    ): Result<String>

    suspend fun getInsuranceRecords(
        scope: ComplianceScope
    ): Result<List<InsuranceComplianceData>>

    suspend fun getExpiringInsurance(
        scope: ComplianceScope,
        withinDays: Int = 60
    ): Result<List<InsuranceComplianceData>>


    /*
     * ---------------------------------------------------------
     * LICENSES
     * ---------------------------------------------------------
     */

    suspend fun createLicenseRecord(
        actorId: String,
        license: CreateLicenseComplianceData
    ): Result<String>

    suspend fun getLicenseRecords(
        scope: ComplianceScope
    ): Result<List<LicenseComplianceData>>

    suspend fun getExpiringLicenses(
        scope: ComplianceScope,
        withinDays: Int = 60
    ): Result<List<LicenseComplianceData>>


    /*
     * ---------------------------------------------------------
     * CERTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun createCertification(
        actorId: String,
        certification: CreateCertificationData
    ): Result<String>

    suspend fun getCertifications(
        scope: ComplianceScope
    ): Result<List<CertificationData>>

    suspend fun getExpiringCertifications(
        scope: ComplianceScope,
        withinDays: Int = 60
    ): Result<List<CertificationData>>


    /*
     * ---------------------------------------------------------
     * STAFF / PROFESSIONAL COMPLIANCE
     * ---------------------------------------------------------
     */

    suspend fun getProfessionalCompliance(
        professionalId: String
    ): Result<ProfessionalComplianceData>

    suspend fun verifyProfessionalCompliance(
        verifierId: String,
        professionalId: String
    ): Result<Unit>

    suspend fun suspendProfessional(
        actorId: String,
        professionalId: String,
        reason: String
    ): Result<Unit>

    suspend fun reinstateProfessional(
        actorId: String,
        professionalId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY SAFETY
     * ---------------------------------------------------------
     */

    suspend fun getPropertySafetyStatus(
        propertyId: String
    ): Result<PropertySafetyStatusData>

    suspend fun updateSafetyStatus(
        actorId: String,
        propertyId: String,
        status: PropertySafetyStatusData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ALERTS
     * ---------------------------------------------------------
     */

    suspend fun getComplianceAlerts(
        scope: ComplianceScope
    ): Result<List<ComplianceAlertData>>

    suspend fun acknowledgeAlert(
        actorId: String,
        alertId: String
    ): Result<Unit>

    suspend fun dismissAlert(
        actorId: String,
        alertId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateComplianceReport(
        actorId: String,
        scope: ComplianceScope,
        format: ComplianceReportFormat
    ): Result<String>

    suspend fun getComplianceReports(
        scope: ComplianceScope
    ): Result<List<ComplianceReportData>>


    /*
     * ---------------------------------------------------------
     * AUDIT
     * ---------------------------------------------------------
     */

    suspend fun getComplianceHistory(
        entityType: ComplianceEntityType,
        entityId: String,
        limit: Int = 100
    ): Result<List<ComplianceAuditData>>


    /*
     * ---------------------------------------------------------
     * DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getComplianceDashboard(
        scope: ComplianceScope
    ): Result<ComplianceDashboardData>

    fun observeComplianceDashboard(
        scope: ComplianceScope
    ): Flow<Result<ComplianceDashboardData>>
}


/*
 * =============================================================
 * SCOPE
 * =============================================================
 */

data class ComplianceScope(
    val organizationId: String? = null,
    val propertyId: String? = null,
    val unitId: String? = null,
    val landlordId: String? = null,
    val tenantId: String? = null,
    val staffId: String? = null,
    val professionalId: String? = null
)


/*
 * =============================================================
 * REQUIREMENT
 * =============================================================
 */

data class CreateComplianceRequirementData(
    val name: String,
    val description: String?,
    val entityType: ComplianceEntityType,
    val category: ComplianceCategory,
    val mandatory: Boolean,
    val recurrenceMonths: Int?,
    val gracePeriodDays: Int = 0,
    val requiredDocuments: List<String> = emptyList()
)

data class UpdateComplianceRequirementData(
    val name: String?,
    val description: String?,
    val category: ComplianceCategory?,
    val mandatory: Boolean?,
    val recurrenceMonths: Int?,
    val gracePeriodDays: Int?,
    val requiredDocuments: List<String>?
)

data class ComplianceRequirementData(
    val id: String,
    val name: String,
    val description: String?,
    val entityType: ComplianceEntityType,
    val category: ComplianceCategory,
    val mandatory: Boolean,
    val recurrenceMonths: Int?,
    val gracePeriodDays: Int,
    val requiredDocuments: List<String>,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String
)


/*
 * =============================================================
 * RECORD
 * =============================================================
 */

data class CreateComplianceRecordData(
    val requirementId: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val title: String,
    val referenceNumber: String?,
    val issuedBy: String?,
    val issuedAt: String?,
    val expiresAt: String?,
    val notes: String?,
    val documentIds: List<String> = emptyList()
)

data class UpdateComplianceRecordData(
    val title: String?,
    val referenceNumber: String?,
    val issuedBy: String?,
    val issuedAt: String?,
    val expiresAt: String?,
    val notes: String?,
    val documentIds: List<String>?
)

data class ComplianceRecordData(
    val id: String,
    val requirementId: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val title: String,
    val referenceNumber: String?,
    val issuedBy: String?,
    val issuedAt: String?,
    val expiresAt: String?,
    val status: ComplianceStatus,
    val verificationStatus: ComplianceVerificationStatus,
    val notes: String?,
    val documentIds: List<String>,
    val createdAt: String,
    val updatedAt: String
)


/*
 * =============================================================
 * DOCUMENTS
 * =============================================================
 */

data class ComplianceDocumentRequirementData(
    val id: String,
    val name: String,
    val description: String?,
    val required: Boolean
)

data class MissingComplianceDocumentData(
    val requirementId: String,
    val requirementName: String,
    val documentName: String,
    val entityType: ComplianceEntityType,
    val entityId: String
)


/*
 * =============================================================
 * VERIFICATION
 * =============================================================
 */

data class ComplianceVerificationData(
    val status: ComplianceVerificationStatus,
    val verifierId: String,
    val notes: String?,
    val verifiedAt: String
)


/*
 * =============================================================
 * EXPIRY
 * =============================================================
 */

data class ComplianceExpirySummaryData(
    val totalRecords: Int,
    val validRecords: Int,
    val expiringWithin7Days: Int,
    val expiringWithin30Days: Int,
    val expiringWithin60Days: Int,
    val expiredRecords: Int
)


/*
 * =============================================================
 * CHECK
 * =============================================================
 */

data class ComplianceCheckResultData(
    val id: String,
    val scope: ComplianceScope,
    val status: ComplianceOverallStatus,
    val score: Double,
    val totalRequirements: Int,
    val satisfiedRequirements: Int,
    val missingRequirements: Int,
    val expiredRequirements: Int,
    val expiringRequirements: Int,
    val failedRequirements: Int,
    val findings: List<ComplianceFindingData>,
    val checkedAt: String
)

data class ComplianceFindingData(
    val requirementId: String,
    val requirementName: String,
    val status: ComplianceStatus,
    val severity: ComplianceSeverity,
    val message: String,
    val actionRequired: String?
)


/*
 * =============================================================
 * SCORE
 * =============================================================
 */

data class ComplianceScoreData(
    val score: Double,
    val status: ComplianceOverallStatus,
    val mandatoryRequirementsMet: Int,
    val mandatoryRequirementsTotal: Int,
    val optionalRequirementsMet: Int,
    val optionalRequirementsTotal: Int,
    val calculatedAt: String
)

data class ComplianceScoreDataPoint(
    val period: String,
    val score: Double
)


/*
 * =============================================================
 * INSPECTIONS
 * =============================================================
 */

data class CreateComplianceInspectionData(
    val entityType: ComplianceEntityType,
    val entityId: String,
    val inspectionType: ComplianceInspectionType,
    val inspectorId: String?,
    val scheduledAt: String?,
    val notes: String?
)

data class ComplianceInspectionData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val inspectionType: ComplianceInspectionType,
    val inspectorId: String?,
    val scheduledAt: String?,
    val completedAt: String?,
    val status: ComplianceInspectionStatus,
    val result: InspectionResultData?,
    val notes: String?
)

data class InspectionResultData(
    val outcome: InspectionOutcome,
    val score: Double?,
    val findings: List<String>,
    val correctiveActions: List<String>,
    val nextInspectionAt: String?
)


/*
 * =============================================================
 * DEFICIENCIES
 * =============================================================
 */

data class CreateComplianceDeficiencyData(
    val entityType: ComplianceEntityType,
    val entityId: String,
    val category: ComplianceCategory,
    val title: String,
    val description: String,
    val severity: ComplianceSeverity,
    val dueDate: String?
)

data class ComplianceDeficiencyData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val category: ComplianceCategory,
    val title: String,
    val description: String,
    val severity: ComplianceSeverity,
    val status: DeficiencyStatus,
    val dueDate: String?,
    val resolvedAt: String?,
    val resolution: String?,
    val escalationLevel: Int
)


/*
 * =============================================================
 * INSURANCE
 * =============================================================
 */

data class CreateInsuranceComplianceData(
    val entityType: ComplianceEntityType,
    val entityId: String,
    val provider: String,
    val policyNumber: String,
    val coverageType: String,
    val coverageAmount: Double?,
    val startDate: String,
    val expiryDate: String,
    val documentId: String?
)

data class InsuranceComplianceData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val provider: String,
    val policyNumber: String,
    val coverageType: String,
    val coverageAmount: Double?,
    val startDate: String,
    val expiryDate: String,
    val status: ComplianceStatus,
    val documentId: String?
)


/*
 * =============================================================
 * LICENSE
 * =============================================================
 */

data class CreateLicenseComplianceData(
    val entityType: ComplianceEntityType,
    val entityId: String,
    val licenseType: String,
    val licenseNumber: String,
    val issuingAuthority: String,
    val issuedAt: String,
    val expiresAt: String,
    val documentId: String?
)

data class LicenseComplianceData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val licenseType: String,
    val licenseNumber: String,
    val issuingAuthority: String,
    val issuedAt: String,
    val expiresAt: String,
    val status: ComplianceStatus,
    val documentId: String?
)


/*
 * =============================================================
 * CERTIFICATION
 * =============================================================
 */

data class CreateCertificationData(
    val entityType: ComplianceEntityType,
    val entityId: String,
    val certificationName: String,
    val certificationNumber: String?,
    val issuingOrganization: String?,
    val issuedAt: String?,
    val expiresAt: String?,
    val documentId: String?
)

data class CertificationData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val certificationName: String,
    val certificationNumber: String?,
    val issuingOrganization: String?,
    val issuedAt: String?,
    val expiresAt: String?,
    val status: ComplianceStatus,
    val documentId: String?
)


/*
 * =============================================================
 * PROFESSIONAL
 * =============================================================
 */

data class ProfessionalComplianceData(
    val professionalId: String,
    val verified: Boolean,
    val active: Boolean,
    val licensesValid: Boolean,
    val certificationsValid: Boolean,
    val insuranceValid: Boolean,
    val identityVerified: Boolean,
    val complianceScore: Double,
    val issues: List<String>
)


/*
 * =============================================================
 * PROPERTY SAFETY
 * =============================================================
 */

data class PropertySafetyStatusData(
    val propertyId: String,
    val overallStatus: SafetyStatus,
    val fireSafety: SafetyCheckStatus,
    val electricalSafety: SafetyCheckStatus,
    val structuralSafety: SafetyCheckStatus,
    val gasSafety: SafetyCheckStatus,
    val sanitation: SafetyCheckStatus,
    val emergencyExits: SafetyCheckStatus,
    val lastInspectionAt: String?,
    val nextInspectionAt: String?,
    val issues: List<String>
)


/*
 * =============================================================
 * ALERT
 * =============================================================
 */

data class ComplianceAlertData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val recordId: String?,
    val title: String,
    val message: String,
    val severity: ComplianceSeverity,
    val dueDate: String?,
    val acknowledged: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * REPORT
 * =============================================================
 */

data class ComplianceReportData(
    val id: String,
    val scope: ComplianceScope,
    val format: ComplianceReportFormat,
    val status: String,
    val generatedAt: String,
    val documentId: String?
)


/*
 * =============================================================
 * AUDIT
 * =============================================================
 */

data class ComplianceAuditData(
    val id: String,
    val entityType: ComplianceEntityType,
    val entityId: String,
    val action: String,
    val actorId: String,
    val oldStatus: String?,
    val newStatus: String?,
    val notes: String?,
    val timestamp: String
)


/*
 * =============================================================
 * DASHBOARD
 * =============================================================
 */

data class ComplianceDashboardData(
    val overallScore: Double,
    val status: ComplianceOverallStatus,
    val totalRequirements: Int,
    val compliantRequirements: Int,
    val pendingRequirements: Int,
    val expiredRequirements: Int,
    val expiringRequirements: Int,
    val openDeficiencies: Int,
    val criticalAlerts: Int,
    val upcomingInspections: Int,
    val missingDocuments: Int,
    val recentAlerts: List<ComplianceAlertData>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ComplianceEntityType {
    ORGANIZATION,
    PROPERTY,
    UNIT,
    LANDLORD,
    TENANT,
    STAFF,
    BROKER,
    TECHNICIAN,
    CONTRACTOR
}

enum class ComplianceCategory {
    IDENTITY,
    LICENSE,
    CERTIFICATION,
    INSURANCE,
    SAFETY,
    FIRE_SAFETY,
    ELECTRICAL,
    STRUCTURAL,
    SANITATION,
    TAX,
    RENTAL,
    LEASE,
    DOCUMENTATION,
    PROFESSIONAL,
    PROPERTY,
    OTHER
}

enum class ComplianceStatus {
    COMPLIANT,
    PENDING,
    EXPIRING,
    EXPIRED,
    MISSING,
    FAILED,
    SUSPENDED
}

enum class ComplianceVerificationStatus {
    NOT_VERIFIED,
    PENDING,
    VERIFIED,
    REJECTED,
    REQUIRES_CORRECTION
}

enum class ComplianceOverallStatus {
    COMPLIANT,
    PARTIALLY_COMPLIANT,
    NON_COMPLIANT,
    CRITICAL
}

enum class ComplianceSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class ComplianceInspectionType {
    ROUTINE,
    MOVE_IN,
    MOVE_OUT,
    SAFETY,
    FIRE,
    ELECTRICAL,
    STRUCTURAL,
    HEALTH,
    COMPLIANCE,
    EMERGENCY
}

enum class ComplianceInspectionStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    MISSED
}

enum class InspectionOutcome {
    PASSED,
    PASSED_WITH_CONDITIONS,
    FAILED,
    REQUIRES_FOLLOW_UP
}

enum class DeficiencyStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    ESCALATED,
    CLOSED
}

enum class SafetyStatus {
    SAFE,
    CONDITIONAL,
    UNSAFE,
    UNKNOWN
}

enum class SafetyCheckStatus {
    PASSED,
    FAILED,
    PENDING,
    NOT_APPLICABLE
}

enum class ComplianceReportFormat {
    PDF,
    CSV,
    XLSX,
    JSON
}