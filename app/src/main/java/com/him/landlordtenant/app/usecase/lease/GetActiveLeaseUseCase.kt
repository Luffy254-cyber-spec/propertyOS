package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.LeaseDetailsData
import com.him.landlordtenant.app.interfaces.LeaseRepository

class GetActiveLeaseUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(tenantId: String): Result<LeaseDetailsData?> {
        return try {
            leaseRepository.getActiveTenantLease(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
