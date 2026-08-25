package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * MAINTENANCE REQUEST MODEL
 * =============================================================
 *
 * Handles maintenance problems reported by:
 *
 * - Tenant
 * - Landlord
 * - Property manager
 * - Caretaker
 *
 * Examples:
 *
 * - Broken water pipe
 * - Electrical fault
 * - Leaking roof
 * - Broken door
 * - Painting
 * - Plumbing
 * - Blocked drainage
 * - Broken window
 * - Faulty socket
 * - Damaged tiles
 * - Garbage problem
 * - Security problem
 *
 * =============================================================
 *
 * MAINTENANCE FLOW
 *
 * TENANT
 *    ↓
 * Report Problem
 *    ↓
 * Add Description
 *    ↓
 * Add Photos / Videos
 *    ↓
 * Select Category
 *    ↓
 * Select Priority
 *    ↓
 * Submit
 *    ↓
 * LANDLORD / MANAGER
 *    ↓
 * Review
 *    ↓
 * Assign Technician
 *    ↓
 * Technician Accepts
 *    ↓
 * Work Begins
 *    ↓
 * Work Completed
 *    ↓
 * Tenant Confirms
 *    ↓
 * CLOSED
 *
 * =============================================================
 */

data class MaintenanceRequest(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val requestNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String = "",

    val floorId: String? = null,

    val houseId: String = "",

    val houseNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PEOPLE
     * ---------------------------------------------------------
     */

    val tenantId: String? = null,

    val tenantName: String? = null,

    val landlordId: String = "",

    val propertyManagerId: String? = null,

    /*
     * ---------------------------------------------------------
     * PROBLEM
     * ---------------------------------------------------------
     */

    val title: String = "",

    val description: String = "",

    val category:
    MaintenanceCategory =
        MaintenanceCategory.OTHER,

    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    val priority:
    MaintenancePriority =
        MaintenancePriority.MEDIUM,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    MaintenanceStatus =
        MaintenanceStatus.SUBMITTED,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val attachments:
    List<MaintenanceAttachment> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val location:
    MaintenanceLocation? = null,

    /*
     * ---------------------------------------------------------
     * TECHNICIAN
     * ---------------------------------------------------------
 */

    val assignedProfessionalId: String? = null,

    val assignedProfessionalName: String? = null,

    val assignedProfessionalPhone: String? = null,

    /*
     * ---------------------------------------------------------
     * ESTIMATED COST
     * ---------------------------------------------------------
 */

    val estimatedCost: Double? = null,

    val approvedBudget: Double? = null,

    val actualCost: Double? = null,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * SCHEDULE
     * ---------------------------------------------------------
 */

    val preferredVisitDate: String? = null,

    val scheduledVisitDate: String? = null,

    val scheduledVisitTime: String? = null,

    /*
     * ---------------------------------------------------------
     * TECHNICIAN NOTES
     * ---------------------------------------------------------
 */

    val technicianNotes: String? = null,

    val landlordNotes: String? = null,

    val tenantNotes: String? = null,

    /*
     * ---------------------------------------------------------
     * COMPLETION
     * ---------------------------------------------------------
 */

    val completionDescription: String? = null,

    val completionAttachments:
    List<MaintenanceAttachment> =
        emptyList(),

    val completedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANT CONFIRMATION
     * ---------------------------------------------------------
 */

    val tenantConfirmedCompletion:
    Boolean = false,

    val tenantConfirmedAt: String? = null,

    val tenantRating: Int? = null,

    val tenantFeedback: String? = null,

    /*
     * ---------------------------------------------------------
     * EMERGENCY
     * ---------------------------------------------------------
 */

    val isEmergency: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val closedAt: String? = null,

    val syncStatus: SyncStatus = SyncStatus.SYNCED
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether a technician has been assigned.
     */

    val hasAssignedProfessional: Boolean
        get() =
            !assignedProfessionalId.isNullOrBlank()


    /**
     * Whether the request is currently open.
     */

    val isOpen: Boolean
        get() =
            status != MaintenanceStatus.CLOSED &&
                    status != MaintenanceStatus.CANCELLED


    /**
     * Whether work has started.
     */

    val workHasStarted: Boolean
        get() =
            status == MaintenanceStatus.IN_PROGRESS


    /**
     * Whether work has been completed.
     */

    val isCompleted: Boolean
        get() =
            status == MaintenanceStatus.COMPLETED ||
                    status == MaintenanceStatus.AWAITING_CONFIRMATION ||
                    status == MaintenanceStatus.CLOSED


    /**
     * Whether tenant confirmation is required.
     */

    val requiresTenantConfirmation: Boolean
        get() =
            status ==
                    MaintenanceStatus.AWAITING_CONFIRMATION


    /**
     * Whether the request is urgent.
     */

    val isUrgent: Boolean
        get() =
            priority ==
                    MaintenancePriority.URGENT ||
                    isEmergency
}


