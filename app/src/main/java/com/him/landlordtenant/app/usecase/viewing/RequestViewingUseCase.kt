package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.CreateViewingRequestData
import com.him.landlordtenant.app.interfaces.ViewingRepository

class RequestViewingUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(
        userId: String,
        request: CreateViewingRequestData
    ): Result<String> {
        if (request.propertyId.isBlank()) {
            return Result.failure(IllegalArgumentException("Property ID is required."))
        }
        return try {
            viewingRepository.createViewingRequest(userId, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
