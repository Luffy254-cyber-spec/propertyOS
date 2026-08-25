package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * MESSAGE REPOSITORY
 * =============================================================
 */

interface MessageRepository {

    suspend fun createConversation(createdBy: String, conversation: CreateConversationData): Result<String>

    suspend fun getConversation(conversationId: String): Result<ConversationDetailsData>

    fun observeConversation(conversationId: String): Flow<Result<ConversationDetailsData>>

    suspend fun getUserConversations(userId: String): Result<List<ConversationSummaryData>>

    fun observeUserConversations(userId: String): Flow<Result<List<ConversationSummaryData>>>

    suspend fun getPropertyConversations(propertyId: String): Result<List<ConversationSummaryData>>

    suspend fun sendMessage(senderId: String, conversationId: String, message: SendMessageData): Result<String>

    suspend fun getMessages(conversationId: String, page: Int = 1, pageSize: Int = 50): Result<List<MessageData>>

    fun observeMessages(conversationId: String): Flow<Result<List<MessageData>>>

    suspend fun markMessageAsRead(userId: String, messageId: String): Result<Unit>
    suspend fun markConversationAsRead(userId: String, conversationId: String): Result<Unit>

    suspend fun getUnreadMessageCount(userId: String): Result<Int>

    suspend fun createPropertyAnnouncement(userId: String, propertyId: String, announcement: PropertyAnnouncementData): Result<String>

    suspend fun getPropertyAnnouncements(propertyId: String): Result<List<PropertyAnnouncementData>>
}

data class ConversationDetailsData(
    val id: String,
    val type: ConversationType,
    val title: String?,
    val propertyId: String?,
    val participants: List<ConversationParticipantData>,
    val lastMessage: MessageData?,
    val unreadCount: Int,
    val createdAt: String
)
