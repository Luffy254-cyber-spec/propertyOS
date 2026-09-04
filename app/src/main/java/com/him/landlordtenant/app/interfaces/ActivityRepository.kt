package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun logActivity(userId: String, activity: CreateActivityData): Result<String>
    suspend fun getUserActivities(userId: String, limit: Int = 20): Result<List<ActivityLogData>>
    fun observeUserActivities(userId: String): Flow<Result<List<ActivityLogData>>>
}

data class CreateActivityData(
    val title: String,
    val subtitle: String,
    val amount: String? = null,
    val type: String, // "PROPERTY", "TENANT", "PAYMENT", "MAINTENANCE", "SYSTEM"
    val status: String = "SUCCESS" // "SUCCESS", "FAILURE", "PENDING"
)

data class ActivityLogData(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val amount: String? = null,
    val type: String = "",
    val status: String = "",
    val timestamp: Long = 0L
)
