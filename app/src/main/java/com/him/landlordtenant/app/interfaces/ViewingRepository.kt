package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * VIEWING REPOSITORY
 * =============================================================
 *
 * Handles property viewing appointments.
 *
 * Supports:
 * - Viewing requests
 * - Landlord approval
 * - Broker involvement
 * - Date/time scheduling
 * - Multiple attendees
 * - Property directions
 * - Map locations
 * - Viewing reminders
 * - Cancellations
 * - Rescheduling
 * - Check-in
 * - Check-out
 * - Viewing feedback
 * - Viewing history
 * - No-show tracking
 *
 * =============================================================
 */

interface ViewingRepository {

    /*
     * ---------------------------------------------------------
     * VIEWING REQUESTS
     * ---------------------------------------------------------
     */

    suspend fun createViewingRequest(
        requesterId: String,
        request: CreateViewingRequestData
    ): Result<String>

    suspend fun getViewing(
        viewingId: String
    ): Result<ViewingDetailsData>

    fun observeViewing(
        viewingId: String
    ): Flow<Result<ViewingDetailsData>>

    suspend fun updateViewingRequest(
        requesterId: String,
        viewingId: String,
        request: CreateViewingRequestData
    ): Result<Unit>

    suspend fun cancelViewing(
        userId: String,
        viewingId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TENANT / APPLICANT VIEWINGS
     * ---------------------------------------------------------
     */

    suspend fun getUserViewings(
        userId: String
    ): Result<List<ViewingSummaryData>>

    fun observeUserViewings(
        userId: String
    ): Flow<Result<List<ViewingSummaryData>>>

    suspend fun getUpcomingViewings(
        userId: String
    ): Result<List<ViewingSummaryData>>

    suspend fun getPastViewings(
        userId: String
    ): Result<List<ViewingSummaryData>>


    /*
     * ---------------------------------------------------------
     * LANDLORD VIEWINGS
     * ---------------------------------------------------------
     */

    suspend fun getLandlordViewings(
        landlordId: String
    ): Result<List<ViewingSummaryData>>

    suspend fun getPendingViewingRequests(
        landlordId: String
    ): Result<List<ViewingSummaryData>>

    suspend fun getPropertyViewings(
        propertyId: String
    ): Result<List<ViewingSummaryData>>

    suspend fun getUnitViewings(
        unitId: String
    ): Result<List<ViewingSummaryData>>


    /*
     * ---------------------------------------------------------
     * APPROVAL
     * ---------------------------------------------------------
     */

    suspend fun approveViewing(
        landlordId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun rejectViewing(
        landlordId: String,
        viewingId: String,
        reason: String?
    ): Result<Unit>

    suspend fun confirmViewing(
        userId: String,
        viewingId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SCHEDULING
     * ---------------------------------------------------------
     */

    suspend fun getAvailableSlots(
        propertyId: String,
        date: String
    ): Result<List<ViewingTimeSlotData>>

    suspend fun scheduleViewing(
        userId: String,
        viewingId: String,
        schedule: ViewingScheduleData
    ): Result<Unit>

    suspend fun rescheduleViewing(
        userId: String,
        viewingId: String,
        schedule: ViewingScheduleData
    ): Result<Unit>

    suspend fun getViewingSchedule(
        viewingId: String
    ): Result<ViewingScheduleData?>


    /*
     * ---------------------------------------------------------
     * MULTIPLE ATTENDEES
     * ---------------------------------------------------------
     */

    suspend fun addAttendee(
        userId: String,
        viewingId: String,
        attendee: ViewingAttendeeData
    ): Result<String>

    suspend fun removeAttendee(
        userId: String,
        viewingId: String,
        attendeeId: String
    ): Result<Unit>

    suspend fun getAttendees(
        viewingId: String
    ): Result<List<ViewingAttendeeData>>


    /*
     * ---------------------------------------------------------
     * BROKER / AGENT
     * ---------------------------------------------------------
     */

    suspend fun assignBroker(
        landlordId: String,
        viewingId: String,
        brokerId: String
    ): Result<Unit>

    suspend fun removeBroker(
        landlordId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun getAssignedBroker(
        viewingId: String
    ): Result<ProfessionalSummaryData?>


    /*
     * ---------------------------------------------------------
     * LOCATION & DIRECTIONS
     * ---------------------------------------------------------
     */

    suspend fun getPropertyLocation(
        viewingId: String
    ): Result<ViewingLocationData>

    suspend fun getDirections(
        viewingId: String,
        fromLatitude: Double,
        fromLongitude: Double
    ): Result<ViewingDirectionsData>

    suspend fun shareLocation(
        userId: String,
        viewingId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * CHECK-IN / CHECK-OUT
     * ---------------------------------------------------------
     */

    suspend fun checkIn(
        userId: String,
        viewingId: String,
        latitude: Double?,
        longitude: Double?
    ): Result<Unit>

    suspend fun checkOut(
        userId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun getCheckInStatus(
        viewingId: String
    ): Result<ViewingCheckInData>


    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
     */

    suspend fun sendViewingReminder(
        viewingId: String,
        hoursBefore: Int
    ): Result<Unit>

    suspend fun processViewingReminders(): Result<Int>


    /*
     * ---------------------------------------------------------
     * NO-SHOWS
     * ---------------------------------------------------------
     */

    suspend fun markNoShow(
        markedBy: String,
        viewingId: String,
        userId: String
    ): Result<Unit>

    suspend fun getNoShowHistory(
        userId: String
    ): Result<List<ViewingSummaryData>>


    /*
     * ---------------------------------------------------------
     * FEEDBACK
     * ---------------------------------------------------------
     */

    suspend fun submitViewingFeedback(
        userId: String,
        viewingId: String,
        feedback: ViewingFeedbackData
    ): Result<String>

    suspend fun getViewingFeedback(
        viewingId: String
    ): Result<ViewingFeedbackData?>


    /*
     * ---------------------------------------------------------
     * PROPERTY INTEREST
     * ---------------------------------------------------------
     */

    suspend fun expressInterest(
        userId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun removeInterest(
        userId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun getInterestedApplicants(
        propertyId: String
    ): Result<List<ViewingApplicantData>>


    /*
     * ---------------------------------------------------------
     * CONVERSION
     * ---------------------------------------------------------
     */

    /**
     * Mark applicant as having proceeded after viewing.
     */
    suspend fun proceedToApplication(
        userId: String,
        viewingId: String
    ): Result<String>

    /**
     * Mark viewing as converted into tenancy.
     */
    suspend fun convertToTenancy(
        landlordId: String,
        viewingId: String,
        tenantId: String,
        unitId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getViewingAnalytics(
        userId: String
    ): Result<ViewingAnalyticsData>

    suspend fun getPropertyViewingAnalytics(
        propertyId: String
    ): Result<PropertyViewingAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateViewingRequestData(
    val propertyId: String,
    val unitId: String?,
    val preferredDate: String,
    val preferredTime: String,
    val alternativeDate: String?,
    val alternativeTime: String?,
    val numberOfAttendees: Int,
    val message: String?
)

data class ViewingSummaryData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val requesterId: String,
    val requesterName: String?,
    val scheduledDate: String?,
    val scheduledTime: String?,
    val status: ViewingStatus,
    val brokerId: String?,
    val createdAt: String
)

data class ViewingDetailsData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val requesterId: String,
    val requesterName: String?,
    val requesterPhone: String?,
    val landlordId: String,
    val schedule: ViewingScheduleData?,
    val location: ViewingLocationData?,
    val attendees: List<ViewingAttendeeData>,
    val broker: ProfessionalSummaryData?,
    val status: ViewingStatus,
    val checkIn: ViewingCheckInData?,
    val feedback: ViewingFeedbackData?,
    val createdAt: String,
    val updatedAt: String
)

data class ViewingScheduleData(
    val date: String,
    val startTime: String,
    val endTime: String?,
    val timezone: String = "Africa/Nairobi"
)

data class ViewingTimeSlotData(
    val date: String,
    val startTime: String,
    val endTime: String,
    val available: Boolean
)

data class ViewingAttendeeData(
    val id: String = "",
    val name: String,
    val phoneNumber: String?,
    val relationship: String?,
    val confirmed: Boolean = false
)

data class ViewingLocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val landmark: String?,
    val directionsNote: String?
)

data class ViewingDirectionsData(
    val distanceKm: Double,
    val durationMinutes: Int,
    val encodedPolyline: String?,
    val destinationAddress: String?,
    val mapUrl: String?
)

data class ViewingCheckInData(
    val checkedIn: Boolean,
    val checkedInAt: String?,
    val checkedOut: Boolean,
    val checkedOutAt: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class ViewingFeedbackData(
    val id: String = "",
    val viewingId: String,
    val userId: String,
    val rating: Int,
    val comment: String?,
    val interested: Boolean,
    val createdAt: String
)

data class ViewingApplicantData(
    val userId: String,
    val name: String?,
    val phoneNumber: String?,
    val viewingId: String,
    val viewingDate: String?,
    val status: ViewingStatus,
    val interested: Boolean
)

data class ViewingAnalyticsData(
    val totalViewings: Int,
    val upcomingViewings: Int,
    val completedViewings: Int,
    val cancelledViewings: Int,
    val noShows: Int,
    val interestedApplicants: Int,
    val applications: Int,
    val conversionRate: Double
)

data class PropertyViewingAnalyticsData(
    val propertyId: String,
    val totalViewings: Int,
    val uniqueApplicants: Int,
    val completedViewings: Int,
    val cancellations: Int,
    val noShows: Int,
    val interestedApplicants: Int,
    val tenancyConversions: Int,
    val conversionRate: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ViewingStatus {

    REQUESTED,

    PENDING_APPROVAL,

    APPROVED,

    CONFIRMED,

    SCHEDULED,

    RESCHEDULE_REQUESTED,

    IN_PROGRESS,

    COMPLETED,

    CANCELLED,

    REJECTED,

    NO_SHOW
}