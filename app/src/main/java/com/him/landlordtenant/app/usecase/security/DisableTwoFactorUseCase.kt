package com.him.landlordtenant.app.usecase.security

import com.him.landlordtenant.app.interfaces.SecurityRepository

class DisableTwoFactorUseCase(
    private val securityRepository: SecurityRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            securityRepository.disableTwoFactor(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
