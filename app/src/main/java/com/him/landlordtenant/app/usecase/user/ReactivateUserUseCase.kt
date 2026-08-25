package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository

/**
 * Reactivates a previously deactivated user account.
 *
 * Reactivation should be treated as a privileged operation.
 * The backend must verify that the caller has permission to
 * reactivate the account.
 */
class ReactivateUserUseCase(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(
        actorId: String,
        userId: String,
        reason: String? = null
    ): Result<Unit> {

        val cleanActorId = actorId.trim()
        val cleanUserId = userId.trim()
        val cleanReason = reason?.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            permissionRepository.enableUserAccess(
                actorId = cleanActorId,
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}