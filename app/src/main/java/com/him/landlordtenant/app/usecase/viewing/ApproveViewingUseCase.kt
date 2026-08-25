package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.ViewingRepository

class ApproveViewingUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(
        landlordId: String,
        viewingId: String
    ): Result<Unit> {
        return try {
            viewingRepository.approveViewing(landlordId, viewingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
