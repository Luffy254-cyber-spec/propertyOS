package com.him.landlordtenant.app.data.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * =============================================================
 * EMERGENCY CONTACT MODEL
 * =============================================================
 */

data class EmergencyContact(
    val id: String = "",
    val name: String = "",
    val organizationName: String? = null,
    val description: String? = null,
    val category: EmergencyContactCategory = EmergencyContactCategory.OTHER,
    val phoneNumbers: List<EmergencyPhoneNumber> = emptyList(),
    val email: String? = null,
    val website: String? = null,
    val whatsappNumber: String? = null,
    val county: String? = null,
    val subCounty: String? = null,
    val town: String? = null,
    val estate: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val operatingHours: EmergencyOperatingHours = EmergencyOperatingHours(),
    val available: Boolean = true,
    val availableForEmergency: Boolean = true,
    val services: List<EmergencyService> = emptyList(),
    val verification: EmergencyContactVerification = EmergencyContactVerification(),
    val distanceKm: Double? = null,
    val status: EmergencyContactStatus = EmergencyContactStatus.ACTIVE,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    val isVerified: Boolean
        get() = verification.status == EmergencyVerificationStatus.VERIFIED

    val canCall: Boolean
        get() = phoneNumbers.any { it.available }

    val isCurrentlyAvailable: Boolean
        get() = status == EmergencyContactStatus.ACTIVE && available
}

enum class EmergencyContactCategory(val displayName: String) {
    POLICE("Police"),
    FIRE_AND_RESCUE("Fire & Rescue"),
    AMBULANCE("Ambulance"),
    HOSPITAL("Hospital"),
    PHARMACY("Pharmacy"),
    ELECTRICITY("Electricity"),
    WATER("Water Services"),
    COUNTY_GOVERNMENT("County Government"),
    SECURITY("Security"),
    LOCKSMITH("Locksmith"),
    ROAD_EMERGENCY("Road Emergency"),
    DISASTER_RESPONSE("Disaster Response"),
    MEDICAL_EMERGENCY("Medical Emergency"),
    GAS_EMERGENCY("Gas Emergency"),
    INTERNET_SERVICE("Internet Service"),
    OTHER("Other")
}

data class EmergencyPhoneNumber(
    val number: String = "",
    val label: EmergencyPhoneLabel = EmergencyPhoneLabel.MAIN,
    val available: Boolean = true,
    val primary: Boolean = false
)

enum class EmergencyPhoneLabel(val displayName: String) {
    MAIN("Main"),
    EMERGENCY("Emergency"),
    HOTLINE("Hotline"),
    MOBILE("Mobile"),
    LANDLINE("Landline"),
    WHATSAPP("WhatsApp"),
    ALTERNATIVE("Alternative")
}

data class EmergencyOperatingHours(
    val alwaysOpen: Boolean = false,
    val schedule: List<EmergencyDaySchedule> = emptyList()
)

data class EmergencyDaySchedule(
    val day: EmergencyDay = EmergencyDay.MONDAY,
    val open: Boolean = true,
    val openingTime: String? = "00:00",
    val closingTime: String? = "23:59"
)

enum class EmergencyDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

data class EmergencyService(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val available: Boolean = true
)

data class EmergencyContactVerification(
    val status: EmergencyVerificationStatus = EmergencyVerificationStatus.PENDING,
    val verifiedBy: String? = null,
    val verifiedAt: String? = null,
    val verificationSource: String? = null,
    val notes: String? = null
)

enum class EmergencyVerificationStatus(val displayName: String) {
    PENDING("Pending"),
    VERIFIED("Verified"),
    REQUIRES_UPDATE("Requires Update"),
    SUSPENDED("Suspended")
}

enum class EmergencyContactStatus(val displayName: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    REQUIRES_UPDATE("Requires Update")
}
