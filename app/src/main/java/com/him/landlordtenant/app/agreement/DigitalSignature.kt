package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * DIGITAL SIGNATURE
 * =============================================================
 */
data class DigitalSignature(
    val userId: String = "",
    val userName: String = "",
    val userRole: String = "",
    val timestamp: String = "",
    val ipAddress: String? = null,
    val deviceId: String? = null,
    val signatureHash: String = "", // Cryptographic hash of the agreement content + user secret
    val signatureImageUrl: String? = null // Optional URL to a hand-drawn signature image
)
