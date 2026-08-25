package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * ANNOUNCEMENT MODEL
 * =============================================================
 *
 * Used by:
 *
 * - Landlords
 * - Property managers
 * - Caretakers
 * - Administrators
 *
 * To communicate with:
 *
 * - All tenants
 * - Specific apartments
 * - Specific floors
 * - Specific houses
 * - Individual tenants
 * - Prospective tenants
 *
 * Examples:
 *
 * "Water will be unavailable tomorrow from 8 AM to 2 PM."
 *
 * "House G1 has been renovated and is now available."
 *
 * "Rent for August is due on 28 August."
 *
 * "Emergency: Please avoid the east entrance."
 *
 * =============================================================
 */

data class Announcement(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * CONTENT
     * ---------------------------------------------------------
     */

    val title: String = "",

    val message: String = "",

    val shortDescription: String? = null,

    /*
     * ---------------------------------------------------------
     * TYPE
     * ---------------------------------------------------------
     */

    val type:
    AnnouncementType =
        AnnouncementType.GENERAL,

    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    val priority:
    AnnouncementPriority =
        AnnouncementPriority.NORMAL,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    AnnouncementStatus =
        AnnouncementStatus.DRAFT,

    /*
     * ---------------------------------------------------------
     * AUTHOR
     * ---------------------------------------------------------
 */

    val createdBy: String = "",

    val createdByName: String? = null,

    val createdByRole:
    AnnouncementAuthorRole =
        AnnouncementAuthorRole.LANDLORD,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
 */

    val apartmentId: String? = null,

    val apartmentName: String? = null,

    /*
     * ---------------------------------------------------------
     * TARGET AUDIENCE
     * ---------------------------------------------------------
 */

    val audience:
    AnnouncementAudience =
        AnnouncementAudience.ALL_TENANTS,

    /*
     * Specific recipients.
     */

    val recipientTenantIds:
    List<String> =
        emptyList(),

    val recipientHouseIds:
    List<String> =
        emptyList(),

    val recipientFloorIds:
    List<String> =
        emptyList(),

    val recipientApartmentIds:
    List<String> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * ATTACHMENTS
     * ---------------------------------------------------------
 */

    val attachments:
    List<AnnouncementAttachment> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * ACTION
     * ---------------------------------------------------------
 *
 * Some announcements should contain a button.
 *
 * Example:
 *
 * "House G1 is now available."
 *
 * [ VIEW HOUSE ]
 *
 * ---------------------------------------------------------
 */

    val action:
    AnnouncementAction? = null,

    /*
     * ---------------------------------------------------------
     * SCHEDULING
     * ---------------------------------------------------------
 */

    val schedule:
    AnnouncementSchedule =
        AnnouncementSchedule(),

    /*
     * ---------------------------------------------------------
     * NOTIFICATION SETTINGS
     * ---------------------------------------------------------
 */

    val notification:
    AnnouncementNotificationSettings =
        AnnouncementNotificationSettings(),

    /*
     * ---------------------------------------------------------
     * READ / ACKNOWLEDGEMENT
     * ---------------------------------------------------------
 */

    val acknowledgement:
    AnnouncementAcknowledgement =
        AnnouncementAcknowledgement(),

    /*
     * ---------------------------------------------------------
     * EXPIRATION
     * ---------------------------------------------------------
 */

    val expiresAt: String? = null,

    /*
     * ---------------------------------------------------------
     * PINNING
     * ---------------------------------------------------------
 */

    val pinned: Boolean = false,

    /*
     * ---------------------------------------------------------
     * COMMENTS
     * ---------------------------------------------------------
 */

    val commentsEnabled: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val publishedAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isPublished: Boolean
        get() =
            status ==
                    AnnouncementStatus.PUBLISHED

    val isDraft: Boolean
        get() =
            status ==
                    AnnouncementStatus.DRAFT

    val isUrgent: Boolean
        get() =
            priority ==
                    AnnouncementPriority.URGENT

    val isEmergency: Boolean
        get() =
            type ==
                    AnnouncementType.EMERGENCY

    val hasAction: Boolean
        get() =
            action != null

    val hasAttachments: Boolean
        get() =
            attachments.isNotEmpty()
}


/*
 * =============================================================
 * ANNOUNCEMENT TYPE
 * =============================================================
 */

