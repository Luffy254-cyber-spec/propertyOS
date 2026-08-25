package com.him.landlordtenant.app.data.dto.auth

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * USER DTO
 * =============================================================
 *
 * Remote/API representation of a user.
 *
 * DTOs are intentionally separate from the application models.
 *
 * Model:
 *   Used by the application/domain layer.
 *
 * DTO:
 *   Used for Firebase/API/network serialization.
 *
 * =============================================================
 */

@Serializable
data class UserDto(

    /*
     * ---------------------------------------------------------
     * IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val email: String? = null,

    val phoneNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PERSONAL INFORMATION
     * ---------------------------------------------------------
     */

    val firstName: String = "",

    val lastName: String = "",

    val profileImageUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * ACCOUNT
     * ---------------------------------------------------------
     */

    val role: String = "TENANT",

    val accountStatus: String = "ACTIVE",

    val emailVerified: Boolean = false,

    val phoneVerified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    val twoFactorEnabled: Boolean = false,

    val biometricEnabled: Boolean = false,

    /*
     * ---------------------------------------------------------
     * PROFILE
     * ---------------------------------------------------------
     */

    val nationalIdMasked: String? = null,

    val county: String? = null,

    val town: String? = null,

    /*
     * ---------------------------------------------------------
     * NOTIFICATION PREFERENCES
     * ---------------------------------------------------------
     */

    val pushNotificationsEnabled: Boolean = true,

    val smsNotificationsEnabled: Boolean = true,

    val emailNotificationsEnabled: Boolean = true,

    /*
     * ---------------------------------------------------------
     * ACCOUNT TIMESTAMPS
     * ---------------------------------------------------------
     */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val lastLoginAt: String? = null,

    /*
     * ---------------------------------------------------------
     * DEVICE
     * ---------------------------------------------------------
     */

    val lastDeviceId: String? = null,

    val lastDeviceName: String? = null,

    val lastAppVersion: String? = null
) {

    val fullName: String
        get() = "$firstName $lastName".trim()
}