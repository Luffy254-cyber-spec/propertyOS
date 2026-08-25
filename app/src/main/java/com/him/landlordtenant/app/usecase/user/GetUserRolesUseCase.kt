package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository
import com.him.landlordtenant.app.interfaces.UserRoleData

/**
 * Retrieves all roles assigned to a user.
 *
 * A user may have multiple roles in the property-management
 * platform, for example:
 *
 * TENANT
 * LANDLORD
 * CARETAKER
 * PROPERTY_MANAGER
 * BROKER
 * TECHNICIAN
 * ADMIN
 */
class GetUserRolesUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Result<List<UserRoleData>> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            permissionRepository.getUserRoles(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}