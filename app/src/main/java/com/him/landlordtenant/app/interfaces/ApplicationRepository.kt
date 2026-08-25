package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * APPLICATION REPOSITORY
 * =============================================================
 *
 * Handles the process of a prospective tenant applying for a
 * property/unit after discovering or viewing it.
 *
 * Supports:
 *
 * - Rental applications
 * - Applicant profiles
 * - Document submission
 * - Identity verification
 * - Employment information
 * - Income information
 * - References
 * - Previous landlord references
 * - Application screening
 * - Landlord review
 * - Approval / rejection
 * - Application withdrawal
 * - Application status tracking
 * - Application scoring
 * - Application notes
 * - Application history
 * - Conversion to tenancy
 *
 * =============================================================
 */

interface ApplicationRepository {

    /*
     * ---------------------------------------------------------
     * APPLICATION CREATION
     * ---------------------------------------------------------
     */

    suspend fun createApplication(
        applicantId: String,
        application: CreateRentalApplicationData
    ): Result<String>

    suspend fun getApplication(
        applicationId: String
    ): Result<RentalApplicationDetailsData>

    fun observeApplication(
        applicationId: String
    ): Flow<Result<RentalApplicationDetailsData>>

    suspend fun updateApplication(
        applicantId: String,
        applicationId: String,
        application: CreateRentalApplicationData
    ): Result<Unit>

