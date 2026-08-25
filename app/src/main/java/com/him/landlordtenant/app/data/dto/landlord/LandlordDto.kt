package com.him.landlordtenant.app.data.dto.landlord

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * LANDLORD DTO
 * =============================================================
 *
 * Remote/API representation of a landlord.
 *
 * This DTO contains landlord profile, verification, property
 * ownership/management summaries, financial summaries,
 * communication preferences and account information.
 *
 * Detailed properties, tenants, bills and payments are kept in
 * their respective DTOs and referenced using IDs.
 *
 * =============================================================
 */

@Serializable
data class LandlordDto(

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
     * LANDLORD TYPE
     * ---------------------------------------------------------
     */

    val landlordType: String = "INDIVIDUAL",

    /*
     * ---------------------------------------------------------
     * BUSINESS INFORMATION
     * ---------------------------------------------------------
     *
     * Useful when the landlord operates through a company,
     * agency or property-management business.
     *
     * ---------------------------------------------------------
     */

    val businessName: String? = null,

    val businessRegistrationNumber: String? = null,

    val taxNumberMasked: String? = null,

    /*
     * ---------------------------------------------------------
     * IDENTITY / VERIFICATION
     * ---------------------------------------------------------
     */

    val idType: String? = null,

    val idNumberMasked: String? = null,

    val identityVerified: Boolean = false,

    val identityVerificationStatus: String = "PENDING",

    val identityVerifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * ADDRESS
     * ---------------------------------------------------------
     */

    val county: String? = null,

    val town: String? = null,

    val physicalAddress: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY SUMMARY
     * ---------------------------------------------------------
     */

    val propertyIds: List<String> = emptyList(),

    val managedPropertyIds: List<String> = emptyList(),

    val totalProperties: Int = 0,

    val totalUnits: Int = 0,

    val occupiedUnits: Int = 0,

    val vacantUnits: Int = 0,

    /*
     * ---------------------------------------------------------
     * TENANT SUMMARY
     * ---------------------------------------------------------
     */

    val activeTenantCount: Int = 0,

    val pendingTenantApplications: Int = 0,

    /*
     * ---------------------------------------------------------
     * FINANCIAL SUMMARY
     * ---------------------------------------------------------
     */

    val currency: String = "KES",

    val expectedMonthlyRent: Double = 0.0,

    val collectedMonthlyRent: Double = 0.0,

    val outstandingRent: Double = 0.0,

    val totalExpenses: Double = 0.0,

    val netMonthlyIncome: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * COLLECTION PERFORMANCE
     * ---------------------------------------------------------
 */

    val rentCollectionRate: Double = 0.0,

    val overdueTenantCount: Int = 0,

    val overdueAmount: Double = 0.0,

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
     * NOTIFICATION PREFERENCES
     * ---------------------------------------------------------
 */

    val rentPaymentNotificationsEnabled: Boolean = true,

    val maintenanceNotificationsEnabled: Boolean = true,

    val tenantApplicationNotificationsEnabled: Boolean = true,

    val propertyNotificationsEnabled: Boolean = true,

    val systemNotificationsEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * MANAGEMENT FEATURES
     * ---------------------------------------------------------
 */

    val automatedRentRemindersEnabled: Boolean = true,

    val automatedBillRemindersEnabled: Boolean = true,

    val automatedAgreementRenewalsEnabled: Boolean = true,

    val maintenanceAutomationEnabled: Boolean = true,

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
     * SECURITY
     * ---------------------------------------------------------
 */

    val twoFactorEnabled: Boolean = false,

    val biometricEnabled: Boolean = false,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastActiveAt: String? = null,

    val lastLoginAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
     */

    val fullName: String
        get() = "$firstName $lastName".trim()

    val hasVacancies: Boolean
        get() = vacantUnits > 0

    val hasOutstandingRent: Boolean
        get() = outstandingRent > 0.0

    val hasOverdueTenants: Boolean
        get() = overdueTenantCount > 0

    val occupancyRate: Double
        get() {
            if (totalUnits <= 0) return 0.0

            return (
                    occupiedUnits.toDouble() /
                            totalUnits.toDouble()
                    ) * 100.0
        }
}