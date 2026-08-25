package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.Apartment
import kotlinx.coroutines.flow.Flow

interface ApartmentRepository {
    fun getApartments(): Flow<List<Apartment>>
    suspend fun getApartmentById(id: String): Apartment?
    suspend fun saveApartment(apartment: Apartment): Result<Unit>
    suspend fun deleteApartment(apartment: Apartment): Result<Unit>
    suspend fun syncApartments(): Result<Unit>
}
