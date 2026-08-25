package com.him.landlordtenant.app.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun <T : Any> saveData(collectionPath: String, documentId: String, data: T): Result<Unit> = try {
        firestore.collection(collectionPath).document(documentId).set(data, SetOptions.merge()).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun <T> getData(collectionPath: String, documentId: String, clazz: Class<T>): Result<T?> = try {
        val snapshot = firestore.collection(collectionPath).document(documentId).get().await()
        Result.success(snapshot.toObject(clazz))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteData(collectionPath: String, documentId: String): Result<Unit> = try {
        firestore.collection(collectionPath).document(documentId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun collection(path: String) = firestore.collection(path)
    fun document(path: String) = firestore.document(path)
}
