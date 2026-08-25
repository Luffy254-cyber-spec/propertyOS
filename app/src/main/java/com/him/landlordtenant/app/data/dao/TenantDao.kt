package com.him.landlordtenant.app.data.dao

import androidx.room.*
import com.him.landlordtenant.app.data.entities.TenantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tenant: TenantEntity)

    @Update
    suspend fun update(tenant: TenantEntity)

    @Delete
    suspend fun delete(tenant: TenantEntity)

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getById(id: String): TenantEntity?

    @Query("SELECT * FROM tenants")
    fun getAll(): Flow<List<TenantEntity>>
}



