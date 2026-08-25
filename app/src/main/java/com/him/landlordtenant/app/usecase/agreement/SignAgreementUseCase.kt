package com.him.landlordtenant.app.usecase.agreement

import com.him.landlordtenant.app.interfaces.AgreementRepository
import com.him.landlordtenant.app.interfaces.ContractDigitalSignatureData

class SignAgreementUseCase(
    private val agreementRepository: AgreementRepository
) {
    suspend fun signAsTenant(
        tenantId: String,
        agreementId: String,
        signature: ContractDigitalSignatureData
    ): Result<Unit> {
        return try {
            agreementRepository.signAsTenant(tenantId, agreementId, signature)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signAsLandlord(
        landlordId: String,
        agreementId: String,
        signature: ContractDigitalSignatureData
    ): Result<Unit> {
        return try {
            agreementRepository.signAsLandlord(landlordId, agreementId, signature)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
