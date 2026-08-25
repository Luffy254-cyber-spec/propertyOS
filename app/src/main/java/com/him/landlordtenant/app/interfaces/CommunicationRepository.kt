package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * COMMUNICATION REPOSITORY
 * =============================================================
 */

interface CommunicationRepository {

    suspend fun createConversation(
        createdBy: String,
        conversation: CreateConversationData
    ): Result<String>

    suspend fun getConversation(
        conversationId: String
    ): Result<ConversationData>

    fun observeConversation(
        conversationId: String
    ): Flow<Result<ConversationData>>

    suspend fun getUserConversations(
        userId: String
    ): Result<List<ConversationData>>

    fun observeUserConversations(
        userId: String
    ): Flow<Result<List<ConversationData>>>

    suspend fun archiveConversation(
        userId: String,
        conversationId: String
    ): Result<Unit>

    suspend fun unarchiveConversation(
        userId: String,
        conversationId: String
    ): Result<Unit>

    suspend fun addParticipant(
        userId: String,
        conversationId: String,
        participantId: String
    ): Result<Unit>

    suspend fun removeParticipant(
        userId: String,
        conversationId: String,
        participantId: String
    ): Result<Unit>

    suspend fun getParticipants(
        conversationId: String
    ): Result<List<ConversationParticipantData>>

    suspend fun sendMessage(
        senderId: String,
        message: SendMessageData
    ): Result<String>

    suspend fun getMessages(
        conversationId: String,
        limit: Int = 50,
        beforeMessageId: String? = null
    ): Result<List<MessageData>>

    fun observeMessages(
        conversationId: String
    ): Flow<Result<List<MessageData>>>

    suspend fun editMessage(
        userId: String,
        messageId: String,
        newContent: String
    ): Result<Unit>

    suspend fun deleteMessage(
        userId: String,
        messageId: String
    ): Result<Unit>

    suspend fun markMessageDelivered(
        userId: String,
        messageId: String
    ): Result<Unit>

    suspend fun markMessageRead(
        userId: String,
        messageId: String
    ): Result<Unit>

    suspend fun markConversationRead(
        userId: String,
        conversationId: String
    ): Result<Unit>

    suspend fun getUnreadCount(
        userId: String
    ): Result<Int>

    suspend fun addReaction(
        userId: String,
        messageId: String,
        reaction: String
    ): Result<Unit>

    suspend fun removeReaction(
        userId: String,
        messageId: String,
        reaction: String
    ): Result<Unit>

    suspend fun getReactions(
        messageId: String
    ): Result<List<MessageReactionData>>

    suspend fun attachFile(
        userId: String,
        messageId: String,
        attachment: MessageAttachmentData
    ): Result<String>

    suspend fun getAttachments(
        messageId: String
    ): Result<List<MessageAttachmentData>>

    suspend fun removeAttachment(
        userId: String,
        attachmentId: String
    ): Result<Unit>

    suspend fun createAnnouncement(
        userId: String,
        announcement: CreateAnnouncementData
    ): Result<String>

    suspend fun updateAnnouncement(
        userId: String,
        announcementId: String,
        update: UpdateAnnouncementData
    ): Result<Unit>

    suspend fun publishAnnouncement(
        userId: String,
        announcementId: String
    ): Result<Unit>

    suspend fun cancelAnnouncement(
        userId: String,
        announcementId: String
    ): Result<Unit>

    suspend fun getPropertyAnnouncements(
        propertyId: String
    ): Result<List<AnnouncementData>>

    fun observePropertyAnnouncements(
        propertyId: String
    ): Flow<Result<List<AnnouncementData>>>

    suspend fun sendPropertyBroadcast(
        userId: String,
        propertyId: String,
        message: String,
        channel: CommunicationChannel
    ): Result<String>

    suspend fun sendTenantBroadcast(
        userId: String,
        tenantIds: List<String>,
        message: String,
        channel: CommunicationChannel
    ): Result<String>

    suspend fun sendStaffBroadcast(
        userId: String,
        staffIds: List<String>,
        message: String,
        channel: CommunicationChannel
    ): Result<String>

    suspend fun createTemplate(
        userId: String,
        template: CreateMessageTemplateData
    ): Result<String>

    suspend fun updateTemplate(
        userId: String,
        templateId: String,
        update: UpdateMessageTemplateData
    ): Result<Unit>

    suspend fun deleteTemplate(
        userId: String,
        templateId: String
    ): Result<Unit>

    suspend fun getTemplates(
        organizationId: String
    ): Result<List<MessageTemplateData>>

    suspend fun sendTemplateMessage(
        userId: String,
        templateId: String,
        recipientId: String,
        variables: Map<String, String>,
        channel: CommunicationChannel
    ): Result<String>

    suspend fun scheduleMessage(
        userId: String,
        message: ScheduleMessageData
    ): Result<String>

