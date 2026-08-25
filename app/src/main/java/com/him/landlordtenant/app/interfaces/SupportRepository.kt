package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * SUPPORT REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Support tickets
 * - Tenant complaints
 * - Landlord complaints
 * - Property disputes
 * - Payment disputes
 * - Maintenance disputes
 * - Emergency requests
 * - Staff assignment
 * - Escalation
 * - SLA tracking
 * - Attachments
 * - Internal support notes
 * - Ticket messaging
 * - Resolution history
 * - Satisfaction ratings
 * - Support analytics
 *
 * =============================================================
 */

interface SupportRepository {

    /*
     * ---------------------------------------------------------
     * TICKETS
     * ---------------------------------------------------------
     */

    suspend fun createTicket(
        userId: String,
        ticket: CreateSupportTicketData
    ): Result<String>

    suspend fun getTicket(
        ticketId: String
    ): Result<SupportTicketData>

    fun observeTicket(
        ticketId: String
    ): Flow<Result<SupportTicketData>>

    suspend fun updateTicket(
        userId: String,
        ticketId: String,
        update: UpdateSupportTicketData
    ): Result<Unit>

    suspend fun closeTicket(
        userId: String,
        ticketId: String,
        resolution: String
    ): Result<Unit>

    suspend fun reopenTicket(
        userId: String,
        ticketId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * USER TICKETS
     * ---------------------------------------------------------
     */

    suspend fun getUserTickets(
        userId: String,
        status: SupportTicketStatus? = null
    ): Result<List<SupportTicketData>>

    fun observeUserTickets(
        userId: String
    ): Flow<Result<List<SupportTicketData>>>


    /*
     * ---------------------------------------------------------
     * TICKET MESSAGES
     * ---------------------------------------------------------
     */

    suspend fun sendMessage(
        userId: String,
        ticketId: String,
        message: String
    ): Result<String>

    suspend fun getMessages(
        ticketId: String
    ): Result<List<SupportMessageData>>

    fun observeMessages(
        ticketId: String
    ): Flow<Result<List<SupportMessageData>>>


    /*
     * ---------------------------------------------------------
     * ATTACHMENTS
     * ---------------------------------------------------------
     */

    suspend fun addAttachment(
        userId: String,
        ticketId: String,
        attachment: SupportAttachmentData
    ): Result<String>

    suspend fun removeAttachment(
        userId: String,
        ticketId: String,
        attachmentId: String
    ): Result<Unit>

    suspend fun getAttachments(
        ticketId: String
    ): Result<List<SupportAttachmentData>>


    /*
     * ---------------------------------------------------------
     * COMPLAINTS
     * ---------------------------------------------------------
     */

    suspend fun createComplaint(
        userId: String,
        complaint: CreateComplaintData
    ): Result<String>

    suspend fun getComplaint(
        complaintId: String
    ): Result<ComplaintData>

    suspend fun getUserComplaints(
        userId: String
    ): Result<List<ComplaintData>>

    suspend fun withdrawComplaint(
        userId: String,
        complaintId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DISPUTES
     * ---------------------------------------------------------
     */

    suspend fun createDispute(
        userId: String,
        dispute: CreateDisputeData
    ): Result<String>

    suspend fun getDispute(
        disputeId: String
    ): Result<DisputeData>

    suspend fun getUserDisputes(
        userId: String
    ): Result<List<DisputeData>>

    suspend fun submitEvidence(
        userId: String,
        disputeId: String,
        evidence: DisputeEvidenceData
    ): Result<String>

    suspend fun withdrawDispute(
        userId: String,
        disputeId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EMERGENCIES
     * ---------------------------------------------------------
     */

    suspend fun createEmergencyRequest(
        userId: String,
        request: CreateEmergencyRequestData
    ): Result<String>

    suspend fun getEmergencyRequest(
        requestId: String
    ): Result<EmergencyRequestData>

    suspend fun cancelEmergencyRequest(
        userId: String,
        requestId: String,
        reason: String?
    ): Result<Unit>

    suspend fun escalateEmergency(
        userId: String,
        requestId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STAFF MANAGEMENT
     * ---------------------------------------------------------
     */

    suspend fun getUnassignedTickets(
        staffId: String
    ): Result<List<SupportTicketData>>

    suspend fun assignTicket(
        staffId: String,
        ticketId: String,
        assigneeId: String
    ): Result<Unit>

    suspend fun unassignTicket(
        staffId: String,
        ticketId: String
    ): Result<Unit>

    suspend fun transferTicket(
        staffId: String,
        ticketId: String,
        newAssigneeId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * INTERNAL NOTES
     * ---------------------------------------------------------
     */

    suspend fun addInternalNote(
        staffId: String,
        ticketId: String,
        note: String
    ): Result<String>

    suspend fun getInternalNotes(
        staffId: String,
        ticketId: String
    ): Result<List<SupportInternalNoteData>>


    /*
     * ---------------------------------------------------------
     * ESCALATION
     * ---------------------------------------------------------
     */

    suspend fun escalateTicket(
        staffId: String,
        ticketId: String,
        escalation: CreateEscalationData
    ): Result<Unit>

    suspend fun deescalateTicket(
        staffId: String,
        ticketId: String,
        reason: String
    ): Result<Unit>

    suspend fun getEscalationHistory(
        ticketId: String
    ): Result<List<SupportEscalationData>>


    /*
     * ---------------------------------------------------------
     * SLA
     * ---------------------------------------------------------
     */

    suspend fun getTicketSla(
        ticketId: String
    ): Result<SupportSlaData>

    suspend fun getBreachedSlaTickets(
        staffId: String
    ): Result<List<SupportTicketData>>

    suspend fun getTicketsApproachingSla(
        staffId: String,
        minutesRemaining: Int = 60
    ): Result<List<SupportTicketData>>


    /*
     * ---------------------------------------------------------
     * RESOLUTION
     * ---------------------------------------------------------
     */

    suspend fun resolveTicket(
        staffId: String,
        ticketId: String,
        resolution: String
    ): Result<Unit>

    suspend fun rejectTicket(
        staffId: String,
        ticketId: String,
        reason: String
    ): Result<Unit>

    suspend fun requestMoreInformation(
        staffId: String,
        ticketId: String,
        message: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SATISFACTION
     * ---------------------------------------------------------
     */

    suspend fun rateSupport(
        userId: String,
        ticketId: String,
        rating: Int,
        comment: String?
    ): Result<Unit>

    suspend fun getSupportRating(
        ticketId: String
    ): Result<SupportRatingData>


    /*
     * ---------------------------------------------------------
     * KNOWLEDGE BASE
     * ---------------------------------------------------------
     */

    suspend fun searchHelpArticles(
        query: String
    ): Result<List<HelpArticleData>>

    suspend fun getHelpArticle(
        articleId: String
    ): Result<HelpArticleData>

    suspend fun getPopularHelpArticles(
        limit: Int = 10
    ): Result<List<HelpArticleData>>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getSupportAnalytics(
        startDate: String,
        endDate: String
    ): Result<SupportAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateSupportTicketData(
    val subject: String,
    val description: String,
    val category: SupportCategory,
    val priority: SupportPriority = SupportPriority.NORMAL,
    val propertyId: String?,
    val leaseId: String?,
    val paymentId: String?,
    val relatedUserId: String?
)

data class UpdateSupportTicketData(
    val subject: String?,
    val description: String?,
    val category: SupportCategory?,
    val priority: SupportPriority?
)

data class SupportTicketData(
    val id: String,
    val ticketNumber: String,
    val creatorId: String,
    val subject: String,
    val description: String,
    val category: SupportCategory,
    val priority: SupportPriority,
    val status: SupportTicketStatus,
    val propertyId: String?,
    val leaseId: String?,
    val paymentId: String?,
    val relatedUserId: String?,
    val assignedTo: String?,
    val escalated: Boolean,
    val slaBreached: Boolean,
    val createdAt: String,
    val updatedAt: String?,
    val resolvedAt: String?
)

data class SupportMessageData(
    val id: String,
    val ticketId: String,
    val senderId: String,
    val senderName: String?,
    val message: String,
    val internal: Boolean,
    val createdAt: String
)

data class SupportAttachmentData(
    val id: String = "",
    val fileName: String,
    val fileUrl: String,
    val mimeType: String,
    val fileSize: Long,
    val uploadedAt: String
)

data class CreateComplaintData(
    val subject: String,
    val description: String,
    val category: ComplaintCategory,
    val againstUserId: String?,
    val propertyId: String?,
    val leaseId: String?
)

data class ComplaintData(
    val id: String,
    val complainantId: String,
    val againstUserId: String?,
    val propertyId: String?,
    val leaseId: String?,
    val subject: String,
    val description: String,
    val category: ComplaintCategory,
    val status: ComplaintStatus,
    val createdAt: String,
    val resolvedAt: String?
)

data class CreateDisputeData(
    val title: String,
    val description: String,
    val type: DisputeType,
    val againstUserId: String?,
    val propertyId: String?,
    val leaseId: String?,
    val paymentId: String?
)

data class DisputeData(
    val id: String,
    val disputeNumber: String,
    val creatorId: String,
    val againstUserId: String?,
    val title: String,
    val description: String,
    val type: DisputeType,
    val propertyId: String?,
    val leaseId: String?,
    val paymentId: String?,
    val status: DisputeStatus,
    val assignedMediatorId: String?,
    val createdAt: String,
    val resolvedAt: String?
)

data class DisputeEvidenceData(
    val id: String = "",
    val disputeId: String,
    val submittedBy: String,
    val description: String?,
    val fileUrl: String?,
    val evidenceType: EvidenceType,
    val submittedAt: String
)

data class CreateEmergencyRequestData(
    val propertyId: String?,
    val location: EmergencyLocationData,
    val type: EmergencyType,
    val description: String,
    val contactPhone: String
)

data class EmergencyLocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String?
)

data class EmergencyRequestData(
    val id: String,
    val requesterId: String,
    val propertyId: String?,
    val location: EmergencyLocationData,
    val type: EmergencyType,
    val description: String,
    val contactPhone: String,
    val priority: SupportPriority,
    val status: EmergencyStatus,
    val assignedTo: String?,
    val createdAt: String,
    val resolvedAt: String?
)

data class SupportInternalNoteData(
    val id: String,
    val ticketId: String,
    val staffId: String,
    val note: String,
    val createdAt: String
)

data class CreateEscalationData(
    val level: EscalationLevel,
    val reason: String,
    val assignedTo: String?
)

data class SupportEscalationData(
    val id: String,
    val ticketId: String,
    val previousLevel: EscalationLevel?,
    val newLevel: EscalationLevel,
    val reason: String,
    val escalatedBy: String,
    val assignedTo: String?,
    val createdAt: String
)

data class SupportSlaData(
    val ticketId: String,
    val responseDeadline: String,
    val resolutionDeadline: String,
    val responseTimeMinutes: Int?,
    val resolutionTimeMinutes: Int?,
    val responseBreached: Boolean,
    val resolutionBreached: Boolean,
    val remainingMinutes: Int?
)

data class SupportRatingData(
    val ticketId: String,
    val userId: String,
    val rating: Int,
    val comment: String?,
    val createdAt: String
)

data class HelpArticleData(
    val id: String,
    val title: String,
    val summary: String?,
    val content: String,
    val category: SupportCategory?,
    val viewCount: Int,
    val helpfulCount: Int,
    val updatedAt: String
)

data class SupportAnalyticsData(
    val totalTickets: Int,
    val openTickets: Int,
    val resolvedTickets: Int,
    val escalatedTickets: Int,
    val breachedSlaTickets: Int,
    val averageResponseTimeMinutes: Double,
    val averageResolutionTimeMinutes: Double,
    val averageRating: Double,
    val ticketsByCategory: Map<SupportCategory, Int>,
    val ticketsByPriority: Map<SupportPriority, Int>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class SupportCategory {

    RENT,

    PAYMENT,

    BILL,

    MAINTENANCE,

    PROPERTY,

    AGREEMENT,

    LEASE,

    DEPOSIT,

    LANDLORD,

    TENANT,

    BROKER,

    TECHNICIAN,

    ACCOUNT,

    VERIFICATION,

    FRAUD,

    SAFETY,

    APP_PROBLEM,

    OTHER
}

enum class SupportPriority {

    LOW,

    NORMAL,

    HIGH,

    URGENT,

    CRITICAL
}

enum class SupportTicketStatus {

    OPEN,

    IN_PROGRESS,

    WAITING_FOR_USER,

    WAITING_FOR_STAFF,

    ESCALATED,

    RESOLVED,

    CLOSED,

    REOPENED
}

enum class ComplaintCategory {

    HARASSMENT,

    PROPERTY_DAMAGE,

    UNAUTHORIZED_ACCESS,

    PAYMENT,

    RENT,

    MAINTENANCE,

    NOISE,

    CLEANLINESS,

    SECURITY,

    FRAUD,

    CONTRACT,

    OTHER
}

enum class ComplaintStatus {

    SUBMITTED,

    UNDER_REVIEW,

    INVESTIGATING,

    RESOLVED,

    DISMISSED,

    WITHDRAWN
}

enum class DisputeType {

    RENT,

    DEPOSIT,

    PAYMENT,

    PROPERTY_DAMAGE,

    MAINTENANCE,

    LEASE_TERMINATION,

    CONTRACT,

    SERVICE,

    OTHER
}

enum class DisputeStatus {

    OPEN,

    UNDER_REVIEW,

    MEDIATION,

    ESCALATED,

    RESOLVED,

    CLOSED,

    WITHDRAWN
}

enum class EvidenceType {

    IMAGE,

    VIDEO,

    DOCUMENT,

    RECEIPT,

    AGREEMENT,

    MESSAGE,

    PAYMENT_RECORD,

    OTHER
}

enum class EmergencyType {

    FIRE,

    WATER_LEAK,

    ELECTRICAL,

    GAS,

    SECURITY,

    FLOODING,

    STRUCTURAL_DAMAGE,

    LOCKOUT,

    MEDICAL,

    OTHER
}

enum class EmergencyStatus {

    REPORTED,

    ACKNOWLEDGED,

    DISPATCHED,

    IN_PROGRESS,

    RESOLVED,

    CANCELLED
}

enum class EscalationLevel {

    LEVEL_1,

    LEVEL_2,

    LEVEL_3,

    MANAGEMENT,

    ADMIN
}