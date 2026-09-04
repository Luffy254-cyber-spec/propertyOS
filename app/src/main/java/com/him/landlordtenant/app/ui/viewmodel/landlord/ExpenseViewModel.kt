package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ExpenseData
import com.him.landlordtenant.app.interfaces.ExpenseRepository
import com.him.landlordtenant.app.interfaces.CreateExpenseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository,
    private val activityRepository: com.him.landlordtenant.app.interfaces.ActivityRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseData>>(emptyList())
    val expenses: StateFlow<List<ExpenseData>> = _expenses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            expenseRepository.getLandlordExpenses(userId).onSuccess {
                _expenses.value = it.sortedByDescending { e -> e.date }
            }
            _isLoading.value = false
        }
    }

    fun addExpense(propertyId: String?, category: String, amount: Double, description: String) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val data = CreateExpenseData(propertyId, category, amount, description)
            expenseRepository.logExpense(userId, data).onSuccess {
                activityRepository.logActivity(
                    userId = userId,
                    activity = com.him.landlordtenant.app.interfaces.CreateActivityData(
                        title = "Expense Logged",
                        subtitle = description,
                        amount = "KSh $amount",
                        type = "EXPENSE",
                        status = "SUCCESS"
                    )
                )
                loadExpenses()
            }.onFailure {
                activityRepository.logActivity(
                    userId = userId,
                    activity = com.him.landlordtenant.app.interfaces.CreateActivityData(
                        title = "Expense Logging Failed",
                        subtitle = it.message ?: "Network error",
                        type = "EXPENSE",
                        status = "FAILURE"
                    )
                )
            }
        }
    }
}
