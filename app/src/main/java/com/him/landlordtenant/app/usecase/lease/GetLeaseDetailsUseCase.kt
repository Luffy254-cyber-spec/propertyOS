package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.LeaseDetailsData
import com.him.landlordtenant.app.interfaces.LeaseRepository

class GetLeaseDetailsUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(leaseId: String): Result<LeaseDetailsData> {
        return try {
            leaseRepository.getLease(leaseId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
