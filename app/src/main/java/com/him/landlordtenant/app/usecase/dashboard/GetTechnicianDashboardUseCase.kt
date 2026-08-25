package com.him.landlordtenant.app.usecase.dashboard

import com.him.landlordtenant.app.interfaces.DashboardRepository
import com.him.landlordtenant.app.interfaces.TechnicianDashboardData

class GetTechnicianDashboardUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String): Result<TechnicianDashboardData> {
        return try {
            dashboardRepository.getTechnicianDashboard(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
