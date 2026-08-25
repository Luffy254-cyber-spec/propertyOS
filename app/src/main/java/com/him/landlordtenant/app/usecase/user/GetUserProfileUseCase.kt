package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository
import com.him.landlordtenant.app.interfaces.UserProfileData

class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfileData> {
        val cleanUserId = userId.trim()
        if (cleanUserId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID is required."))
        }
        return try {
            userRepository.getUserProfile(cleanUserId)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
