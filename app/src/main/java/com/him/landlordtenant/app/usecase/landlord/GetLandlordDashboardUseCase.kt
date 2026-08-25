package com.him.landlordtenant.app.usecase.landlord

import com.him.landlordtenant.app.interfaces.LandlordDashboardData
import com.him.landlordtenant.app.interfaces.LandlordRepository

class GetLandlordDashboardUseCase(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(landlordId: String): Result<LandlordDashboardData> {
        return try {
            landlordRepository.getDashboard(landlordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
