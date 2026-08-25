package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * FLOOR MODEL
 * =============================================================
 *
 * Represents one floor inside an apartment/property.
 *
 * Example:
 *
 * Green View Apartments
 *      │
 *      ├── Floor 1
 *      │      ├── G1-01
 *      │      ├── G1-02
 *      │      └── G1-03
 *      │
 *      └── Floor 2
 *             ├── G2-01
 *             ├── G2-02
 *             └── G2-03
 *
 * =============================================================
 */

data class Floor(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String = "",

    /*
     * ---------------------------------------------------------
     * FLOOR INFORMATION
     * ---------------------------------------------------------
     */

    val floorNumber: Int = 0,

    val floorName: String = "",

    val description: String = "",

    /*
     * ---------------------------------------------------------
     * HOUSE INFORMATION
     * ---------------------------------------------------------
     */

    val houseIds: List<String> = emptyList(),

    val totalHouses: Int = 0,

    val occupiedHouses: Int = 0,

    val vacantHouses: Int = 0,

    val notReadyHouses: Int = 0,

    val reservedHouses: Int = 0,

    /*
     * ---------------------------------------------------------
     * FLOOR STATUS
     * ---------------------------------------------------------
     */

    val status: FloorStatus = FloorStatus.ACTIVE,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    val imageUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * FACILITIES
     * ---------------------------------------------------------
     */

    val facilities: List<FloorFacility> = emptyList(),

    /*
     * ---------------------------------------------------------
     * ACCESS INFORMATION
     * ---------------------------------------------------------
     */

    val hasLiftAccess: Boolean = false,

    val hasStairAccess: Boolean = true,

    val hasEmergencyExit: Boolean = false,

    /*
     * ---------------------------------------------------------
     * FLOOR LOCATION
     * ---------------------------------------------------------
     */

    val buildingName: String? = null,

    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
     */

    val isUnderMaintenance: Boolean = false,

    val maintenanceDescription: String? = null,

    /*
     * ---------------------------------------------------------
     * DISPLAY / VISIBILITY
     * ---------------------------------------------------------
     */

    val isVisibleToTenants: Boolean = true,

    /*
     * ---------------------------------------------------------
     * AUDIT INFORMATION
     * ---------------------------------------------------------
     */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val createdBy: String? = null,

    val updatedBy: String? = null

) {

    /*
     * =========================================================
     * COMPUTED INFORMATION
     * =========================================================
     */

    /**
     * Number of houses currently available.
     */

    val availableHouses: Int
        get() =
            vacantHouses


    /**
     * Occupancy percentage.
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
     * Whether the floor currently has available houses.
     */

    val hasAvailableHouses: Boolean
        get() =
            vacantHouses > 0


    /**
     * Whether tenants can see this floor.
     */

    val isAvailableToTenants: Boolean
        get() =
            status == FloorStatus.ACTIVE &&
                    isVisibleToTenants


    /**
     * Total houses that require attention.
     */

    val housesRequiringAttention: Int
        get() =
            notReadyHouses +
                    reservedHouses
}


/*
 * =============================================================
 * FLOOR STATUS
 * =============================================================
 */

enum class FloorStatus(

    val displayName: String

) {

    ACTIVE(
        "Active"
    ),

    INACTIVE(
        "Inactive"
    ),

    UNDER_CONSTRUCTION(
        "Under Construction"
    ),

    UNDER_MAINTENANCE(
        "Under Maintenance"
    ),

    CLOSED(
        "Closed"
    ),

    ARCHIVED(
        "Archived"
    )
}


/*
 * =============================================================
 * FLOOR FACILITIES
 * =============================================================
 */

enum class FloorFacility(

    val displayName: String

) {

    LIFT(
        "Lift"
    ),

    STAIRCASE(
        "Staircase"
    ),

    EMERGENCY_EXIT(
        "Emergency Exit"
    ),

    FIRE_EXIT(
        "Fire Exit"
    ),

    FIRE_EXTINGUISHER(
        "Fire Extinguisher"
    ),

    FIRE_ALARM(
        "Fire Alarm"
    ),

    CCTV(
        "CCTV"
    ),

    SECURITY_LIGHTING(
        "Security Lighting"
    ),

    COMMON_AREA(
        "Common Area"
    ),

    WATER_POINT(
        "Water Point"
    ),

    ELECTRICAL_PANEL(
        "Electrical Panel"
    ),

    LAUNDRY_AREA(
        "Laundry Area"
    ),

    STORAGE_AREA(
        "Storage Area"
    ),

    OTHER(
        "Other"
    )
}