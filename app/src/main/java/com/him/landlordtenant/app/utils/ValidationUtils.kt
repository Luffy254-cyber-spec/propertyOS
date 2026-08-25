package com.him.landlordtenant.app.utils

import android.util.Patterns

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        // Basic Kenyan phone validation (supports +254..., 07..., 01...)
        val regex = Regex("^(?:\\+254|254|0)(?:7|1)[0-9]{8}$")
        return phone.isNotBlank() && regex.matches(phone.replace(" ", ""))
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun getPasswordStrength(password: String): Int {
        if (password.isEmpty()) return 0
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        
        return when {
            score <= 1 -> 1 // Weak
            score <= 2 -> 2 // Medium
            else -> 3 // Strong
        }
    }
}
