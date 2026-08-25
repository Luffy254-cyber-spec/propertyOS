package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.UserDao
import com.him.landlordtenant.app.data.entities.toDomain
import com.him.landlordtenant.app.data.entities.toEntity
import com.him.landlordtenant.app.data.model.User
import com.him.landlordtenant.app.data.model.UserRole
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.UserDataExportData
import com.him.landlordtenant.app.interfaces.UserRoleData
import com.him.landlordtenant.app.interfaces.UserProfileData
import com.him.landlordtenant.app.interfaces.UserRepository
import com.him.landlordtenant.app.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkHelper: NetworkHelper
) : UserRepository {

    override fun getUsers(): Flow<List<User>> = userDao.getAll().map { it.map { entity -> entity.toDomain() } }

    override suspend fun getUserById(id: String): User? {
        val localUser = userDao.getById(id)?.toDomain()
        
        if (!networkHelper.isNetworkAvailable()) {
            return localUser
        }

        // Online: Fetch fresh data and update cache
        val remoteUser = try {
            // Try Realtime Database (Landlords)
            val rtUser = withTimeoutOrNull(5000) {
                firebaseDataSource.readData("users/$id", User::class.java).getOrNull()
            }
            if (rtUser != null) rtUser else {
                // Try Firestore (Tenants)
                withTimeoutOrNull(5000) {
                    firestoreDataSource.getData("users", id, User::class.java).getOrNull()
                }
            }
        } catch (e: Exception) {
            null
        }

        if (remoteUser != null) {
            userDao.insert(remoteUser.toEntity())
            return remoteUser
        }

        return localUser
    }

    override suspend fun saveUser(user: User): Result<Unit> = try {
        userDao.insert(user.toEntity())
        
        if (user.roles.contains(UserRole.LANDLORD)) {
            // Save to Realtime Database
            withTimeoutOrNull(10000) {
                firebaseDataSource.writeData("users/${user.id}", user)
            }
        } else {
            // Save to Firestore
            withTimeoutOrNull(10000) {
                firestoreDataSource.saveData("users", user.id, user)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteUser(user: User): Result<Unit> = try {
        userDao.delete(user.toEntity())
        if (user.roles.contains(UserRole.LANDLORD)) {
            firebaseDataSource.getReference("users/${user.id}").removeValue()
        } else {
            firestoreDataSource.deleteData("users", user.id)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserProfile(userId: String): Result<UserProfileData> {
        val user = getUserById(userId) ?: throw Exception("Profile not found")
        return Result.success(UserProfileData(
            id = user.id,
            fullName = user.fullName,
            email = user.email ?: "",
            phoneNumber = user.phoneNumber,
            profileImageUrl = user.profileImageUrl,
            roles = user.roles.map { it.name },
            activeRole = user.activeRole.name,
            isVerified = user.emailVerified
        ))
    }

    override suspend fun updateUserProfile(userId: String, profile: UserProfileData): Result<Unit> {
        val user = getUserById(userId) ?: return Result.failure(Exception("User not found"))
        val updatedUser = user.copy(
            email = profile.email,
            phoneNumber = profile.phoneNumber,
            profileImageUrl = profile.profileImageUrl
        )
        return saveUser(updatedUser)
    }

    override suspend fun deleteUserAccount(userId: String): Result<Unit> {
        val user = getUserById(userId) ?: return Result.failure(Exception("User not found"))
        return deleteUser(user)
    }

    override suspend fun exportUserData(userId: String): Result<UserDataExportData> = Result.failure(NotImplementedError())

    override suspend fun switchActiveRole(userId: String, roleId: String): Result<UserRoleData> = Result.failure(NotImplementedError())

    override suspend fun updateNotificationPreferences(
        userId: String, pushEnabled: Boolean?, emailEnabled: Boolean?, smsEnabled: Boolean?, whatsappEnabled: Boolean?,
        rentReminders: Boolean?, paymentReceipts: Boolean?, arrearsAlerts: Boolean?, billReminders: Boolean?,
        maintenanceUpdates: Boolean?, agreementUpdates: Boolean?, propertyUpdates: Boolean?, viewingReminders: Boolean?,
        messageNotifications: Boolean?, vacancyAlerts: Boolean?, securityAlerts: Boolean?, marketingNotifications: Boolean?
    ): Result<Unit> = Result.success(Unit)

    override suspend fun updatePrivacySettings(
        userId: String, profileVisible: Boolean?, phoneVisible: Boolean?, emailVisible: Boolean?, showOnlineStatus: Boolean?,
        allowDirectMessages: Boolean?, allowPropertyRecommendations: Boolean?, allowLocationSharing: Boolean?, showInPublicDirectory: Boolean?
    ): Result<Unit> = Result.success(Unit)

    override suspend fun updateUserPhoto(userId: String, imagePath: String): Result<String> = Result.success(imagePath)

    override suspend fun updateUserPreferences(
        userId: String, language: String?, currency: String?, theme: String?, timezone: String?, autoPlayVideos: Boolean?, showPropertyRecommendations: Boolean?
    ): Result<Unit> = Result.success(Unit)
}
