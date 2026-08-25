package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * MESSAGE MODEL
 * =============================================================
 *
 * Supports:
 *
 * 1. Tenant <-> Landlord private chat
 * 2. Tenant <-> Property Manager chat
 * 3. Tenant group chat
 * 4. Apartment announcements
 * 5. Maintenance-related messages
 * 6. System messages
 * 7. Media/file attachments
 * 8. Replies
 * 9. Read receipts
 * 10. Message reactions
 * 11. Message moderation
 *
 * =============================================================
 *
 * PRIVATE CHAT
 *
 * Tenant
 *    ↓
 * Landlord
 *    ↓
 * Conversation
 *    ↓
 * Messages
 *
 *
 * APARTMENT GROUP CHAT
 *
 * Apartment
 *      ↓
 * Group Chat
 *      ↓
 * All approved tenants
 *      ↓
 * Landlord / Manager
 *
 * =============================================================
 */

data class Message(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val conversationId: String = "",

    val senderId: String = "",

    val senderName: String = "",

    val senderRole:
    MessageSenderRole =
        MessageSenderRole.TENANT,

    /*
     * ---------------------------------------------------------
     * RECIPIENT
     * ---------------------------------------------------------
     *
     * Used primarily for private messages.
     *
     * Group messages can leave recipientId null.
     * ---------------------------------------------------------
     */

    val recipientId: String? = null,

    val recipientName: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY CONTEXT
     * ---------------------------------------------------------
     */

    val apartmentId: String? = null,

    val houseId: String? = null,

    /*
     * ---------------------------------------------------------
     * MESSAGE CONTENT
     * ---------------------------------------------------------
 */

    val text: String = "",

    val type:
    MessageType =
        MessageType.TEXT,

    /*
     * ---------------------------------------------------------
     * ATTACHMENTS
     * ---------------------------------------------------------
 */

    val attachments:
    List<MessageAttachment> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * REPLY
     * ---------------------------------------------------------
 */

    val replyTo:
    MessageReply? = null,

    /*
     * ---------------------------------------------------------
     * FORWARDING
     * ---------------------------------------------------------
 */

    val forwarded:
    Boolean = false,

    val originalMessageId:
    String? = null,

    /*
     * ---------------------------------------------------------
     * REACTIONS
     * ---------------------------------------------------------
 */

    val reactions:
    List<MessageReaction> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * MESSAGE STATUS
     * ---------------------------------------------------------
 */

    val status:
    MessageStatus =
        MessageStatus.SENT,

    /*
     * ---------------------------------------------------------
     * READ INFORMATION
     * ---------------------------------------------------------
 */

    val readBy:
    List<MessageReadReceipt> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * EDIT / DELETE
     * ---------------------------------------------------------
 */

    val edited:
    Boolean = false,

    val editedAt:
    String? = null,

    val deleted:
    Boolean = false,

    val deletedAt:
    String? = null,

    /*
     * ---------------------------------------------------------
     * MODERATION
     * ---------------------------------------------------------
 */

    val moderation:
    MessageModeration? = null,

    /*
     * ---------------------------------------------------------
     * SYSTEM INFORMATION
     * ---------------------------------------------------------
 */

    val systemAction:
    MessageSystemAction? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt:
    String? = null,

    val updatedAt:
    String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether this is a group message.
     */

    val isGroupMessage: Boolean
        get() =
            type == MessageType.GROUP_ANNOUNCEMENT ||
                    type == MessageType.SYSTEM


    /**
     * Whether this message has attachments.
     */

    val hasAttachments: Boolean
        get() =
            attachments.isNotEmpty()


    /**
     * Whether the message has reactions.
     */

    val hasReactions: Boolean
        get() =
            reactions.isNotEmpty()


    /**
     * Whether the message has been edited.
     */

    val wasEdited: Boolean
        get() =
            edited


    /**
     * Whether the message has been deleted.
     */

    val wasDeleted: Boolean
        get() =
            deleted


    /**
     * Whether the message can still be edited.
     *
     * The backend should enforce the actual time limit.
     */

    val canDisplayContent: Boolean
        get() =
            !deleted ||
                    type == MessageType.SYSTEM
}


/*
 * =============================================================
 * MESSAGE TYPE
 * =============================================================
 */

enum class MessageType(

    val displayName: String

) {

    TEXT(
        "Text"
    ),

    IMAGE(
        "Image"
    ),

    VIDEO(
        "Video"
    ),

    AUDIO(
        "Audio"
    ),

    DOCUMENT(
        "Document"
    ),

    LOCATION(
        "Location"
    ),

    CONTACT(
        "Contact"
    ),

    PAYMENT_RECEIPT(
        "Payment Receipt"
    ),

    MAINTENANCE_REQUEST(
        "Maintenance Request"
    ),

    AGREEMENT(
        "Agreement"
    ),

    ANNOUNCEMENT(
        "Announcement"
    ),

    GROUP_ANNOUNCEMENT(
        "Group Announcement"
    ),

    SYSTEM(
        "System Message"
    )
}


