package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.enums.NotificationType
import com.him.landlordtenant.app.enums.NotificationPriority

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * NOTIFICATION REPOSITORY
 * =============================================================
 *
 * Central notification system for:
 *
 * - Rent reminders
 * - Bill reminders
 * - Payment confirmations
 * - Agreement signing
 * - Agreement expiry
 * - Maintenance updates
 * - Technician appointments
 * - Property viewing appointments
 * - New property listings
 * - Tenant announcements
 * - Landlord announcements
 * - Messages
 * - Security alerts
 * - Account alerts
 * - System notifications
 *
 * Channels:
 *
 * - In-app
 * - Push notification
 * - Email
 * - SMS
 *
 * =============================================================
 */

interface NotificationRepository {

    /*
     * ---------------------------------------------------------
     * NOTIFICATION CREATION
     * ---------------------------------------------------------
     */

    suspend fun createNotification(
        notification: CreateNotificationData
    ): Result<String>

    suspend fun createBulkNotifications(
        notifications: List<CreateNotificationData>
    ): Result<List<String>>

    suspend fun getNotification(
        notificationId: String
    ): Result<NotificationData>


    /*
     * ---------------------------------------------------------
     * USER NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun getUserNotifications(
        userId: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Result<List<NotificationData>>

    fun observeUserNotifications(
        userId: String
    ): Flow<Result<List<NotificationData>>>

    suspend fun getUnreadNotifications(
        userId: String
    ): Result<List<NotificationData>>

    suspend fun getUnreadCount(
        userId: String
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * READ / UNREAD
     * ---------------------------------------------------------
     */

    suspend fun markAsRead(
        userId: String,
        notificationId: String
    ): Result<Unit>

    suspend fun markAsUnread(
        userId: String,
        notificationId: String
    ): Result<Unit>

    suspend fun markAllAsRead(
        userId: String
    ): Result<Unit>

    suspend fun deleteNotification(
        userId: String,
        notificationId: String
    ): Result<Unit>

