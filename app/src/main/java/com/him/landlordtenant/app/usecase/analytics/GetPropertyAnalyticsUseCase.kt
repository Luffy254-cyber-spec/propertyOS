package com.him.landlordtenant.app.usecase.analytics

import com.him.landlordtenant.app.interfaces.AnalyticsRepository
import com.him.landlordtenant.app.interfaces.GeneralPropertyAnalyticsData

class GetPropertyAnalyticsUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(
        propertyId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<GeneralPropertyAnalyticsData> {
        return try {
            analyticsRepository.getPropertyAnalytics(propertyId, startDate, endDate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