    suspend fun withdrawApplication(
        applicantId: String,
        applicationId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPLICANT APPLICATIONS
     * ---------------------------------------------------------
     */

    suspend fun getApplicantApplications(
        applicantId: String
    ): Result<List<ApplicationSummaryData>>

    fun observeApplicantApplications(
        applicantId: String
    ): Flow<Result<List<ApplicationSummaryData>>>

    suspend fun getActiveApplications(
        applicantId: String
    ): Result<List<ApplicationSummaryData>>

    suspend fun getApplicationHistory(
        applicantId: String
    ): Result<List<ApplicationSummaryData>>


    /*
     * ---------------------------------------------------------
     * LANDLORD APPLICATIONS
     * ---------------------------------------------------------
     */

    suspend fun getLandlordApplications(
        landlordId: String
    ): Result<List<ApplicationSummaryData>>

    suspend fun getPendingApplications(
        landlordId: String
    ): Result<List<ApplicationSummaryData>>

    suspend fun getPropertyApplications(
        propertyId: String
    ): Result<List<ApplicationSummaryData>>

    suspend fun getUnitApplications(
        unitId: String
    ): Result<List<ApplicationSummaryData>>


    /*
     * ---------------------------------------------------------
     * APPLICATION STATUS
     * ---------------------------------------------------------
     */

    suspend fun updateStatus(
        reviewerId: String,
        applicationId: String,
        status: ApplicationStatus
    ): Result<Unit>

    suspend fun submitApplication(
        applicantId: String,
        applicationId: String
    ): Result<Unit>

    suspend fun approveApplication(
        landlordId: String,
        applicationId: String
    ): Result<Unit>

    suspend fun rejectApplication(
        landlordId: String,
        applicationId: String,
        reason: String
    ): Result<Unit>

    suspend fun requestMoreInformation(
        landlordId: String,
        applicationId: String,
        message: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPLICANT PROFILE
     * ---------------------------------------------------------
     */

    suspend fun getApplicantProfile(
        applicantId: String
    ): Result<ApplicantProfileData>

    suspend fun updateApplicantProfile(
        applicantId: String,
        profile: ApplicantProfileData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * IDENTITY VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitIdentityVerification(
        applicantId: String,
        verification: ApplicantIdentityVerificationData
    ): Result<String>

    suspend fun getIdentityVerification(
        applicantId: String
    ): Result<ApplicantIdentityVerificationData>

    suspend fun verifyApplicantIdentity(
        applicantId: String,
        verifiedBy: String
    ): Result<Unit>

    suspend fun rejectApplicantIdentity(
        applicantId: String,
        rejectedBy: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPLICATION DOCUMENTS
     * ---------------------------------------------------------
     */

    suspend fun uploadDocument(
        applicantId: String,
        applicationId: String,
        document: ApplicationDocumentData
    ): Result<String>

    suspend fun getDocuments(
        applicationId: String
    ): Result<List<ApplicationDocumentData>>

    suspend fun deleteDocument(
        applicantId: String,
        applicationId: String,
        documentId: String
    ): Result<Unit>

    suspend fun verifyDocument(
        documentId: String,
        verifiedBy: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EMPLOYMENT
     * ---------------------------------------------------------
     */

    suspend fun addEmployment(
        applicantId: String,
        applicationId: String,
        employment: EmploymentData
    ): Result<String>

    suspend fun getEmployment(
        applicationId: String
    ): Result<List<EmploymentData>>

    suspend fun removeEmployment(
        applicantId: String,
        employmentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * INCOME
     * ---------------------------------------------------------
     */

    suspend fun addIncomeSource(
        applicantId: String,
        applicationId: String,
        income: IncomeSourceData
    ): Result<String>

    suspend fun getIncomeSources(
        applicationId: String
    ): Result<List<IncomeSourceData>>

    suspend fun removeIncomeSource(
        applicantId: String,
        incomeId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REFERENCES
     * ---------------------------------------------------------
     */

    suspend fun addReference(
        applicantId: String,
        applicationId: String,
        reference: ApplicantReferenceData
    ): Result<String>

    suspend fun getReferences(
        applicationId: String
    ): Result<List<ApplicantReferenceData>>

    suspend fun removeReference(
        applicantId: String,
        referenceId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PREVIOUS TENANCY
     * ---------------------------------------------------------
     */

    suspend fun addPreviousTenancy(
        applicantId: String,
        applicationId: String,
        tenancy: PreviousTenancyData
    ): Result<String>

    suspend fun getPreviousTenancies(
        applicationId: String
    ): Result<List<PreviousTenancyData>>

    suspend fun removePreviousTenancy(
        applicantId: String,
        tenancyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SCREENING
     * ---------------------------------------------------------
     */

    suspend fun startScreening(
        reviewerId: String,
        applicationId: String
    ): Result<String>

    suspend fun getScreening(
        applicationId: String
    ): Result<ApplicationScreeningData>

    suspend fun updateScreening(
        reviewerId: String,
        applicationId: String,
        screening: ApplicationScreeningData
    ): Result<Unit>

    suspend fun completeScreening(
        reviewerId: String,
        applicationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPLICATION SCORE
     * ---------------------------------------------------------
     */

    suspend fun calculateApplicationScore(
        applicationId: String
    ): Result<ApplicationScoreData>

    suspend fun getApplicationScore(
        applicationId: String
    ): Result<ApplicationScoreData?>


    /*
     * ---------------------------------------------------------
     * LANDLORD NOTES
     * ---------------------------------------------------------
     */

    suspend fun addApplicationNote(
        landlordId: String,
        applicationId: String,
        note: String
    ): Result<String>

    suspend fun getApplicationNotes(
        applicationId: String
    ): Result<List<ApplicationNoteData>>

    suspend fun deleteApplicationNote(
        landlordId: String,
        noteId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * COMMUNICATION
     * ---------------------------------------------------------
     */

    suspend fun sendApplicationMessage(
        userId: String,
        applicationId: String,
        message: String
    ): Result<String>

    fun observeApplicationMessages(
        applicationId: String
    ): Flow<Result<List<ApplicationMessageData>>>


    /*
     * ---------------------------------------------------------
     * DUPLICATE / CONFLICT CHECKING
     * ---------------------------------------------------------
     */

    suspend fun checkExistingApplication(
        applicantId: String,
        propertyId: String,
        unitId: String?
    ): Result<Boolean>

    suspend fun checkUnitAvailability(
        unitId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * TENANCY CONVERSION
     * ---------------------------------------------------------
     */

    suspend fun convertToTenancy(
        landlordId: String,
        applicationId: String
    ): Result<String>

    suspend fun createAgreementFromApplication(
        landlordId: String,
        applicationId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getApplicationAnalytics(
        landlordId: String
    ): Result<ApplicationAnalyticsData>

    suspend fun getPropertyApplicationAnalytics(
        propertyId: String
    ): Result<PropertyApplicationAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateRentalApplicationData(
    val propertyId: String,
    val unitId: String?,
    val viewingId: String?,
    val intendedMoveInDate: String,
    val intendedLeaseDurationMonths: Int,
    val numberOfOccupants: Int,
    val occupantsDescription: String?,
    val pets: Boolean,
    val petsDescription: String?,
    val smoking: Boolean,
    val additionalMessage: String?
)

data class ApplicationSummaryData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val applicantId: String,
    val applicantName: String?,
    val intendedMoveInDate: String,
    val status: ApplicationStatus,
    val screeningStatus: ScreeningStatus,
    val score: Double?,
    val createdAt: String
)

data class RentalApplicationDetailsData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val viewingId: String?,
    val applicantId: String,
    val applicantName: String?,
    val applicantProfile: ApplicantProfileData?,
    val application: CreateRentalApplicationData,
    val identityVerification: ApplicantIdentityVerificationData?,
    val documents: List<ApplicationDocumentData>,
    val employment: List<EmploymentData>,
    val incomeSources: List<IncomeSourceData>,
    val references: List<ApplicantReferenceData>,
    val previousTenancies: List<PreviousTenancyData>,
    val screening: ApplicationScreeningData?,
    val score: ApplicationScoreData?,
    val status: ApplicationStatus,
    val notes: List<ApplicationNoteData>,
    val createdAt: String,
    val updatedAt: String
)

data class ApplicantProfileData(
    val applicantId: String,
    val fullName: String,
    val phoneNumber: String?,
    val email: String?,
    val profileImageUrl: String?,
    val dateOfBirth: String?,
    val nationality: String?,
    val occupation: String?,
    val maritalStatus: String?,
    val emergencyContactName: String?,
    val emergencyContactPhone: String?
)

data class ApplicantIdentityVerificationData(
    val applicantId: String,
    val documentType: ApplicantIdentityDocumentType,
    val documentNumber: String?,
    val frontImageUrl: String?,
    val backImageUrl: String?,
    val selfieUrl: String?,
    val status: VerificationStatus,
    val verifiedAt: String?
)

data class ApplicationDocumentData(
    val id: String = "",
    val type: ApplicationDocumentType,
    val name: String,
    val fileUrl: String,
    val verified: Boolean = false,
    val uploadedAt: String
)

data class EmploymentData(
    val id: String = "",
    val employerName: String,
    val jobTitle: String,
    val employmentType: EmploymentType,
    val startDate: String?,
    val monthlyIncome: Double?,
    val employerPhone: String?,
    val employerEmail: String?
)

data class IncomeSourceData(
    val id: String = "",
    val source: String,
    val description: String?,
    val monthlyAmount: Double,
    val verified: Boolean = false
)

data class ApplicantReferenceData(
    val id: String = "",
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String?,
    val notes: String?
)

data class PreviousTenancyData(
    val id: String = "",
    val landlordName: String,
    val landlordPhone: String?,
    val propertyAddress: String?,
    val startDate: String?,
    val endDate: String?,
    val monthlyRent: Double?,
    val reasonForLeaving: String?
)

data class ApplicationScreeningData(
    val applicationId: String,
    val status: ScreeningStatus,
    val identityCheck: Boolean,
    val documentsCheck: Boolean,
    val employmentCheck: Boolean,
    val incomeCheck: Boolean,
    val referenceCheck: Boolean,
    val previousTenancyCheck: Boolean,
    val notes: String?,
    val completedAt: String?
)

data class ApplicationScoreData(
    val applicationId: String,
    val overallScore: Double,
    val identityScore: Double,
    val incomeScore: Double,
    val employmentScore: Double,
    val referenceScore: Double,
    val tenancyHistoryScore: Double,
    val documentScore: Double,
    val calculatedAt: String
)

data class ApplicationNoteData(
    val id: String = "",
    val applicationId: String,
    val landlordId: String,
    val note: String,
    val createdAt: String
)

data class ApplicationMessageData(
    val id: String,
    val applicationId: String,
    val senderId: String,
    val senderName: String?,
    val message: String,
    val createdAt: String,
    val read: Boolean
)


data class PropertyApplicationAnalyticsData(
    val propertyId: String,
    val totalApplications: Int,
    val pendingApplications: Int,
    val approvedApplications: Int,
    val rejectedApplications: Int,
    val averageScore: Double,
    val tenancyConversions: Int,
    val conversionRate: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ApplicationStatus {

    DRAFT,

    SUBMITTED,

    UNDER_REVIEW,

    SCREENING,

    MORE_INFORMATION_REQUIRED,

    APPROVED,

    REJECTED,

    WITHDRAWN,

    EXPIRED,

    CONVERTED_TO_TENANCY
}

enum class ScreeningStatus {

    NOT_STARTED,

    IN_PROGRESS,

    COMPLETED,

    FAILED,

    REQUIRES_REVIEW
}

enum class ApplicantIdentityDocumentType {

    NATIONAL_ID,

    PASSPORT,

    DRIVING_LICENSE,

    OTHER
}

enum class ApplicationDocumentType {

    NATIONAL_ID,

    PASSPORT,

    PAYSLIP,

    BANK_STATEMENT,

    EMPLOYMENT_LETTER,

    TAX_DOCUMENT,

    REFERENCE_LETTER,

    PREVIOUS_RENT_RECEIPT,

    OTHER
}


