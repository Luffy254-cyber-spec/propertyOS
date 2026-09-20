package com.him.landlordtenant.app.data.model.communication

import com.him.landlordtenant.app.enums.MessageSenderRole

/**
 * Enhanced Message types for WhatsApp-like chat
 */
enum class MessageType {
    TEXT, IMAGE, VIDEO, AUDIO, DOCUMENT, LOCATION, CONTACT, CALL_LOG, SYSTEM
}

/**
 * Message Status for read receipts
 */
enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ, FAILED
}

/**
 * Message Attachment for media messages
 */
data class ChatAttachment(
    val id: String = "",
    val url: String = "",
    val fileName: String? = null,
    val mimeType: String? = null,
    val sizeBytes: Long = 0L,
    val thumbnailUrl: String? = null,
    val durationMs: Long? = null, // For audio/video
    val latitude: Double? = null, // For location
    val longitude: Double? = null, // For location
    val contactName: String? = null, // For contact
    val contactPhone: String? = null // For contact
)

/**
 * Message Reaction
 */
data class ChatReaction(
    val emoji: String = "",
    val userId: String = "",
    val userName: String = ""
)

/**
 * Reply reference
 */
data class ChatReplyReference(
    val messageId: String = "",
    val senderName: String = "",
    val textPreview: String = "",
    val type: MessageType = MessageType.TEXT
)

/**
 * Main Message Model
 */
data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderRole: MessageSenderRole = MessageSenderRole.TENANT,
    val text: String = "",
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT,
    val timestamp: Long = System.currentTimeMillis(),
    val attachments: List<ChatAttachment> = emptyList(),
    val reactions: Map<String, ChatReaction> = emptyMap(), // reactionId to reaction
    val replyTo: ChatReplyReference? = null,
    val forwarded: Boolean = false,
    val deletedForEveryone: Boolean = false,
    val pinned: Boolean = false,
    val isEdited: Boolean = false,
    val isSystemMessage: Boolean = false,
    val starredBy: Map<String, Boolean> = emptyMap(), // userId to true
    val deliveredTo: Map<String, Long> = emptyMap(), // userId to timestamp
    val readBy: Map<String, Long> = emptyMap(), // userId to timestamp
    val deletedForUsers: Map<String, Boolean> = emptyMap(), // userId to true
    val expiresAt: Long? = null // For disappearing messages
)

/**
 * Conversation / Chat Room Model
 */
data class ChatConversation(
    val id: String = "",
    val name: String = "",
    val iconUrl: String? = null,
    val isGroup: Boolean = false,
    val isCommunity: Boolean = false,
    val lastMessage: ChatMessage? = null,
    val unreadCount: Int = 0,
    val participants: List<ChatParticipant> = emptyList(),
    val participantIds: List<String> = emptyList(),
    val wallpaperUrls: Map<String, String> = emptyMap(), // userId to url
    val createdAt: Long = System.currentTimeMillis(),
    val disappearingMessagesDuration: Long = 0, // 0 means disabled, otherwise in milliseconds
    val mutedBy: Map<String, Boolean> = emptyMap(),
    val nicknames: Map<String, String> = emptyMap(), // userId to nickname
    val themeId: Map<String, String> = emptyMap(), // userId to theme name/id
    val pinnedBy: Map<String, Boolean> = emptyMap(), // userId to true
    val deletedForUsers: Map<String, Boolean> = emptyMap() // userId to true
)

/**
 * Chat Participant info
 */
data class ChatParticipant(
    val id: String = "",
    val name: String = "",
    val role: MessageSenderRole = MessageSenderRole.TENANT,
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L,
    val isTyping: Boolean = false,
    val isRecording: Boolean = false
)

/**
 * Call details
 */
data class CallSession(
    val id: String = "",
    val callerId: String = "",
    val callerName: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val isVideo: Boolean = false,
    val status: CallStatus = CallStatus.DIALING,
    val startTime: Long = 0L,
    val durationSeconds: Int = 0
)

enum class CallStatus {
    DIALING, RINGING, CONNECTED, ENDED, MISSED, REJECTED, BUSY
}
