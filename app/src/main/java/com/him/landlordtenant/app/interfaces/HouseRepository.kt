package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.House
import kotlinx.coroutines.flow.Flow

interface HouseRepository {
    fun getHouses(): Flow<List<House>>
    suspend fun getHouseById(id: String): House?
    suspend fun saveHouse(house: House): Result<Unit>
    suspend fun deleteHouse(house: House): Result<Unit>
}
