package com.him.landlordtenant.app.usecase.dashboard

import com.him.landlordtenant.app.interfaces.DashboardRepository
import com.him.landlordtenant.app.interfaces.GenericLandlordDashboardData

class GetLandlordDashboardUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String): Result<GenericLandlordDashboardData> {
        return try {
            dashboardRepository.getLandlordDashboard(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
