package com.him.landlordtenant.app.usecase.rent

import com.him.landlordtenant.app.interfaces.RentBalanceData
import com.him.landlordtenant.app.interfaces.TenantRepository

class GetRentSummaryUseCase(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String): Result<RentBalanceData> {
        return try {
            tenantRepository.getRentBalance(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
