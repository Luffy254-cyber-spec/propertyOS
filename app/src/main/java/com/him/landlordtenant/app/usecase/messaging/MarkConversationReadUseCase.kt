package com.him.landlordtenant.app.usecase.messaging

import com.him.landlordtenant.app.interfaces.MessageRepository

class MarkConversationReadUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(userId: String, conversationId: String): Result<Unit> {
        return try {
            messageRepository.markConversationAsRead(userId, conversationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
