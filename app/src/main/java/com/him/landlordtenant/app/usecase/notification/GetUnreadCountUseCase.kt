package com.him.landlordtenant.app.usecase.notification

import com.him.landlordtenant.app.interfaces.NotificationRepository

class GetUnreadCountUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String): Result<Int> {
        return try {
            notificationRepository.getUnreadCount(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
