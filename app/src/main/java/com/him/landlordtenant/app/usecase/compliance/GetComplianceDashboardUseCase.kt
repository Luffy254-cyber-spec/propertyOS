package com.him.landlordtenant.app.usecase.compliance

import com.him.landlordtenant.app.interfaces.ComplianceDashboardData
import com.him.landlordtenant.app.interfaces.ComplianceRepository
import com.him.landlordtenant.app.interfaces.ComplianceScope

class GetComplianceDashboardUseCase(
    private val complianceRepository: ComplianceRepository
) {
    suspend operator fun invoke(scope: ComplianceScope): Result<ComplianceDashboardData> {
        return try {
            complianceRepository.getComplianceDashboard(scope)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
