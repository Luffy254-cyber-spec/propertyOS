package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.CloudinaryDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val cloudinaryDataSource: CloudinaryDataSource
) : DocumentRepository {

    override suspend fun uploadImage(uri: String): Result<String> {
        val requestId = "media_${System.currentTimeMillis()}"
        return cloudinaryDataSource.uploadImage(uri, requestId)
    }

    override suspend fun uploadDocument(userId: String, document: UploadDocumentData): Result<String> {
        val id = firestoreDataSource.collection("documents").document().id
        return firestoreDataSource.saveData("documents", id, document).map { id }
    }

    override suspend fun getDocument(documentId: String): Result<DocumentData> {
        return firestoreDataSource.getData("documents", documentId, DocumentData::class.java)
            .map { it ?: throw Exception("Document not found") }
    }

    override suspend fun updateDocument(userId: String, documentId: String, document: UpdateDocumentData): Result<Unit> {
        return firestoreDataSource.saveData("documents", documentId, document)
    }

    override suspend fun deleteDocument(userId: String, documentId: String): Result<Unit> {
        return firestoreDataSource.deleteData("documents", documentId)
    }

    override suspend fun getUserDocuments(userId: String): Result<List<DocumentData>> = Result.failure(NotImplementedError())

    override fun observeUserDocuments(userId: String): Flow<Result<List<DocumentData>>> = flow { emit(Result.failure(NotImplementedError())) }

    override suspend fun getVerificationStatus(documentId: String): Result<DocumentVerificationStatus> = Result.failure(NotImplementedError())
}
