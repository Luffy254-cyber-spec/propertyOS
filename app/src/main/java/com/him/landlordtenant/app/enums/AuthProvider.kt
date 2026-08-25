package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * AUTH PROVIDER
 * =============================================================
 */
enum class AuthProvider(
    val displayName: String
) {
    EMAIL("Email & Password"),
    GOOGLE("Google"),
    PHONE("Phone OTP"),
    APPLE("Apple"),
    FACEBOOK("Facebook");

    companion object {
        fun fromValue(value: String?): AuthProvider? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
