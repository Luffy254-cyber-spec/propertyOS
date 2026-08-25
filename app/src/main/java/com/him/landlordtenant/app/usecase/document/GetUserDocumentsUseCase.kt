package com.him.landlordtenant.app.usecase.document

import com.him.landlordtenant.app.interfaces.DocumentData
import com.him.landlordtenant.app.interfaces.DocumentRepository

class GetUserDocumentsUseCase(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(userId: String): Result<List<DocumentData>> {
        return try {
            documentRepository.getUserDocuments(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
