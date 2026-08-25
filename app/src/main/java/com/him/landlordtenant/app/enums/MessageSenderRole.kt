package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MESSAGE SENDER ROLE
 * =============================================================
 */
enum class MessageSenderRole(
    val displayName: String
) {
    TENANT("Tenant"),
    LANDLORD("Landlord"),
    PROPERTY_MANAGER("Property Manager"),
    CARETAKER("Caretaker"),
    BROKER("Broker"),
    TECHNICIAN("Technician"),
    SYSTEM("System"),
    SUPPORT("Support");

    companion object {
        fun fromValue(value: String?): MessageSenderRole? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
