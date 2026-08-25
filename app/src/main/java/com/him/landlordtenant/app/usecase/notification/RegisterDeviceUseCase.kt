package com.him.landlordtenant.app.usecase.notification

import com.him.landlordtenant.app.interfaces.NotificationDeviceData
import com.him.landlordtenant.app.interfaces.NotificationRepository

class RegisterDeviceUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String, device: NotificationDeviceData): Result<String> {
        return try {
            notificationRepository.registerDevice(userId, device)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
