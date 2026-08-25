package com.him.landlordtenant.app.usecase.notification

import com.him.landlordtenant.app.interfaces.NotificationRepository

class MarkNotificationReadUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String, notificationId: String): Result<Unit> {
        return try {
            notificationRepository.markAsRead(userId, notificationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
