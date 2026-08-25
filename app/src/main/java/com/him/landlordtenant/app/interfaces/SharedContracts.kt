package com.him.landlordtenant.app.interfaces

// Shared Data Contracts across Repositories

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED,
    DELETED
}

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    LOCATION,
    SYSTEM,
    AUTOMATED
}

enum class ConversationType {
    DIRECT,
    PROPERTY_GROUP,
    STAFF_GROUP,
    MAINTENANCE,
    VIEWING,
    SUPPORT,
    ANNOUNCEMENT
}

enum class EmploymentType {
    FULL_TIME,
    PART_TIME,
    SELF_EMPLOYED,
    CONTRACT,
    BUSINESS_OWNER,
    UNEMPLOYED,
    STUDENT,
    RETIRED,
    OTHER
}

enum class AnnouncementPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

enum class AnnouncementStatus {
    DRAFT,
    SCHEDULED,
    PUBLISHED,
    EXPIRED,
    CANCELLED
}

enum class InspectionType {
    MOVE_IN,
    ROUTINE,
    MAINTENANCE,
    MOVE_OUT,
    DAMAGE_ASSESSMENT
}

enum class InspectionStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class PropertyCondition {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR,
    DAMAGED
}

enum class DocumentVerificationStatus {
    PENDING,
    VERIFIED,
    REJECTED,
    EXPIRED
}

enum class IdentityDocumentType {
    NATIONAL_ID,
    PASSPORT,
    DRIVING_LICENSE,
    ALIEN_ID,
    OTHER
}

data class ProfessionalSummaryData(
    val id: String,
    val displayName: String,
    val businessName: String?,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val yearsOfExperience: Int,
    val verified: Boolean,
    val identityVerified: Boolean,
    val available: Boolean,
    val distanceKm: Double?,
    val profileImageUrl: String?
)

data class MaintenanceSummaryData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val tenantId: String?,
    val tenantName: String?,
    val title: String,
    val category: String,
    val priority: String,
    val status: String,
    val assignedProfessionalId: String?,
    val createdAt: String
)

data class RentBalanceData(
    val monthlyRent: Double,
    val amountPaid: Double,
    val outstandingAmount: Double,
    val arrearsAmount: Double,
    val dueDate: String
)

data class PaymentHistoryItemData(
    val id: String,
    val amount: Double,
    val date: String,
    val reference: String?,
    val status: String
)

data class NotificationSummaryData(
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String
)

data class ConversationSummaryData(
    val id: String,
    val participantName: String,
    val lastMessage: String?,
    val lastMessageAt: String?,
    val unreadCount: Int
)

data class CreateConversationData(
    val type: ConversationType,
    val title: String?,
    val propertyId: String?,
    val participants: List<String>
)

data class ProfessionalRequestData(
    val title: String,
    val description: String,
    val preferredDate: String?,
    val location: String?
)

data class UserProfileData(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val profileImageUrl: String?,
    val roles: List<String>,
    val activeRole: String,
    val isVerified: Boolean
)

data class IdentityDocumentData(
    val type: String,
    val number: String,
    val imageUrl: String,
    val expiryDate: String?
)

data class VerificationStatusData(
    val status: String,
    val verifiedAt: String?,
    val verifiedBy: String?,
    val notes: String?
)

data class ProfessionalVerificationData(
    val professionalId: String,
    val status: String,
    val verifiedAt: String?,
    val notes: String?
)

data class MessageData(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String?,
    val content: String?,
    val type: MessageType,
    val status: MessageStatus,
    val replyToMessageId: String?,
    val createdAt: String,
    val editedAt: String?,
    val deletedAt: String?
)

data class ConversationData(
    val id: String,
    val title: String?,
    val type: ConversationType,
    val participants: List<ConversationParticipantData>,
    val lastMessage: MessageData?,
    val unreadCount: Int,
    val createdAt: String
)

data class ConversationParticipantData(
    val userId: String,
    val displayName: String,
    val role: String?,
    val joinedAt: String
)

data class UpdateAnnouncementData(
    val title: String?,
    val content: String?,
    val priority: AnnouncementPriority?,
    val scheduledAt: String?,
    val expiresAt: String?
)

data class PropertyAnnouncementData(
    val id: String = "",
    val propertyId: String,
    val title: String,
    val message: String,
    val priority: AnnouncementPriority,
    val scheduledAt: String?,
    val expiresAt: String?,
    val published: Boolean = false
)

data class CreateAnnouncementData(
    val organizationId: String,
    val propertyId: String,
    val title: String,
    val content: String,
    val priority: AnnouncementPriority,
    val scheduledAt: String? = null,
    val expiresAt: String? = null
)

data class AnnouncementData(
    val id: String,
    val propertyId: String,
    val title: String,
    val content: String,
    val priority: AnnouncementPriority,
    val status: AnnouncementStatus,
    val createdBy: String,
    val scheduledAt: String?,
    val publishedAt: String?,
    val expiresAt: String?
)

data class SendMessageData(
    val conversationId: String,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val replyToMessageId: String? = null,
    val attachmentIds: List<String> = emptyList()
)

data class MessageReactionData(
    val id: String,
    val messageId: String,
    val userId: String,
    val reaction: String
)

data class MessageAttachmentData(
    val id: String = "",
    val fileName: String,
    val fileUrl: String,
    val mimeType: String,
    val sizeBytes: Long
)

data class PropertyProfitabilityData(
    val propertyId: String,
    val grossRentalIncome: Double = 0.0,
    val otherIncome: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netIncome: Double = 0.0,
    val operatingExpenseRatio: Double = 0.0,
    val occupancyRate: Double = 0.0
)

data class StaffAnalyticsData(
    val staffId: String,
    val staffName: String? = null,
    val tasksAssigned: Int = 0,
    val tasksCompleted: Int = 0,
    val overdueTasks: Int = 0,
    val completionRate: Double = 0.0,
    val averageCompletionHours: Double = 0.0,
    val performanceScore: Double = 0.0
)

data class RecurringTaskData(
    val id: String = "",
    val title: String,
    val description: String? = null,
    val frequency: String,
    val assignedTo: String? = null,
    val priority: String,
    val nextRunAt: String? = null,
    val active: Boolean = true
)

data class MeterReadingData(
    val id: String = "",
    val type: String,
    val reading: Double,
    val date: String,
    val notes: String? = null
)

data class InspectionScheduleData(
    val id: String = "",
    val propertyId: String,
    val unitId: String?,
    val inspectionDate: String,
    val type: String
)

data class CreateInspectionData(
    val propertyId: String,
    val unitId: String?,
    val type: String,
    val inspectionDate: String
)

data class InspectionItemData(
    val id: String = "",
    val name: String,
    val category: String,
    val condition: String
)

data class InspectionReportData(
    val id: String = "",
    val propertyId: String = "",
    val unitId: String? = null,
    val inspectionDate: String = "",
    val items: List<InspectionItemData> = emptyList(),
    val overallCondition: PropertyCondition = PropertyCondition.GOOD,
    val summary: String = "",
    val damageFound: Boolean = false,
    val estimatedDamageCost: Double? = null
)

data class InspectionData(
    val id: String,
    val leaseId: String,
    val type: InspectionType,
    val scheduledDate: String,
    val completedDate: String?,
    val status: InspectionStatus,
    val items: List<InspectionItemData>,
    val report: InspectionReportData?
)


data class ApplicationAnalyticsData(
    val totalApplications: Int,
    val approvedCount: Int,
    val rejectedCount: Int,
    val conversionRate: Double
)
