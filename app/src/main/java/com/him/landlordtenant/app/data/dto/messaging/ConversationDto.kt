package com.him.landlordtenant.app.data.dto.messaging

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * CONVERSATION DTO
 * =============================================================
 *
 * Represents a messaging conversation between two or more
 * participants.
 *
 * Supports:
 * - Tenant ↔ Landlord
 * - Tenant ↔ Caretaker
 * - Tenant ↔ Broker
 * - Tenant ↔ Technician
 * - Landlord ↔ Tenant
 * - Group conversations
 * - Property-related conversations
 * - Maintenance conversations
 * - Agreement conversations
 * - Payment conversations
 * - System conversations
 *
 * =============================================================
 */

@Serializable
data class ConversationDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * CONVERSATION TYPE
     * ---------------------------------------------------------
     */

    val type: String = "DIRECT",

    val category: String = "GENERAL",

    val title: String? = null,

    val description: String? = null,

    /*
     * ---------------------------------------------------------
     * PARTICIPANTS
     * ---------------------------------------------------------
     */

    val participantIds: List<String> = emptyList(),

    val participantCount: Int = 0,

    val adminIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * CURRENT USER
     * ---------------------------------------------------------
     */

    val currentUserId: String? = null,

    val currentUserRole: String? = null,

    /*
     * ---------------------------------------------------------
     * LAST MESSAGE
     * ---------------------------------------------------------
 */

    val lastMessageId: String? = null,

    val lastMessageContent: String? = null,

    val lastMessageType: String? = null,

    val lastMessageSenderId: String? = null,

    val lastMessageSenderName: String? = null,

    val lastMessageAt: String? = null,

    /*
     * ---------------------------------------------------------
     * UNREAD
     * ---------------------------------------------------------
 */

    val unreadCount: Int = 0,

    val unreadMentionCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * PROPERTY CONTEXT
     * ---------------------------------------------------------
 */

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val houseId: String? = null,

    val agreementId: String? = null,

    val maintenanceRequestId: String? = null,

    val billId: String? = null,

    /*
     * ---------------------------------------------------------
     * PARTICIPANT DISPLAY
     * ---------------------------------------------------------
 */

    val participantNames: List<String> = emptyList(),

    val participantProfileImages: List<String> = emptyList(),

    val participantRoles: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * CONVERSATION SETTINGS
     * ---------------------------------------------------------
 */

    val muted: Boolean = false,

    val pinned: Boolean = false,

    val archived: Boolean = false,

    val notificationsEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
 */

    val encrypted: Boolean = true,

    val encryptionVersion: String? = null,

    /*
     * ---------------------------------------------------------
     * MODERATION
     * ---------------------------------------------------------
 */

    val flagged: Boolean = false,

    val moderationStatus: String = "CLEAR",

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status: String = "ACTIVE",

    val closedReason: String? = null,

    val closedBy: String? = null,

    val closedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * MESSAGE STATISTICS
     * ---------------------------------------------------------
 */

    val messageCount: Int = 0,

    val attachmentCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastActivityAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isGroup: Boolean
        get() =
            type.uppercase() == "GROUP" ||
                    participantIds.size > 2

    val isDirect: Boolean
        get() =
            type.uppercase() == "DIRECT" &&
                    participantIds.size <= 2

    val hasUnreadMessages: Boolean
        get() = unreadCount > 0

    val hasMentions: Boolean
        get() = unreadMentionCount > 0

    val isMuted: Boolean
        get() = muted || !notificationsEnabled

    val isArchived: Boolean
        get() = archived

    val isClosed: Boolean
        get() =
            status.uppercase() == "CLOSED"

    val hasPropertyContext: Boolean
        get() =
            propertyId != null ||
                    apartmentId != null ||
                    houseId != null

    val hasServiceContext: Boolean
        get() =
            maintenanceRequestId != null

    val hasFinancialContext: Boolean
        get() =
            billId != null

    val isFlagged: Boolean
        get() =
            flagged ||
                    moderationStatus.uppercase() != "CLEAR"
}