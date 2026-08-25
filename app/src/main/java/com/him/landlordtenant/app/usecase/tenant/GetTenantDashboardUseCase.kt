package com.him.landlordtenant.app.usecase.tenant

import com.him.landlordtenant.app.interfaces.TenantDashboardData
import com.him.landlordtenant.app.interfaces.TenantRepository

class GetTenantDashboardUseCase(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String): Result<TenantDashboardData> {
        return try {
            tenantRepository.getDashboard(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
