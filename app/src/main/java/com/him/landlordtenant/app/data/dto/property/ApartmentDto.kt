package com.him.landlordtenant.app.data.dto.property

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * APARTMENT DTO
 * =============================================================
 *
 * Remote/API representation of an apartment/property complex.
 *
 * Apartment
 *    ├── Floors
 *    │     └── Houses
 *    │
 *    ├── Images
 *    ├── Amenities
 *    ├── Landlord
 *    └── Location
 *
 * Detailed floor/house information is referenced by IDs and
 * retrieved through their respective DTOs.
 *
 * =============================================================
 */

@Serializable
data class ApartmentDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    val name: String = "",

    val description: String? = null,

    /*
     * ---------------------------------------------------------
     * OWNER / MANAGEMENT
     * ---------------------------------------------------------
     */

    val landlordId: String = "",

    val propertyManagerId: String? = null,

    val caretakerId: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY TYPE
     * ---------------------------------------------------------
     */

    val propertyType: String = "APARTMENT",

    val category: String? = null,

    /*
     * ---------------------------------------------------------
     * ADDRESS
     * ---------------------------------------------------------
 */

    val county: String = "",

    val town: String = "",

    val area: String? = null,

    val estate: String? = null,

    val street: String? = null,

    val buildingNumber: String? = null,

    val postalAddress: String? = null,

    /*
     * ---------------------------------------------------------
     * GEOLOCATION
     * ---------------------------------------------------------
     */

    val latitude: Double? = null,

    val longitude: Double? = null,

    val googlePlaceId: String? = null,

    val googleMapsUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * STRUCTURE
     * ---------------------------------------------------------
 */

    val totalFloors: Int = 0,

    val totalUnits: Int = 0,

    val occupiedUnits: Int = 0,

    val vacantUnits: Int = 0,

    val reservedUnits: Int = 0,

    val maintenanceUnits: Int = 0,

    /*
     * ---------------------------------------------------------
     * FLOOR REFERENCES
     * ---------------------------------------------------------
 */

    val floorIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val coverImageUrl: String? = null,

    val imageIds: List<String> = emptyList(),

    val videoUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * AMENITIES
     * ---------------------------------------------------------
 */

    val amenities: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
 */

    val gated: Boolean = false,

    val securityGuard: Boolean = false,

    val cctvAvailable: Boolean = false,

    val accessControlAvailable: Boolean = false,

    /*
     * ---------------------------------------------------------
     * PARKING
     * ---------------------------------------------------------
 */

    val parkingAvailable: Boolean = false,

    val parkingSpaces: Int = 0,

    val visitorParkingAvailable: Boolean = false,

    /*
     * ---------------------------------------------------------
     * UTILITIES
     * ---------------------------------------------------------
 */

    val electricityAvailable: Boolean = true,

    val waterAvailable: Boolean = true,

    val internetAvailable: Boolean = false,

    val backupGeneratorAvailable: Boolean = false,

    val boreholeAvailable: Boolean = false,

    /*
     * ---------------------------------------------------------
     * LISTING
     * ---------------------------------------------------------
 */

    val listedForRent: Boolean = true,

    val listedForSale: Boolean = false,

    val acceptingApplications: Boolean = true,

    val featured: Boolean = false,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verified: Boolean = false,

    val verificationStatus: String = "PENDING",

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * STATISTICS
     * ---------------------------------------------------------
 */

    val averageRating: Double = 0.0,

    val reviewCount: Int = 0,

    val viewCount: Int = 0,

    val inquiryCount: Int = 0,

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

    val publishedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val hasVacancies: Boolean
        get() = vacantUnits > 0

    val occupancyRate: Double
        get() {
            if (totalUnits <= 0) return 0.0

            return (
                    occupiedUnits.toDouble() /
                            totalUnits.toDouble()
                    ) * 100.0
        }

    val hasLocation: Boolean
        get() =
            latitude != null &&
                    longitude != null

    val hasMedia: Boolean
        get() =
            !coverImageUrl.isNullOrBlank() ||
                    imageIds.isNotEmpty() ||
                    videoUrls.isNotEmpty()

    val hasSecurity: Boolean
        get() =
            gated ||
                    securityGuard ||
                    cctvAvailable ||
                    accessControlAvailable
}