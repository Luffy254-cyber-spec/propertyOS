package com.him.landlordtenant.app.data.dao

import androidx.room.*
import com.him.landlordtenant.app.data.entities.ApartmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apartment: ApartmentEntity)

    @Update
    suspend fun update(apartment: ApartmentEntity)

    @Delete
    suspend fun delete(apartment: ApartmentEntity)

    @Query("SELECT * FROM apartments WHERE id = :id")
    suspend fun getById(id: String): ApartmentEntity?

    @Query("SELECT * FROM apartments")
    fun getAll(): Flow<List<ApartmentEntity>>
}



