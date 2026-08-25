package com.him.landlordtenant.app.data.dao

import androidx.room.*
import com.him.landlordtenant.app.data.entities.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: MaintenanceEntity)

    @Update
    suspend fun update(request: MaintenanceEntity)

    @Delete
    suspend fun delete(request: MaintenanceEntity)

    @Query("SELECT * FROM maintenance_requests WHERE id = :id")
    suspend fun getById(id: String): MaintenanceEntity?

    @Query("SELECT * FROM maintenance_requests")
    fun getAll(): Flow<List<MaintenanceEntity>>
    @Query("SELECT * FROM maintenance_requests WHERE tenantId = :tenantId")
    suspend fun getByTenantId(tenantId: String): List<MaintenanceEntity>

    @Query("SELECT * FROM maintenance_requests WHERE syncStatus = 'PENDING'")
    suspend fun getPendingSync(): List<MaintenanceEntity>
}



