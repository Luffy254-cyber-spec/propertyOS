package com.him.landlordtenant.app.network

import com.him.landlordtenant.app.network.dto.MpesaRequestDto
import com.him.landlordtenant.app.network.dto.MpesaResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentAPI {
    @POST("payments/mpesa/stkpush")
    suspend fun initiateMpesaStkPush(@Body request: MpesaRequestDto): MpesaResponseDto

    @POST("payments/airtel/stkpush")
    suspend fun initiateAirtelStkPush(@Body request: MpesaRequestDto): MpesaResponseDto
}
