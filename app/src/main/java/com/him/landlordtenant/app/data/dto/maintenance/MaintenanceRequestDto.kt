package com.him.landlordtenant.app.data.dto.maintenance

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * MAINTENANCE REQUEST DTO
 * =============================================================
 *
 * Remote/API representation of a tenant/property maintenance
 * request.
 *
 * Supports:
 * - Tenant maintenance reporting
 * - Landlord/caretaker assignment
 * - Technician assignment
 * - Priority management
 * - Status tracking
 * - Scheduling
 * - Cost tracking
 * - Media evidence
 * - Completion confirmation
 *
 * =============================================================
 */

@Serializable
data class MaintenanceRequestDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PROPERTY REFERENCES
     * ---------------------------------------------------------
     */

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val floorId: String? = null,

    val houseId: String? = null,

    val tenantId: String? = null,

    /*
     * ---------------------------------------------------------
     * REQUESTER
     * ---------------------------------------------------------
     */

    val requestedBy: String = "",

    val requestedByRole: String = "TENANT",

    /*
     * ---------------------------------------------------------
     * REQUEST DETAILS
     * ---------------------------------------------------------
     */

    val title: String = "",

    val description: String = "",

    val category: String = "GENERAL",

    val subCategory: String? = null,

    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    val priority: String = "MEDIUM",

    val emergency: Boolean = false,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
     */

    val status: String = "SUBMITTED",

    val statusMessage: String? = null,

    /*
     * ---------------------------------------------------------
     * ASSIGNMENT
     * ---------------------------------------------------------
 */

    val assignedTo: String? = null,

    val assignedToType: String? = null,

    val assignedProfessionalId: String? = null,

    val assignedCaretakerId: String? = null,

    val assignedManagerId: String? = null,

    /*
     * ---------------------------------------------------------
     * SCHEDULING
     * ---------------------------------------------------------
 */

    val preferredVisitDate: String? = null,

    val preferredVisitTime: String? = null,

    val scheduledDate: String? = null,

    val scheduledStartTime: String? = null,

    val scheduledEndTime: String? = null,

    /*
     * ---------------------------------------------------------
     * ACCESS
     * ---------------------------------------------------------
 */

    val tenantAvailableForVisit: Boolean = true,

    val accessInstructions: String? = null,

    val requiresTenantPresence: Boolean = true,

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val locationDescription: String? = null,

    val room: String? = null,

    val exactLocation: String? = null,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
 */

    val attachmentIds: List<String> = emptyList(),

    val imageUrls: List<String> = emptyList(),

    val videoUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * ESTIMATES
     * ---------------------------------------------------------
 */

    val estimatedCost: Double? = null,

    val approvedCost: Double? = null,

    val actualCost: Double? = null,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * PAYMENT
     * ---------------------------------------------------------
 */

    val paymentRequired: Boolean = false,

    val paymentResponsibleParty: String? = null,

    val paymentStatus: String = "NOT_REQUIRED",

    val paymentId: String? = null,

    /*
     * ---------------------------------------------------------
     * TECHNICIAN / PROFESSIONAL
     * ---------------------------------------------------------
 */

    val professionalRequired: Boolean = false,

    val professionalSpecialization: String? = null,

    val professionalVerified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * COMPLETION
     * ---------------------------------------------------------
 */

    val completedAt: String? = null,

    val completedBy: String? = null,

    val completionNotes: String? = null,

    val completionAttachmentIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TENANT CONFIRMATION
     * ---------------------------------------------------------
 */

    val tenantConfirmationRequired: Boolean = true,

    val tenantConfirmed: Boolean = false,

    val tenantConfirmedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * RATING
     * ---------------------------------------------------------
 */

    val rating: Int? = null,

    val feedback: String? = null,

    /*
     * ---------------------------------------------------------
     * AUTOMATION
     * ---------------------------------------------------------
 */

    val automaticAssignment: Boolean = false,

    val automaticNotificationsEnabled: Boolean = true,

    val reminderSent: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val acknowledgedAt: String? = null,

    val assignedAt: String? = null,

    val startedAt: String? = null,

    val cancelledAt: String? = null,

    /*
     * ---------------------------------------------------------
     * CANCELLATION
     * ---------------------------------------------------------
 */

    val cancelledBy: String? = null,

    val cancellationReason: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isOpen: Boolean
        get() = status.uppercase() !in setOf(
            "COMPLETED",
            "CANCELLED",
            "CLOSED"
        )

    val isAssigned: Boolean
        get() =
            assignedTo != null ||
                    assignedProfessionalId != null ||
                    assignedCaretakerId != null ||
                    assignedManagerId != null

    val isScheduled: Boolean
        get() = scheduledDate != null

    val isCompleted: Boolean
        get() =
            status.uppercase() == "COMPLETED"

    val requiresPayment: Boolean
        get() =
            paymentRequired &&
                    paymentStatus.uppercase() != "PAID"
}