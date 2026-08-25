package com.him.landlordtenant.app.ui.screens.tenant

import java.text.NumberFormat
import java.util.Locale

fun formatPaymentMoney(amount: Double): String {
    return try {
        val formatter = NumberFormat.getInstance(Locale.getDefault())
        formatter.format(amount)
    } catch (e: Exception) {
        amount.toString()
    }
}

fun houseTypeName(type: String): String {
    return when (type.uppercase()) {
        "1BR" -> "1 Bedroom"
        "2BR" -> "2 Bedroom"
        "3BR" -> "3 Bedroom"
        "BS" -> "Bedsitter"
        "ST" -> "Studio"
        else -> type
    }
}

fun formatAgreementMoney(amount: Double): String = formatPaymentMoney(amount)
