package com.him.landlordtenant.app.interfaces.repository.impl

import android.util.Log
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.enums.MessageSenderRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val userRepository: UserRepository,
    private val agreementRepository: AgreementRepository
) : MessageRepository, ChatRepository {

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
                    // Start by getting all unread counts for this user
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

    override suspend fun getOrCreateDirectConversation(user1Id: String, user2Id: String): Result<String> {
        return try {
            if (user1Id.isEmpty() || user2Id.isEmpty()) {
                Result.failure(Exception("Invalid user IDs"))
            } else {
                val conversationId = if (user1Id < user2Id) "${user1Id}_${user2Id}" else "${user2Id}_${user1Id}"
                val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
                
                if (!snapshot.exists()) {
                    val user1 = userRepository.getUserById(user1Id)
                    val user2 = userRepository.getUserById(user2Id)
                    
                    val participants = listOfNotNull(
                        user1?.let { u -> 
                            ChatParticipant(
                                id = u.id, 
                                name = u.fullName, 
                                role = MessageSenderRole.entries.find { it.name == u.activeRole?.name } ?: MessageSenderRole.TENANT
                            ) 
                        },
                        user2?.let { u -> 
                            ChatParticipant(
                                id = u.id, 
                                name = u.fullName, 
                                role = MessageSenderRole.entries.find { it.name == u.activeRole?.name } ?: MessageSenderRole.TENANT
                            ) 
                        }
                    )

                    val newConversation = ChatConversation(
                        id = conversationId,
                        name = user2?.fullName ?: "User",
                        isGroup = false,
                        participants = participants,
                        participantIds = listOf(user1Id, user2Id),
                        createdAt = System.currentTimeMillis()
                    )
                    firebaseDataSource.writeData("conversations/$conversationId", newConversation)
                }
                Result.success(conversationId)
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error in getOrCreateDirectConversation", e)
            Result.failure(e)
        }
    }

    override suspend fun clearChat(conversationId: String, userId: String): Result<Unit> = try {
        val messagesRef = firebaseDataSource.getReference("messages/$conversationId")
        val snapshot = messagesRef.get().await()
        
        snapshot.children.forEach { msgSnapshot ->
            msgSnapshot.ref.child("deletedForUsers").child(userId).setValue(true)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteChat(conversationId: String, userId: String): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/deletedForUsers/$userId", true)
        clearChat(conversationId, userId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun pinConversation(conversationId: String, userId: String, pinned: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/pinnedBy/$userId", pinned)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getOrCreateCommunityConversation(apartmentId: String, landlordId: String, apartmentName: String): Result<String> = try {
        val conversationId = "comm_$apartmentId"
        val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
        
        if (!snapshot.exists()) {
            val landlord = userRepository.getUserById(landlordId)
            val participants = listOfNotNull(
                landlord?.let { u -> 
                    ChatParticipant(id = u.id, name = u.fullName, role = MessageSenderRole.LANDLORD) 
                }
            )

            val communityConversation = ChatConversation(
                id = conversationId,
                name = "$apartmentName Announcements",
                isGroup = true,
                isCommunity = true,
                participants = participants,
                participantIds = listOf(landlordId),
                createdAt = System.currentTimeMillis()
            )
            firebaseDataSource.writeData("conversations/$conversationId", communityConversation)
            
            // Initial System Message
            sendSystemMessage(conversationId, "📢 This is the official announcement channel for $apartmentName. Only admins can post here.")
        }
        Result.success(conversationId)
    } catch (e: Exception) {
        Log.e("ChatRepository", "Error in getOrCreateCommunityConversation", e)
        Result.failure(e)
    }

    override suspend fun getOrCreateTenantGroup(apartmentId: String, landlordId: String, apartmentName: String): Result<String> = try {
        val conversationId = "tenant_$apartmentId"
        val snapshot = firebaseDataSource.getReference("conversations/$conversationId").get().await()
        
        if (!snapshot.exists()) {
            val landlord = userRepository.getUserById(landlordId)
            val participants = listOfNotNull(
                landlord?.let { u -> 
                    ChatParticipant(id = u.id, name = u.fullName, role = MessageSenderRole.LANDLORD) 
                }
            )

            val tenantConversation = ChatConversation(
                id = conversationId,
                name = "$apartmentName Community",
                isGroup = true,
                isCommunity = true,
                participants = participants,
                participantIds = listOf(landlordId),
                createdAt = System.currentTimeMillis()
            )
            firebaseDataSource.writeData("conversations/$conversationId", tenantConversation)
            
            // Initial System Messages
            sendSystemMessage(conversationId, "🤝 Welcome to the $apartmentName Community!")
            
            // Fetch real rules from the General Agreement
            val rulesMessage = try {
                val agreementsSnapshot = firebaseDataSource.getReference("agreements")
                    .orderByChild("propertyId").equalTo(apartmentId)
                    .get().await()
                
                val generalAgreement = agreementsSnapshot.children.mapNotNull { it.getValue(AgreementDetailsData::class.java) }
                    .find { it.unitId == "GENERAL" }
                
                if (generalAgreement != null && generalAgreement.rules.isNotEmpty()) {
                    "📜 Building Rules: \n" + generalAgreement.rules.mapIndexed { i, rule -> "${i + 1}. $rule" }.joinToString("\n")
                } else {
                    "📜 Building Rules: \n1. Respect quiet hours (10PM - 6AM)\n2. Proper waste disposal is mandatory\n3. Friendly vibes only! ✨"
                }
            } catch (e: Exception) {
                "📜 Building Rules: \n1. Respect quiet hours (10PM - 6AM)\n2. Proper waste disposal is mandatory\n3. Friendly vibes only! ✨"
            }
            
            sendSystemMessage(conversationId, rulesMessage)
        }
        Result.success(conversationId)
    } catch (e: Exception) {
        Log.e("ChatRepository", "Error in getOrCreateTenantGroup", e)
        Result.failure(e)
    }

    override suspend fun addTenantToCommunityGroup(tenantId: String, apartmentId: String): Result<Unit> = try {
        // Add to both Announcement and Discussion groups
        val announcementId = "comm_$apartmentId"
        val discussionId = "tenant_$apartmentId"
        
        val tenant = userRepository.getUserById(tenantId) ?: throw Exception("Tenant not found")
        val participant = ChatParticipant(id = tenant.id, name = tenant.fullName, role = MessageSenderRole.TENANT)

        listOf(announcementId, discussionId).forEach { conversationId ->
            val ref = firebaseDataSource.getReference("conversations/$conversationId")
            val snapshot = ref.get().await()
            val conversation = snapshot.getValue(ChatConversation::class.java)

            if (conversation != null && !conversation.participantIds.contains(tenantId)) {
                val updatedParticipants = conversation.participants + participant
                val updatedIds = conversation.participantIds + tenantId
                ref.child("participants").setValue(updatedParticipants)
                ref.child("participantIds").setValue(updatedIds)
                
                // Post welcome message in Discussion group only
                if (conversationId == discussionId) {
                    sendSystemMessage(discussionId, "🏠 Welcome ${tenant.fullName} to our community! Feel free to say hi to your neighbors. ✨")
                    
                    // Send building rules as a reminder
                    val rulesMessage = try {
                        val agreementsSnapshot = firebaseDataSource.getReference("agreements")
                            .orderByChild("propertyId").equalTo(apartmentId)
                            .get().await()
                        
                        val generalAgreement = agreementsSnapshot.children.mapNotNull { it.getValue(AgreementDetailsData::class.java) }
                            .find { it.unitId == "GENERAL" }
                        
                        if (generalAgreement != null && generalAgreement.rules.isNotEmpty()) {
                            "📜 Building Rules: \n" + generalAgreement.rules.mapIndexed { i, rule -> "${i + 1}. $rule" }.joinToString("\n")
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                    
                    rulesMessage?.let { sendSystemMessage(discussionId, it) }
                    sendSystemMessage(discussionId, "👋 Say hello to ${tenant.fullName}!")
                }
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ChatRepository", "Error in addTenantToCommunityGroup", e)
        Result.failure(e)
    }

    override suspend fun getAnnouncements(apartmentId: String): Flow<List<ChatMessage>> = callbackFlow {
        val ref = firebaseDataSource.getReference("announcements/$apartmentId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
                    .sortedByDescending { it.timestamp }
                trySend(messages)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun postAnnouncement(landlordId: String, apartmentId: String, text: String): Result<Unit> = try {
        val landlord = userRepository.getUserById(landlordId) ?: throw Exception("Landlord not found")
        val ref = firebaseDataSource.getReference("announcements/$apartmentId").push()
        val announcement = ChatMessage(
            id = ref.key ?: "",
            conversationId = "ann_$apartmentId",
            senderId = landlordId,
            senderName = landlord.fullName,
            senderRole = MessageSenderRole.LANDLORD,
            text = text,
            type = com.him.landlordtenant.app.data.model.communication.MessageType.TEXT,
            timestamp = System.currentTimeMillis(),
            isSystemMessage = true
        )
        firebaseDataSource.writeData("announcements/$apartmentId/${announcement.id}", announcement)
        
        // Post a system message in the community discussion about the new announcement
        sendSystemMessage("tenant_$apartmentId", "🔔 New official announcement: \"$text\"")
        
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ChatRepository", "Error in postAnnouncement", e)
        Result.failure(e)
    }

    override suspend fun sendMessage(message: ChatMessage): Result<Unit> = try {
        val messagesRef = firebaseDataSource.getReference("messages/${message.conversationId}").push()
        val finalMessage = message.copy(id = messagesRef.key ?: "", status = com.him.landlordtenant.app.data.model.communication.MessageStatus.SENT)
        
        firebaseDataSource.writeData("messages/${message.conversationId}/${finalMessage.id}", finalMessage)
        firebaseDataSource.writeData("conversations/${message.conversationId}/lastMessage", finalMessage)
            
        val conversationSnapshot = firebaseDataSource.getReference("conversations/${message.conversationId}").get().await()
        val conversation = conversationSnapshot.getValue(ChatConversation::class.java)
        
        conversation?.participants?.filter { it.id != message.senderId }?.forEach { recipient ->
            // Increment unread count for recipients
            val unreadRef = firebaseDataSource.getReference("unread_counts/${recipient.id}/${message.conversationId}")
            val currentCount = unreadRef.get().await().getValue(Int::class.java) ?: 0
            unreadRef.setValue(currentCount + 1)

            val presenceSnapshot = firebaseDataSource.getReference("presence/${recipient.id}/online").get().await()
            if (presenceSnapshot.getValue(Boolean::class.java) == true) {
                firebaseDataSource.writeData("messages/${message.conversationId}/${finalMessage.id}/deliveredTo/${recipient.id}", System.currentTimeMillis())
                firebaseDataSource.getReference("messages/${message.conversationId}/${finalMessage.id}/status").setValue(com.him.landlordtenant.app.data.model.communication.MessageStatus.DELIVERED.name)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendSystemMessage(conversationId: String, text: String): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("messages/$conversationId").push()
        val message = ChatMessage(
            id = ref.key ?: "",
            conversationId = conversationId,
            senderId = "system",
            senderName = "propertyOS",
            text = text,
            type = com.him.landlordtenant.app.data.model.communication.MessageType.SYSTEM,
            timestamp = System.currentTimeMillis(),
            isSystemMessage = true
        )
        firebaseDataSource.writeData("messages/$conversationId/${message.id}", message)
        firebaseDataSource.writeData("conversations/$conversationId/lastMessage", message)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMessage(conversationId: String, messageId: String, forEveryone: Boolean): Result<Unit> = try {
        if (forEveryone) {
            firebaseDataSource.getReference("messages/$conversationId/$messageId").removeValue().await()
        } else {
            // Handled by deleteMessageForUser signature if it's just for current user
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMessageForUser(conversationId: String, messageId: String, userId: String): Result<Unit> = try {
        firebaseDataSource.writeData("messages/$conversationId/$messageId/deletedForUsers/$userId", true)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun pinMessage(conversationId: String, messageId: String, isPinned: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("messages/$conversationId/$messageId/isPinned", isPinned)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addReaction(conversationId: String, messageId: String, emoji: String, userId: String): Result<Unit> = try {
        val reactionId = "react_${userId}"
        val reaction = ChatReaction(userId = userId, emoji = emoji)
        firebaseDataSource.writeData("messages/$conversationId/$messageId/reactions/$reactionId", reaction)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun editMessage(conversationId: String, messageId: String, newText: String): Result<Unit> = try {
        val updates = mapOf(
            "text" to newText,
            "isEdited" to true,
            "updatedAt" to System.currentTimeMillis()
        )
        firebaseDataSource.getReference("messages/$conversationId/$messageId").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun toggleStarMessage(conversationId: String, messageId: String, userId: String): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("messages/$conversationId/$messageId/starredBy/$userId")
        val snapshot = ref.get().await()
        if (snapshot.exists()) {
            ref.removeValue().await()
        } else {
            ref.setValue(true).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun forwardMessage(messageId: String, targetConversationIds: List<String>): Result<Unit> = try {
        // This is complex because we need to find the source message first.
        // Assuming we have a global messages node or we iterate conversations (not efficient).
        // In a real app, forward usually involves sending a new message with 'forwarded' flag set to true.
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getSharedMedia(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val ref = firebaseDataSource.getReference("messages/$conversationId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mediaMessages = snapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
                    .filter { it.type != com.him.landlordtenant.app.data.model.communication.MessageType.TEXT && it.attachments.isNotEmpty() }
                    .sortedByDescending { it.timestamp }
                trySend(mediaMessages)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun getOnlineStatus(userId: String): Flow<Boolean> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(false)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("presence/$userId/online")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Boolean::class.java) ?: false)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
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
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Long::class.java) ?: 0L)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun getTotalUnreadCount(userId: String): Flow<Int> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(0)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("unread_counts/$userId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val total = snapshot.children.sumOf { it.getValue(Int::class.java) ?: 0 }
                trySend(total)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> = try {
        val status = mapOf("online" to isOnline, "lastSeen" to System.currentTimeMillis())
        firebaseDataSource.writeData("presence/$userId", status)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setTypingStatus(conversationId: String, userId: String, isTyping: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/typing/$userId", isTyping)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setRecordingStatus(conversationId: String, userId: String, isRecording: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/recording/$userId", isRecording)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markAsRead(conversationId: String, userId: String): Result<Unit> = try {
        // Reset unread count for this user in this conversation
        firebaseDataSource.getReference("unread_counts/$userId/$conversationId").setValue(0)

        val messagesRef = firebaseDataSource.getReference("messages/$conversationId")
        val snapshot = messagesRef.get().await()
        snapshot.children.forEach { msgSnapshot ->
            val message = msgSnapshot.getValue(ChatMessage::class.java)
            if (message != null && message.senderId != userId && !message.readBy.containsKey(userId)) {
                msgSnapshot.ref.child("readBy").child(userId).setValue(System.currentTimeMillis())
                msgSnapshot.ref.child("status").setValue(com.him.landlordtenant.app.data.model.communication.MessageStatus.READ.name)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markAllAsRead(userId: String): Result<Unit> = try {
        val conversationsSnapshot = firebaseDataSource.getReference("conversations").get().await()
        conversationsSnapshot.children.forEach { convSnapshot ->
            val conversation = convSnapshot.getValue(ChatConversation::class.java)
            if (conversation != null && conversation.participantIds.contains(userId)) {
                markAsRead(conversation.id, userId)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setNickname(conversationId: String, userId: String, nickname: String): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/nicknames/$userId", nickname)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setChatTheme(conversationId: String, userId: String, themeId: String): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/themeId/$userId", themeId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setChatWallpaper(conversationId: String, userId: String, wallpaperUrl: String): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/wallpaperUrl/$userId", wallpaperUrl)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun muteNotifications(conversationId: String, userId: String, muted: Boolean): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/mutedBy/$userId", muted)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setDisappearingMessages(conversationId: String, durationMs: Long): Result<Unit> = try {
        firebaseDataSource.writeData("conversations/$conversationId/disappearingMessagesDuration", durationMs)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun blockUser(currentUserId: String, targetUserId: String): Result<Unit> = try {
        firebaseDataSource.writeData("blocks/$currentUserId/$targetUserId", true)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun unblockUser(currentUserId: String, targetUserId: String): Result<Unit> = try {
        firebaseDataSource.getReference("blocks/$currentUserId/$targetUserId").removeValue().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun isUserBlocked(currentUserId: String, targetUserId: String): Flow<Boolean> = callbackFlow {
        val ref = firebaseDataSource.getReference("blocks/$currentUserId/$targetUserId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun reportUser(reporterId: String, targetUserId: String, reason: String): Result<Unit> = try {
        val reportId = "report_${System.currentTimeMillis()}"
        val report = mapOf("reporterId" to reporterId, "targetUserId" to targetUserId, "reason" to reason, "timestamp" to System.currentTimeMillis())
        firebaseDataSource.writeData("reports/$reportId", report)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun initiateCall(call: CallSession): Result<String> = try {
        val ref = firebaseDataSource.getReference("calls").push()
        val id = ref.key ?: ""
        val finalCall = call.copy(id = id)
        firebaseDataSource.writeData("calls/$id", finalCall)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun endCall(callId: String): Result<Unit> = try {
        firebaseDataSource.getReference("calls/$callId").updateChildren(mapOf("status" to CallStatus.ENDED.name, "endTime" to System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getActiveCall(userId: String): Flow<CallSession?> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }
        val ref = firebaseDataSource.getReference("calls")
        val query = ref.orderByChild("receiverId").equalTo(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val call = snapshot.children.mapNotNull { it.getValue(CallSession::class.java) }
                        .firstOrNull { it.status == CallStatus.DIALING || it.status == CallStatus.RINGING }
                    trySend(call)
                } catch (e: Exception) {
                    Log.e("ChatRepository", "Error parsing active call", e)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    // Unimplemented MessageRepository members
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
    override suspend fun createPropertyAnnouncement(userId: String, propertyId: String, announcement: PropertyAnnouncementData): Result<String> = try {
        postAnnouncement(userId, propertyId, announcement.message).map { announcement.id }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getPropertyAnnouncements(propertyId: String): Result<List<PropertyAnnouncementData>> = Result.failure(NotImplementedError())
}
