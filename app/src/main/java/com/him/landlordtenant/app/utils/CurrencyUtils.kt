package com.him.landlordtenant.app.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun formatCurrency(amount: Double, currencyCode: String = "KSh"): String {
        return try {
            val formatter = NumberFormat.getInstance(Locale.getDefault())
            "$currencyCode ${formatter.format(amount)}"
        } catch (e: Exception) {
            "$currencyCode $amount"
        }
    }
}
