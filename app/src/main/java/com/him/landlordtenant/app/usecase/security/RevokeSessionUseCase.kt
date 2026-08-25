package com.him.landlordtenant.app.usecase.security

import com.him.landlordtenant.app.interfaces.SecurityRepository

class RevokeSessionUseCase(
    private val securityRepository: SecurityRepository
) {
    suspend operator fun invoke(actorId: String, sessionId: String): Result<Unit> {
        return try {
            securityRepository.revokeSession(actorId, sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
