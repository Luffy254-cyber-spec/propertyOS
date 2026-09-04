package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.ApartmentDao
import com.him.landlordtenant.app.data.entities.toDomain
import com.him.landlordtenant.app.data.entities.toEntity
import com.him.landlordtenant.app.data.model.Apartment
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.ApartmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApartmentRepositoryImpl @Inject constructor(
    private val apartmentDao: ApartmentDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource
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
            
            // Save to Realtime Database
            firebaseDataSource.writeData("apartments/${apartment.id}", apartment)
            
            // Try Firestore as fallback
            try {
                firestoreDataSource.saveData("apartments", apartment.id, apartment)
            } catch (e: Exception) {
                println("Firestore apartment save failed: ${e.message}")
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteApartment(apartment: Apartment): Result<Unit> {
        return try {
            apartmentDao.delete(apartment.toEntity())
            
            firebaseDataSource.getReference("apartments/${apartment.id}").removeValue()
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