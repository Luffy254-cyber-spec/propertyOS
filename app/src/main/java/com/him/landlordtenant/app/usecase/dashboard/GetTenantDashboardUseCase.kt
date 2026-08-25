package com.him.landlordtenant.app.usecase.dashboard

import com.him.landlordtenant.app.interfaces.DashboardRepository
import com.him.landlordtenant.app.interfaces.GenericTenantDashboardData

class GetTenantDashboardUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String): Result<GenericTenantDashboardData> {
        return try {
            dashboardRepository.getTenantDashboard(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
