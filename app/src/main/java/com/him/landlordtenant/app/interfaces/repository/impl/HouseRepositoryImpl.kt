package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.HouseDao
import com.him.landlordtenant.app.data.entities.toDomain
import com.him.landlordtenant.app.data.entities.toEntity
import com.him.landlordtenant.app.data.model.House
import com.him.landlordtenant.app.interfaces.HouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(
    private val houseDao: HouseDao
) : HouseRepository {

    override fun getHouses(): Flow<List<House>> {
        return houseDao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getHouseById(id: String): House? {
        return houseDao.getById(id)?.toDomain()
    }

    override suspend fun saveHouse(house: House): Result<Unit> {
        return try {
            houseDao.insert(house.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteHouse(house: House): Result<Unit> {
        return try {
            houseDao.delete(house.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}