    suspend fun updateScheduledMessage(
        userId: String,
        scheduledMessageId: String,
        update: UpdateScheduledMessageData
    ): Result<Unit>

    suspend fun cancelScheduledMessage(
        userId: String,
        scheduledMessageId: String
    ): Result<Unit>

    suspend fun getScheduledMessages(
        userId: String
    ): Result<List<ScheduledMessageData>>

    suspend fun sendRentReminder(
        tenantId: String,
        billId: String
    ): Result<String>

    suspend fun sendRentOverdueMessage(
        tenantId: String,
        billId: String
    ): Result<String>

    suspend fun sendPaymentConfirmation(
        tenantId: String,
        paymentId: String
    ): Result<String>

    suspend fun sendMaintenanceUpdate(
        tenantId: String,
        maintenanceRequestId: String
    ): Result<String>

    suspend fun sendViewingConfirmation(
        tenantId: String,
        viewingId: String
    ): Result<String>

    suspend fun sendAgreementReminder(
        tenantId: String,
        agreementId: String
    ): Result<String>

    suspend fun sendMoveInInstructions(
        tenantId: String,
        unitId: String
    ): Result<String>

    suspend fun sendMoveOutInstructions(
        tenantId: String,
        unitId: String
    ): Result<String>

    suspend fun getCommunicationPreferences(
        userId: String
    ): Result<CommunicationPreferencesData>

    suspend fun updateCommunicationPreferences(
        userId: String,
        preferences: CommunicationPreferencesData
    ): Result<Unit>

    suspend fun blockUser(
        userId: String,
        blockedUserId: String
    ): Result<Unit>

    suspend fun unblockUser(
        userId: String,
        blockedUserId: String
    ): Result<Unit>

    suspend fun reportMessage(
        userId: String,
        messageId: String,
        reason: String
    ): Result<String>

    suspend fun searchMessages(
        userId: String,
        query: String
    ): Result<List<MessageData>>

    suspend fun searchConversations(
        userId: String,
        query: String
    ): Result<List<ConversationData>>

    suspend fun getCommunicationHistory(
        userId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<CommunicationHistoryData>>

    suspend fun getCommunicationAnalytics(
        organizationId: String,
        startDate: String,
        endDate: String
    ): Result<CommunicationAnalyticsData>
}

enum class CommunicationChannel {
    IN_APP,
    PUSH,
    SMS,
    EMAIL
}

enum class MessageTemplateCategory {
    RENT,
    PAYMENT,
    MAINTENANCE,
    VIEWING,
    AGREEMENT,
    MOVE_IN,
    MOVE_OUT,
    ANNOUNCEMENT,
    GENERAL
}

data class CreateMessageTemplateData(
    val organizationId: String,
    val name: String,
    val description: String?,
    val category: MessageTemplateCategory,
    val content: String,
    val variables: List<String>
)

data class UpdateMessageTemplateData(
    val name: String?,
    val description: String?,
    val category: MessageTemplateCategory?,
    val content: String?,
    val variables: List<String>?
)

data class MessageTemplateData(
    val id: String,
    val organizationId: String,
    val name: String,
    val description: String?,
    val category: MessageTemplateCategory,
    val content: String,
    val variables: List<String>
)

data class ScheduleMessageData(
    val recipientId: String?,
    val conversationId: String?,
    val content: String,
    val channel: CommunicationChannel,
    val scheduledAt: String,
    val templateId: String? = null
)

data class UpdateScheduledMessageData(
    val content: String?,
    val channel: CommunicationChannel?,
    val scheduledAt: String?
)

data class ScheduledMessageData(
    val id: String,
    val recipientId: String?,
    val conversationId: String?,
    val content: String,
    val channel: CommunicationChannel,
    val scheduledAt: String,
    val status: ScheduledMessageStatus
)

enum class ScheduledMessageStatus {
    SCHEDULED,
    PROCESSING,
    SENT,
    FAILED,
    CANCELLED
}

data class CommunicationPreferencesData(
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = true,
    val smsEnabled: Boolean = true,
    val rentReminders: Boolean = true,
    val maintenanceUpdates: Boolean = true,
    val announcements: Boolean = true,
    val marketingMessages: Boolean = false
)

data class CommunicationHistoryData(
    val id: String,
    val userId: String,
    val recipientId: String?,
    val conversationId: String?,
    val messageType: MessageType,
    val channel: CommunicationChannel,
    val status: MessageStatus,
    val timestamp: String
)

data class CommunicationAnalyticsData(
    val totalMessages: Int,
    val directMessages: Int,
    val announcements: Int,
    val automatedMessages: Int,
    val unreadMessages: Int,
    val averageResponseTimeMinutes: Double,
    val deliveryRate: Double,
    val readRate: Double
)
