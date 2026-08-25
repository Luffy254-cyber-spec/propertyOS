package com.him.landlordtenant.app.usecase.security

import com.him.landlordtenant.app.interfaces.SecurityDashboardData
import com.him.landlordtenant.app.interfaces.SecurityRepository

class GetSecurityDashboardUseCase(
    private val securityRepository: SecurityRepository
) {
    suspend operator fun invoke(userId: String): Result<SecurityDashboardData> {
        return try {
            securityRepository.getSecurityDashboard(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
