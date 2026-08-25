package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Pair<Double, Double>>
    fun observeLocation(): Flow<Pair<Double, Double>>
}