/*
 * =============================================================
 * MAINTENANCE CATEGORY
 * =============================================================
 */

enum class MaintenanceCategory(

    val displayName: String

) {

    PLUMBING(
        "Plumbing"
    ),

    ELECTRICAL(
        "Electrical"
    ),

    PAINTING(
        "Painting"
    ),

    CARPENTRY(
        "Carpentry"
    ),

    ROOFING(
        "Roofing"
    ),

    MASONRY(
        "Masonry"
    ),

    WINDOWS(
        "Windows"
    ),

    DOORS(
        "Doors"
    ),

    DRAINAGE(
        "Drainage"
    ),

    WATER(
        "Water"
    ),

    ELECTRICITY(
        "Electricity"
    ),

    INTERNET(
        "Internet"
    ),

    SECURITY(
        "Security"
    ),

    GARBAGE(
        "Garbage"
    ),

    APPLIANCE(
        "Appliance"
    ),

    CLEANING(
        "Cleaning"
    ),

    PEST_CONTROL(
        "Pest Control"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * MAINTENANCE PRIORITY
 * =============================================================
 */

enum class MaintenancePriority(

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
    ),

    EMERGENCY(
        "Emergency"
    )
}


/*
 * =============================================================
 * MAINTENANCE STATUS
 * =============================================================
 */

enum class MaintenanceStatus(

    val displayName: String

) {

    DRAFT(
        "Draft"
    ),

    SUBMITTED(
        "Submitted"
    ),

    UNDER_REVIEW(
        "Under Review"
    ),

    APPROVED(
        "Approved"
    ),

    REJECTED(
        "Rejected"
    ),

    ASSIGNING_TECHNICIAN(
        "Assigning Technician"
    ),

    ASSIGNED(
        "Technician Assigned"
    ),

    TECHNICIAN_ACCEPTED(
        "Technician Accepted"
    ),

    SCHEDULED(
        "Scheduled"
    ),

    IN_PROGRESS(
        "In Progress"
    ),

    ON_HOLD(
        "On Hold"
    ),

    COMPLETED(
        "Completed"
    ),

    AWAITING_CONFIRMATION(
        "Awaiting Tenant Confirmation"
    ),

    CLOSED(
        "Closed"
    ),

    CANCELLED(
        "Cancelled"
    )
}


/*
 * =============================================================
 * MAINTENANCE ATTACHMENT
 * =============================================================
 *
 * Photos and videos uploaded by the tenant/landlord/technician.
 * =============================================================
 */

data class MaintenanceAttachment(

    val id: String = "",

    val url: String = "",

    val type:
    MaintenanceAttachmentType =
        MaintenanceAttachmentType.IMAGE,

    val fileName: String? = null,

    val description: String? = null,

    val uploadedBy: String? = null,

    val uploadedAt: String? = null
)


/*
 * =============================================================
 * ATTACHMENT TYPE
 * =============================================================
 */

enum class MaintenanceAttachmentType(

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
    )
}


/*
 * =============================================================
 * MAINTENANCE LOCATION
 * =============================================================
 *
 * Exact location of the reported problem.
 *
 * Useful for:
 *
 * - Apartment common areas
 * - Inside house
 * - Kitchen
 * - Bathroom
 * - Bedroom
 * - Balcony
 * - Parking
 * - Compound
 *
 * =============================================================
 */

data class MaintenanceLocation(

    val area:
    MaintenanceArea =
        MaintenanceArea.OTHER,

    val description: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null
)


/*
 * =============================================================
 * MAINTENANCE AREA
 * =============================================================
 */

enum class MaintenanceArea(

    val displayName: String

) {

    BEDROOM(
        "Bedroom"
    ),

    LIVING_ROOM(
        "Living Room"
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

    HALLWAY(
        "Hallway"
    ),

    COMPOUND(
        "Compound"
    ),

    PARKING(
        "Parking"
    ),

    ROOF(
        "Roof"
    ),

    STAIRCASE(
        "Staircase"
    ),

    COMMON_AREA(
        "Common Area"
    ),

    OTHER(
        "Other"
    )
}
