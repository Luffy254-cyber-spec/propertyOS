package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.CreateLeaseData
import com.him.landlordtenant.app.interfaces.LeaseRepository

class CreateLeaseUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(
        createdBy: String,
        leaseData: CreateLeaseData
    ): Result<String> {
        return try {
            leaseRepository.createLease(createdBy, leaseData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
