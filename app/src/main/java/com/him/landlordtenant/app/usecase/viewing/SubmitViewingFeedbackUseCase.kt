package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.ViewingFeedbackData
import com.him.landlordtenant.app.interfaces.ViewingRepository

class SubmitViewingFeedbackUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(
        userId: String,
        viewingId: String,
        feedback: ViewingFeedbackData
    ): Result<String> {
        return try {
            viewingRepository.submitViewingFeedback(userId, viewingId, feedback)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
