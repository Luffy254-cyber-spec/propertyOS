package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * PROPERTY VISIT MODEL
 * =============================================================
 *
 * Handles property viewing appointments.
 *
 * Typical flow:
 *
 * Tenant
 *   ↓
 * Finds Apartment
 *   ↓
 * Views House
 *   ↓
 * Request Viewing
 *   ↓
 * Select Date & Time
 *   ↓
 * Landlord receives notification
 *   ↓
 * Landlord approves / rejects
 *   ↓
 * Tenant receives confirmation
 *   ↓
 * Tenant gets directions
 *   ↓
 * Visit takes place
 *   ↓
 * COMPLETED
 *
 * =============================================================
 */

data class PropertyVisit(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * VISITOR
     * ---------------------------------------------------------
     */

    val visitorId: String = "",

    val visitorName: String = "",

    val visitorPhone: String? = null,

    val visitorEmail: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String = "",

    val apartmentName: String? = null,

    val floorId: String? = null,

    val floorNumber: Int? = null,

    val houseId: String? = null,

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
     */

    val landlordId: String = "",

    val landlordName: String? = null,

    val landlordPhone: String? = null,

    /*
     * ---------------------------------------------------------
     * BROKER / AGENT
     * ---------------------------------------------------------
     */

    val agentId: String? = null,

    val agentName: String? = null,

    val agentPhone: String? = null,

    /*
     * ---------------------------------------------------------
     * VISIT TYPE
     * ---------------------------------------------------------
     */

    val type:
    PropertyVisitType =
        PropertyVisitType.HOUSE_VIEWING,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    PropertyVisitStatus =
        PropertyVisitStatus.REQUESTED,

    /*
     * ---------------------------------------------------------
     * SCHEDULE
     * ---------------------------------------------------------
 */

    val schedule:
    VisitSchedule =
        VisitSchedule(),

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val location:
    VisitLocation? = null,

    /*
     * ---------------------------------------------------------
     * DIRECTIONS
     * ---------------------------------------------------------
 */

    val directions:
    VisitDirections? = null,

    /*
     * ---------------------------------------------------------
     * VISITOR INFORMATION
     * ---------------------------------------------------------
 */

    val visitorDetails:
    VisitVisitorDetails =
        VisitVisitorDetails(),

    /*
     * ---------------------------------------------------------
     * PROPERTY INFORMATION
     * ---------------------------------------------------------
 */

    val propertySnapshot:
    VisitPropertySnapshot? = null,

    /*
     * ---------------------------------------------------------
     * APPROVAL
     * ---------------------------------------------------------
 */

    val approval:
    VisitApproval =
        VisitApproval(),

    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
 */

    val reminders:
    VisitReminderSettings =
        VisitReminderSettings(),

    /*
     * ---------------------------------------------------------
     * CANCELLATION
     * ---------------------------------------------------------
 */

    val cancellation:
    VisitCancellation? = null,

    /*
     * ---------------------------------------------------------
     * COMPLETION
     * ---------------------------------------------------------
 */

    val completion:
    VisitCompletion? = null,

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

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isPending: Boolean
        get() =
            status ==
                    PropertyVisitStatus.REQUESTED ||
                    status ==
                    PropertyVisitStatus.PENDING_APPROVAL

    val isApproved: Boolean
        get() =
            status ==
                    PropertyVisitStatus.APPROVED

    val isCancelled: Boolean
        get() =
            status ==
                    PropertyVisitStatus.CANCELLED

    val isCompleted: Boolean
        get() =
            status ==
                    PropertyVisitStatus.COMPLETED

    val canBeCancelled: Boolean
        get() =
            status ==
                    PropertyVisitStatus.REQUESTED ||
                    status ==
                    PropertyVisitStatus.PENDING_APPROVAL ||
                    status ==
                    PropertyVisitStatus.APPROVED
}


/*
 * =============================================================
 * PROPERTY VISIT TYPE
 * =============================================================
 */

enum class PropertyVisitType(

    val displayName: String

) {

    HOUSE_VIEWING(
        "House Viewing"
    ),

    APARTMENT_VIEWING(
        "Apartment Viewing"
    ),

    FOLLOW_UP_VIEWING(
        "Follow-up Viewing"
    ),

    INSPECTION(
        "Property Inspection"
    ),

    MAINTENANCE_VISIT(
        "Maintenance Visit"
    )
}


/*
 * =============================================================
 * PROPERTY VISIT STATUS
 * =============================================================
 */

enum class PropertyVisitStatus(

    val displayName: String

) {

    REQUESTED(
        "Requested"
    ),

    PENDING_APPROVAL(
        "Pending Approval"
    ),

    APPROVED(
        "Approved"
    ),

    ARRIVED(
        "Visitor Arrived"
    ),

    IN_PROGRESS(
        "Visit In Progress"
    ),

    COMPLETED(
        "Completed"
    ),

    CANCELLED(
        "Cancelled"
    ),

    REJECTED(
        "Rejected"
    ),

    NO_SHOW(
        "No Show"
    ),

    RESCHEDULED(
        "Rescheduled"
    )
}


