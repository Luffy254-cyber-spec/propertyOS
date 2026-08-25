package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * AGREEMENT ACCEPTANCE
 * =============================================================
 */
data class AgreementAcceptance(
    val agreementId: String = "",
    val userId: String = "",
    val accepted: Boolean = false,
    val timestamp: String = "",
    val comment: String? = null,
    val digitalSignature: DigitalSignature? = null
)
