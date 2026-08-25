package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository
import com.him.landlordtenant.app.interfaces.UserProfileData

class UpdateUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, profile: UserProfileData): Result<Unit> {
        return try {
            userRepository.updateUserProfile(userId, profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
