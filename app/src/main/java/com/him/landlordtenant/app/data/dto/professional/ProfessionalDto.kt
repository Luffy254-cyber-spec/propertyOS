package com.him.landlordtenant.app.data.dto.professional

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * PROFESSIONAL DTO
 * =============================================================
 *
 * Remote/API representation of a service professional.
 *
 * Examples:
 * - Plumber
 * - Electrician
 * - Carpenter
 * - Painter
 * - Mason
 * - Cleaner
 * - Appliance technician
 * - Security technician
 * - HVAC technician
 * - Broker / property agent
 *
 * Supports professional discovery, certification verification,
 * service-area matching, ratings, availability and assignment
 * to maintenance/service requests.
 *
 * =============================================================
 */

@Serializable
data class ProfessionalDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String? = null,

    val referenceNumber: String = "",

    /*
     * ---------------------------------------------------------
     * PERSONAL INFORMATION
     * ---------------------------------------------------------
     */

    val firstName: String = "",

    val lastName: String = "",

    val businessName: String? = null,

    val profileImageUrl: String? = null,

    val email: String? = null,

    val phoneNumber: String? = null,

    val alternativePhoneNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PROFESSIONAL TYPE
     * ---------------------------------------------------------
     */

    val professionalType: String = "INDIVIDUAL",

    val primaryCategory: String = "GENERAL",

    val specializations: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * PROFESSIONAL DESCRIPTION
     * ---------------------------------------------------------
     */

    val bio: String? = null,

    val yearsOfExperience: Int = 0,

    val services: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * CERTIFICATIONS
     * ---------------------------------------------------------
     */

    val certificationIds: List<String> = emptyList(),

    val certifications: List<String> = emptyList(),

    val certificationStatus: String = "PENDING",

    val certificationsVerified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * BUSINESS / REGISTRATION
     * ---------------------------------------------------------
 */

    val businessRegistrationNumber: String? = null,

    val licenseNumberMasked: String? = null,

    val taxNumberMasked: String? = null,

    /*
     * ---------------------------------------------------------
     * IDENTITY VERIFICATION
     * ---------------------------------------------------------
 */

    val identityVerified: Boolean = false,

    val identityVerificationStatus: String = "PENDING",

    val identityVerifiedAt: String? = null,

    val verificationDocumentIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * SERVICE AREA
     * ---------------------------------------------------------
 */

    val counties: List<String> = emptyList(),

    val towns: List<String> = emptyList(),

    val serviceAreas: List<String> = emptyList(),

    val latitude: Double? = null,

    val longitude: Double? = null,

    val serviceRadiusKm: Double = 20.0,

    /*
     * ---------------------------------------------------------
     * AVAILABILITY
     * ---------------------------------------------------------
 */

    val availabilityStatus: String = "AVAILABLE",

    val availableDays: List<String> = emptyList(),

    val workingHoursStart: String? = null,

    val workingHoursEnd: String? = null,

    val acceptsEmergencyJobs: Boolean = false,

    /*
     * ---------------------------------------------------------
     * PRICING
     * ---------------------------------------------------------
 */

    val currency: String = "KES",

    val minimumServiceFee: Double? = null,

    val hourlyRate: Double? = null,

    val callOutFee: Double? = null,

    val emergencyFee: Double? = null,

    /*
     * ---------------------------------------------------------
     * PERFORMANCE
     * ---------------------------------------------------------
 */

    val completedJobs: Int = 0,

    val cancelledJobs: Int = 0,

    val activeJobs: Int = 0,

    val averageRating: Double = 0.0,

    val reviewCount: Int = 0,

    val responseRate: Double = 0.0,

    val averageResponseTimeMinutes: Int? = null,

    /*
     * ---------------------------------------------------------
     * TRUST / SAFETY
     * ---------------------------------------------------------
 */

    val backgroundChecked: Boolean = false,

    val backgroundCheckStatus: String = "NOT_CHECKED",

    val verifiedProfessional: Boolean = false,

    val trustedByPlatform: Boolean = false,

    /*
     * ---------------------------------------------------------
     * CONTACT / COMMUNICATION
     * ---------------------------------------------------------
 */

    val preferredContactMethod: String = "IN_APP",

    val whatsappEnabled: Boolean = true,

    val smsEnabled: Boolean = true,

    val emailEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * PLATFORM SETTINGS
     * ---------------------------------------------------------
 */

    val acceptsNewJobs: Boolean = true,

    val automaticJobMatchingEnabled: Boolean = true,

    val notificationsEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * PAYMENT
     * ---------------------------------------------------------
 */

    val paymentAccountVerified: Boolean = false,

    val paymentMethodType: String? = null,

    /*
     * ---------------------------------------------------------
     * DOCUMENTS
     * ---------------------------------------------------------
 */

    val documentIds: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val accountStatus: String = "ACTIVE",

    val profileStatus: String = "PENDING",

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastActiveAt: String? = null,

    val verifiedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val fullName: String
        get() = "$firstName $lastName".trim()

    val displayName: String
        get() = businessName?.takeIf {
            it.isNotBlank()
        } ?: fullName

    val isVerified: Boolean
        get() =
            identityVerified &&
                    certificationsVerified &&
                    verifiedProfessional

    val isAvailable: Boolean
        get() =
            availabilityStatus.uppercase() == "AVAILABLE" &&
                    acceptsNewJobs &&
                    accountStatus.uppercase() == "ACTIVE"

    val hasRatings: Boolean
        get() = reviewCount > 0

    val hasServiceArea: Boolean
        get() =
            counties.isNotEmpty() ||
                    towns.isNotEmpty() ||
                    serviceAreas.isNotEmpty()

    val canReceiveEmergencyJobs: Boolean
        get() =
            isAvailable &&
                    acceptsEmergencyJobs
}