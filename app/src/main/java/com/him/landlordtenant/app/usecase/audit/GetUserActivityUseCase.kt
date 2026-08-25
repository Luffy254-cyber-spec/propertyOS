package com.him.landlordtenant.app.usecase.audit

import com.him.landlordtenant.app.interfaces.AuditLogData
import com.him.landlordtenant.app.interfaces.AuditLogRepository

class GetUserActivityUseCase(
    private val auditLogRepository: AuditLogRepository
) {
    suspend operator fun invoke(userId: String, limit: Int = 100): Result<List<AuditLogData>> {
        return try {
            auditLogRepository.getUserActivity(userId, limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
