package com.him.landlordtenant.app.usecase.intergration

import com.him.landlordtenant.app.interfaces.GeocodeRequest
import com.him.landlordtenant.app.interfaces.GeocodeResultData
import com.him.landlordtenant.app.interfaces.IntegrationRepository

class GeocodeAddressUseCase(
    private val integrationRepository: IntegrationRepository
) {
    suspend operator fun invoke(address: String): Result<GeocodeResultData> {
        return try {
            integrationRepository.geocodeAddress(GeocodeRequest(address))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
