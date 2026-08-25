package com.him.landlordtenant.app.usecase.messaging

import com.him.landlordtenant.app.interfaces.MessageRepository
import com.him.landlordtenant.app.interfaces.SendMessageData

class SendMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(
        senderId: String,
        conversationId: String,
        message: SendMessageData
    ): Result<String> {
        if (message.content.isBlank()) {
            return Result.failure(IllegalArgumentException("Message content cannot be empty."))
        }
        return try {
            messageRepository.sendMessage(senderId, conversationId, message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
