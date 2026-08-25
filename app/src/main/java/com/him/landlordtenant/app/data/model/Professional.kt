package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * PROFESSIONAL MODEL
 * =============================================================
 *
 * Represents a service provider working with landlords,
 * tenants, apartments, and property managers.
 *
 * Examples:
 *
 * - Plumber
 * - Electrician
 * - Painter
 * - Mason
 * - Carpenter
 * - Cleaner
 * - Welder
 * - Appliance Technician
 * - Internet Technician
 * - CCTV Technician
 * - Locksmith
 * - Gardener
 * - Pest Control
 * - HVAC Technician
 * - General Maintenance Technician
 * - Broker
 *
 * =============================================================
 */

data class Professional(

    /*
     * ---------------------------------------------------------
     * IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String? = null,

    val businessName: String = "",

    val fullName: String = "",

    val profileImageUrl: String? = null,

    val phoneNumber: String = "",

    val alternativePhoneNumber: String? = null,

    val email: String? = null,

    /*
     * ---------------------------------------------------------
     * PROFESSIONAL TYPE
     * ---------------------------------------------------------
     */

    val category:
    ProfessionalCategory =
        ProfessionalCategory.GENERAL_MAINTENANCE,

    val services:
    List<ProfessionalService> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * DESCRIPTION
     * ---------------------------------------------------------
     */

    val description: String? = null,

    val yearsOfExperience: Int = 0,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verification:
    ProfessionalVerification =
        ProfessionalVerification(),

    /*
     * ---------------------------------------------------------
     * CERTIFICATIONS
     * ---------------------------------------------------------
 */

    val certifications:
    List<ProfessionalCertification> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * SERVICE LOCATION
     * ---------------------------------------------------------
 */

    val serviceAreas:
    List<ServiceArea> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
 */

    val latitude: Double? = null,

    val longitude: Double? = null,

    val locationName: String? = null,

    /*
     * ---------------------------------------------------------
     * AVAILABILITY
     * ---------------------------------------------------------
 */

    val availability:
    ProfessionalAvailability =
        ProfessionalAvailability(),

    /*
     * ---------------------------------------------------------
     * PRICING
     * ---------------------------------------------------------
 */

    val pricing:
    ProfessionalPricing =
        ProfessionalPricing(),

    /*
     * ---------------------------------------------------------
     * RATING
     * ---------------------------------------------------------
 */

    val rating:
    ProfessionalRating =
        ProfessionalRating(),

    /*
     * ---------------------------------------------------------
     * EMERGENCY SERVICES
     * ---------------------------------------------------------
 */

    val emergencyService:
    Boolean = false,

    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
 */

    val status:
    ProfessionalStatus =
        ProfessionalStatus.ACTIVE,

    /*
     * ---------------------------------------------------------
     * CONTACT PREFERENCES
     * ---------------------------------------------------------
 */

    val contactPreferences:
    ProfessionalContactPreferences =
        ProfessionalContactPreferences(),

    /*
     * ---------------------------------------------------------
     * DOCUMENTS
     * ---------------------------------------------------------
 */

    val documents:
    List<ProfessionalDocument> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * PORTFOLIO
     * ---------------------------------------------------------
 */

    val portfolio:
    List<PortfolioItem> =
        emptyList(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastActiveAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isVerified: Boolean
        get() =
            verification.status ==
                    VerificationStatus.VERIFIED

    val isAvailable: Boolean
        get() =
            status ==
                    ProfessionalStatus.ACTIVE &&
                    availability.available

    val isEmergencyAvailable: Boolean
        get() =
            emergencyService &&
                    availability.available
}


/*
 * =============================================================
 * PROFESSIONAL CATEGORY
 * =============================================================
 */

enum class ProfessionalCategory(

    val displayName: String

) {

    PLUMBER(
        "Plumber"
    ),

    ELECTRICIAN(
        "Electrician"
    ),

    PAINTER(
        "Painter"
    ),

    MASON(
        "Mason"
    ),

    CARPENTER(
        "Carpenter"
    ),

    WELDER(
        "Welder"
    ),

    CLEANER(
        "Cleaner"
    ),

    GARDENER(
        "Gardener"
    ),

    LOCKSMITH(
        "Locksmith"
    ),

    PEST_CONTROL(
        "Pest Control"
    ),

    HVAC_TECHNICIAN(
        "HVAC Technician"
    ),

    APPLIANCE_TECHNICIAN(
        "Appliance Technician"
    ),

    CCTV_TECHNICIAN(
        "CCTV Technician"
    ),

    INTERNET_TECHNICIAN(
        "Internet Technician"
    ),

    ROOFER(
        "Roofer"
    ),

    TILER(
        "Tiler"
    ),

    GLASS_WORKER(
        "Glass Worker"
    ),

    MOVING_SERVICE(
        "Moving Service"
    ),

    SECURITY(
        "Security"
    ),

    BROKER(
        "Broker"
    ),

    PROPERTY_MANAGER(
        "Property Manager"
    ),

    GENERAL_MAINTENANCE(
        "General Maintenance"
    )
}


/*
 * =============================================================
 * PROFESSIONAL SERVICE
 * =============================================================
 */

data class ProfessionalService(

    val id: String = "",

    val name: String = "",

    val description: String? = null,

    val category:
    ProfessionalCategory =
        ProfessionalCategory.GENERAL_MAINTENANCE,

    val estimatedPrice:
    Double? = null,

    val priceUnit:
    PriceUnit =
        PriceUnit.FIXED,

    val available: Boolean = true
)


/*
 * =============================================================
 * PRICE UNIT
 * =============================================================
 */

enum class PriceUnit(

    val displayName: String

) {

    FIXED(
        "Fixed Price"
    ),

    PER_HOUR(
        "Per Hour"
    ),

    PER_DAY(
        "Per Day"
    ),

    PER_JOB(
        "Per Job"
    ),

    NEGOTIABLE(
        "Negotiable"
    ),

    QUOTE_REQUIRED(
        "Quote Required"
    )
}


/*
 * =============================================================
 * VERIFICATION
 * =============================================================
 */

data class ProfessionalVerification(

    val status:
    VerificationStatus =
        VerificationStatus.PENDING,

    val idVerified: Boolean = false,

    val phoneVerified: Boolean = false,

    val emailVerified: Boolean = false,

    val businessVerified: Boolean = false,

    val certificationVerified: Boolean = false,

    val verifiedAt: String? = null,

    val verifiedBy: String? = null,

    val verificationNote: String? = null
)




/*
 * =============================================================
 * CERTIFICATION
 * =============================================================
 */

data class ProfessionalCertification(

    val id: String = "",

    val name: String = "",

    val issuingOrganization: String? = null,

    val certificateNumber: String? = null,

    val issueDate: String? = null,

    val expiryDate: String? = null,

    val documentUrl: String? = null,

    val verificationStatus:
    CertificationVerificationStatus =
        CertificationVerificationStatus.PENDING
)


/*
 * =============================================================
 * CERTIFICATION STATUS
 * =============================================================
 */

enum class CertificationVerificationStatus(

    val displayName: String

) {

    PENDING(
        "Pending"
    ),

    VERIFIED(
        "Verified"
    ),

    EXPIRED(
        "Expired"
    ),

    REJECTED(
        "Rejected"
    )
}


/*
 * =============================================================
 * SERVICE AREA
 * =============================================================
 */

data class ServiceArea(

    val county: String = "",

    val subCounty: String? = null,

    val town: String? = null,

    val estate: String? = null,

    val radiusKm: Double? = null
)


/*
 * =============================================================
 * AVAILABILITY
 * =============================================================
 */

data class ProfessionalAvailability(

    val available: Boolean = true,

    val availableToday: Boolean = true,

    val availableForEmergency: Boolean = false,

    val workingHours:
    List<WorkingHour> =
        emptyList()
)


/*
 * =============================================================
 * WORKING HOUR
 * =============================================================
 */

data class WorkingHour(

    val day:
    DayOfWeek =
        DayOfWeek.MONDAY,

    val enabled: Boolean = true,

    val startTime: String = "08:00",

    val endTime: String = "17:00"
)


/*
 * =============================================================
 * DAY OF WEEK
 * =============================================================
 */

enum class DayOfWeek {

    MONDAY,

    TUESDAY,

    WEDNESDAY,

    THURSDAY,

    FRIDAY,

    SATURDAY,

    SUNDAY
}


/*
 * =============================================================
 * PRICING
 * =============================================================
 */

data class ProfessionalPricing(

    val currency: String = "KES",

    val minimumCharge: Double? = null,

    val callOutFee: Double? = null,

    val emergencyFee: Double? = null,

    val negotiable: Boolean = true
)


/*
 * =============================================================
 * RATING
 * =============================================================
 */

data class ProfessionalRating(

    val average: Double = 0.0,

    val totalReviews: Int = 0,

    val fiveStarReviews: Int = 0,

    val fourStarReviews: Int = 0,

    val threeStarReviews: Int = 0,

    val twoStarReviews: Int = 0,

    val oneStarReviews: Int = 0
)


/*
 * =============================================================
 * CONTACT PREFERENCES
 * =============================================================
 */

data class ProfessionalContactPreferences(

    val allowPhoneCalls: Boolean = true,

    val allowSms: Boolean = true,

    val allowWhatsApp: Boolean = true,

    val allowInAppMessages: Boolean = true
)


/*
 * =============================================================
 * DOCUMENT
 * =============================================================
 */

data class ProfessionalDocument(

    val id: String = "",

    val name: String = "",

    val type:
    ProfessionalDocumentType =
        ProfessionalDocumentType.OTHER,

    val documentUrl: String = "",

    val verified: Boolean = false,

    val uploadedAt: String? = null
)


/*
 * =============================================================
 * DOCUMENT TYPE
 * =============================================================
 */

enum class ProfessionalDocumentType {

    NATIONAL_ID,

    PASSPORT,

    BUSINESS_PERMIT,

    CERTIFICATE,

    LICENSE,

    TAX_DOCUMENT,

    INSURANCE,

    OTHER
}


/*
 * =============================================================
 * PORTFOLIO ITEM
 * =============================================================
 */

data class PortfolioItem(

    val id: String = "",

    val title: String = "",

    val description: String? = null,

    val imageUrls:
    List<String> =
        emptyList(),

    val completedAt: String? = null
)


/*
 * =============================================================
 * PROFESSIONAL STATUS
 * =============================================================
 */

enum class ProfessionalStatus(

    val displayName: String

) {

    ACTIVE(
        "Active"
    ),

    BUSY(
        "Busy"
    ),

    OFFLINE(
        "Offline"
    ),

    SUSPENDED(
        "Suspended"
    ),

    BLOCKED(
        "Blocked"
    ),

    INACTIVE(
        "Inactive"
    )
}