package com.him.landlordtenant.app.usecase.bill

import com.him.landlordtenant.app.interfaces.BillDetailsData
import com.him.landlordtenant.app.interfaces.BillRepository

class GetLandlordBillsUseCase(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(landlordId: String): Result<List<BillDetailsData>> {
        return try {
            billRepository.getLandlordBills(landlordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
