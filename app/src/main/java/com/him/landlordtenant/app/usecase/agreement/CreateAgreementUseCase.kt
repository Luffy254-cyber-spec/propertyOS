package com.him.landlordtenant.app.usecase.agreement

import com.him.landlordtenant.app.interfaces.AgreementCreateData
import com.him.landlordtenant.app.interfaces.AgreementRepository

class CreateAgreementUseCase(
    private val agreementRepository: AgreementRepository
) {
    suspend operator fun invoke(
        actorId: String,
        agreementData: AgreementCreateData
    ): Result<String> {
        return try {
            agreementRepository.createAgreement(actorId, agreementData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
