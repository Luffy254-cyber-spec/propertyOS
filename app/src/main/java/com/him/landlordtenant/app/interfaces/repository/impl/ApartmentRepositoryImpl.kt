package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.ApartmentDao
import com.him.landlordtenant.app.data.entities.toDomain
import com.him.landlordtenant.app.data.entities.toEntity
import com.him.landlordtenant.app.data.model.Apartment
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.ApartmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApartmentRepositoryImpl @Inject constructor(
    private val apartmentDao: ApartmentDao,
    private val firestoreDataSource: FirestoreDataSource
) : ApartmentRepository {

    override fun getApartments(): Flow<List<Apartment>> {
        return apartmentDao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getApartmentById(id: String): Apartment? {
        return apartmentDao.getById(id)?.toDomain()
    }

    override suspend fun saveApartment(apartment: Apartment): Result<Unit> {
        return try {
            apartmentDao.insert(apartment.toEntity())
            firestoreDataSource.saveData("apartments", apartment.id, apartment)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteApartment(apartment: Apartment): Result<Unit> {
        return try {
            apartmentDao.delete(apartment.toEntity())
            firestoreDataSource.deleteData("apartments", apartment.id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncApartments(): Result<Unit> {
        return Result.success(Unit)
    }
}