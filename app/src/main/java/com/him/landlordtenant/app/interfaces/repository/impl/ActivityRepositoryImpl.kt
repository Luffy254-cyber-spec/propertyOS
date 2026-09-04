package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.ActivityLogData
import com.him.landlordtenant.app.interfaces.ActivityRepository
import com.him.landlordtenant.app.interfaces.CreateActivityData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : ActivityRepository {

    override suspend fun logActivity(userId: String, activity: CreateActivityData): Result<String> = try {
        val ref = firebaseDataSource.getReference("activities/$userId").push()
        val id = ref.key ?: ""
        val logData = ActivityLogData(
            id = id,
            userId = userId,
            title = activity.title,
            subtitle = activity.subtitle,
            amount = activity.amount,
            type = activity.type,
            status = activity.status,
            timestamp = System.currentTimeMillis()
        )
        firebaseDataSource.writeData("activities/$userId/$id", logData)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserActivities(userId: String, limit: Int): Result<List<ActivityLogData>> = try {
        val snapshot = firebaseDataSource.getReference("activities/$userId")
            .orderByChild("timestamp")
            .limitToLast(limit)
            .get()
            .await()
        
        val list = snapshot.children.mapNotNull { it.getValue(ActivityLogData::class.java) }
            .sortedByDescending { it.timestamp }
        Result.success(list)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeUserActivities(userId: String): Flow<Result<List<ActivityLogData>>> = callbackFlow {
        val ref = firebaseDataSource.getReference("activities/$userId")
            .orderByChild("timestamp")
            .limitToLast(50)
            
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(ActivityLogData::class.java) }
                    .sortedByDescending { it.timestamp }
                trySend(Result.success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}
