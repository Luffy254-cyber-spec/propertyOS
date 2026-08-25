package com.him.landlordtenant.app.usecase.arrears

import com.him.landlordtenant.app.interfaces.BillRepository
import com.him.landlordtenant.app.interfaces.ArrearsSummaryData

class GetArrearsUseCase(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(userId: String, isLandlord: Boolean): Result<ArrearsSummaryData> {
        return try {
            if (isLandlord) {
                billRepository.getLandlordArrears(userId)
            } else {
                billRepository.getTenantArrears(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
