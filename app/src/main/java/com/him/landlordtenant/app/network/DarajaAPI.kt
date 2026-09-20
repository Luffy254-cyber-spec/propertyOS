package com.him.landlordtenant.app.network

import com.him.landlordtenant.app.network.dto.*
import retrofit2.http.*

interface DarajaAPI {
    @GET("oauth/v1/generate")
    suspend fun getAccessToken(
        @Query("grant_type") grantType: String = "client_credentials",
        @Header("Authorization") authHeader: String
    ): OAuthResponse

    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun initiateStkPush(
        @Header("Authorization") bearerToken: String,
        @Body request: StkPushRequest
    ): StkPushResponse

    @POST("mpesa/stkpushquery/v1/query")
    suspend fun queryStkPushStatus(
        @Header("Authorization") bearerToken: String,
        @Body request: StkPushQueryRequest
    ): StkPushQueryResponse
}

data class OAuthResponse(
    val access_token: String,
    val expires_in: String
)

data class StkPushRequest(
    val BusinessShortCode: String,
    val Password: String,
    val Timestamp: String,
    val TransactionType: String = "CustomerPayBillOnline",
    val Amount: Int,
    val PartyA: String,
    val PartyB: String,
    val PhoneNumber: String,
    val CallBackURL: String,
    val AccountReference: String,
    val TransactionDesc: String
)

data class StkPushResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
)

data class StkPushQueryRequest(
    val BusinessShortCode: String,
    val Password: String,
    val Timestamp: String,
    val CheckoutRequestID: String
)

data class StkPushQueryResponse(
    val ResponseCode: String,
    val ResponseDescription: String,
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResultCode: String? = null,
    val ResultDesc: String? = null
)
