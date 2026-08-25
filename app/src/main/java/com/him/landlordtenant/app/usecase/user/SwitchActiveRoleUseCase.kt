package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRoleData
import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Switches the currently active role of a user.
 *
 * A user may have multiple assigned roles, but only one role
 * should normally be active for a particular application session.
 */
class SwitchActiveRoleUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        roleId: String
    ): Result<UserRoleData> {

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
            userRepository.switchActiveRole(
                userId = cleanUserId,
                roleId = cleanRoleId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}