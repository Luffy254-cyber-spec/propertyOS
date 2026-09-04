package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : ExpenseRepository {

    override suspend fun logExpense(userId: String, expense: CreateExpenseData): Result<String> = try {
        val id = firestoreDataSource.collection("expenses").document().id
        val data = ExpenseData(
            id = id,
            landlordId = userId,
            propertyId = expense.propertyId,
            category = expense.category,
            amount = expense.amount,
            description = expense.description,
            date = expense.date
        )
        firestoreDataSource.saveData("expenses", id, data).map { id }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLandlordExpenses(landlordId: String): Result<List<ExpenseData>> = try {
        val snapshot = firestoreDataSource.collection("expenses")
            .whereEqualTo("landlordId", landlordId)
            .get()
            .await()
        Result.success(snapshot.toObjects(ExpenseData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeLandlordExpenses(landlordId: String): Flow<Result<List<ExpenseData>>> = flow {
        emit(getLandlordExpenses(landlordId))
    }

    override suspend fun deleteExpense(landlordId: String, expenseId: String): Result<Unit> = try {
        firestoreDataSource.deleteData("expenses", expenseId)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
