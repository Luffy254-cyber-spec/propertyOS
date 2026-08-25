package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * DOCUMENT REPOSITORY
 * =============================================================
 */

interface DocumentRepository {

    suspend fun uploadImage(uri: String): Result<String>

    suspend fun uploadDocument(userId: String, document: UploadDocumentData): Result<String>

    suspend fun getDocument(documentId: String): Result<DocumentData>

    suspend fun updateDocument(userId: String, documentId: String, document: UpdateDocumentData): Result<Unit>

    suspend fun deleteDocument(userId: String, documentId: String): Result<Unit>

    suspend fun getUserDocuments(userId: String): Result<List<DocumentData>>

    fun observeUserDocuments(userId: String): Flow<Result<List<DocumentData>>>

    suspend fun getVerificationStatus(documentId: String): Result<DocumentVerificationStatus>
}

data class UploadDocumentData(
    val type: String,
    val fileName: String,
    val fileUrl: String,
    val description: String? = null
)

data class UpdateDocumentData(
    val description: String?,
    val verificationStatus: DocumentVerificationStatus?
)

data class DocumentData(
    val id: String,
    val type: String,
    val fileName: String,
    val fileUrl: String,
    val description: String?,
    val verificationStatus: DocumentVerificationStatus,
    val createdAt: String
)
