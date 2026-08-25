package com.him.landlordtenant.app.usecase.agreement

import com.him.landlordtenant.app.interfaces.AgreementDetailsData
import com.him.landlordtenant.app.interfaces.AgreementRepository

class GetAgreementDetailsUseCase(
    private val agreementRepository: AgreementRepository
) {
    suspend operator fun invoke(agreementId: String): Result<AgreementDetailsData> {
        return try {
            agreementRepository.getAgreement(agreementId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
