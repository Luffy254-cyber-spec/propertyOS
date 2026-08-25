package com.him.landlordtenant.app.usecase.security

import com.him.landlordtenant.app.interfaces.SecurityRepository
import com.him.landlordtenant.app.interfaces.SecuritySessionData

class GetActiveSessionsUseCase(
    private val securityRepository: SecurityRepository
) {
    suspend operator fun invoke(userId: String): Result<List<SecuritySessionData>> {
        return try {
            securityRepository.getActiveSessions(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