/*
 * =============================================================
 * VISIT SCHEDULE
 * =============================================================
 */

data class VisitSchedule(

    val date: String? = null,

    val startTime: String? = null,

    val endTime: String? = null,

    val timezone: String = "Africa/Nairobi",

    val durationMinutes: Int = 30,

    val flexibleTime: Boolean = false
)


/*
 * =============================================================
 * VISIT LOCATION
 * =============================================================
 */

data class VisitLocation(

    val latitude: Double? = null,

    val longitude: Double? = null,

    val address: String? = null,

    val county: String? = null,

    val town: String? = null,

    val estate: String? = null,

    val buildingName: String? = null,

    val googleMapsUrl: String? = null
)


/*
 * =============================================================
 * VISIT DIRECTIONS
 * =============================================================
 */

data class VisitDirections(

    val destinationLatitude: Double? = null,

    val destinationLongitude: Double? = null,

    val googleMapsUrl: String? = null,

    val estimatedDistanceKm: Double? = null,

    val estimatedTravelMinutes: Int? = null,

    val instructions: String? = null
)


/*
 * =============================================================
 * VISITOR DETAILS
 * =============================================================
 */

data class VisitVisitorDetails(

    val numberOfVisitors: Int = 1,

    val additionalVisitors:
    List<AdditionalVisitor> =
        emptyList(),

    val identificationRequired: Boolean = false,

    val identificationVerified: Boolean = false,

    val specialRequirements: String? = null
)


/*
 * =============================================================
 * ADDITIONAL VISITOR
 * =============================================================
 */

data class AdditionalVisitor(

    val name: String = "",

    val phoneNumber: String? = null,

    val relationship: String? = null
)


/*
 * =============================================================
 * PROPERTY SNAPSHOT
 * =============================================================
 *
 * Stores the property information as it existed when the
 * viewing was requested.
 *
 * This is useful because rent/availability/images can change
 * after a viewing is booked.
 *
 * =============================================================
 */

data class VisitPropertySnapshot(

    val houseType: String? = null,

    val bedrooms: Int? = null,

    val bathrooms: Int? = null,

    val monthlyRent: Double? = null,

    val deposit: Double? = null,

    val availabilityStatus:
    String? = null,

    val imageUrls:
    List<String> =
        emptyList()
)


/*
 * =============================================================
 * VISIT APPROVAL
 * =============================================================
 */

data class VisitApproval(

    val approved: Boolean = false,

    val approvedBy: String? = null,

    val approvedAt: String? = null,

    val rejectionReason: String? = null,

    val landlordNote: String? = null
)


/*
 * =============================================================
 * VISIT REMINDER SETTINGS
 * =============================================================
 */

data class VisitReminderSettings(

    val enabled: Boolean = true,

    val remind24HoursBefore: Boolean = true,

    val remind2HoursBefore: Boolean = true,

    val remind30MinutesBefore: Boolean = true,

    val notificationSent24Hours: Boolean = false,

    val notificationSent2Hours: Boolean = false,

    val notificationSent30Minutes: Boolean = false
)


/*
 * =============================================================
 * VISIT CANCELLATION
 * =============================================================
 */

data class VisitCancellation(

    val cancelledBy: String? = null,

    val cancelledAt: String? = null,

    val reason:
    VisitCancellationReason? = null,

    val note: String? = null
)


/*
 * =============================================================
 * CANCELLATION REASON
 * =============================================================
 */

enum class VisitCancellationReason(

    val displayName: String

) {

    CHANGE_OF_PLANS(
        "Change of Plans"
    ),

    PROPERTY_NO_LONGER_AVAILABLE(
        "Property No Longer Available"
    ),

    FOUND_ANOTHER_HOUSE(
        "Found Another House"
    ),

    LANDLORD_UNAVAILABLE(
        "Landlord Unavailable"
    ),

    TENANT_UNAVAILABLE(
        "Tenant Unavailable"
    ),

    EMERGENCY(
        "Emergency"
    ),

    WEATHER(
        "Weather"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * VISIT COMPLETION
 * =============================================================
 */

data class VisitCompletion(

    val completedAt: String? = null,

    val completedBy: String? = null,

    val visitorArrived: Boolean = false,

    val visitorLeft: Boolean = false,

    val visitorRating:
    Int? = null,

    val visitorFeedback: String? = null,

    val landlordRating:
    Int? = null,

    val landlordFeedback: String? = null,

    val interestedInProperty: Boolean? = null,

    val followUpRequired: Boolean = false,

    val followUpNotes: String? = null
)