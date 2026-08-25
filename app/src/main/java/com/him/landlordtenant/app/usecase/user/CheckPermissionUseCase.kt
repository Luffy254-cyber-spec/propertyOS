package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository

/**
 * Checks whether a user has a specific permission.
 */
class CheckPermissionUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        userId: String,
        permission: String
    ): Result<Boolean> {

        val cleanUserId = userId.trim()
        val cleanPermission = permission.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanPermission.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Permission is required."
                )
            )
        }

        return try {
            permissionRepository.hasPermission(
                userId = cleanUserId,
                permission = cleanPermission
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}