package com.him.landlordtenant.app.network

import retrofit2.http.*

interface StripeAPI {
    @FormUrlEncoded
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Header("Authorization") authHeader: String,
        @Field("amount") amount: Int,
        @Field("currency") currency: String = "kes",
        @Field("payment_method_types[]") paymentMethodType: String = "card",
        @Field("metadata[paymentId]") paymentId: String
    ): StripePaymentIntentResponse
}

data class StripePaymentIntentResponse(
    val id: String,
    val client_secret: String,
    val amount: Int,
    val currency: String
)
