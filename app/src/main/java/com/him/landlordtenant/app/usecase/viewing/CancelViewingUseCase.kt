package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.ViewingRepository

class CancelViewingUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(
        userId: String,
        viewingId: String,
        reason: String? = null
    ): Result<Unit> {
        return try {
            viewingRepository.cancelViewing(userId, viewingId, reason)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
