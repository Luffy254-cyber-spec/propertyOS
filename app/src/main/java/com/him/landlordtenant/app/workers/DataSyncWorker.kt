package com.him.landlordtenant.app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.him.landlordtenant.app.data.dao.MaintenanceDao
import com.him.landlordtenant.app.data.model.SyncStatus
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val maintenanceDao: MaintenanceDao,
    private val firestoreDataSource: FirestoreDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("DataSyncWorker", "Starting background data synchronization...")
        
        return try {
            syncPendingMaintenanceRequests()
            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Error syncing data", e)
            Result.retry()
        }
    }

    private suspend fun syncPendingMaintenanceRequests() {
        val pending = maintenanceDao.getPendingSync()
        Log.d("DataSyncWorker", "Found ${pending.size} pending maintenance requests")
        
        pending.forEach { entity ->
            try {
                val data = mapOf(
                    "id" to entity.id,
                    "tenantId" to entity.tenantId,
                    "title" to entity.title,
                    "description" to entity.description,
                    "category" to entity.category.name,
                    "priority" to entity.priority.name,
                    "status" to entity.status.name,
                    "createdAt" to entity.createdAt?.toLongOrNull()
                )
                firestoreDataSource.saveData("maintenance_requests", entity.id, data)
                
                // Update local status to SYNCED
                maintenanceDao.update(entity.copy(syncStatus = SyncStatus.SYNCED))
                Log.d("DataSyncWorker", "Synced request ${entity.id}")
            } catch (e: Exception) {
                Log.e("DataSyncWorker", "Failed to sync request ${entity.id}", e)
            }
        }
    }
}
