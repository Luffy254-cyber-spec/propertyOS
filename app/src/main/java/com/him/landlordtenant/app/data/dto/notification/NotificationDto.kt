package com.him.landlordtenant.app.data.dto.notification

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * NOTIFICATION DTO
 * =============================================================
 *
 * Represents an application notification delivered to a user.
 *
 * Supports:
 * - Rent reminders
 * - Overdue bills
 * - Payment confirmations
 * - Agreement requests
 * - Agreement signing
 * - Maintenance updates
 * - New messages
 * - Property viewing reminders
 * - Broker notifications
 * - Professional job assignments
 * - Announcements
 * - Emergency alerts
 *
 * =============================================================
 */

@Serializable
data class NotificationDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * RECIPIENT
     * ---------------------------------------------------------
     */

    val userId: String = "",

    val recipientType: String? = null,

    /*
     * ---------------------------------------------------------
     * NOTIFICATION TYPE
     * ---------------------------------------------------------
     */

    val type: String = "GENERAL",

    val category: String = "SYSTEM",

    /*
     * ---------------------------------------------------------
     * CONTENT
     * ---------------------------------------------------------
     */

    val title: String = "",

    val message: String = "",

    val shortMessage: String? = null,

    val imageUrl: String? = null,

    val icon: String? = null,

    /*
     * ---------------------------------------------------------
     * ACTION
     * ---------------------------------------------------------
     */

    val actionType: String? = null,

    val actionRoute: String? = null,

    val actionId: String? = null,

    val actionData: Map<String, String> = emptyMap(),

    /*
     * ---------------------------------------------------------
     * RELATED OBJECTS
     * ---------------------------------------------------------
     */

    val propertyId: String? = null,

    val houseId: String? = null,

    val apartmentId: String? = null,

    val agreementId: String? = null,

    val billId: String? = null,

    val paymentId: String? = null,

    val maintenanceRequestId: String? = null,

    val professionalId: String? = null,

    val conversationId: String? = null,

    val messageId: String? = null,

    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    val priority: String = "NORMAL",

    val urgent: Boolean = false,

    val requiresAction: Boolean = false,

    /*
     * ---------------------------------------------------------
     * READ STATE
     * ---------------------------------------------------------
     */

    val read: Boolean = false,

    val readAt: String? = null,

    /*
     * ---------------------------------------------------------
     * DELIVERY
     * ---------------------------------------------------------
 */

    val inAppEnabled: Boolean = true,

    val pushEnabled: Boolean = true,

    val smsEnabled: Boolean = false,

    val emailEnabled: Boolean = false,

    val pushSent: Boolean = false,

    val smsSent: Boolean = false,

    val emailSent: Boolean = false,

    /*
     * ---------------------------------------------------------
     * DELIVERY TIMESTAMPS
     * ---------------------------------------------------------
 */

    val pushSentAt: String? = null,

    val smsSentAt: String? = null,

    val emailSentAt: String? = null,

    /*
     * ---------------------------------------------------------
     * SCHEDULING
     * ---------------------------------------------------------
 */

    val scheduled: Boolean = false,

    val scheduledFor: String? = null,

    /*
     * ---------------------------------------------------------
     * EXPIRATION
     * ---------------------------------------------------------
 */

    val expiresAt: String? = null,

    val expired: Boolean = false,

    /*
     * ---------------------------------------------------------
     * USER ACTION
     * ---------------------------------------------------------
 */

    val actionCompleted: Boolean = false,

    val actionCompletedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * SYSTEM / AUTOMATION
     * ---------------------------------------------------------
 */

    val automated: Boolean = false,

    val automationRuleId: String? = null,

    val sourceEventId: String? = null,

    /*
     * ---------------------------------------------------------
     * GROUPING
     * ---------------------------------------------------------
 */

    val groupKey: String? = null,

    val collapseKey: String? = null,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status: String = "ACTIVE",

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

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

    val isUnread: Boolean
        get() = !read

    val needsImmediateAttention: Boolean
        get() =
            urgent ||
                    priority.uppercase() == "CRITICAL"

    val hasAction: Boolean
        get() =
            requiresAction &&
                    !actionCompleted

    val hasDeepLink: Boolean
        get() =
            !actionRoute.isNullOrBlank()

    val isDeliveryComplete: Boolean
        get() =
            (!pushEnabled || pushSent) &&
                    (!smsEnabled || smsSent) &&
                    (!emailEnabled || emailSent)

    val isExpiredNotification: Boolean
        get() =
            expired ||
                    status.uppercase() == "EXPIRED"

    val isActive: Boolean
        get() =
            status.uppercase() == "ACTIVE" &&
                    !isExpiredNotification
}