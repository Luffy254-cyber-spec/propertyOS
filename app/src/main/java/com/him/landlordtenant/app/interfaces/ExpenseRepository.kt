package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun logExpense(userId: String, expense: CreateExpenseData): Result<String>
    suspend fun getLandlordExpenses(landlordId: String): Result<List<ExpenseData>>
    fun observeLandlordExpenses(landlordId: String): Flow<Result<List<ExpenseData>>>
    suspend fun deleteExpense(landlordId: String, expenseId: String): Result<Unit>
}

data class CreateExpenseData(
    val propertyId: String?,
    val category: String,
    val amount: Double,
    val description: String,
    val date: Long = System.currentTimeMillis()
)

data class ExpenseData(
    val id: String = "",
    val landlordId: String = "",
    val propertyId: String? = null,
    val propertyName: String? = null,
    val category: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val date: Long = 0L
)
