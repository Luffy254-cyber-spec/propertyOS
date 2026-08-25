package com.him.landlordtenant.app.utils

object Constants {
    const val APP_NAME = "propertyOS"
    
    // API Endpoints (Placeholders)
    const val BASE_URL = "https://api.propertyos.com/"
    const val CLOUDINARY_CLOUD_NAME = "your_cloud_name"
    
    // Firebase Collections
    const val USERS_COLLECTION = "users"
    const val APARTMENTS_COLLECTION = "apartments"
    const val HOUSES_COLLECTION = "houses"
    const val AGREEMENTS_COLLECTION = "agreements"
    const val MAINTENANCE_COLLECTION = "maintenance_requests"
    const val BILLS_COLLECTION = "bills"
    const val PAYMENTS_COLLECTION = "payments"
    
    // Shared Preferences Keys
    const val PREFS_NAME = "property_os_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    
    // Notification Channels
    const val CHANNEL_RENT = "rent_reminders"
    const val CHANNEL_BILLS = "bill_reminders"
    const val CHANNEL_MAINTENANCE = "maintenance_updates"
}
