package com.him.landlordtenant.app.data.remote

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val database: FirebaseDatabase
) {
    suspend fun <T> writeData(path: String, data: T): Result<Unit> = try {
        database.getReference(path).setValue(data).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun <T> readData(path: String, clazz: Class<T>): Result<T?> = try {
        val snapshot = database.getReference(path).get().await()
        Result.success(snapshot.getValue(clazz))
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getReference(path: String) = database.getReference(path)
}
