package com.him.landlordtenant.app.data.dto.property

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * HOUSE DTO
 * =============================================================
 *
 * Remote/API representation of an individual rentable unit.
 *
 * Apartment
 *    └── Floor
 *          └── House
 *
 * A House represents the actual unit a tenant can:
 * - View
 * - Apply for
 * - Reserve
 * - Rent
 * - Move into
 *
 * =============================================================
 */

@Serializable
data class HouseDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String = "",

    val floorId: String = "",

    val referenceNumber: String = "",

    val houseNumber: String = "",

    val name: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY INFORMATION
     * ---------------------------------------------------------
     */

    val houseType: String = "ONE_BEDROOM",

    val description: String? = null,

    val floorNumber: Int = 0,

    /*
     * ---------------------------------------------------------
     * SIZE
     * ---------------------------------------------------------
     */

    val sizeSquareMeters: Double? = null,

    val bedroomCount: Int = 1,

    val bathroomCount: Int = 1,

    val kitchenCount: Int = 1,

    val livingRoomCount: Int = 1,

    /*
     * ---------------------------------------------------------
     * RENT
     * ---------------------------------------------------------
 */

    val monthlyRent: Double = 0.0,

    val serviceCharge: Double = 0.0,

    val currency: String = "KES",

    val depositAmount: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * AVAILABILITY
     * ---------------------------------------------------------
 */

    val availabilityStatus: String = "AVAILABLE",

    val availableFrom: String? = null,

    val occupied: Boolean = false,

    val tenantId: String? = null,

    val agreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * APPLICATION / RESERVATION
     * ---------------------------------------------------------
 */

    val applicationAllowed: Boolean = true,

    val reservationAllowed: Boolean = true,

    val reservedByTenantId: String? = null,

    val reservationExpiresAt: String? = null,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val coverImageUrl: String? = null,

    val imageIds: List<String> = emptyList(),

    val videoUrls: List<String> = emptyList(),

    val virtualTourUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * FEATURES
     * ---------------------------------------------------------
 */

    val features: List<String> = emptyList(),

    val amenities: List<String> = emptyList(),

    val furnished: Boolean = false,

    val balcony: Boolean = false,

    val parkingIncluded: Boolean = false,

    val parkingSpaceNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * UTILITIES
     * ---------------------------------------------------------
 */

    val electricityMeterNumber: String? = null,

    val waterMeterNumber: String? = null,

    val electricityPrepaid: Boolean = false,

    val waterMetered: Boolean = true,

    val internetAvailable: Boolean = false,

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
 */

    val securityDepositRequired: Boolean = true,

    val securityFeatures: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * VIEWING
     * ---------------------------------------------------------
 */

    val viewingAvailable: Boolean = true,

    val viewingInstructions: String? = null,

    val viewingContactId: String? = null,

    /*
     * ---------------------------------------------------------
     * LISTING
     * ---------------------------------------------------------
 */

    val listedForRent: Boolean = true,

    val featured: Boolean = false,

    val verified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * STATISTICS
     * ---------------------------------------------------------
 */

    val viewCount: Int = 0,

    val inquiryCount: Int = 0,

    val applicationCount: Int = 0,

    val averageRating: Double = 0.0,

    val reviewCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val latitude: Double? = null,

    val longitude: Double? = null,

    val googleMapsUrl: String? = null,

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

    val updatedAt: String? = null,

    val publishedAt: String? = null,

    val lastOccupiedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
     */

    val totalMonthlyCost: Double
        get() =
            monthlyRent + serviceCharge

    val isAvailable: Boolean
        get() =
            availabilityStatus == "AVAILABLE" &&
                    !occupied

    val isReserved: Boolean
        get() =
            availabilityStatus == "RESERVED" ||
                    reservedByTenantId != null

    val hasMedia: Boolean
        get() =
            !coverImageUrl.isNullOrBlank() ||
                    imageIds.isNotEmpty() ||
                    videoUrls.isNotEmpty()

    val hasVirtualTour: Boolean
        get() =
            !virtualTourUrl.isNullOrBlank()

    val hasLocation: Boolean
        get() =
            latitude != null &&
                    longitude != null
}