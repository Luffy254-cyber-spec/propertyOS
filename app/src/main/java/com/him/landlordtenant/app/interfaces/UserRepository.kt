package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun getUserById(id: String): User?
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun deleteUser(user: User): Result<Unit>

    suspend fun getUserProfile(userId: String): Result<UserProfileData>
    suspend fun updateUserProfile(userId: String, profile: UserProfileData): Result<Unit>
    suspend fun deleteUserAccount(userId: String): Result<Unit>

    suspend fun exportUserData(userId: String): Result<UserDataExportData>
    
    suspend fun switchActiveRole(userId: String, roleId: String): Result<UserRoleData>
    
    suspend fun updateNotificationPreferences(
        userId: String,
        pushEnabled: Boolean?,
        emailEnabled: Boolean?,
        smsEnabled: Boolean?,
        whatsappEnabled: Boolean?,
        rentReminders: Boolean?,
        paymentReceipts: Boolean?,
        arrearsAlerts: Boolean?,
        billReminders: Boolean?,
        maintenanceUpdates: Boolean?,
        agreementUpdates: Boolean?,
        propertyUpdates: Boolean?,
        viewingReminders: Boolean?,
        messageNotifications: Boolean?,
        vacancyAlerts: Boolean?,
        securityAlerts: Boolean?,
        marketingNotifications: Boolean?
    ): Result<Unit>

    suspend fun updatePrivacySettings(
        userId: String,
        profileVisible: Boolean?,
        phoneVisible: Boolean?,
        emailVisible: Boolean?,
        showOnlineStatus: Boolean?,
        allowDirectMessages: Boolean?,
        allowPropertyRecommendations: Boolean?,
        allowLocationSharing: Boolean?,
        showInPublicDirectory: Boolean?
    ): Result<Unit>

    suspend fun updateUserPhoto(userId: String, imagePath: String): Result<String>

    suspend fun updateUserPreferences(
        userId: String,
        language: String?,
        currency: String?,
        theme: String?,
        timezone: String?,
        autoPlayVideos: Boolean?,
        showPropertyRecommendations: Boolean?
    ): Result<Unit>
}
