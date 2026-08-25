package com.him.landlordtenant.app.usecase.analytics

import com.him.landlordtenant.app.interfaces.AnalyticsRepository
import com.him.landlordtenant.app.interfaces.AnalyticsScope
import com.him.landlordtenant.app.interfaces.RevenueAnalyticsData

class GetRevenueAnalyticsUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<RevenueAnalyticsData> {
        return try {
            analyticsRepository.getRevenueAnalytics(scope, startDate, endDate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
