package com.him.landlordtenant.app.usecase.dashboard

import com.him.landlordtenant.app.interfaces.AdminDashboardData
import com.him.landlordtenant.app.interfaces.DashboardRepository

class GetAdminDashboardUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String): Result<AdminDashboardData> {
        return try {
            dashboardRepository.getAdminDashboard(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
