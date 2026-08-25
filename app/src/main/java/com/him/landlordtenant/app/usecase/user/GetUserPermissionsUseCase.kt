package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository
import com.him.landlordtenant.app.interfaces.UserPermissionData

/**
 * Retrieves all permissions currently granted to a user.
 *
 * Permissions determine what the user is allowed to do,
 * while roles determine what type of user they are.
 */
class GetUserPermissionsUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Result<List<UserPermissionData>> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            permissionRepository.getUserPermissions(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}