package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.LeaseRepository
import com.him.landlordtenant.app.interfaces.LeaseTerminationData

class TerminateLeaseUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(
        userId: String,
        leaseId: String,
        terminationData: LeaseTerminationData
    ): Result<Unit> {
        return try {
            leaseRepository.terminateLease(userId, terminationData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
