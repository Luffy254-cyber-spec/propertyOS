package com.him.landlordtenant.app.interfaces

data class CreateLeaseData(
    val tenantId: String,
    val propertyId: String,
    val unitId: String,
    val startDate: String,
    val endDate: String?,
    val monthlyRent: Double,
    val securityDeposit: Double
)

data class LeaseTerminationData(
    val leaseId: String,
    val reason: String,
    val terminationDate: String
)

data class UserDataExportData(
    val url: String,
    val expiresAt: String
)

data class UserRoleData(
    val roleId: String,
    val roleName: String
)
