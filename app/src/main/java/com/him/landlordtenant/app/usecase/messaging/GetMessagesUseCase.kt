package com.him.landlordtenant.app.usecase.messaging

import com.him.landlordtenant.app.interfaces.MessageData
import com.him.landlordtenant.app.interfaces.MessageRepository

class GetMessagesUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(
        conversationId: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Result<List<MessageData>> {
        return try {
            messageRepository.getMessages(conversationId, page, pageSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
