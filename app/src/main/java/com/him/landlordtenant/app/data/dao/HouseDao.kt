package com.him.landlordtenant.app.data.dao

import androidx.room.*
import com.him.landlordtenant.app.data.entities.HouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(house: HouseEntity)

    @Update
    suspend fun update(house: HouseEntity)

    @Delete
    suspend fun delete(house: HouseEntity)

    @Query("SELECT * FROM houses WHERE id = :id")
    suspend fun getById(id: String): HouseEntity?

    @Query("SELECT * FROM houses")
    fun getAll(): Flow<List<HouseEntity>>
}



