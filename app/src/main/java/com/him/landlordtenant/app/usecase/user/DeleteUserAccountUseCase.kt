package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository

class DeleteUserAccountUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            userRepository.deleteUserAccount(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
