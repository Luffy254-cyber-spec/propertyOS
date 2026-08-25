package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * AGREEMENT VERSION
 * =============================================================
 */
data class AgreementVersion(
    val versionNumber: String = "1.0",
    val changes: String? = null,
    val createdAt: String = "",
    val createdBy: String = "",
    val active: Boolean = true
)
