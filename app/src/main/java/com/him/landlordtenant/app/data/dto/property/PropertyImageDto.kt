package com.him.landlordtenant.app.data.dto.property

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * PROPERTY IMAGE DTO
 * =============================================================
 *
 * Remote/API representation of an image or media asset
 * belonging to a property, apartment, floor or house.
 *
 * Supports:
 * - Property galleries
 * - House previews
 * - Cover images
 * - Verification photos
 * - Construction/completion photos
 * - Before/after maintenance photos
 *
 * =============================================================
 */

@Serializable
data class PropertyImageDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val floorId: String? = null,

    val houseId: String? = null,

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    val url: String = "",

    val thumbnailUrl: String? = null,

    val mediumUrl: String? = null,

    val highResolutionUrl: String? = null,

    val storagePath: String? = null,

    val fileName: String? = null,

    val mimeType: String = "image/jpeg",

    val fileSizeBytes: Long = 0L,

    /*
     * ---------------------------------------------------------
     * MEDIA TYPE
     * ---------------------------------------------------------
     */

    val mediaType: String = "IMAGE",

    val category: String = "GENERAL",

    /*
     * ---------------------------------------------------------
     * DESCRIPTION
     * ---------------------------------------------------------
     */

    val title: String? = null,

    val description: String? = null,

    val altText: String? = null,

    /*
     * ---------------------------------------------------------
     * IMAGE DIMENSIONS
     * ---------------------------------------------------------
     */

    val width: Int? = null,

    val height: Int? = null,

    val aspectRatio: Double? = null,

    /*
     * ---------------------------------------------------------
     * DISPLAY
     * ---------------------------------------------------------
 */

    val isCover: Boolean = false,

    val isFeatured: Boolean = false,

    val displayOrder: Int = 0,

    val visibleToPublic: Boolean = true,

    /*
     * ---------------------------------------------------------
     * UPLOAD INFORMATION
     * ---------------------------------------------------------
 */

    val uploadedBy: String? = null,

    val uploadedByRole: String? = null,

    val uploadedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verified: Boolean = false,

    val verificationStatus: String = "PENDING",

    val verifiedBy: String? = null,

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * MODERATION
     * ---------------------------------------------------------
 */

    val moderationStatus: String = "PENDING",

    val moderationReason: String? = null,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status: String = "ACTIVE",

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isImage: Boolean
        get() = mediaType.uppercase() == "IMAGE"

    val isVideo: Boolean
        get() = mediaType.uppercase() == "VIDEO"

    val isApproved: Boolean
        get() =
            verificationStatus.uppercase() == "VERIFIED" &&
                    moderationStatus.uppercase() == "APPROVED"

    val hasThumbnail: Boolean
        get() = !thumbnailUrl.isNullOrBlank()

    val hasHighResolutionVersion: Boolean
        get() = !highResolutionUrl.isNullOrBlank()

    val hasOwnerReference: Boolean
        get() =
            propertyId != null ||
                    apartmentId != null ||
                    floorId != null ||
                    houseId != null
}