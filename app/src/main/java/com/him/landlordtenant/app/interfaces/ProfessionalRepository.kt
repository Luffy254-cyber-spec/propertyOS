package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface ProfessionalRepository {

    suspend fun registerProfessional(
        userId: String,
        profile: CreateProfessionalData
    ): Result<String>

    suspend fun updateProfessionalProfile(
        professionalId: String,
        profile: CreateProfessionalData
    ): Result<Unit>

    suspend fun getProfessional(
        professionalId: String
    ): Result<ProfessionalDetailsData>

    fun observeProfessional(
        professionalId: String
    ): Flow<Result<ProfessionalDetailsData>>

    suspend fun deactivateProfessional(
        professionalId: String,
        reason: String?
    ): Result<Unit>

    suspend fun searchProfessionals(
        query: String,
        category: String?,
        latitude: Double?,
        longitude: Double?,
        radiusKm: Double = 25.0,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ProfessionalSummaryData>>

    suspend fun getNearbyProfessionals(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0,
        category: String? = null
    ): Result<List<ProfessionalSummaryData>>

    suspend fun getProfessionalsByCategory(
        category: String
    ): Result<List<ProfessionalSummaryData>>

    suspend fun getVerifiedProfessionals(
        category: String? = null
    ): Result<List<ProfessionalSummaryData>>

    suspend fun getTopRatedProfessionals(
        category: String? = null,
        limit: Int = 20
    ): Result<List<ProfessionalSummaryData>>

    suspend fun submitVerification(
        professionalId: String,
        verification: ProfessionalVerificationData
    ): Result<String>

    suspend fun getVerificationStatus(
        professionalId: String
    ): Result<VerificationStatusData>

    suspend fun uploadIdentityDocument(
        professionalId: String,
        document: IdentityDocumentData
    ): Result<String>

    suspend fun addCertification(
        professionalId: String,
        certification: ProfessionalCertificationData
    ): Result<String>

    suspend fun getCertifications(
        professionalId: String
    ): Result<List<ProfessionalCertificationData>>

    suspend fun getSkills(
        professionalId: String
    ): Result<List<ProfessionalSkillData>>

    suspend fun addSkill(
        professionalId: String,
        skill: ProfessionalSkillData
    ): Result<String>

    suspend fun getServiceAreas(
        professionalId: String
    ): Result<List<ServiceAreaData>>

    suspend fun addServiceArea(
        professionalId: String,
        area: ServiceAreaData
    ): Result<String>

    suspend fun getAvailability(
        professionalId: String
    ): Result<ProfessionalAvailabilityData>

    suspend fun updateAvailability(
        professionalId: String,
        availability: ProfessionalAvailabilityData
    ): Result<Unit>

    suspend fun setOnlineStatus(
        professionalId: String,
        online: Boolean
    ): Result<Unit>

    suspend fun getPortfolio(
        professionalId: String
    ): Result<List<PortfolioItemData>>

    suspend fun uploadPortfolioItem(
        professionalId: String,
        portfolio: PortfolioItemData
    ): Result<String>

    suspend fun getProfessionalJobs(
        professionalId: String
    ): Result<List<ProfessionalJobData>>

    suspend fun acceptJob(
        professionalId: String,
        jobId: String
    ): Result<Unit>

    suspend fun rejectJob(
        professionalId: String,
        jobId: String,
        reason: String?
    ): Result<Unit>

    suspend fun startJob(
        professionalId: String,
        jobId: String
    ): Result<Unit>

    suspend fun completeJob(
        professionalId: String,
        jobId: String
    ): Result<Unit>

    suspend fun rateProfessional(
        userId: String,
        professionalId: String,
        review: ProfessionalReviewData
    ): Result<String>

    suspend fun getReviews(
        professionalId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ProfessionalReviewData>>

    suspend fun saveProfessional(
        userId: String,
        professionalId: String
    ): Result<Unit>

    suspend fun removeSavedProfessional(
        userId: String,
        professionalId: String
    ): Result<Unit>

    suspend fun getSavedProfessionals(
        userId: String
    ): Result<List<ProfessionalSummaryData>>

    suspend fun sendMessage(
        userId: String,
        professionalId: String,
        message: String
    ): Result<String>

    fun observeMessages(
        userId: String,
        professionalId: String
    ): Flow<Result<List<ProfessionalMessageData>>>
}

data class CreateProfessionalData(
    val businessName: String?,
    val displayName: String,
    val phoneNumber: String,
    val email: String?,
    val description: String?,
    val category: String,
    val yearsOfExperience: Int,
    val baseLatitude: Double?,
    val baseLongitude: Double?
)

data class ProfessionalDetailsData(
    val id: String,
    val displayName: String,
    val businessName: String?,
    val phoneNumber: String?,
    val email: String?,
    val description: String?,
    val category: String,
    val yearsOfExperience: Int,
    val verified: Boolean,
    val identityVerified: Boolean,
    val verificationStatus: String,
    val skills: List<ProfessionalSkillData>,
    val certifications: List<ProfessionalCertificationData>,
    val serviceAreas: List<ServiceAreaData>,
    val availability: ProfessionalAvailabilityData?,
    val portfolio: List<PortfolioItemData>,
    val rating: ProfessionalRatingSummaryData,
    val profileImageUrl: String?
)


data class ProfessionalCertificationData(
    val id: String = "",
    val name: String,
    val issuingOrganization: String,
    val certificateNumber: String?,
    val issueDate: String?,
    val expiryDate: String?,
    val documentUrl: String?,
    val verified: Boolean = false
)

data class ProfessionalSkillData(
    val id: String = "",
    val name: String,
    val level: String
)

data class ServiceAreaData(
    val id: String = "",
    val county: String,
    val town: String?,
    val latitude: Double?,
    val longitude: Double?,
    val radiusKm: Double
)

data class ProfessionalAvailabilityData(
    val available: Boolean,
    val emergencyAvailable: Boolean,
    val workingDays: List<String>,
    val startTime: String?,
    val endTime: String?
)

data class PortfolioItemData(
    val id: String = "",
    val title: String,
    val description: String?,
    val imageUrls: List<String>,
    val videoUrl: String?,
    val completedAt: String?
)

data class ProfessionalJobData(
    val id: String,
    val requestId: String,
    val propertyId: String,
    val propertyName: String?,
    val unitName: String?,
    val category: String,
    val title: String,
    val status: String,
    val agreedAmount: Double?,
    val scheduledDate: String?,
    val createdAt: String
)

data class ProfessionalReviewData(
    val id: String = "",
    val reviewerId: String,
    val reviewerName: String?,
    val rating: Int,
    val comment: String?,
    val jobId: String?,
    val createdAt: String
)

data class ProfessionalRatingSummaryData(
    val averageRating: Double,
    val totalReviews: Int,
    val fiveStars: Int,
    val fourStars: Int,
    val threeStars: Int,
    val twoStars: Int,
    val oneStar: Int
)

data class ProfessionalMessageData(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val createdAt: String,
    val read: Boolean
)
