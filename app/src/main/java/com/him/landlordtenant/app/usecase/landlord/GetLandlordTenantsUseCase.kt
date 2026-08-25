package com.him.landlordtenant.app.usecase.landlord

import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.interfaces.TenantSummaryData

class GetLandlordTenantsUseCase(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(landlordId: String): Result<List<TenantSummaryData>> {
        return try {
            landlordRepository.getTenants(landlordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
