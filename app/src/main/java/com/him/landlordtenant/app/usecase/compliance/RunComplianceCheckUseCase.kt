package com.him.landlordtenant.app.usecase.compliance

import com.him.landlordtenant.app.interfaces.ComplianceCheckResultData
import com.him.landlordtenant.app.interfaces.ComplianceRepository
import com.him.landlordtenant.app.interfaces.ComplianceScope

class RunComplianceCheckUseCase(
    private val complianceRepository: ComplianceRepository
) {
    suspend operator fun invoke(
        actorId: String,
        scope: ComplianceScope
    ): Result<ComplianceCheckResultData> {
        return try {
            complianceRepository.runComplianceCheck(actorId, scope)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
