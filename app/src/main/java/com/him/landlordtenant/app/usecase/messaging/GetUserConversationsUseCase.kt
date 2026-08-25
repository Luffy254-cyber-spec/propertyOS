package com.him.landlordtenant.app.usecase.messaging

import com.him.landlordtenant.app.interfaces.ConversationSummaryData
import com.him.landlordtenant.app.interfaces.MessageRepository

class GetUserConversationsUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(userId: String): Result<List<ConversationSummaryData>> {
        return try {
            messageRepository.getUserConversations(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
