package com.him.landlordtenant.app.usecase.bill

import com.him.landlordtenant.app.interfaces.BillRepository
import com.him.landlordtenant.app.interfaces.CreateBillData

class CreateBillUseCase(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(
        landlordId: String,
        billData: CreateBillData
    ): Result<String> {
        return try {
            billRepository.createBill(landlordId, billData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
