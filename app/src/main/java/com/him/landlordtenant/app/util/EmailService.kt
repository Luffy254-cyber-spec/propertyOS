package com.him.landlordtenant.app.util

import javax.inject.Inject
import javax.inject.Singleton

interface EmailService {
    suspend fun sendLoginAlert(email: String, deviceName: String)
    suspend fun sendVerificationEmail(email: String, verificationCode: String)
}

@Singleton
class MockEmailService @Inject constructor() : EmailService {
    override suspend fun sendLoginAlert(email: String, deviceName: String) {
        // In a real app, this would call a backend or SMTP service
        println("EMAIL ALERT: New login to $email from $deviceName")
    }

    override suspend fun sendVerificationEmail(email: String, verificationCode: String) {
        println("EMAIL VERIFICATION: Code $verificationCode sent to $email")
    }
}
