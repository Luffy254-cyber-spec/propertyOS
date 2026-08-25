package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.LeaseRepository
import com.him.landlordtenant.app.interfaces.LeaseSummaryData

class GetExpiringLeasesUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(
        landlordId: String,
        daysUntilExpiry: Int = 30
    ): Result<List<LeaseSummaryData>> {
        return try {
            leaseRepository.getExpiringLeases(landlordId, daysUntilExpiry)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
