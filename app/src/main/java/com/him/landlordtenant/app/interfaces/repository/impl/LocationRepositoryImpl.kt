package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.interfaces.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor() : LocationRepository {
    override suspend fun getCurrentLocation(): Result<Pair<Double, Double>> {
        return Result.success(0.0 to 0.0) // Mock
    }

    override fun observeLocation(): Flow<Pair<Double, Double>> = flow {
        emit(0.0 to 0.0)
    }
}
