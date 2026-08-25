package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * REVIEW REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Property reviews
 * - Landlord reviews
 * - Tenant reviews
 * - Broker reviews
 * - Technician reviews
 * - Service-provider reviews
 * - Ratings
 * - Verified reviews
 * - Review replies
 * - Review reports
 * - Review moderation
 * - Reputation scores
 * - Review statistics
 *
 * =============================================================
 */

interface ReviewRepository {

    /*
     * ---------------------------------------------------------
     * CREATE REVIEW
     * ---------------------------------------------------------
     */

    suspend fun createReview(
        userId: String,
        review: CreateReviewData
    ): Result<String>

    suspend fun updateReview(
        userId: String,
        reviewId: String,
        update: UpdateReviewData
    ): Result<Unit>

    suspend fun deleteReview(
        userId: String,
        reviewId: String
    ): Result<Unit>

    suspend fun getReview(
        reviewId: String
    ): Result<ReviewData>


    /*
     * ---------------------------------------------------------
     * REVIEWS BY TARGET
     * ---------------------------------------------------------
     */

    suspend fun getPropertyReviews(
        propertyId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ReviewData>>

    fun observePropertyReviews(
        propertyId: String
    ): Flow<Result<List<ReviewData>>>

    suspend fun getLandlordReviews(
        landlordId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ReviewData>>

    suspend fun getTenantReviews(
        tenantId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ReviewData>>

    suspend fun getBrokerReviews(
        brokerId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ReviewData>>

    suspend fun getProfessionalReviews(
        professionalId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ReviewData>>


    /*
     * ---------------------------------------------------------
     * USER'S REVIEWS
     * ---------------------------------------------------------
     */

    suspend fun getReviewsByUser(
        userId: String
    ): Result<List<ReviewData>>

    suspend fun getUserReviewForTarget(
        userId: String,
        targetId: String,
        targetType: ReviewTargetType
    ): Result<ReviewData?>

    suspend fun canReview(
        userId: String,
        targetId: String,
        targetType: ReviewTargetType
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * RATINGS
     * ---------------------------------------------------------
     */

    suspend fun getRatingSummary(
        targetId: String,
        targetType: ReviewTargetType
    ): Result<RatingSummaryData>

    suspend fun getRatingDistribution(
        targetId: String,
        targetType: ReviewTargetType
    ): Result<RatingDistributionData>


    /*
     * ---------------------------------------------------------
     * REVIEW REPLIES
     * ---------------------------------------------------------
     */

    suspend fun replyToReview(
        userId: String,
        reviewId: String,
        reply: String
    ): Result<String>

    suspend fun updateReviewReply(
        userId: String,
        replyId: String,
        reply: String
    ): Result<Unit>

    suspend fun deleteReviewReply(
        userId: String,
        replyId: String
    ): Result<Unit>

    suspend fun getReviewReplies(
        reviewId: String
    ): Result<List<ReviewReplyData>>


    /*
     * ---------------------------------------------------------
     * REVIEW HELPFULNESS
     * ---------------------------------------------------------
     */

    suspend fun markHelpful(
        userId: String,
        reviewId: String
    ): Result<Unit>

    suspend fun removeHelpful(
        userId: String,
        reviewId: String
    ): Result<Unit>

    suspend fun getHelpfulCount(
        reviewId: String
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * REVIEW REPORTING
     * ---------------------------------------------------------
     */

    suspend fun reportReview(
        userId: String,
        reviewId: String,
        report: CreateReviewReportData
    ): Result<String>

    suspend fun getReviewReports(
        reviewId: String
    ): Result<List<ReviewReportData>>


    /*
     * ---------------------------------------------------------
     * MODERATION
     * ---------------------------------------------------------
     */

    suspend fun getPendingReviews(
        moderatorId: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Result<List<ReviewData>>

    suspend fun approveReview(
        moderatorId: String,
        reviewId: String
    ): Result<Unit>

    suspend fun rejectReview(
        moderatorId: String,
        reviewId: String,
        reason: String
    ): Result<Unit>

    suspend fun hideReview(
        moderatorId: String,
        reviewId: String,
        reason: String
    ): Result<Unit>

    suspend fun restoreReview(
        moderatorId: String,
        reviewId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REPUTATION
     * ---------------------------------------------------------
     */

    suspend fun getReputationScore(
        userId: String
    ): Result<ReputationScoreData>

    suspend fun recalculateReputation(
        userId: String
    ): Result<ReputationScoreData>

    suspend fun getReputationHistory(
        userId: String
    ): Result<List<ReputationHistoryData>>


    /*
     * ---------------------------------------------------------
     * VERIFIED REVIEWS
     * ---------------------------------------------------------
     */

    suspend fun verifyReview(
        moderatorId: String,
        reviewId: String,
        verificationType: ReviewVerificationType
    ): Result<Unit>

    suspend fun isReviewVerified(
        reviewId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * REVIEW ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getReviewAnalytics(
        targetId: String,
        targetType: ReviewTargetType
    ): Result<ReviewAnalyticsData>

    suspend fun getPlatformReviewAnalytics(
        startDate: String,
        endDate: String
    ): Result<PlatformReviewAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateReviewData(
    val targetId: String,
    val targetType: ReviewTargetType,
    val rating: Int,
    val title: String?,
    val comment: String?,
    val categories: Map<String, Int> = emptyMap()
)

data class UpdateReviewData(
    val rating: Int?,
    val title: String?,
    val comment: String?,
    val categories: Map<String, Int>?
)

data class ReviewData(
    val id: String,
    val authorId: String,
    val authorName: String?,
    val targetId: String,
    val targetType: ReviewTargetType,
    val rating: Int,
    val title: String?,
    val comment: String?,
    val categories: Map<String, Int>,
    val verified: Boolean,
    val verificationType: ReviewVerificationType?,
    val helpfulCount: Int,
    val status: ReviewStatus,
    val createdAt: String,
    val updatedAt: String?
)

data class RatingSummaryData(
    val targetId: String,
    val targetType: ReviewTargetType,
    val averageRating: Double,
    val totalReviews: Int,
    val fiveStarPercentage: Double,
    val fourStarPercentage: Double,
    val threeStarPercentage: Double,
    val twoStarPercentage: Double,
    val oneStarPercentage: Double
)

data class RatingDistributionData(
    val oneStar: Int,
    val twoStar: Int,
    val threeStar: Int,
    val fourStar: Int,
    val fiveStar: Int
)

data class ReviewReplyData(
    val id: String,
    val reviewId: String,
    val authorId: String,
    val authorName: String?,
    val reply: String,
    val createdAt: String,
    val updatedAt: String?
)

data class CreateReviewReportData(
    val reason: ReviewReportReason,
    val description: String?
)

data class ReviewReportData(
    val id: String,
    val reviewId: String,
    val reporterId: String,
    val reason: ReviewReportReason,
    val description: String?,
    val status: ReviewReportStatus,
    val createdAt: String
)

data class ReputationScoreData(
    val userId: String,
    val overallScore: Double,
    val reviewScore: Double,
    val reliabilityScore: Double,
    val communicationScore: Double,
    val professionalismScore: Double,
    val paymentScore: Double?,
    val totalReviews: Int,
    val verifiedReviews: Int,
    val reputationLevel: ReputationLevel
)

data class ReputationHistoryData(
    val id: String,
    val userId: String,
    val previousScore: Double,
    val newScore: Double,
    val reason: String,
    val timestamp: String
)

data class ReviewAnalyticsData(
    val targetId: String,
    val targetType: ReviewTargetType,
    val totalReviews: Int,
    val averageRating: Double,
    val verifiedReviewPercentage: Double,
    val responseRate: Double,
    val averageResponseTimeHours: Double,
    val categoryScores: Map<String, Double>
)

data class PlatformReviewAnalyticsData(
    val totalReviews: Int,
    val averageRating: Double,
    val verifiedReviews: Int,
    val pendingReviews: Int,
    val reportedReviews: Int,
    val reviewsByTargetType: Map<ReviewTargetType, Int>,
    val ratingsByTargetType: Map<ReviewTargetType, Double>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ReviewTargetType {

    PROPERTY,

    LANDLORD,

    TENANT,

    BROKER,

    TECHNICIAN,

    CONTRACTOR,

    PROPERTY_MANAGER,

    CARETAKER,

    SERVICE_PROVIDER
}

enum class ReviewVerificationType {

    RENTAL_HISTORY,

    COMPLETED_TRANSACTION,

    COMPLETED_VIEWING,

    COMPLETED_MAINTENANCE,

    VERIFIED_TENANCY,

    VERIFIED_LANDLORD,

    VERIFIED_SERVICE
}

enum class ReviewStatus {

    PENDING,

    PUBLISHED,

    HIDDEN,

    REJECTED,

    DELETED
}

enum class ReviewReportReason {

    SPAM,

    ABUSE,

    HARASSMENT,

    FALSE_INFORMATION,

    CONFLICT_OF_INTEREST,

    FAKE_REVIEW,

    PERSONAL_INFORMATION,

    DISCRIMINATION,

    OTHER
}

enum class ReviewReportStatus {

    PENDING,

    INVESTIGATING,

    RESOLVED,

    DISMISSED
}

enum class ReputationLevel {

    NEW,

    BASIC,

    TRUSTED,

    HIGHLY_TRUSTED,

    EXCELLENT
}