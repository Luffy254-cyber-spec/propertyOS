package com.him.landlordtenant.app.usecase.security

import com.him.landlordtenant.app.interfaces.SecurityRepository
import com.him.landlordtenant.app.interfaces.TwoFactorMethod

class EnableTwoFactorUseCase(
    private val securityRepository: SecurityRepository
) {
    suspend operator fun invoke(userId: String, method: TwoFactorMethod): Result<Unit> {
        return try {
            securityRepository.enableTwoFactor(userId, method)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
