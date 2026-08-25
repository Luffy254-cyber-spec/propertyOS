package com.him.landlordtenant.app.usecase.bill

import com.him.landlordtenant.app.interfaces.BillDetailsData
import com.him.landlordtenant.app.interfaces.BillRepository

class GetTenantBillsUseCase(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(tenantId: String): Result<List<BillDetailsData>> {
        return try {
            billRepository.getTenantBills(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
