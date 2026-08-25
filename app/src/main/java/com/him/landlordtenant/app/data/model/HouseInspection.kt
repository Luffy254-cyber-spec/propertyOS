package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * HOUSE INSPECTION MODEL
 * =============================================================
 *
 * Used for:
 *
 * - Initial house inspection
 * - Tenant move-in inspection
 * - Routine inspections
 * - Maintenance inspections
 * - Tenant move-out inspection
 * - Final inspection
 *
 * The inspection determines the actual condition of a house.
 *
 * IMPORTANT:
 *
 * PropertyImage.kt stores the media.
 *
 * HouseInspection.kt determines what that media means.
 *
 * Example:
 *
 * PropertyImage
 *     -> bedroom-wall.jpg
 *
 * HouseInspection
 *     -> Bedroom
 *     -> Wall
 *     -> Condition = DAMAGED
 *     -> Repair required = true
 *
 * =============================================================
 */

data class HouseInspection(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String = "",

    val apartmentName: String? = null,

    val floorId: String? = null,

    val floorNumber: Int? = null,

    val houseId: String = "",

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANT
     * ---------------------------------------------------------
     *
     * Can be null for inspections before a tenant moves in.
     */

    val tenantId: String? = null,

    val tenantName: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
     */

    val landlordId: String = "",

    val landlordName: String? = null,

    /*
     * ---------------------------------------------------------
     * INSPECTION TYPE
     * ---------------------------------------------------------
     */

    val type:
    HouseInspectionType =
        HouseInspectionType.MOVE_IN,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    HouseInspectionStatus =
        HouseInspectionStatus.SCHEDULED,

    /*
     * ---------------------------------------------------------
     * INSPECTOR
     * ---------------------------------------------------------
 */

    val inspector:
    InspectionInspector =
        InspectionInspector(),

    /*
     * ---------------------------------------------------------
     * OVERALL CONDITION
     * ---------------------------------------------------------
 */

    val overallCondition:
    HouseCondition =
        HouseCondition.GOOD,

    /*
     * ---------------------------------------------------------
     * ROOM INSPECTIONS
     * ---------------------------------------------------------
 */

    val rooms:
    List<RoomInspection> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * INVENTORY
     * ---------------------------------------------------------
 */

    val inventory:
    List<InventoryItemInspection> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * DAMAGE REPORTS
     * ---------------------------------------------------------
 */

    val damages:
    List<DamageReport> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * REPAIR REQUIREMENTS
     * ---------------------------------------------------------
 */

    val repairs:
    List<InspectionRepair> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * UTILITIES
     * ---------------------------------------------------------
 */

    val utilities:
    UtilityInspection =
        UtilityInspection(),

    /*
     * ---------------------------------------------------------
     * CLEANLINESS
     * ---------------------------------------------------------
 */

    val cleanliness:
    CleanlinessInspection =
        CleanlinessInspection(),

    /*
     * ---------------------------------------------------------
     * SAFETY
     * ---------------------------------------------------------
 */

    val safety:
    SafetyInspection =
        SafetyInspection(),

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val media:
    List<InspectionMedia> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * SIGN-OFF
     * ---------------------------------------------------------
 */

    val signatures:
    InspectionSignatures =
        InspectionSignatures(),

    /*
     * ---------------------------------------------------------
     * FINAL RESULT
     * ---------------------------------------------------------
 */

    val result:
    InspectionResult =
        InspectionResult(),

    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
 */

    val notes: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val scheduledAt: String? = null,

    val startedAt: String? = null,

    val completedAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null,
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isCompleted: Boolean
        get() =
            status ==
                    HouseInspectionStatus.COMPLETED

    val hasDamage: Boolean
        get() =
            damages.isNotEmpty()

    val requiresRepairs: Boolean
        get() =
            repairs.any {
                it.required
            }

    val canBecomeVacant: Boolean
        get() =
            isCompleted &&
                    result.approvedForOccupancy

    val hasTenantSignature: Boolean
        get() =
            signatures.tenantSigned

    val hasLandlordSignature: Boolean
        get() =
            signatures.landlordSigned
}


/*
 * =============================================================
 * INSPECTION TYPE
 * =============================================================
 */

enum class HouseInspectionType(

    val displayName: String

) {

    INITIAL(
        "Initial Inspection"
    ),

    MOVE_IN(
        "Move-In Inspection"
    ),

    ROUTINE(
        "Routine Inspection"
    ),

    MAINTENANCE(
        "Maintenance Inspection"
    ),

    MOVE_OUT(
        "Move-Out Inspection"
    ),

    FINAL(
        "Final Inspection"
    )
}


/*
 * =============================================================
 * INSPECTION STATUS
 * =============================================================
 */

enum class HouseInspectionStatus(

    val displayName: String

) {

    SCHEDULED(
        "Scheduled"
    ),

    IN_PROGRESS(
        "In Progress"
    ),

    WAITING_FOR_TENANT(
        "Waiting for Tenant"
    ),

    WAITING_FOR_LANDLORD(
        "Waiting for Landlord"
    ),

    COMPLETED(
        "Completed"
    ),

    DISPUTED(
        "Disputed"
    ),

    CANCELLED(
        "Cancelled"
    )
}


