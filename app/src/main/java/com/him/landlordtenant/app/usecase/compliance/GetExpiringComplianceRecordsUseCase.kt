package com.him.landlordtenant.app.usecase.compliance

import com.him.landlordtenant.app.interfaces.ComplianceRecordData
import com.him.landlordtenant.app.interfaces.ComplianceRepository
import com.him.landlordtenant.app.interfaces.ComplianceScope

class GetExpiringComplianceRecordsUseCase(
    private val complianceRepository: ComplianceRepository
) {
    suspend operator fun invoke(
        scope: ComplianceScope,
        withinDays: Int = 30
    ): Result<List<ComplianceRecordData>> {
        return try {
            complianceRepository.getExpiringRecords(scope, withinDays)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
