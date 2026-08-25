package com.him.landlordtenant.app.data.preferences

data class UserPreferences(
    val useDarkMode: Boolean = false,
    val language: String = "en",
    val currency: String = "KES",
    val biometricLogin: Boolean = false,
    val locationEnabled: Boolean = true,
    val pushNotifications: Boolean = true
)

