package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * NOTIFICATION MODEL
 * =============================================================
 *
 * Central notification model for the entire application.
 *
 * Notifications can be generated when:
 *
 * - A tenant joins an apartment
 * - A tenant joins a house
 * - An agreement is accepted
 * - Rent is due
 * - A bill is generated
 * - A bill becomes overdue
 * - Payment succeeds
 * - Payment fails
 * - A receipt is generated
 * - A maintenance request is created
 * - A technician is assigned
 * - Maintenance is completed
 * - A house becomes vacant
 * - A house becomes occupied
 * - A landlord sends an announcement
 * - A new message arrives
 * - An emergency is reported
 *
 * =============================================================
 *
 * NOTIFICATION FLOW
 *
 * Event
 *   ↓
 * Backend creates notification
 *   ↓
 * Push notification
 *   ↓
 * Android notification tray
 *   ↓
 * User opens notification
 *   ↓
 * Application navigates to relevant screen
 *
 * =============================================================
 */

data class Notification(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String = "",

    /*
     * ---------------------------------------------------------
     * NOTIFICATION TYPE
     * ---------------------------------------------------------
     */

    val type:
    NotificationType =
        NotificationType.SYSTEM,

    /*
     * ---------------------------------------------------------
     * CONTENT
     * ---------------------------------------------------------
     */

    val title: String = "",

    val message: String = "",

    val shortMessage: String? = null,

    /*
     * ---------------------------------------------------------
     * ICON / DISPLAY
     * ---------------------------------------------------------
     */

    val icon:
    NotificationIcon =
        NotificationIcon.SYSTEM,

    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    val priority:
    NotificationPriority =
        NotificationPriority.NORMAL,

    /*
     * ---------------------------------------------------------
     * READ STATUS
     * ---------------------------------------------------------
 */

    val status:
    NotificationStatus =
        NotificationStatus.UNREAD,

    /*
     * ---------------------------------------------------------
     * ACTION
     * ---------------------------------------------------------
     *
     * Determines where the user goes after tapping.
     *
     * Example:
     *
     * Rent notification
     *     ↓
     * Bills screen
     *
     * Maintenance notification
     *     ↓
     * Maintenance details
     *
     * ---------------------------------------------------------
     */

    val action:
    NotificationAction? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY CONTEXT
     * ---------------------------------------------------------
 */

    val apartmentId: String? = null,

    val apartmentName: String? = null,

    val floorId: String? = null,

    val houseId: String? = null,

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * RELATED RECORD
     * ---------------------------------------------------------
 */

    val referenceId: String? = null,

    val referenceType: String? = null,

    /*
     * ---------------------------------------------------------
     * SENDER
     * ---------------------------------------------------------
 */

    val senderId: String? = null,

    val senderName: String? = null,

    /*
     * ---------------------------------------------------------
     * CHANNELS
     * ---------------------------------------------------------
 */

    val delivery:
    NotificationDelivery =
        NotificationDelivery(),

    /*
     * ---------------------------------------------------------
     * SCHEDULE
     * ---------------------------------------------------------
 */

    val scheduledAt: String? = null,

    val expiresAt: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val readAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether notification is unread.
     */

    val isUnread: Boolean
        get() =
            status == NotificationStatus.UNREAD


    /**
     * Whether notification is high priority.
     */

    val isHighPriority: Boolean
        get() =
            priority == NotificationPriority.HIGH ||
                    priority == NotificationPriority.URGENT


    /**
     * Whether notification has an action.
     */

    val isActionable: Boolean
        get() =
            action != null


    /**
     * Whether notification has expired.
     *
     * Actual date comparison should normally be performed using
     * java.time.Instant on the application/backend.
     */

    val hasExpiry: Boolean
        get() =
            !expiresAt.isNullOrBlank()
}


/*
 * =============================================================
 * NOTIFICATION TYPE
 * =============================================================
 */

