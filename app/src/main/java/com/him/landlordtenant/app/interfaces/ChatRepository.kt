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
    
    // Messages
    fun getChatMessages(conversationId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    suspend fun deleteMessage(messageId: String, forEveryone: Boolean): Result<Unit>
    suspend fun pinMessage(messageId: String, isPinned: Boolean): Result<Unit>
    suspend fun addReaction(messageId: String, emoji: String, userId: String): Result<Unit>
    suspend fun forwardMessage(messageId: String, targetConversationIds: List<String>): Result<Unit>
    
    // Status & Indicators
    fun getOnlineStatus(userId: String): Flow<Boolean>
    suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit>
    suspend fun setTypingStatus(conversationId: String, userId: String, isTyping: Boolean): Result<Unit>
    suspend fun setRecordingStatus(conversationId: String, userId: String, isRecording: Boolean): Result<Unit>
    suspend fun markAsRead(conversationId: String, userId: String): Result<Unit>
    
    // Calls
    suspend fun initiateCall(call: CallSession): Result<String>
    suspend fun endCall(callId: String): Result<Unit>
    fun getActiveCall(userId: String): Flow<CallSession?>
}
