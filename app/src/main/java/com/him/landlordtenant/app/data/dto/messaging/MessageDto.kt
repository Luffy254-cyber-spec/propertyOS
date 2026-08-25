package com.him.landlordtenant.app.data.dto.messaging

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * MESSAGE DTO
 * =============================================================
 *
 * Represents a single message inside a conversation.
 *
 * Supports:
 * - Tenant ↔ Landlord
 * - Tenant ↔ Caretaker
 * - Tenant ↔ Broker
 * - Tenant ↔ Technician
 * - Landlord ↔ Professional
 * - Landlord ↔ Tenant
 * - System/automated messages
 * - Text messages
 * - Images
 * - Videos
 * - Documents
 * - Voice messages
 * - Location sharing
 * - Replies
 * - Read receipts
 * - Message reactions
 * - Message editing/deletion
 *
 * =============================================================
 */

@Serializable
data class MessageDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val conversationId: String = "",

    val referenceNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * SENDER
     * ---------------------------------------------------------
     */

    val senderId: String = "",

    val senderType: String = "TENANT",

    val senderName: String? = null,

    val senderProfileImageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * RECIPIENT
     * ---------------------------------------------------------
     */

    val recipientId: String? = null,

    val recipientType: String? = null,

    /*
     * ---------------------------------------------------------
     * MESSAGE CONTENT
     * ---------------------------------------------------------
 */

    val messageType: String = "TEXT",

    val content: String? = null,

    val formattedContent: String? = null,

    /*
     * ---------------------------------------------------------
     * ATTACHMENTS
     * ---------------------------------------------------------
 */

    val attachmentIds: List<String> = emptyList(),

    val attachmentUrls: List<String> = emptyList(),

    val attachmentNames: List<String> = emptyList(),

    val attachmentTypes: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * REPLY
     * ---------------------------------------------------------
 */

    val replyToMessageId: String? = null,

    val replyToContent: String? = null,

    val replyToSenderName: String? = null,

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val latitude: Double? = null,

    val longitude: Double? = null,

    val locationName: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY CONTEXT
     * ---------------------------------------------------------
 */

    val propertyId: String? = null,

    val houseId: String? = null,

    val apartmentId: String? = null,

    val maintenanceRequestId: String? = null,

    val agreementId: String? = null,

    val billId: String? = null,

    /*
     * ---------------------------------------------------------
     * SYSTEM MESSAGE
     * ---------------------------------------------------------
 */

    val systemMessage: Boolean = false,

    val systemEventType: String? = null,

    /*
     * ---------------------------------------------------------
     * READ STATUS
     * ---------------------------------------------------------
 */

    val delivered: Boolean = false,

    val deliveredAt: String? = null,

    val read: Boolean = false,

    val readAt: String? = null,

    /*
     * ---------------------------------------------------------
     * REACTIONS
     * ---------------------------------------------------------
 */

    val reactions: Map<String, Int> = emptyMap(),

    val currentUserReaction: String? = null,

    /*
     * ---------------------------------------------------------
     * MESSAGE STATE
     * ---------------------------------------------------------
 */

    val edited: Boolean = false,

    val editedAt: String? = null,

    val deleted: Boolean = false,

    val deletedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * MODERATION
     * ---------------------------------------------------------
 */

    val flagged: Boolean = false,

    val flaggedReason: String? = null,

    val moderationStatus: String = "CLEAR",

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
 */

    val encrypted: Boolean = false,

    /*
     * ---------------------------------------------------------
     * NOTIFICATION
     * ---------------------------------------------------------
 */

    val notificationSent: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val sentAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isRead: Boolean
        get() = read

    val isDelivered: Boolean
        get() = delivered

    val hasAttachments: Boolean
        get() =
            attachmentIds.isNotEmpty() ||
                    attachmentUrls.isNotEmpty()

    val hasReply: Boolean
        get() =
            !replyToMessageId.isNullOrBlank()

    val hasLocation: Boolean
        get() =
            latitude != null &&
                    longitude != null

    val hasReactions: Boolean
        get() =
            reactions.isNotEmpty()

    val isSystemGenerated: Boolean
        get() =
            systemMessage ||
                    senderType.uppercase() == "SYSTEM"

    val isDeleted: Boolean
        get() = deleted

    val isEdited: Boolean
        get() = edited

    val isFlagged: Boolean
        get() =
            flagged ||
                    moderationStatus.uppercase() != "CLEAR"
}