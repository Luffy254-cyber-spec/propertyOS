package com.him.landlordtenant.app.usecase.intergration

import com.him.landlordtenant.app.interfaces.CommunicationDeliveryResultData
import com.him.landlordtenant.app.interfaces.IntegrationRepository
import com.him.landlordtenant.app.interfaces.SmsRequest

class SendSmsUseCase(
    private val integrationRepository: IntegrationRepository
) {
    suspend operator fun invoke(
        actorId: String?,
        recipient: String,
        message: String
    ): Result<CommunicationDeliveryResultData> {
        return try {
            integrationRepository.sendSms(actorId, SmsRequest(recipient, message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