/*
 * =============================================================
 * MESSAGE SENDER ROLE
 * =============================================================
 */

enum class MessageSenderRole(

    val displayName: String

) {

    TENANT(
        "Tenant"
    ),

    LANDLORD(
        "Landlord"
    ),

    PROPERTY_MANAGER(
        "Property Manager"
    ),

    CARETAKER(
        "Caretaker"
    ),

    BROKER(
        "Broker"
    ),

    TECHNICIAN(
        "Technician"
    ),

    ADMIN(
        "Administrator"
    ),

    SYSTEM(
        "System"
    )
}


/*
 * =============================================================
 * MESSAGE STATUS
 * =============================================================
 */

enum class MessageStatus(

    val displayName: String

) {

    SENDING(
        "Sending"
    ),

    SENT(
        "Sent"
    ),

    DELIVERED(
        "Delivered"
    ),

    READ(
        "Read"
    ),

    FAILED(
        "Failed"
    ),

    DELETED(
        "Deleted"
    )
}


/*
 * =============================================================
 * MESSAGE ATTACHMENT
 * =============================================================
 */

data class MessageAttachment(

    val id: String = "",

    val url: String = "",

    val fileName: String? = null,

    val type:
    MessageAttachmentType =
        MessageAttachmentType.IMAGE,

    val mimeType: String? = null,

    val sizeBytes: Long = 0L,

    val thumbnailUrl: String? = null
)


/*
 * =============================================================
 * MESSAGE ATTACHMENT TYPE
 * =============================================================
 */

enum class MessageAttachmentType(

    val displayName: String

) {

    IMAGE(
        "Image"
    ),

    VIDEO(
        "Video"
    ),

    AUDIO(
        "Audio"
    ),

    DOCUMENT(
        "Document"
    ),

    PDF(
        "PDF"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * MESSAGE REPLY
 * =============================================================
 *
 * Stores a small snapshot of the message being replied to.
 * =============================================================
 */

data class MessageReply(

    val messageId: String = "",

    val senderId: String = "",

    val senderName: String = "",

    val previewText: String = "",

    val messageType:
    MessageType =
        MessageType.TEXT
)


/*
 * =============================================================
 * MESSAGE REACTION
 * =============================================================
 */

data class MessageReaction(

    val emoji: String = "",

    val userId: String = "",

    val userName: String = "",

    val createdAt: String? = null
)


/*
 * =============================================================
 * MESSAGE READ RECEIPT
 * =============================================================
 */

data class MessageReadReceipt(

    val userId: String = "",

    val userName: String? = null,

    val readAt: String? = null
)


/*
 * =============================================================
 * MESSAGE MODERATION
 * =============================================================
 *
 * Useful for the apartment group chat.
 *
 * A landlord/admin can report or moderate problematic content.
 * =============================================================
 */

data class MessageModeration(

    val status:
    MessageModerationStatus =
        MessageModerationStatus.CLEAR,

    val reportedBy:
    List<String> =
        emptyList(),

    val reportReason: String? = null,

    val reviewedBy: String? = null,

    val reviewedAt: String? = null,

    val moderatorNote: String? = null
)


/*
 * =============================================================
 * MESSAGE MODERATION STATUS
 * =============================================================
 */

enum class MessageModerationStatus(

    val displayName: String

) {

    CLEAR(
        "Clear"
    ),

    REPORTED(
        "Reported"
    ),

    UNDER_REVIEW(
        "Under Review"
    ),

    HIDDEN(
        "Hidden"
    ),

    REMOVED(
        "Removed"
    ),

    RESTORED(
        "Restored"
    )
}


/*
 * =============================================================
 * SYSTEM MESSAGE ACTION
 * =============================================================
 *
 * Messages generated automatically by the application.
 * =============================================================
 */

data class MessageSystemAction(

    val action:
    MessageSystemActionType =
        MessageSystemActionType.NONE,

    val referenceId: String? = null,

    val referenceType: String? = null,

    val description: String? = null
)


/*
 * =============================================================
 * SYSTEM MESSAGE ACTION TYPE
 * =============================================================
 */

enum class MessageSystemActionType(

    val displayName: String

) {

    NONE(
        "None"
    ),

    TENANT_JOINED(
        "Tenant Joined Apartment"
    ),

    TENANT_LEFT(
        "Tenant Left Apartment"
    ),

    RENT_PAID(
        "Rent Paid"
    ),

    BILL_GENERATED(
        "Bill Generated"
    ),

    PAYMENT_FAILED(
        "Payment Failed"
    ),

    MAINTENANCE_CREATED(
        "Maintenance Request Created"
    ),

    MAINTENANCE_COMPLETED(
        "Maintenance Completed"
    ),

    AGREEMENT_SIGNED(
        "Agreement Signed"
    ),

    HOUSE_BECAME_VACANT(
        "House Became Vacant"
    ),

    HOUSE_BECAME_OCCUPIED(
        "House Became Occupied"
    ),

    APARTMENT_ANNOUNCEMENT(
        "Apartment Announcement"
    )
}