/*
 * =============================================================
 * INSPECTOR
 * =============================================================
 */

data class InspectionInspector(

    val userId: String? = null,

    val name: String? = null,

    val role:
    InspectionInspectorRole =
        InspectionInspectorRole.LANDLORD,

    val phoneNumber: String? = null
)


/*
 * =============================================================
 * INSPECTOR ROLE
 * =============================================================
 */

enum class InspectionInspectorRole {

    LANDLORD,

    PROPERTY_MANAGER,

    CARETAKER,

    TENANT,

    TECHNICIAN,

    PROFESSIONAL,

    ADMIN
}


/*
 * =============================================================
 * ROOM INSPECTION
 * =============================================================
 */

data class RoomInspection(

    val id: String = "",

    val room:
    PropertyRoomType =
        PropertyRoomType.OTHER,

    val roomName: String? = null,

    val condition:
    HouseCondition =
        HouseCondition.GOOD,

    val walls:
    ConditionRating =
        ConditionRating.GOOD,

    val floor:
    ConditionRating =
        ConditionRating.GOOD,

    val ceiling:
    ConditionRating =
        ConditionRating.GOOD,

    val doors:
    ConditionRating =
        ConditionRating.GOOD,

    val windows:
    ConditionRating =
        ConditionRating.GOOD,

    val electrical:
    ConditionRating =
        ConditionRating.GOOD,

    val plumbing:
    ConditionRating =
        ConditionRating.GOOD,

    val cleanliness:
    ConditionRating =
        ConditionRating.GOOD,

    val notes: String? = null,

    val mediaIds:
    List<String> =
        emptyList()
)


/*
 * =============================================================
 * CONDITION RATING
 * =============================================================
 */

enum class ConditionRating(

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

    POOR(
        "Poor"
    ),

    DAMAGED(
        "Damaged"
    ),

    NOT_APPLICABLE(
        "Not Applicable"
    )
}


/*
 * =============================================================
 * INVENTORY ITEM INSPECTION
 * =============================================================
 *
 * Records items supplied with the house.
 *
 * Examples:
 *
 * - Curtains
 * - Cooker
 * - Water heater
 * - Light bulbs
 * - Keys
 * - Wardrobes
 * - Shelves
 *
 * =============================================================
 */

data class InventoryItemInspection(

    val id: String = "",

    val name: String = "",

    val category:
    InventoryCategory =
        InventoryCategory.OTHER,

    val expectedQuantity: Int = 0,

    val actualQuantity: Int = 0,

    val condition:
    ConditionRating =
        ConditionRating.GOOD,

    val missing: Boolean = false,

    val damaged: Boolean = false,

    val notes: String? = null,

    val mediaIds:
    List<String> =
        emptyList()
)


/*
 * =============================================================
 * INVENTORY CATEGORY
 * =============================================================
 */

enum class InventoryCategory {

    KEYS,

    LIGHTING,

    ELECTRICAL,

    PLUMBING,

    KITCHEN,

    BATHROOM,

    FURNITURE,

    CURTAINS,

    APPLIANCES,

    SECURITY,

    OTHER
}


/*
 * =============================================================
 * DAMAGE REPORT
 * =============================================================
 */

data class DamageReport(

    val id: String = "",

    val location:
    PropertyRoomType =
        PropertyRoomType.OTHER,

    val description: String = "",

    val severity:
    DamageSeverity =
        DamageSeverity.MINOR,

    val existedBeforeTenant: Boolean? = null,

    val causedByTenant: Boolean? = null,

    val estimatedRepairCost: Double = 0.0,

    val tenantLiable: Boolean = false,

    val resolved: Boolean = false,

    val resolutionNotes: String? = null,

    val mediaIds:
    List<String> =
        emptyList()
)


/*
 * =============================================================
 * DAMAGE SEVERITY
 * =============================================================
 */

enum class DamageSeverity(

    val displayName: String

) {

    MINOR(
        "Minor"
    ),

    MODERATE(
        "Moderate"
    ),

    MAJOR(
        "Major"
    ),

    CRITICAL(
        "Critical"
    )
}


/*
 * =============================================================
 * INSPECTION REPAIR
 * =============================================================
 */

data class InspectionRepair(

    val id: String = "",

    val description: String = "",

    val category:
    RepairCategory =
        RepairCategory.OTHER,

    val required: Boolean = true,

    val priority:
    RepairPriority =
        RepairPriority.MEDIUM,

    val estimatedCost: Double = 0.0,

    val assignedProfessionalId: String? = null,

    val assignedProfessionalName: String? = null,

    val completed: Boolean = false,

    val completedAt: String? = null,

    val notes: String? = null
)


/*
 * =============================================================
 * REPAIR CATEGORY
 * =============================================================
 */

enum class RepairCategory {

    ELECTRICAL,

    PLUMBING,