enum class NotificationType(

    val displayName: String

) {

    /*
     * ---------------------------------------------------------
     * ACCOUNT
     * ---------------------------------------------------------
     */

    ACCOUNT_CREATED(
        "Account Created"
    ),

    ACCOUNT_VERIFIED(
        "Account Verified"
    ),

    LOGIN_ALERT(
        "Login Alert"
    ),

    PROFILE_UPDATE(
        "Profile Updated"
    ),

    /*
     * ---------------------------------------------------------
     * APARTMENT
     * ---------------------------------------------------------
 */

    APARTMENT_INVITATION(
        "Apartment Invitation"
    ),

    APARTMENT_JOINED(
        "Apartment Joined"
    ),

    APARTMENT_UPDATE(
        "Apartment Update"
    ),

    /*
     * ---------------------------------------------------------
     * HOUSE
     * ---------------------------------------------------------
 */

    HOUSE_ASSIGNMENT(
        "House Assignment"
    ),

    HOUSE_AVAILABLE(
        "House Available"
    ),

    HOUSE_OCCUPIED(
        "House Occupied"
    ),

    HOUSE_NOT_READY(
        "House Not Ready"
    ),

    HOUSE_VACATED(
        "House Vacated"
    ),

    /*
     * ---------------------------------------------------------
     * AGREEMENT
     * ---------------------------------------------------------
 */

    AGREEMENT_CREATED(
        "Agreement Created"
    ),

    AGREEMENT_UPDATED(
        "Agreement Updated"
    ),

    AGREEMENT_ACCEPTED(
        "Agreement Accepted"
    ),

    AGREEMENT_REJECTED(
        "Agreement Rejected"
    ),

    AGREEMENT_EXPIRING(
        "Agreement Expiring"
    ),

    AGREEMENT_TERMINATED(
        "Agreement Terminated"
    ),

    /*
     * ---------------------------------------------------------
     * RENT
     * ---------------------------------------------------------
 */

    RENT_DUE_SOON(
        "Rent Due Soon"
    ),

    RENT_DUE_TODAY(
        "Rent Due Today"
    ),

    RENT_OVERDUE(
        "Rent Overdue"
    ),

    RENT_PAID(
        "Rent Paid"
    ),

    /*
     * ---------------------------------------------------------
     * BILLS
     * ---------------------------------------------------------
 */

    BILL_GENERATED(
        "Bill Generated"
    ),

    BILL_DUE_SOON(
        "Bill Due Soon"
    ),

    BILL_DUE_TODAY(
        "Bill Due Today"
    ),

    BILL_OVERDUE(
        "Bill Overdue"
    ),

    BILL_PARTIALLY_PAID(
        "Bill Partially Paid"
    ),

    BILL_PAID(
        "Bill Paid"
    ),

    /*
     * ---------------------------------------------------------
     * PAYMENTS
     * ---------------------------------------------------------
 */

    PAYMENT_INITIATED(
        "Payment Initiated"
    ),

    PAYMENT_PROCESSING(
        "Payment Processing"
    ),

    PAYMENT_SUCCESS(
        "Payment Successful"
    ),

    PAYMENT_FAILED(
        "Payment Failed"
    ),

    PAYMENT_CANCELLED(
        "Payment Cancelled"
    ),

    PAYMENT_REVERSED(
        "Payment Reversed"
    ),

    PAYMENT_REFUNDED(
        "Payment Refunded"
    ),

    /*
     * ---------------------------------------------------------
     * RECEIPTS
     * ---------------------------------------------------------
 */

    RECEIPT_GENERATED(
        "Receipt Generated"
    ),

    RECEIPT_VERIFIED(
        "Receipt Verified"
    ),

    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
 */

    MAINTENANCE_CREATED(
        "Maintenance Request Created"
    ),

    MAINTENANCE_UPDATED(
        "Maintenance Updated"
    ),

    MAINTENANCE_ASSIGNED(
        "Technician Assigned"
    ),

    MAINTENANCE_ACCEPTED(
        "Technician Accepted"
    ),

    MAINTENANCE_SCHEDULED(
        "Maintenance Scheduled"
    ),

    MAINTENANCE_STARTED(
        "Maintenance Started"
    ),

    MAINTENANCE_COMPLETED(
        "Maintenance Completed"
    ),

    MAINTENANCE_CANCELLED(
        "Maintenance Cancelled"
    ),

    /*
     * ---------------------------------------------------------
     * MESSAGING
     * ---------------------------------------------------------
 */

    NEW_MESSAGE(
        "New Message"
    ),

    GROUP_MESSAGE(
        "Apartment Group Message"
    ),

    MENTION(
        "Mention"
    ),

    /*
     * ---------------------------------------------------------
     * ANNOUNCEMENTS
     * ---------------------------------------------------------
 */

    ANNOUNCEMENT(
        "Announcement"
    ),

    /*
     * ---------------------------------------------------------
     * EMERGENCY
     * ---------------------------------------------------------
 */

    EMERGENCY(
        "Emergency"
    ),

    SECURITY_ALERT(
        "Security Alert"
    ),

    /*
     * ---------------------------------------------------------
     * SYSTEM
     * ---------------------------------------------------------
 */

    SYSTEM(
        "System Notification"
    )
}


