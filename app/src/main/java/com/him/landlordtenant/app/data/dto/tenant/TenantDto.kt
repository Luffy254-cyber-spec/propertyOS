package com.him.landlordtenant.app.data.dto.tenant

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * TENANT DTO
 * =============================================================
 *
 * Remote/API representation of a tenant.
 *
 * This should contain transport/storage-friendly values.
 * Domain-specific enum conversion will be handled later
 * by TenantMapper.
 *
 * =============================================================
 */

@Serializable
data class TenantDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String = "",

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PERSONAL INFORMATION
     * ---------------------------------------------------------
     */

    val firstName: String = "",

    val lastName: String = "",

    val email: String? = null,

    val phoneNumber: String? = null,

    val alternativePhoneNumber: String? = null,

    val profileImageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION / VERIFICATION
     * ---------------------------------------------------------
     */

    val idType: String? = null,

    val idNumberMasked: String? = null,

    val identityVerified: Boolean = false,

    val identityVerificationStatus: String = "PENDING",

    val identityVerifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * EMERGENCY INFORMATION
     * ---------------------------------------------------------
     */

    val emergencyContactId: String? = null,

    val emergencyContactName: String? = null,

    val emergencyContactPhone: String? = null,

    val emergencyContactRelationship: String? = null,

    /*
     * ---------------------------------------------------------
     * CURRENT PROPERTY
     * ---------------------------------------------------------
     */

    val currentApartmentId: String? = null,

    val currentHouseId: String? = null,

    val currentHouseNumber: String? = null,

    val currentFloorId: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANCY
     * ---------------------------------------------------------
     */

    val tenancyStatus: String = "PROSPECTIVE",

    val membershipId: String? = null,

    val agreementId: String? = null,

    val moveInDate: String? = null,

    val expectedMoveOutDate: String? = null,

    /*
     * ---------------------------------------------------------
     * OCCUPANCY
     * ---------------------------------------------------------
     */

    val numberOfOccupants: Int = 1,

    val occupantIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * FINANCIAL SUMMARY
     * ---------------------------------------------------------
     *
     * These are summary values returned by the backend.
     * Detailed financial records remain in Bill/Payment DTOs.
     *
     * ---------------------------------------------------------
     */

    val monthlyRent: Double = 0.0,

    val outstandingBalance: Double = 0.0,

    val totalPaid: Double = 0.0,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * RENT STATUS
     * ---------------------------------------------------------
     */

    val rentStatus: String = "CURRENT",

    val nextRentDueDate: String? = null,

    val daysOverdue: Int = 0,

    /*
     * ---------------------------------------------------------
     * COMMUNICATION
     * ---------------------------------------------------------
     */

    val preferredContactMethod: String = "IN_APP",

    val whatsappEnabled: Boolean = true,

    val smsEnabled: Boolean = true,

    val emailEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * NOTIFICATIONS
     * ---------------------------------------------------------
     */

    val rentRemindersEnabled: Boolean = true,

    val billRemindersEnabled: Boolean = true,

    val maintenanceNotificationsEnabled: Boolean = true,

    val announcementNotificationsEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * ACCOUNT
     * ---------------------------------------------------------
 */

    val accountStatus: String = "ACTIVE",

    val onboardingCompleted: Boolean = false,

    val profileCompleted: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastActiveAt: String? = null
) {

    val fullName: String
        get() = "$firstName $lastName".trim()

    val hasOutstandingBalance: Boolean
        get() = outstandingBalance > 0.0

    val isOverdue: Boolean
        get() = daysOverdue > 0
}