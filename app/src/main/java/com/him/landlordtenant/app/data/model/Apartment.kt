package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * APARTMENT MODEL
 * =============================================================
 *
 * Represents an entire apartment/property managed by a landlord.
 *
 * Example:
 *
 * Green View Apartments
 * ├── Floor 1
 * │   ├── G1-01
 * │   ├── G1-02
 * │   └── G1-03
 * │
 * ├── Floor 2
 * │   ├── G2-01
 * │   ├── G2-02
 *   └── G2-03
 *
 * An Apartment contains multiple floors and houses.
 *
 * =============================================================
 */

data class Apartment(

    /*
     * ---------------------------------------------------------
     * BASIC IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val landlordId: String = "",

    val managerId: String? = null,

    val name: String = "",

    val description: String = "",

    /*
     * ---------------------------------------------------------
     * PROPERTY CLASSIFICATION
     * ---------------------------------------------------------
     */

    val propertyType:
    PropertyType = PropertyType.APARTMENT,

    val category:
    ApartmentCategory = ApartmentCategory.RESIDENTIAL,

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
     */

    val county: String = "",

    val town: String = "",

    val estate: String = "",

    val address: String = "",

    val landmark: String = "",

    val latitude: Double? = null,

    val longitude: Double? = null,

    val googleMapsPlaceId: String? = null,

    val googleMapsUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY SIZE
     * ---------------------------------------------------------
     */

    val numberOfFloors: Int = 0,

    val totalHouses: Int = 0,

    val occupiedHouses: Int = 0,

    val vacantHouses: Int = 0,

    val housesNotReady: Int = 0,

    /*
     * ---------------------------------------------------------
     * PROPERTY DESCRIPTION
     * ---------------------------------------------------------
     */

    val yearBuilt: Int? = null,

    val numberOfBuildings: Int = 1,

    val compoundSizeSquareMeters: Double? = null,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    val coverImageUrl: String? = null,

    val imageUrls: List<String> = emptyList(),

    val videoUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * AMENITIES
     * ---------------------------------------------------------
     */

    val amenities:
    List<ApartmentAmenity> = emptyList(),

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    val securityFeatures:
    List<SecurityFeature> = emptyList(),

    /*
     * ---------------------------------------------------------
     * UTILITIES
     * ---------------------------------------------------------
     */

    val utilities:
    ApartmentUtilities = ApartmentUtilities(),

    /*
     * ---------------------------------------------------------
     * HOUSE TYPES AVAILABLE
     * ---------------------------------------------------------
     */

    val availableHouseTypes:
    List<HouseType> = emptyList(),

    /*
     * ---------------------------------------------------------
     * RENT RANGE
     * ---------------------------------------------------------
     *
     * Useful for apartment search.
     *
     * Example:
     *
     * From KES 8,000
     * To   KES 35,000
     *
     * ---------------------------------------------------------
     */

    val minimumRent: Double = 0.0,

    val maximumRent: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * CONTACT INFORMATION
     * ---------------------------------------------------------
     */

    val contactPhone: String = "",

    val contactEmail: String = "",

    val whatsappNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY RULES
     * ---------------------------------------------------------
     */

    val rules:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TENANCY AGREEMENT
     * ---------------------------------------------------------
     *
     * Default agreement used when tenants join this apartment.
     *
     * The actual signed agreement should be stored separately
     * in Agreement.kt.
     * ---------------------------------------------------------
     */

    val defaultAgreementId: String? = null,

    val agreementRequired:
    Boolean = true,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    val verificationStatus:
    VerificationStatus = VerificationStatus.PENDING,

    val verifiedAt: String? = null,

    val verifiedBy: String? = null,

    /*
     * ---------------------------------------------------------
     * LISTING
     * ---------------------------------------------------------
     */

    val listingStatus:
    ApartmentListingStatus =
        ApartmentListingStatus.DRAFT,

    val isPublished:
    Boolean = false,

    val isFeatured:
    Boolean = false,

    /*
     * ---------------------------------------------------------
     * VIEW / DISCOVERY INFORMATION
     * ---------------------------------------------------------
     */

    val viewsCount: Long = 0,

    val inquiriesCount: Long = 0,

    val applicationsCount: Long = 0,

    /*
     * ---------------------------------------------------------
     * RATING
     * ---------------------------------------------------------
     */

    val rating: Double = 0.0,

    val reviewCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * MANAGEMENT
     * ---------------------------------------------------------
     */

    val caretakerName: String? = null,

    val caretakerPhone: String? = null,

    /*
     * ---------------------------------------------------------
     * CREATION / UPDATE
     * ---------------------------------------------------------
     */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val createdBy: String? = null,

    val updatedBy: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Occupancy percentage.
     *
     * Example:
     *
     * 30 occupied / 40 houses = 75%
     */

    val occupancyPercentage: Double
        get() {

            if (totalHouses <= 0) {
                return 0.0
            }

            return (
                    occupiedHouses.toDouble() /
                            totalHouses.toDouble()
                    ) * 100.0
        }


    /**
     * Vacancy percentage.
     */

    val vacancyPercentage: Double
        get() {

            if (totalHouses <= 0) {
                return 0.0
            }

            return (
                    vacantHouses.toDouble() /
                            totalHouses.toDouble()
                    ) * 100.0
        }


    /**
     * Whether tenants can currently discover this apartment.
     */

    val isAvailableForTenants: Boolean
        get() =

            listingStatus ==
                    ApartmentListingStatus.PUBLISHED &&

                    isPublished &&

                    verificationStatus ==
                    VerificationStatus.VERIFIED &&

                    vacantHouses > 0


    /**
     * Whether the apartment has a valid GPS location.
     */

    val hasLocation: Boolean
        get() =

            latitude != null &&
                    longitude != null


    /**
     * Whether the apartment has media.
     */

    val hasMedia: Boolean
        get() =

            imageUrls.isNotEmpty() ||
                    videoUrls.isNotEmpty()


    /**
     * Whether WhatsApp contact is available.
     */

    val hasWhatsApp: Boolean
        get() =

            !whatsappNumber.isNullOrBlank()


    /**
     * Human-readable rent range.
     */

    val rentRange: String
        get() {

            if (
                minimumRent <= 0.0 &&
                maximumRent <= 0.0
            ) {
                return "Rent not specified"
            }

            if (
                minimumRent > 0.0 &&
                maximumRent <= 0.0
            ) {
                return "From KES ${minimumRent.toInt()}"
            }

            if (
                minimumRent <= 0.0 &&
                maximumRent > 0.0
            ) {
                return "Up to KES ${maximumRent.toInt()}"
            }

            return "KES ${minimumRent.toInt()} - KES ${maximumRent.toInt()}"
        }
}