/*
 * =============================================================
 * NOTIFICATION ICON
 * =============================================================
 */

enum class NotificationIcon(

    val displayName: String

) {

    HOME(
        "Home"
    ),

    RENT(
        "Rent"
    ),

    BILL(
        "Bill"
    ),

    PAYMENT(
        "Payment"
    ),

    RECEIPT(
        "Receipt"
    ),

    AGREEMENT(
        "Agreement"
    ),

    MAINTENANCE(
        "Maintenance"
    ),

    MESSAGE(
        "Message"
    ),

    ANNOUNCEMENT(
        "Announcement"
    ),

    EMERGENCY(
        "Emergency"
    ),

    SECURITY(
        "Security"
    ),

    ACCOUNT(
        "Account"
    ),

    SYSTEM(
        "System"
    )
}


/*
 * =============================================================
 * NOTIFICATION PRIORITY
 * =============================================================
 */

enum class NotificationPriority(

    val displayName: String

) {

    LOW(
        "Low"
    ),

    NORMAL(
        "Normal"
    ),

    HIGH(
        "High"
    ),

    URGENT(
        "Urgent"
    )
}


/*
 * =============================================================
 * NOTIFICATION STATUS
 * =============================================================
 */

enum class NotificationStatus(

    val displayName: String

) {

    UNREAD(
        "Unread"
    ),

    READ(
        "Read"
    ),

    DISMISSED(
        "Dismissed"
    ),

    EXPIRED(
        "Expired"
    )
}


/*
 * =============================================================
 * NOTIFICATION ACTION
 * =============================================================
 *
 * Determines what happens when the notification is tapped.
 * =============================================================
 */

data class NotificationAction(

    val type:
    NotificationActionType =
        NotificationActionType.OPEN_HOME,

    val destinationId: String? = null,

    val destinationReference: String? = null
)


/*
 * =============================================================
 * NOTIFICATION ACTION TYPE
 * =============================================================
 */

enum class NotificationActionType(

    val displayName: String

) {

    OPEN_HOME(
        "Open Home"
    ),

    OPEN_APARTMENT(
        "Open Apartment"
    ),

    OPEN_HOUSE(
        "Open House"
    ),

    OPEN_AGREEMENT(
        "Open Agreement"
    ),

    OPEN_BILLS(
        "Open Bills"
    ),

    OPEN_BILL(
        "Open Bill"
    ),

    OPEN_PAYMENT(
        "Open Payment"
    ),

    OPEN_RECEIPT(
        "Open Receipt"
    ),

    OPEN_MAINTENANCE(
        "Open Maintenance"
    ),

    OPEN_MESSAGE(
        "Open Message"
    ),

    OPEN_CONVERSATION(
        "Open Conversation"
    ),

    OPEN_ANNOUNCEMENT(
        "Open Announcement"
    ),

    OPEN_PROFILE(
        "Open Profile"
    ),

    OPEN_EMERGENCY(
        "Open Emergency"
    ),

    OPEN_NOTIFICATIONS(
        "Open Notifications"
    )
}


/*
 * =============================================================
 * NOTIFICATION DELIVERY
 * =============================================================
 *
 * Records the channels through which the notification has been
 * delivered.
 *
 * Actual push/SMS/email sending should be handled by backend
 * services rather than this model.
 *
 * =============================================================
 */

data class NotificationDelivery(

    /*
     * In-app
     */

    val inApp: Boolean = true,

    val inAppDeliveredAt: String? = null,

    /*
     * Push notification
     */

    val push: Boolean = false,

    val pushDeliveredAt: String? = null,

    /*
     * SMS
     */

    val sms: Boolean = false,

    val smsDeliveredAt: String? = null,

    /*
     * Email
     */

    val email: Boolean = false,

    val emailDeliveredAt: String? = null,

    /*
     * WhatsApp
     */

    val whatsapp: Boolean = false,

    val whatsappDeliveredAt: String? = null
)