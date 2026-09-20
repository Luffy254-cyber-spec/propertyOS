package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.communication.ChatConversation
import com.him.landlordtenant.app.data.model.communication.ChatMessage
import com.him.landlordtenant.app.data.model.communication.CallSession
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    // Conversations
    fun getConversations(userId: String): Flow<List<ChatConversation>>
    fun observeChatConversation(conversationId: String): Flow<ChatConversation>
    suspend fun getOrCreateDirectConversation(user1Id: String, user2Id: String): Result<String>
    suspend fun clearChat(conversationId: String, userId: String): Result<Unit>
    suspend fun deleteChat(conversationId: String, userId: String): Result<Unit>
    suspend fun pinConversation(conversationId: String, userId: String, pinned: Boolean): Result<Unit>
    
    // Messages
    fun getChatMessages(conversationId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    suspend fun sendSystemMessage(conversationId: String, text: String): Result<Unit>
    suspend fun deleteMessage(conversationId: String, messageId: String, forEveryone: Boolean): Result<Unit>
    suspend fun deleteMessageForUser(conversationId: String, messageId: String, userId: String): Result<Unit>
    suspend fun pinMessage(conversationId: String, messageId: String, isPinned: Boolean): Result<Unit>
    suspend fun addReaction(conversationId: String, messageId: String, emoji: String, userId: String): Result<Unit>
    suspend fun editMessage(conversationId: String, messageId: String, newText: String): Result<Unit>
    suspend fun toggleStarMessage(conversationId: String, messageId: String, userId: String): Result<Unit>
    suspend fun forwardMessage(messageId: String, targetConversationIds: List<String>): Result<Unit>
    fun getSharedMedia(conversationId: String): Flow<List<ChatMessage>>
    
    // Status & Indicators
    fun getOnlineStatus(userId: String): Flow<Boolean>
    fun getLastSeen(userId: String): Flow<Long>
    fun getTotalUnreadCount(userId: String): Flow<Int>
    suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit>
    suspend fun setTypingStatus(conversationId: String, userId: String, isTyping: Boolean): Result<Unit>
    suspend fun setRecordingStatus(conversationId: String, userId: String, isRecording: Boolean): Result<Unit>
    suspend fun markAsRead(conversationId: String, userId: String): Result<Unit>
    suspend fun markAllAsRead(userId: String): Result<Unit>
    
    // Settings & Moderation
    suspend fun setNickname(conversationId: String, userId: String, nickname: String): Result<Unit>
    suspend fun setChatTheme(conversationId: String, userId: String, themeId: String): Result<Unit>
    suspend fun setChatWallpaper(conversationId: String, userId: String, wallpaperUrl: String): Result<Unit>
    suspend fun muteNotifications(conversationId: String, userId: String, muted: Boolean): Result<Unit>
    suspend fun setDisappearingMessages(conversationId: String, durationMs: Long): Result<Unit>
    suspend fun blockUser(currentUserId: String, targetUserId: String): Result<Unit>
    suspend fun unblockUser(currentUserId: String, targetUserId: String): Result<Unit>
    fun isUserBlocked(currentUserId: String, targetUserId: String): Flow<Boolean>
    suspend fun reportUser(reporterId: String, targetUserId: String, reason: String): Result<Unit>
    
    // Community & Announcements
    suspend fun getOrCreateCommunityConversation(apartmentId: String, landlordId: String, apartmentName: String): Result<String>
    suspend fun getOrCreateTenantGroup(apartmentId: String, landlordId: String, apartmentName: String): Result<String>
    suspend fun addTenantToCommunityGroup(tenantId: String, apartmentId: String): Result<Unit>
    suspend fun getAnnouncements(apartmentId: String): Flow<List<com.him.landlordtenant.app.data.model.communication.ChatMessage>>
    suspend fun postAnnouncement(landlordId: String, apartmentId: String, text: String): Result<Unit>
    
    suspend fun initiateCall(call: CallSession): Result<String>
    suspend fun endCall(callId: String): Result<Unit>
    fun getActiveCall(userId: String, groupIds: List<String> = emptyList()): Flow<CallSession?>
}
