package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository

/**
 * Removes a role from a user.
 *
 * This is a privileged operation. The backend must verify that
 * the caller has permission to remove the requested role.
 */
class RemoveUserRoleUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        actorId: String,
        userId: String,
        roleId: String
    ): Result<Unit> {

        val cleanActorId = actorId.trim()
        val cleanUserId = userId.trim()
        val cleanRoleId = roleId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanRoleId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Role ID is required."
                )
            )
        }

        return try {
            permissionRepository.removeRole(
                actorId = cleanActorId,
                userId = cleanUserId,
                roleId = cleanRoleId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}