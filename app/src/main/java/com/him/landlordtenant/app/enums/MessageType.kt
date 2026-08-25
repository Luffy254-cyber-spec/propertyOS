package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MESSAGE TYPE
 * =============================================================
 */
enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    LOCATION,
    CONTACT,
    STicker,
    BILL,
    AGREEMENT_REQUEST,
    MAINTENANCE_UPDATE,
    SYSTEM_ALERT;

    companion object {
        fun fromValue(value: String?): MessageType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
        }
    }
}