enum class AnnouncementType(

    val displayName: String

) {

    GENERAL(
        "General"
    ),

    RENT_REMINDER(
        "Rent Reminder"
    ),

    BILL_REMINDER(
        "Bill Reminder"
    ),

    RENT_UPDATE(
        "Rent Update"
    ),

    MAINTENANCE(
        "Maintenance"
    ),

    WATER_INTERRUPTION(
        "Water Interruption"
    ),

    ELECTRICITY_INTERRUPTION(
        "Electricity Interruption"
    ),

    INTERNET_INTERRUPTION(
        "Internet Interruption"
    ),

    SECURITY(
        "Security"
    ),

    EMERGENCY(
        "Emergency"
    ),

    MEETING(
        "Meeting"
    ),

    RULES(
        "House Rules"
    ),

    VACANT_HOUSE(
        "Vacant House"
    ),

    NEWLY_COMPLETED_HOUSE(
        "Newly Completed House"
    ),

    NEW_PROPERTY(
        "New Property"
    ),

    PROPERTY_UPDATE(
        "Property Update"
    ),

    INSPECTION(
        "Inspection"
    ),

    CLEANING(
        "Cleaning"
    ),

    COMMUNITY(
        "Community"
    ),

    EVENT(
        "Event"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * ANNOUNCEMENT PRIORITY
 * =============================================================
 */

enum class AnnouncementPriority(

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
 * ANNOUNCEMENT STATUS
 * =============================================================
 */

enum class AnnouncementStatus(

    val displayName: String

) {

    DRAFT(
        "Draft"
    ),

    SCHEDULED(
        "Scheduled"
    ),

    PUBLISHED(
        "Published"
    ),

    EXPIRED(
        "Expired"
    ),

    ARCHIVED(
        "Archived"
    ),

    CANCELLED(
        "Cancelled"
    )
}


/*
 * =============================================================
 * ANNOUNCEMENT AUTHOR ROLE
 * =============================================================
 */

enum class AnnouncementAuthorRole(

    val displayName: String

) {

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

    ADMIN(
        "Administrator"
    )
}


/*
 * =============================================================
 * ANNOUNCEMENT AUDIENCE
 * =============================================================
 */

enum class AnnouncementAudience(

    val displayName: String

) {

    ALL_TENANTS(
        "All Tenants"
    ),

    APARTMENT(
        "Entire Apartment"
    ),

    FLOOR(
        "Specific Floor"
    ),

    HOUSE(
        "Specific House"
    ),

    INDIVIDUAL_TENANTS(
        "Specific Tenants"
    ),

    PROSPECTIVE_TENANTS(
        "Prospective Tenants"
    ),

    LANDLORDS(
        "Landlords"
    ),

    STAFF(
        "Staff"
    ),

    EVERYONE(
        "Everyone"
    )
}


/*
 * =============================================================
 * ANNOUNCEMENT ATTACHMENT
 * =============================================================
 */

data class AnnouncementAttachment(

    val id: String = "",

    val name: String = "",

    val url: String = "",

    val type:
    AnnouncementAttachmentType =
        AnnouncementAttachmentType.DOCUMENT,

    val mimeType: String? = null,

    val sizeBytes: Long? = null
)


/*
 * =============================================================
 * ATTACHMENT TYPE
 * =============================================================
 */

enum class AnnouncementAttachmentType(

    val displayName: String

) {

    IMAGE(
        "Image"
    ),

    VIDEO(
        "Video"
    ),

    DOCUMENT(
        "Document"
    ),

    PDF(
        "PDF"
    ),

    AUDIO(
        "Audio"
    )
}


/*
 * =============================================================
 * ANNOUNCEMENT ACTION
 * =============================================================
 *
 * Provides an optional button inside the announcement.
 *
 * =============================================================
 */

data class AnnouncementAction(

    val label: String = "",

    val type:
    AnnouncementActionType =
        AnnouncementActionType.OPEN_SCREEN,

    val targetId: String? = null,

    val targetUrl: String? = null
)


/*
 * =============================================================
 * ACTION TYPE
 * =============================================================
 */

enum class AnnouncementActionType {

    OPEN_SCREEN,

    OPEN_HOUSE,

    OPEN_APARTMENT,

    OPEN_BILL,

    OPEN_PAYMENT,

    OPEN_MAINTENANCE,

    OPEN_VISIT,

    OPEN_AGREEMENT,

    OPEN_DOCUMENT,

    OPEN_MAP,

    OPEN_EXTERNAL_URL
}


/*
 * =============================================================
 * ANNOUNCEMENT SCHEDULE
 * =============================================================
 */

data class AnnouncementSchedule(

    val scheduled: Boolean = false,

    val publishAt: String? = null,

    val timezone: String = "Africa/Nairobi"
)


/*
 * =============================================================
 * NOTIFICATION SETTINGS
 * =============================================================
 */

data class AnnouncementNotificationSettings(

    val pushNotification: Boolean = true,

    val inAppNotification: Boolean = true,

    val smsNotification: Boolean = false,

    val emailNotification: Boolean = false,

    val soundEnabled: Boolean = true,

    val vibrationEnabled: Boolean = true
)


/*
 * =============================================================
 * ACKNOWLEDGEMENT
 * =============================================================
 *
 * Useful for important notices.
 *
 * Example:
 *
 * "All tenants must acknowledge the updated house rules."
 *
 * =============================================================
 */

data class AnnouncementAcknowledgement(

    val required: Boolean = false,

    val totalRecipients: Int = 0,

    val acknowledgedCount: Int = 0,

    val acknowledgedTenantIds:
    List<String> =
        emptyList()
) {

    val acknowledgementPercentage: Double
        get() {

            if (totalRecipients <= 0) {
                return 0.0
            }

            return (
                    acknowledgedCount.toDouble()
                            / totalRecipients.toDouble()
                    ) * 100.0
        }
}