    PAINTING,

    CARPENTRY,

    MASONRY,

    ROOFING,

    SECURITY,

    CLEANING,

    APPLIANCE,

    WINDOWS,

    DOORS,

    OTHER
}


/*
 * =============================================================
 * REPAIR PRIORITY
 * =============================================================
 */

enum class RepairPriority(

    val displayName: String

) {

    LOW(
        "Low"
    ),

    MEDIUM(
        "Medium"
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
 * UTILITY INSPECTION
 * =============================================================
 */

data class UtilityInspection(

    val electricityWorking: Boolean = true,

    val waterWorking: Boolean = true,

    val internetAvailable: Boolean = false,

    val gasAvailable: Boolean = false,

    val meterReading:
    UtilityMeterReading? = null,

    val notes: String? = null
)


/*
 * =============================================================
 * UTILITY METER READING
 * =============================================================
 */

data class UtilityMeterReading(

    val electricityReading: Double? = null,

    val waterReading: Double? = null,

    val gasReading: Double? = null,

    val readingDate: String? = null
)


/*
 * =============================================================
 * CLEANLINESS INSPECTION
 * =============================================================
 */

data class CleanlinessInspection(

    val overall:
    CleanlinessRating =
        CleanlinessRating.CLEAN,

    val floorsClean: Boolean = true,

    val wallsClean: Boolean = true,

    val windowsClean: Boolean = true,

    val kitchenClean: Boolean = true,

    val bathroomClean: Boolean = true,

    val compoundClean: Boolean = true,

    val requiresCleaning: Boolean = false,

    val notes: String? = null
)


/*
 * =============================================================
 * CLEANLINESS RATING
 * =============================================================
 */

enum class CleanlinessRating {

    VERY_CLEAN,

    CLEAN,

    FAIR,

    DIRTY,

    VERY_DIRTY
}


/*
 * =============================================================
 * SAFETY INSPECTION
 * =============================================================
 */

data class SafetyInspection(

    val doorsSecure: Boolean = true,

    val windowsSecure: Boolean = true,

    val locksWorking: Boolean = true,

    val electricalSafe: Boolean = true,

    val plumbingSafe: Boolean = true,

    val fireSafetyAvailable: Boolean = false,

    val emergencyExitAvailable: Boolean = false,

    val securityConcerns: Boolean = false,

    val notes: String? = null
)


/*
 * =============================================================
 * INSPECTION MEDIA
 * =============================================================
 */

data class InspectionMedia(

    val mediaId: String = "",

    val category:
    InspectionMediaCategory =
        InspectionMediaCategory.GENERAL,

    val caption: String? = null,

    val capturedAt: String? = null
)


/*
 * =============================================================
 * INSPECTION MEDIA CATEGORY
 * =============================================================
 */

enum class InspectionMediaCategory {

    GENERAL,

    DAMAGE,

    ROOM,

    INVENTORY,

    METER,

    ELECTRICITY,

    WATER,

    SECURITY,

    BEFORE_REPAIR,

    AFTER_REPAIR
}


/*
 * =============================================================
 * INSPECTION SIGNATURES
 * =============================================================
 */

data class InspectionSignatures(

    val tenantSigned: Boolean = false,

    val tenantSignature:
    String? = null,

    val tenantSignedAt:
    String? = null,

    val landlordSigned: Boolean = false,

    val landlordSignature:
    String? = null,

    val landlordSignedAt:
    String? = null,

    val inspectorSigned: Boolean = false,

    val inspectorSignature:
    String? = null,

    val inspectorSignedAt:
    String? = null
)


/*
 * =============================================================
 * INSPECTION RESULT
 * =============================================================
 */

data class InspectionResult(

    /*
     * Is the house suitable for occupation?
     */

    val approvedForOccupancy: Boolean = true,

    /*
     * Is the house ready for a new tenant?
     */

    val readyForNewTenant: Boolean = false,

    /*
     * Is cleaning required?
     */

    val cleaningRequired: Boolean = false,

    /*
     * Is painting required?
     */

    val paintingRequired: Boolean = false,

    /*
     * Are repairs required?
     */

    val repairsRequired: Boolean = false,

    /*
     * Is the house currently unavailable?
     */

    val houseUnavailable: Boolean = false,

    /*
     * Recommended next state.
     */

    val recommendedHouseStatus:
    RecommendedHouseStatus =
        RecommendedHouseStatus.NOT_READY,

    val finalNotes: String? = null
)


/*
 * =============================================================
 * RECOMMENDED HOUSE STATUS
 * =============================================================
 */

enum class RecommendedHouseStatus(

    val displayName: String

) {

    OCCUPIED(
        "Occupied"
    ),

    NOT_READY(
        "Not Ready"
    ),

    READY(
        "Ready"
    ),

    VACANT(
        "Vacant"
    ),

    UNDER_REPAIR(
        "Under Repair"
    ),

    UNDER_CLEANING(
        "Under Cleaning"
    ),

    MAINTENANCE_REQUIRED(
        "Maintenance Required"
    )
}
