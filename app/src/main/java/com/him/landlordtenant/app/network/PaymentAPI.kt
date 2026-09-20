package com.him.landlordtenant.app.network

import com.him.landlordtenant.app.network.dto.MpesaRequestDto
import com.him.landlordtenant.app.network.dto.MpesaResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentAPI {
    @POST("payments/airtel/stkpush")
    suspend fun initiateAirtelStkPush(@Body request: AirtelRequestDto): AirtelResponseDto
}

data class AirtelRequestDto(
    val phoneNumber: String,
    val amount: Double,
    val merchantCode: String,
    val clientId: String,
    val clientSecret: String
)

data class AirtelResponseDto(
    val status: String,
    val message: String,
    val transactionId: String?
)
