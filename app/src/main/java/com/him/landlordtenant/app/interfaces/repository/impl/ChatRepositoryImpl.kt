package com.him.landlordtenant.app.interfaces.repository.impl

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.enums.MessageSenderRole
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val userRepository: UserRepository,
    private val agreementRepository: AgreementRepository
) : MessageRepository, ChatRepository {

    private fun getDirectChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) "direct_${uid1}_${uid2}" else "direct_${uid2}_${uid1}"
    }

    override fun getConversations(userId: String): Flow<List<ChatConversation>> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val convRef = firebaseDataSource.getReference("conversations")
        val unreadRef = firebaseDataSource.getReference("unread_counts/$userId")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    unreadRef.get().addOnSuccessListener { unreadSnapshot ->
                        val unreadMap = unreadSnapshot.children.associate { it.key to (it.getValue(Int::class.java) ?: 0) }
                        
                        val conversations = snapshot.children.mapNotNull { it.getValue(ChatConversation::class.java) }
                            .filter { conv -> conv.participantIds.contains(userId) }
                            .filter { conv -> conv.deletedForUsers[userId] != true }
                            .map { conv -> 
                                conv.copy(unreadCount = unreadMap[conv.id] ?: 0)
                            }
                            .sortedByDescending { it.lastMessage?.timestamp ?: it.createdAt }
                        trySend(conversations)
                    }
                } catch (e: Exception) {
                    Log.e("ChatRepository", "Error parsing conversations", e)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        convRef.addValueEventListener(listener)
        awaitClose { convRef.removeEventListener(listener) }
    }

    override fun observeChatConversation(conversationId: String): Flow<ChatConversation> = callbackFlow {
        if (conversationId.isEmpty()) {
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("conversations/$conversationId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(ChatConversation::class.java)?.let { trySend(it) }
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun getChatMessages(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        if (conversationId.isEmpty()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("messages/$conversationId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val messages = snapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
                        .sortedByDescending { it.timestamp }
                    trySend(messages)
                } catch (e: Exception) {
                    Log.e("ChatRepository", "Error parsing messages", e)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun getOrCreateDirectConversation(user1Id: String, user2Id: String): Result<String> = try {
        if (user1Id.isEmpty() || user2Id.isEmpty()) throw Exception("Invalid user IDs")
        
        val conversationId = getDirectChatId(user1Id, user2Id)
        val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
        
        if (!snapshot.exists()) {
            val user1 = userRepository.getUserById(user1Id)
            val user2 = userRepository.getUserById(user2Id)
            
            val participants = listOfNotNull(
                user1?.let { u -> ChatParticipant(id = u.id, name = u.fullName, role = MessageSenderRole.entries.find { it.name == u.activeRole?.name } ?: MessageSenderRole.TENANT) },
                user2?.let { u -> ChatParticipant(id = u.id, name = u.fullName, role = MessageSenderRole.entries.find { it.name == u.activeRole?.name } ?: MessageSenderRole.TENANT) }
            )

            val newConversation = ChatConversation(
                id = conversationId,
                name = user2?.fullName ?: "User",
                isGroup = false,
                participants = participants,
                participantIds = listOf(user1Id, user2Id),
                createdAt = System.currentTimeMillis()
            )
            firebaseDataSource.writeData("conversations/$conversationId", newConversation).getOrThrow()
        }
        Result.success(conversationId)
    } catch (e: Exception) {
        Log.e("ChatRepository", "Error in getOrCreateDirectConversation", e)
        Result.failure(e)
    }

    override suspend fun sendMessage(message: ChatMessage): Result<Unit> = try {
        val convId = message.conversationId
        val msgId = firebaseDataSource.getReference("messages/$convId").push().key ?: throw Exception("Key generation failed")
        val finalMessage = message.copy(id = msgId, status = com.him.landlordtenant.app.data.model.communication.MessageStatus.SENT)
        
        val updates = mutableMapOf<String, Any?>()
        updates["messages/$convId/$msgId"] = finalMessage
        updates["conversations/$convId/lastMessage"] = finalMessage
        
        val convSnapshot = firebaseDataSource.getReference("conversations/$convId").get().await()
        val conversation = convSnapshot.getValue(ChatConversation::class.java)
        val pIds = conversation?.participantIds ?: emptyList()

        for (pid in pIds) {
            updates["conversations/$convId/deletedForUsers/$pid"] = null
            if (pid != message.senderId) {
                val currentUnread = firebaseDataSource.getReference("unread_counts/$pid/$convId").get().await().getValue(Int::class.java) ?: 0
                updates["unread_counts/$pid/$convId"] = currentUnread + 1
            }
        }

        withTimeout(15000) {
            firebaseDataSource.getReference("/").updateChildren(updates).await()
        }

        pIds.filter { it != message.senderId }.forEach { recipientId ->
            firebaseDataSource.getReference("presence/$recipientId/online").get().addOnSuccessListener { presence ->
                if (presence.getValue(Boolean::class.java) == true) {
                    val statusUpdate = mapOf(
                        "messages/$convId/$msgId/deliveredTo/$recipientId" to System.currentTimeMillis(),
                        "messages/$convId/$msgId/status" to com.him.landlordtenant.app.data.model.communication.MessageStatus.DELIVERED.name
                    )
                    firebaseDataSource.getReference("/").updateChildren(statusUpdate)
                }
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ChatRepository", "sendMessage failed", e)
        Result.failure(e)
    }

    override suspend fun clearChat(conversationId: String, userId: String): Result<Unit> = try {
        val snapshot = firebaseDataSource.getReference("messages/$conversationId").get().await()
        snapshot.children.forEach { it.ref.child("deletedForUsers").child(userId).setValue(true) }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun deleteChat(conversationId: String, userId: String): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/deletedForUsers/$userId", true).getOrThrow()
        clearChat(conversationId, userId)
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun pinConversation(conversationId: String, userId: String, pinned: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/pinnedBy/$userId", pinned).getOrThrow()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getOrCreateCommunityConversation(apartmentId: String, landlordId: String, apartmentName: String): Result<String> = try {
        val conversationId = "comm_$apartmentId"
        val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
        if (!snapshot.exists()) {
            val landlord = userRepository.getUserById(landlordId)
            val communityConversation = ChatConversation(
                id = conversationId,
                name = "$apartmentName Announcements",
                isGroup = true,
                isCommunity = true,
                participants = listOfNotNull(landlord?.let { ChatParticipant(id = it.id, name = it.fullName, role = MessageSenderRole.LANDLORD) }),
                participantIds = listOf(landlordId),
                createdAt = System.currentTimeMillis()
            )
            firebaseDataSource.writeData("conversations/$conversationId", communityConversation).getOrThrow()
            sendSystemMessage(conversationId, "📢 Announcements for $apartmentName.")
        }
        Result.success(conversationId)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getOrCreateTenantGroup(apartmentId: String, landlordId: String, apartmentName: String): Result<String> = try {
        val conversationId = "tenant_$apartmentId"
        val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
        if (!snapshot.exists()) {
            val landlord = userRepository.getUserById(landlordId)
            val tenantConversation = ChatConversation(
                id = conversationId,
                name = "$apartmentName Community",
                isGroup = true,
                isCommunity = true,
                participants = listOfNotNull(landlord?.let { ChatParticipant(id = it.id, name = it.fullName, role = MessageSenderRole.LANDLORD) }),
                participantIds = listOf(landlordId),
                createdAt = System.currentTimeMillis()
            )
            firebaseDataSource.writeData("conversations/$conversationId", tenantConversation).getOrThrow()
            sendSystemMessage(conversationId, "🤝 Welcome to $apartmentName Community!")
        }
        Result.success(conversationId)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun addTenantToCommunityGroup(tenantId: String, apartmentId: String): Result<Unit> = try {
        val tenant = userRepository.getUserById(tenantId) ?: throw Exception("Tenant not found")
        val participant = ChatParticipant(id = tenant.id, name = tenant.fullName, role = MessageSenderRole.TENANT)
        listOf("comm_$apartmentId", "tenant_$apartmentId").forEach { cid ->
            val ref = firebaseDataSource.getReference("conversations/$cid")
            val conv = ref.get().await().getValue(ChatConversation::class.java)
            if (conv != null && !conv.participantIds.contains(tenantId)) {
                ref.child("participants").setValue(conv.participants + participant)
                ref.child("participantIds").setValue(conv.participantIds + tenantId)
                if (cid.startsWith("tenant_")) sendSystemMessage(cid, "🏠 Welcome ${tenant.fullName}!")
            }
        }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun sendSystemMessage(conversationId: String, text: String): Result<Unit> = try {
        val msgId = firebaseDataSource.getReference("messages/$conversationId").push().key ?: ""
        val message = ChatMessage(id = msgId, conversationId = conversationId, senderId = "system", senderName = "propertyOS", text = text, type = com.him.landlordtenant.app.data.model.communication.MessageType.SYSTEM, timestamp = System.currentTimeMillis(), isSystemMessage = true)
        firebaseDataSource.writeData("messages/$conversationId/$msgId", message).getOrThrow()
        firebaseDataSource.writeData("conversations/$conversationId/lastMessage", message).getOrThrow()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun deleteMessage(conversationId: String, messageId: String, forEveryone: Boolean): Result<Unit> = try {
        if (forEveryone) firebaseDataSource.getReference("messages/$conversationId/$messageId").removeValue().await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun deleteMessageForUser(conversationId: String, messageId: String, userId: String): Result<Unit> = try {
        firebaseDataSource.writeData("messages/$conversationId/$messageId/deletedForUsers/$userId", true).getOrThrow()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun pinMessage(conversationId: String, messageId: String, isPinned: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("messages/$conversationId/$messageId/isPinned", isPinned).getOrThrow()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun addReaction(conversationId: String, messageId: String, emoji: String, userId: String): Result<Unit> = try {
        firebaseDataSource.writeData("messages/$conversationId/$messageId/reactions/$userId", ChatReaction(userId = userId, emoji = emoji)).getOrThrow()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun editMessage(conversationId: String, messageId: String, newText: String): Result<Unit> = try {
        firebaseDataSource.getReference("messages/$conversationId/$messageId").updateChildren(mapOf("text" to newText, "isEdited" to true, "updatedAt" to System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun toggleStarMessage(conversationId: String, messageId: String, userId: String): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("messages/$conversationId/$messageId/starredBy/$userId")
        if (ref.get().await().exists()) ref.removeValue().await() else ref.setValue(true).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override fun getOnlineStatus(userId: String): Flow<Boolean> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(false)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("presence/$userId/online")
        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) { trySend(s.getValue(Boolean::class.java) ?: false) }
            override fun onCancelled(e: DatabaseError) { close(e.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun getLastSeen(userId: String): Flow<Long> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(0L)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("presence/$userId/lastSeen")
        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) { trySend(s.getValue(Long::class.java) ?: 0L) }
            override fun onCancelled(e: DatabaseError) { close(e.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("presence/$userId")
        if (isOnline) {
            ref.child("online").onDisconnect().setValue(false)
            ref.child("lastSeen").onDisconnect().setValue(System.currentTimeMillis())
        }
        ref.updateChildren(mapOf("online" to isOnline, "lastSeen" to System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun markAsRead(conversationId: String, userId: String): Result<Unit> = try {
        firebaseDataSource.getReference("unread_counts/$userId/$conversationId").setValue(0).await()
        val snapshot = firebaseDataSource.getReference("messages/$conversationId").get().await()
        snapshot.children.forEach { child ->
            val msg = child.getValue(ChatMessage::class.java)
            if (msg != null && msg.senderId != userId && !msg.readBy.containsKey(userId)) {
                child.ref.updateChildren(mapOf("readBy/$userId" to System.currentTimeMillis(), "status" to com.him.landlordtenant.app.data.model.communication.MessageStatus.READ.name))
            }
        }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun markAllAsRead(userId: String): Result<Unit> = try {
        val snapshot = firebaseDataSource.getReference("conversations").get().await()
        snapshot.children.forEach { child ->
            val conv = child.getValue(ChatConversation::class.java)
            if (conv != null && conv.participantIds.contains(userId)) markAsRead(conv.id, userId)
        }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun initiateCall(call: CallSession): Result<String> = try {
        val id = firebaseDataSource.getReference("calls").push().key ?: ""
        val finalCall = call.copy(id = id)
        withTimeout(8000) { firebaseDataSource.writeData("calls/$id", finalCall).getOrThrow() }
        Result.success(id)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun endCall(callId: String): Result<Unit> = try {
        firebaseDataSource.getReference("calls/$callId").updateChildren(mapOf("status" to com.him.landlordtenant.app.data.model.communication.CallStatus.ENDED.name, "endTime" to System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override fun getActiveCall(userId: String, groupIds: List<String>): Flow<CallSession?> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("calls")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activeStatuses = listOf(CallStatus.DIALING.name, CallStatus.RINGING.name, CallStatus.CONNECTED.name)
                val call = snapshot.children.mapNotNull { child ->
                    try { child.getValue(CallSession::class.java) } catch (e: Exception) { null }
                }.filter { it.status.name in activeStatuses }
                .firstOrNull { it.callerId == userId || it.receiverId == userId || groupIds.contains(it.receiverId) }
                trySend(call)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        ref.limitToLast(5).addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun setTypingStatus(conversationId: String, userId: String, isTyping: Boolean): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/typing/$userId", isTyping).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun setRecordingStatus(conversationId: String, userId: String, isRecording: Boolean): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/recording/$userId", isRecording).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun setNickname(conversationId: String, userId: String, nickname: String): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/nicknames/$userId", nickname).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun setChatTheme(conversationId: String, userId: String, themeId: String): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/themeId/$userId", themeId).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun setChatWallpaper(conversationId: String, userId: String, wallpaperUrl: String): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/wallpaperUrl/$userId", wallpaperUrl).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun muteNotifications(conversationId: String, userId: String, muted: Boolean): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/mutedBy/$userId", muted).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun setDisappearingMessages(conversationId: String, durationMs: Long): Result<Unit> = try { firebaseDataSource.writeData("conversations/$conversationId/disappearingMessagesDuration", durationMs).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun blockUser(currentUserId: String, targetUserId: String): Result<Unit> = try { firebaseDataSource.writeData("blocks/$currentUserId/$targetUserId", true).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun unblockUser(currentUserId: String, targetUserId: String): Result<Unit> = try { firebaseDataSource.getReference("blocks/$currentUserId/$targetUserId").removeValue().await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override fun isUserBlocked(currentUserId: String, targetUserId: String): Flow<Boolean> = callbackFlow {
        val ref = firebaseDataSource.getReference("blocks/$currentUserId/$targetUserId")
        val l = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) { trySend(s.exists()) }
            override fun onCancelled(e: DatabaseError) { close(e.toException()) }
        }
        ref.addValueEventListener(l)
        awaitClose { ref.removeEventListener(l) }
    }
    override suspend fun reportUser(reporterId: String, targetUserId: String, reason: String): Result<Unit> = try { firebaseDataSource.writeData("reports/${System.currentTimeMillis()}", mapOf("reporterId" to reporterId, "targetUserId" to targetUserId, "reason" to reason)).getOrThrow(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override fun getSharedMedia(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val ref = firebaseDataSource.getReference("messages/$conversationId")
        val l = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                trySend(s.children.mapNotNull { it.getValue(ChatMessage::class.java) }.filter { it.type != com.him.landlordtenant.app.data.model.communication.MessageType.TEXT && it.attachments.isNotEmpty() }.sortedByDescending { it.timestamp })
            }
            override fun onCancelled(e: DatabaseError) { close(e.toException()) }
        }
        ref.addValueEventListener(l)
        awaitClose { ref.removeEventListener(l) }
    }
    override fun getTotalUnreadCount(userId: String): Flow<Int> = callbackFlow {
        if (userId.isEmpty()) { trySend(0); awaitClose {}; return@callbackFlow }
        val ref = firebaseDataSource.getReference("unread_counts/$userId")
        val l = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) { trySend(s.children.sumOf { it.getValue(Int::class.java) ?: 0 }) }
            override fun onCancelled(e: DatabaseError) { close(e.toException()) }
        }
        ref.addValueEventListener(l)
        awaitClose { ref.removeEventListener(l) }
    }

    override suspend fun forwardMessage(messageId: String, targetConversationIds: List<String>): Result<Unit> = Result.success(Unit)
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
    override suspend fun createPropertyAnnouncement(userId: String, propertyId: String, announcement: PropertyAnnouncementData): Result<String> = try { postAnnouncement(userId, propertyId, announcement.message).map { announcement.id } } catch (e: Exception) { Result.failure(e) }
    override suspend fun getPropertyAnnouncements(propertyId: String): Result<List<PropertyAnnouncementData>> = Result.failure(NotImplementedError())
    override suspend fun getAnnouncements(apartmentId: String): Flow<List<ChatMessage>> = flow { emit(emptyList()) }
    override suspend fun postAnnouncement(landlordId: String, apartmentId: String, text: String): Result<Unit> = Result.success(Unit)
}
