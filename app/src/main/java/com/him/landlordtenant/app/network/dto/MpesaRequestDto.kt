package com.him.landlordtenant.app.network.dto

data class MpesaRequestDto(
    val phoneNumber: String,
    val amount: Double,
    val accountReference: String,
    val transactionDesc: String
)