/*
 * =============================================================
 * PROPERTY TYPE
 * =============================================================
 */

enum class PropertyType(

    val displayName: String

) {

    APARTMENT(
        "Apartment"
    ),

    FLATS(
        "Flats"
    ),

    BEDSITTER_COMPLEX(
        "Bedsitter Complex"
    ),

    HOSTEL(
        "Hostel"
    ),

    STUDENT_HOUSING(
        "Student Housing"
    ),

    TOWNHOUSE(
        "Townhouse"
    ),

    RESIDENTIAL_COMPLEX(
        "Residential Complex"
    ),

    COMMERCIAL(
        "Commercial Property"
    ),

    MIXED_USE(
        "Mixed Use"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * APARTMENT CATEGORY
 * =============================================================
 */

enum class ApartmentCategory(

    val displayName: String

) {

    RESIDENTIAL(
        "Residential"
    ),

    COMMERCIAL(
        "Commercial"
    ),

    STUDENT(
        "Student Housing"
    ),

    FAMILY(
        "Family Housing"
    ),

    LUXURY(
        "Luxury"
    ),

    AFFORDABLE(
        "Affordable Housing"
    ),

    MIXED(
        "Mixed"
    )
}


/*
 * =============================================================
 * APARTMENT AMENITIES
 * =============================================================
 */

enum class ApartmentAmenity(

    val displayName: String

) {

    PARKING(
        "Parking"
    ),

    CCTV(
        "CCTV"
    ),

    SECURITY_GUARDS(
        "Security Guards"
    ),

    GATED_COMPOUND(
        "Gated Compound"
    ),

    ELECTRIC_FENCE(
        "Electric Fence"
    ),

    BOREHOLE(
        "Borehole"
    ),

    WATER_TANK(
        "Water Tank"
    ),

    BACKUP_GENERATOR(
        "Backup Generator"
    ),

    SOLAR_POWER(
        "Solar Power"
    ),

    ELEVATOR(
        "Elevator"
    ),

    GYM(
        "Gym"
    ),

    SWIMMING_POOL(
        "Swimming Pool"
    ),

    PLAYGROUND(
        "Playground"
    ),

    GARDEN(
        "Garden"
    ),

    LAUNDRY(
        "Laundry"
    ),

    WIFI(
        "Wi-Fi"
    ),

    INTERNET(
        "Internet"
    ),

    RESTAURANT(
        "Restaurant"
    ),

    SHOP(
        "Shop"
    ),

    COMMON_ROOM(
        "Common Room"
    ),

    ROOFTOP(
        "Rooftop"
    ),

    BALCONY(
        "Balcony"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * SECURITY FEATURES
 * =============================================================
 */

enum class SecurityFeature(

    val displayName: String

) {

    CCTV(
        "CCTV"
    ),

    SECURITY_GUARD(
        "Security Guard"
    ),

    ELECTRIC_FENCE(
        "Electric Fence"
    ),

    GATED_ENTRY(
        "Gated Entry"
    ),

    ACCESS_CARD(
        "Access Card"
    ),

    BIOMETRIC_ACCESS(
        "Biometric Access"
    ),

    INTERCOM(
        "Intercom"
    ),

    SECURITY_LIGHTING(
        "Security Lighting"
    ),

    ALARM_SYSTEM(
        "Alarm System"
    ),

    CONTROLLED_PARKING(
        "Controlled Parking"
    )
}


/*
 * =============================================================
 * APARTMENT UTILITIES
 * =============================================================
 */

data class ApartmentUtilities(

    val waterAvailable:
    Boolean = true,

    val electricityAvailable:
    Boolean = true,

    val sewageAvailable:
    Boolean = true,

    val garbageCollectionAvailable:
    Boolean = true,

    val internetAvailable:
    Boolean = false,

    val gasAvailable:
    Boolean = false,

    val boreholeAvailable:
    Boolean = false,

    val backupWater:
    Boolean = false,

    val backupElectricity:
    Boolean = false,

    val waterProvider:
    String? = null,

    val electricityProvider:
    String? = null
)


/*
 * =============================================================
 * APARTMENT LISTING STATUS
 * =============================================================
 */

enum class ApartmentListingStatus(

    val displayName: String

) {

    DRAFT(
        "Draft"
    ),

    PENDING_REVIEW(
        "Pending Review"
    ),

    PUBLISHED(
        "Published"
    ),

    PAUSED(
        "Paused"
    ),

    SUSPENDED(
        "Suspended"
    ),

    ARCHIVED(
        "Archived"
    )
}



/**
 * =============================================================
 * CONVERSATION MODEL
 * =============================================================
 *
 * A Conversation represents a chat between one or more users.
 *
 * Supported conversations:
 *
 * 1. Tenant <-> Landlord
 * 2. Tenant <-> Property Manager
 * 3. Tenant <-> Caretaker
 * 4. Tenant <-> Technician
 * 5. Apartment Group Chat
 * 6. Landlord Announcement
 * 7. Maintenance Conversation
 * 8. System Conversation
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
 * GROUP CHAT
 *
 * Apartment
 *    ↓
 * Apartment Group
 *    ↓
 * Tenants + Landlord + Managers
 *    ↓
 * Messages
 *
 * =============================================================
 */

data class Conversation(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val type:
    ConversationType =
        ConversationType.PRIVATE,

    /*
     * ---------------------------------------------------------
     * DISPLAY INFORMATION
     * ---------------------------------------------------------
     */

    val title: String = "",

    val description: String? = null,

    val imageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String? = null,

    val apartmentName: String? = null,

    val houseId: String? = null,

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PARTICIPANTS
     * ---------------------------------------------------------
     */

    val participants:
    List<ConversationParticipant> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * ADMINS
     * ---------------------------------------------------------
 */

    val adminIds:
    List<String> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * LAST MESSAGE
     * ---------------------------------------------------------
 */

    val lastMessage:
    LastMessage? = null,

    /*
     * ---------------------------------------------------------
     * UNREAD COUNTS
     * ---------------------------------------------------------
 */

    val unreadCount: Int = 0,

    val unreadMentionCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * CHAT SETTINGS
     * ---------------------------------------------------------
 */

    val settings:
    ConversationSettings =
        ConversationSettings(),

    /*
     * ---------------------------------------------------------
     * GROUP SETTINGS
     * ---------------------------------------------------------
 */

    val groupSettings:
    GroupConversationSettings? = null,

    /*
     * ---------------------------------------------------------
     * ANNOUNCEMENT SETTINGS
     * ---------------------------------------------------------
 */

    val announcementSettings:
    AnnouncementSettings? = null,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    ConversationStatus =
        ConversationStatus.ACTIVE,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastMessageAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether this is a private conversation.
     */

    val isPrivate: Boolean
        get() =
            type == ConversationType.PRIVATE


    /**
     * Whether this is an apartment group.
     */

    val isApartmentGroup: Boolean
        get() =
            type == ConversationType.APARTMENT_GROUP


    /**
     * Whether this is an announcement channel.
     */

    val isAnnouncement: Boolean
        get() =
            type == ConversationType.ANNOUNCEMENT


    /**
     * Whether the conversation has unread messages.
     */

    val hasUnreadMessages: Boolean
        get() =
            unreadCount > 0


    /**
     * Whether the conversation is muted.
     */

    val isMuted: Boolean
        get() =
            settings.muted


    /**
     * Whether the conversation is archived.
     */

    val isArchived: Boolean
        get() =
            settings.archived


    /**
     * Whether the current conversation is active.
     */

    val isActive: Boolean
        get() =
            status == ConversationStatus.ACTIVE
}


enum class ConversationType(

    val displayName: String

) {

    PRIVATE(
        "Private Chat"
    ),

    APARTMENT_GROUP(
        "Apartment Group"
    ),

    ANNOUNCEMENT(
        "Announcement"
    ),

    MAINTENANCE(
        "Maintenance Chat"
    ),

    SUPPORT(
        "Support"
    ),

    SYSTEM(
        "System"
    )
}


enum class ConversationStatus(

    val displayName: String

) {

    ACTIVE(
        "Active"
    ),

    ARCHIVED(
        "Archived"
    ),

    LOCKED(
        "Locked"
    ),

    CLOSED(
        "Closed"
    )
}


data class ConversationParticipant(

    val userId: String = "",

    val name: String = "",

    val profileImageUrl: String? = null,

    val role:
    MessageSenderRole =
        MessageSenderRole.TENANT,

    /*
     * Whether participant is currently active.
     */

    val active: Boolean = true,

    /*
     * Whether participant is an administrator.
     */

    val isAdmin: Boolean = false,

    /*
     * Whether participant can send messages.
     */

    val canSendMessages: Boolean = true,

    /*
     * Whether participant can add others.
     */

    val canAddParticipants: Boolean = false,

    /*
     * Whether participant has muted this chat.
     */

    val muted: Boolean = false,

    /*
     * Last time participant read the conversation.
     */

    val lastReadAt: String? = null,

    /*
     * Date participant joined.
     */

    val joinedAt: String? = null,

    /*
     * Date participant left.
     */

    val leftAt: String? = null
)


data class LastMessage(

    val messageId: String = "",

    val senderId: String = "",

    val senderName: String = "",

    val preview: String = "",

    val type:
    MessageType =
        MessageType.TEXT,

    val sentAt: String? = null,

    val isRead: Boolean = false
)


data class ConversationSettings(

    /*
     * Notifications
     */

    val notificationsEnabled: Boolean = true,

    /*
     * Mute notifications
     */

    val muted: Boolean = false,

    val mutedUntil: String? = null,

    /*
     * Archive
     */

    val archived: Boolean = false,

    /*
     * Pin to top
     */

    val pinned: Boolean = false,

    /*
     * Show previews in notifications
     */

    val showMessagePreview: Boolean = true,

    /*
     * Allow media automatically
     */

    val autoDownloadMedia: Boolean = true
)


data class GroupConversationSettings(

    /*
     * Group name can be changed.
     */

    val allowNameChanges: Boolean = false,

    /*
     * Group image can be changed.
     */

    val allowImageChanges: Boolean = false,

    /*
     * Any participant can add users.
     */

    val allowMemberInvites: Boolean = false,

    /*
     * Whether tenants can send messages.
     */

    val membersCanSendMessages: Boolean = true,

    /*
     * Whether only admins can send announcements.
     */

    val adminsOnlyAnnouncements: Boolean = true,

    /*
     * Whether new tenants automatically join.
     */

    val automaticMembership: Boolean = true,

    /*
     * Maximum number of members.
     */

    val maximumMembers: Int = 500
)


data class AnnouncementSettings(

    /*
     * Only landlord/manager/admin can publish.
     */

    val publishersOnly: Boolean = true,

    /*
     * Whether members can reply.
     */

    val allowReplies: Boolean = false,

    /*
     * Whether announcement sends push notification.
     */

    val pushNotification: Boolean = true,

    /*
     * Whether announcement sends SMS.
     */

    val sendSms: Boolean = false,

    /*
     * Whether announcement sends email.
     */

    val sendEmail: Boolean = false
)