package com.him.landlordtenant.app.data.dto.property

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * FLOOR DTO
 * =============================================================
 *
 * Remote/API representation of a floor inside an apartment.
 *
 * Apartment
 *    └── Floor
 *          └── Houses
 *
 * Houses are referenced by IDs and loaded separately when
 * detailed information is required.
 *
 * =============================================================
 */

@Serializable
data class FloorDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String = "",

    val referenceNumber: String = "",

    val name: String = "",

    /*
     * ---------------------------------------------------------
     * FLOOR INFORMATION
     * ---------------------------------------------------------
     */

    val floorNumber: Int = 0,

    val floorType: String = "RESIDENTIAL",

    val description: String? = null,

    /*
     * ---------------------------------------------------------
     * HOUSE REFERENCES
     * ---------------------------------------------------------
     */

    val houseIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * UNIT STATISTICS
     * ---------------------------------------------------------
 */

    val totalUnits: Int = 0,

    val occupiedUnits: Int = 0,

    val vacantUnits: Int = 0,

    val reservedUnits: Int = 0,

    val maintenanceUnits: Int = 0,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val imageIds: List<String> = emptyList(),

    val coverImageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * ACCESS
     * ---------------------------------------------------------
 */

    val hasElevatorAccess: Boolean = false,

    val hasStairAccess: Boolean = true,

    val hasEmergencyExit: Boolean = true,

    val accessRestricted: Boolean = false,

    /*
     * ---------------------------------------------------------
     * AMENITIES
     * ---------------------------------------------------------
 */

    val amenities: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
 */

    val underMaintenance: Boolean = false,

    val maintenanceNote: String? = null,

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

    val houseCount: Int
        get() = houseIds.size
}