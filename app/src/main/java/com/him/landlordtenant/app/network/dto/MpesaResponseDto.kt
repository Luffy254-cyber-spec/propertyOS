package com.him.landlordtenant.app.network.dto

data class MpesaResponseDto(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
)
