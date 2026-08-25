package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository

/**
 * Assigns an additional role to a user.
 */
class AddUserRoleUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        actorId: String,
        userId: String,
        roleId: String
    ): Result<String> {

        val cleanActorId = actorId.trim()
        val cleanUserId = userId.trim()
        val cleanRoleId = roleId.trim()

        if (cleanActorId.isBlank()) {
            return Result.failure(IllegalArgumentException("Actor ID is required."))
        }

        if (cleanUserId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID is required."))
        }

        if (cleanRoleId.isBlank()) {
            return Result.failure(IllegalArgumentException("Role ID is required."))
        }

        return try {
            permissionRepository.assignRole(
                actorId = cleanActorId,
                userId = cleanUserId,
                roleId = cleanRoleId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}