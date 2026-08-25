package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.PermissionRepository

/**
 * Deactivates a user's access.
 */
class DeactivateUserUseCase(
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
            permissionRepository.disableUserAccess(
                actorId = cleanActorId,
                userId = cleanUserId,
                reason = cleanReason ?: "No reason provided"
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}