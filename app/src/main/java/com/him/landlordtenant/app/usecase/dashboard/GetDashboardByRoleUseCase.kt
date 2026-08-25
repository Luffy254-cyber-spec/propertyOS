package com.him.landlordtenant.app.usecase.dashboard

import com.him.landlordtenant.app.interfaces.DashboardData
import com.him.landlordtenant.app.interfaces.DashboardRepository
import com.him.landlordtenant.app.interfaces.DashboardRole

class GetDashboardByRoleUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String, role: DashboardRole): Result<DashboardData> {
        return try {
            dashboardRepository.getDashboard(userId, role)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
