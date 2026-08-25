package com.him.landlordtenant.app.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadFile(path: String, uri: Uri): Result<String> = try {
        val ref = storage.reference.child(path)
        ref.putFile(uri).await()
        val downloadUrl = ref.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteFile(path: String): Result<Unit> = try {
        storage.reference.child(path).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getReference(path: String) = storage.getReference(path)
}
