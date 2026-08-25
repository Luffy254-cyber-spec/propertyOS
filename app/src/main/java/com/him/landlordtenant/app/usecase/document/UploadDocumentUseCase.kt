package com.him.landlordtenant.app.usecase.document

import com.him.landlordtenant.app.interfaces.DocumentRepository
import com.him.landlordtenant.app.interfaces.UploadDocumentData

class UploadDocumentUseCase(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(
        userId: String,
        documentData: UploadDocumentData
    ): Result<String> {
        return try {
            documentRepository.uploadDocument(userId, documentData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
