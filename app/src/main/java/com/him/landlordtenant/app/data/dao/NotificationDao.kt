package com.him.landlordtenant.app.data.dao

import androidx.room.*
import com.him.landlordtenant.app.data.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Update
    suspend fun update(notification: NotificationEntity)

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getById(id: String): NotificationEntity?

    @Query("SELECT * FROM notifications")
    fun getAll(): Flow<List<NotificationEntity>>
}



