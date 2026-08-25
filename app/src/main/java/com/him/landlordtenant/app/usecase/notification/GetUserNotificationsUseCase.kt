package com.him.landlordtenant.app.usecase.notification

import com.him.landlordtenant.app.interfaces.NotificationData
import com.him.landlordtenant.app.interfaces.NotificationRepository

class GetUserNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        userId: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Result<List<NotificationData>> {
        return try {
            notificationRepository.getUserNotifications(userId, page, pageSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
