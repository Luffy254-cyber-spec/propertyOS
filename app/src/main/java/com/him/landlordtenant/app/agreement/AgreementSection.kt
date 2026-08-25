package com.him.landlordtenant.app.agreement

/**
 * =============================================================
 * AGREEMENT SECTION
 * =============================================================
 */
data class AgreementSection(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val type: AgreementSectionType = AgreementSectionType.STANDARD,
    val order: Int = 0,
    val mandatory: Boolean = true,
    val variables: Map<String, String> = emptyMap() // Placeholders like {{TENANT_NAME}}
)

enum class AgreementSectionType {
    STANDARD,
    FINANCIAL,
    LEGAL,
    TERMINATION,
    UTILITIES,
    RULES,
    CUSTOM
}
