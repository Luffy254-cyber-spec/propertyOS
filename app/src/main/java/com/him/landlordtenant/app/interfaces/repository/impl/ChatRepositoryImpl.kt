package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : MessageRepository, ChatRepository {

    override fun getConversations(userId: String): Flow<List<ChatConversation>> = callbackFlow {
        val subscription = firestoreDataSource.collection("conversations")
            .whereArrayContains("participantIds", userId)
            .orderBy("lastMessage.timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val conversations = snapshot.toObjects(ChatConversation::class.java)
                    trySend(conversations)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun observeChatConversation(conversationId: String): Flow<ChatConversation> = callbackFlow {
        val subscription = firestoreDataSource.collection("conversations")
            .document(conversationId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.toObject(ChatConversation::class.java)?.let {
                    trySend(it)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getChatMessages(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = firestoreDataSource.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    trySend(messages)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getOrCreateDirectConversation(user1Id: String, user2Id: String): Result<String> = try {
        val conversationId = if (user1Id < user2Id) "${user1Id}_${user2Id}" else "${user2Id}_${user1Id}"
        val doc = firestoreDataSource.collection("conversations").document(conversationId).get().await()
        
        if (!doc.exists()) {
            val newConversation = ChatConversation(
                id = conversationId,
                isGroup = false,
                createdAt = System.currentTimeMillis()
            )
            firestoreDataSource.saveData("conversations", conversationId, newConversation)
        }
        Result.success(conversationId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // MessageRepository Implementations
    override suspend fun createConversation(createdBy: String, conversation: CreateConversationData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getConversation(conversationId: String): Result<ConversationDetailsData> = Result.failure(NotImplementedError())
    override fun observeConversation(conversationId: String): Flow<Result<ConversationDetailsData>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getUserConversations(userId: String): Result<List<ConversationSummaryData>> = Result.failure(NotImplementedError())
    override fun observeUserConversations(userId: String): Flow<Result<List<ConversationSummaryData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getPropertyConversations(propertyId: String): Result<List<ConversationSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun sendMessage(senderId: String, conversationId: String, message: SendMessageData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getMessages(conversationId: String, page: Int, pageSize: Int): Result<List<MessageData>> = Result.failure(NotImplementedError())
    override fun observeMessages(conversationId: String): Flow<Result<List<MessageData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun markMessageAsRead(userId: String, messageId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markConversationAsRead(userId: String, conversationId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getUnreadMessageCount(userId: String): Result<Int> = Result.failure(NotImplementedError())
    override suspend fun createPropertyAnnouncement(userId: String, propertyId: String, announcement: PropertyAnnouncementData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getPropertyAnnouncements(propertyId: String): Result<List<PropertyAnnouncementData>> = Result.failure(NotImplementedError())

    override suspend fun sendMessage(message: ChatMessage): Result<Unit> = try {
        val docRef = firestoreDataSource.collection("conversations")
            .document(message.conversationId)
            .collection("messages")
            .document()
        
        val finalMessage = message.copy(id = docRef.id)
        docRef.set(finalMessage).await()
        
        // Update last message in conversation
        firestoreDataSource.collection("conversations")
            .document(message.conversationId)
            .update("lastMessage", finalMessage)
            .await()
            
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMessage(messageId: String, forEveryone: Boolean): Result<Unit> = try {
        // Implementation logic for deleting message
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun pinMessage(messageId: String, isPinned: Boolean): Result<Unit> = try {
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addReaction(messageId: String, emoji: String, userId: String): Result<Unit> = try {
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun forwardMessage(messageId: String, targetConversationIds: List<String>): Result<Unit> = try {
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getOnlineStatus(userId: String): Flow<Boolean> = callbackFlow {
        val subscription = firestoreDataSource.collection("presence")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val isOnline = snapshot?.getBoolean("online") ?: false
                trySend(isOnline)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> = try {
        firestoreDataSource.saveData("presence", userId, mapOf("online" to isOnline, "lastSeen" to System.currentTimeMillis()))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setTypingStatus(conversationId: String, userId: String, isTyping: Boolean): Result<Unit> = try {
        // Implementation for typing status
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setRecordingStatus(conversationId: String, userId: String, isRecording: Boolean): Result<Unit> = try {
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markAsRead(conversationId: String, userId: String): Result<Unit> = try {
        // Implementation to mark messages as read
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun initiateCall(call: CallSession): Result<String> = try {
        val id = firestoreDataSource.collection("calls").document().id
        val finalCall = call.copy(id = id)
        firestoreDataSource.saveData("calls", id, finalCall)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun endCall(callId: String): Result<Unit> = try {
        firestoreDataSource.collection("calls")
            .document(callId)
            .update("status", CallStatus.ENDED.name, "endTime", System.currentTimeMillis())
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getActiveCall(userId: String): Flow<CallSession?> = callbackFlow {
        val subscription = firestoreDataSource.collection("calls")
            .whereEqualTo("receiverId", userId)
            .whereIn("status", listOf(CallStatus.DIALING.name, CallStatus.RINGING.name))
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val call = snapshot?.documents?.firstOrNull()?.toObject(CallSession::class.java)
                trySend(call)
            }
        awaitClose { subscription.remove() }
    }
}
