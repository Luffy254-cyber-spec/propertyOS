package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * PROPERTY MEDIA MODEL
 * =============================================================
 *
 * Represents photos and videos belonging to:
 *
 * - An apartment
 * - A floor
 * - A specific house
 * - A room
 * - A facility
 * - A maintenance/inspection record
 *
 * The same model can therefore be used throughout the app.
 *
 * =============================================================
 */

data class PropertyImage(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String? = null,

    val floorId: String? = null,

    val houseId: String? = null,

    /*
     * ---------------------------------------------------------
     * MEDIA INFORMATION
     * ---------------------------------------------------------
     */

    val type:
    PropertyMediaType =
        PropertyMediaType.IMAGE,

    val category:
    PropertyMediaCategory =
        PropertyMediaCategory.GENERAL,

    val title: String? = null,

    val description: String? = null,

    /*
     * ---------------------------------------------------------
     * FILE INFORMATION
     * ---------------------------------------------------------
     */

    val url: String = "",

    val thumbnailUrl: String? = null,

    val fileName: String? = null,

    val fileSizeBytes: Long? = null,

    val mimeType: String? = null,

    /*
     * ---------------------------------------------------------
     * IMAGE / VIDEO DIMENSIONS
     * ---------------------------------------------------------
     */

    val width: Int? = null,

    val height: Int? = null,

    val durationSeconds: Long? = null,

    /*
     * ---------------------------------------------------------
     * DISPLAY
     * ---------------------------------------------------------
     */

    val isCover: Boolean = false,

    val displayOrder: Int = 0,

    val visibleToTenants: Boolean = true,

    val visibleToGuests: Boolean = true,

    /*
     * ---------------------------------------------------------
     * UPLOAD INFORMATION
     * ---------------------------------------------------------
 */

    val uploadedBy: String? = null,

    val uploadedAt: String? = null,

    val uploadStatus:
    PropertyMediaUploadStatus =
        PropertyMediaUploadStatus.COMPLETED,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verification:
    PropertyMediaVerification =
        PropertyMediaVerification(),

    /*
     * ---------------------------------------------------------
     * PROPERTY CONDITION
     * ---------------------------------------------------------
 */

    val condition:
    PropertyMediaCondition? = null,

    /*
     * ---------------------------------------------------------
     * LOCATION WITHIN HOUSE
     * ---------------------------------------------------------
 */

    val room:
    PropertyRoomType? = null,

    /*
     * ---------------------------------------------------------
     * METADATA
     * ---------------------------------------------------------
 */

    val metadata:
    PropertyMediaMetadata =
        PropertyMediaMetadata(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isImage: Boolean
        get() =
            type ==
                    PropertyMediaType.IMAGE

    val isVideo: Boolean
        get() =
            type ==
                    PropertyMediaType.VIDEO

    val isUploaded: Boolean
        get() =
            uploadStatus ==
                    PropertyMediaUploadStatus.COMPLETED

    val isVerified: Boolean
        get() =
            verification.verified

    val isVisible: Boolean
        get() =
            visibleToTenants ||
                    visibleToGuests
}


/*
 * =============================================================
 * PROPERTY MEDIA TYPE
 * =============================================================
 */

enum class PropertyMediaType(

    val displayName: String

) {

    IMAGE(
        "Image"
    ),

    VIDEO(
        "Video"
    ),

    VIRTUAL_TOUR(
        "Virtual Tour"
    )
}


/*
 * =============================================================
 * PROPERTY MEDIA CATEGORY
 * =============================================================
 */

enum class PropertyMediaCategory(

    val displayName: String

) {

    GENERAL(
        "General"
    ),

    EXTERIOR(
        "Exterior"
    ),

    INTERIOR(
        "Interior"
    ),

    LIVING_ROOM(
        "Living Room"
    ),

    BEDROOM(
        "Bedroom"
    ),

    KITCHEN(
        "Kitchen"
    ),

    BATHROOM(
        "Bathroom"
    ),

    BALCONY(
        "Balcony"
    ),

    PARKING(
        "Parking"
    ),

    COMPOUND(
        "Compound"
    ),

    GARDEN(
        "Garden"
    ),

    SECURITY(
        "Security"
    ),

    AMENITY(
        "Amenity"
    ),

    VIEW(
        "View"
    ),

    FLOOR_PLAN(
        "Floor Plan"
    ),

    MAINTENANCE(
        "Maintenance"
    ),

    INSPECTION(
        "Inspection"
    ),

    BEFORE_REPAIR(
        "Before Repair"
    ),

    AFTER_REPAIR(
        "After Repair"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * UPLOAD STATUS
 * =============================================================
 */

enum class PropertyMediaUploadStatus(

    val displayName: String

) {

    QUEUED(
        "Queued"
    ),

    UPLOADING(
        "Uploading"
    ),

    PROCESSING(
        "Processing"
    ),

    COMPLETED(
        "Completed"
    ),

    FAILED(
        "Failed"
    ),

    CANCELLED(
        "Cancelled"
    )
}


/*
 * =============================================================
 * MEDIA VERIFICATION
 * =============================================================
 */

data class PropertyMediaVerification(

    val verified: Boolean = false,

    val status:
    PropertyMediaVerificationStatus =
        PropertyMediaVerificationStatus.PENDING,

    val verifiedBy: String? = null,

    val verifiedAt: String? = null,

    val note: String? = null
)


/*
 * =============================================================
 * VERIFICATION STATUS
 * =============================================================
 */

enum class PropertyMediaVerificationStatus(

    val displayName: String

) {

    PENDING(
        "Pending"
    ),

    VERIFIED(
        "Verified"
    ),

    REJECTED(
        "Rejected"
    ),

    FLAGGED(
        "Flagged"
    )
}


/*
 * =============================================================
 * PROPERTY MEDIA CONDITION
 * =============================================================
 *
 * Useful for house-condition images.
 *
 * Example:
 *
 * Before tenant moves in:
 *     GOOD
 *
 * After tenant leaves:
 *     NEEDS_PAINTING
 *
 * =============================================================
 */

enum class PropertyMediaCondition(

    val displayName: String

) {

    EXCELLENT(
        "Excellent"
    ),

    GOOD(
        "Good"
    ),

    FAIR(
        "Fair"
    ),

    NEEDS_CLEANING(
        "Needs Cleaning"
    ),

    NEEDS_PAINTING(
        "Needs Painting"
    ),

    NEEDS_REPAIR(
        "Needs Repair"
    ),

    DAMAGED(
        "Damaged"
    ),

    DESTROYED(
        "Destroyed"
    )
}


/*
 * =============================================================
 * PROPERTY ROOM TYPE
 * =============================================================
 */

enum class PropertyRoomType(

    val displayName: String

) {

    LIVING_ROOM(
        "Living Room"
    ),

    BEDROOM(
        "Bedroom"
    ),

    MASTER_BEDROOM(
        "Master Bedroom"
    ),

    KITCHEN(
        "Kitchen"
    ),

    BATHROOM(
        "Bathroom"
    ),

    TOILET(
        "Toilet"
    ),

    DINING_ROOM(
        "Dining Room"
    ),

    BALCONY(
        "Balcony"
    ),

    LAUNDRY(
        "Laundry"
    ),

    STORE(
        "Store"
    ),

    CORRIDOR(
        "Corridor"
    ),

    COMPOUND(
        "Compound"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * MEDIA METADATA
 * =============================================================
 */

data class PropertyMediaMetadata(

    /*
     * Device used to capture the image/video.
     */

    val deviceModel: String? = null,

    /*
     * Whether GPS information exists.
     */

    val locationCaptured: Boolean = false,

    val latitude: Double? = null,

    val longitude: Double? = null,

    /*
     * Whether the media has been edited.
     */

    val edited: Boolean = false,

    /*
     * Optional caption.
     */

    val caption: String? = null,

    /*
     * Optional alt text for accessibility.
     */

    val altText: String? = null
)