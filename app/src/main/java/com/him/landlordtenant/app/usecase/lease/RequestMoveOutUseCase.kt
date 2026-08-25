package com.him.landlordtenant.app.usecase.lease

import com.him.landlordtenant.app.interfaces.LeaseRepository
import com.him.landlordtenant.app.interfaces.MoveOutRequestData

class RequestMoveOutUseCase(
    private val leaseRepository: LeaseRepository
) {
    suspend operator fun invoke(
        tenantId: String,
        leaseId: String,
        request: MoveOutRequestData
    ): Result<String> {
        return try {
            leaseRepository.requestMoveOut(tenantId, leaseId, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
