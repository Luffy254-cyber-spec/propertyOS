package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * AGREEMENT AUDIT LOG
 * =============================================================
 */
data class AgreementAudit(
    val id: String = "",
    val agreementId: String = "",
    val action: String = "", // CREATED, UPDATED, SIGNED, TERMINATED, etc.
    val userId: String = "",
    val userName: String = "",
    val timestamp: String = "",
    val metadata: Map<String, String> = emptyMap(),
    val oldStatus: AgreementStatus? = null,
    val newStatus: AgreementStatus? = null
)