    suspend fun clearAllNotifications(
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PUSH NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun registerDevice(
        userId: String,
        device: NotificationDeviceData
    ): Result<String>

    suspend fun unregisterDevice(
        userId: String,
        deviceId: String
    ): Result<Unit>

    suspend fun getRegisteredDevices(
        userId: String
    ): Result<List<NotificationDeviceData>>

    suspend fun sendPushNotification(
        notificationId: String
    ): Result<Unit>

    suspend fun sendPushToUser(
        userId: String,
        notification: PushNotificationData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EMAIL
     * ---------------------------------------------------------
     */

    suspend fun sendEmailNotification(
        userId: String,
        notification: EmailNotificationData
    ): Result<Unit>

    suspend fun sendBulkEmail(
        userIds: List<String>,
        notification: EmailNotificationData
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * SMS
     * ---------------------------------------------------------
     */

    suspend fun sendSmsNotification(
        userId: String,
        notification: SmsNotificationData
    ): Result<Unit>

    suspend fun sendBulkSms(
        userIds: List<String>,
        notification: SmsNotificationData
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * AUTOMATED RENT REMINDERS
     * ---------------------------------------------------------
     */

    suspend fun sendRentDueReminder(
        tenantId: String,
        billId: String,
        daysBeforeDue: Int
    ): Result<Unit>

    suspend fun sendRentOverdueReminder(
        tenantId: String,
        billId: String,
        daysOverdue: Int
    ): Result<Unit>

    suspend fun processRentReminders(): Result<Int>


    /*
     * ---------------------------------------------------------
     * BILL REMINDERS
     * ---------------------------------------------------------
     */

    suspend fun sendBillDueReminder(
        tenantId: String,
        billId: String,
        daysBeforeDue: Int
    ): Result<Unit>

    suspend fun sendBillOverdueReminder(
        tenantId: String,
        billId: String
    ): Result<Unit>

    suspend fun processBillReminders(): Result<Int>


    /*
     * ---------------------------------------------------------
     * PAYMENT NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun sendPaymentSuccessNotification(
        paymentId: String
    ): Result<Unit>

    suspend fun sendPaymentFailedNotification(
        paymentId: String
    ): Result<Unit>

    suspend fun sendPaymentPendingNotification(
        paymentId: String
    ): Result<Unit>

    suspend fun sendReceiptNotification(
        paymentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AGREEMENT NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun sendAgreementInvitation(
        agreementId: String
    ): Result<Unit>

    suspend fun sendAgreementSignatureReminder(
        agreementId: String
    ): Result<Unit>

    suspend fun sendAgreementSignedNotification(
        agreementId: String
    ): Result<Unit>

    suspend fun sendAgreementExpiryReminder(
        agreementId: String,
        daysBeforeExpiry: Int
    ): Result<Unit>

    suspend fun sendAgreementExpiredNotification(
        agreementId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * MAINTENANCE NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun sendMaintenanceCreatedNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceApprovedNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceAssignedNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceScheduledNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceProgressNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceCompletedNotification(
        requestId: String
    ): Result<Unit>

    suspend fun sendMaintenanceDisputeNotification(
        requestId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY VIEWING NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun sendViewingRequestNotification(
        viewingId: String
    ): Result<Unit>

    suspend fun sendViewingAcceptedNotification(
        viewingId: String
    ): Result<Unit>

    suspend fun sendViewingRejectedNotification(
        viewingId: String
    ): Result<Unit>

    suspend fun sendViewingReminder(
        viewingId: String,
        hoursBefore: Int
    ): Result<Unit>

    suspend fun sendViewingCancelledNotification(
        viewingId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY LISTING NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun notifyMatchingProperty(
        propertyId: String,
        userIds: List<String>
    ): Result<Int>

    suspend fun notifyNewPropertyListing(
        propertyId: String,
        userIds: List<String>
    ): Result<Int>

    suspend fun notifyPropertyStatusChanged(
        propertyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ANNOUNCEMENTS
     * ---------------------------------------------------------
     */

    suspend fun createAnnouncement(
        createdBy: String,
        announcement: CreateAnnouncementData
    ): Result<String>

    suspend fun publishAnnouncement(
        announcementId: String
    ): Result<Unit>

    suspend fun getAnnouncements(
        propertyId: String
    ): Result<List<AnnouncementData>>

    suspend fun sendAnnouncement(
        announcementId: String
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * NOTIFICATION PREFERENCES
     * ---------------------------------------------------------
     */

    suspend fun getPreferences(
        userId: String
    ): Result<NotificationPreferencesData>

    suspend fun updatePreferences(
        userId: String,
        preferences: NotificationPreferencesData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * NOTIFICATION TEMPLATES
     * ---------------------------------------------------------
     */

    suspend fun getTemplates(): Result<List<NotificationTemplateData>>

    suspend fun getTemplate(
        templateId: String
    ): Result<NotificationTemplateData>

    suspend fun createTemplate(
        createdBy: String,
        template: NotificationTemplateData
    ): Result<String>

    suspend fun updateTemplate(
        createdBy: String,
        templateId: String,
        template: NotificationTemplateData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * NOTIFICATION HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getNotificationHistory(
        userId: String,
        startDate: String,
        endDate: String
    ): Result<List<NotificationData>>

    suspend fun getFailedNotifications(
        userId: String
    ): Result<List<NotificationData>>

    suspend fun retryNotification(
        notificationId: String
    ): Result<Unit>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateNotificationData(
    val recipientId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val channels: List<NotificationChannel>,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val relatedEntityId: String? = null,
    val relatedEntityType: String? = null,
    val scheduledAt: String? = null
)

data class NotificationData(
    val id: String,
    val recipientId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val channels: List<NotificationChannel>,
    val priority: NotificationPriority,
    val relatedEntityId: String?,
    val relatedEntityType: String?,
    val read: Boolean,
    val createdAt: String
)

data class PushNotificationData(
    val title: String,
    val message: String,
    val data: Map<String, String> = emptyMap()
)

data class EmailNotificationData(
    val subject: String,
    val body: String,
    val html: Boolean = true
)

data class SmsNotificationData(
    val message: String
)

data class NotificationDeviceData(
    val id: String = "",
    val token: String,
    val platform: NotificationPlatform,
    val deviceName: String?,
    val active: Boolean = true,
    val registeredAt: String
)

data class NotificationPreferencesData(
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = true,
    val smsEnabled: Boolean = true,

    val rentReminders: Boolean = true,
    val billReminders: Boolean = true,
    val paymentNotifications: Boolean = true,
    val agreementNotifications: Boolean = true,
    val maintenanceNotifications: Boolean = true,
    val viewingNotifications: Boolean = true,
    val propertyNotifications: Boolean = true,
    val announcementNotifications: Boolean = true,
    val messageNotifications: Boolean = true,

    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: String? = null,
    val quietHoursEnd: String? = null
)

data class NotificationTemplateData(
    val id: String = "",
    val name: String,
    val type: NotificationType,
    val titleTemplate: String,
    val messageTemplate: String,
    val channels: List<NotificationChannel>,
    val active: Boolean = true
)



/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class NotificationType {

    RENT_DUE,

    RENT_OVERDUE,

    BILL_DUE,

    BILL_OVERDUE,

    PAYMENT_SUCCESS,

    PAYMENT_FAILED,

    PAYMENT_PENDING,

    PAYMENT_RECEIPT,

    AGREEMENT_INVITATION,

    AGREEMENT_SIGNATURE_REQUIRED,

    AGREEMENT_SIGNED,

    AGREEMENT_EXPIRING,

    AGREEMENT_EXPIRED,

    MAINTENANCE_CREATED,

    MAINTENANCE_APPROVED,

    MAINTENANCE_ASSIGNED,

    MAINTENANCE_SCHEDULED,

    MAINTENANCE_PROGRESS,

    MAINTENANCE_COMPLETED,

    MAINTENANCE_DISPUTE,

    VIEWING_REQUEST,

    VIEWING_ACCEPTED,

    VIEWING_REJECTED,

    VIEWING_REMINDER,

    VIEWING_CANCELLED,

    NEW_PROPERTY,

    PROPERTY_STATUS_CHANGED,

    ANNOUNCEMENT,

    MESSAGE,

    SECURITY_ALERT,

    ACCOUNT_ALERT,

    SYSTEM
}

enum class NotificationChannel {

    IN_APP,

    PUSH,

    EMAIL,

    SMS
}

enum class NotificationPriority {

    LOW,

    NORMAL,

    HIGH,

    URGENT
}

enum class NotificationPlatform {

    ANDROID,

    IOS,

    WEB,

    OTHER
}