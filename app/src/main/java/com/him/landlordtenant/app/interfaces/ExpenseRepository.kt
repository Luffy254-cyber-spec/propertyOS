package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * EXPENSE REPOSITORY
 * =============================================================
 *
 * Handles property and landlord expenses.
 *
 * Supports:
 * - Property expenses
 * - Unit expenses
 * - Utility expenses
 * - Repairs
 * - Maintenance costs
 * - Contractor payments
 * - Staff/caretaker expenses
 * - Taxes and government fees
 * - Insurance
 * - Security expenses
 * - Recurring expenses
 * - Expense approvals
 * - Receipts
 * - Budgets
 * - Financial summaries
 * - Property profitability
 * - Expense analytics
 *
 * =============================================================
 */

interface ExpenseRepository {

    /*
     * ---------------------------------------------------------
     * EXPENSE CREATION
     * ---------------------------------------------------------
     */

    suspend fun createExpense(
        userId: String,
        expense: CreateExpenseData
    ): Result<String>

    suspend fun getExpense(
        expenseId: String
    ): Result<ExpenseData>

    suspend fun updateExpense(
        userId: String,
        expenseId: String,
        expense: UpdateExpenseData
    ): Result<Unit>

    suspend fun deleteExpense(
        userId: String,
        expenseId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY EXPENSES
     * ---------------------------------------------------------
     */

    suspend fun getPropertyExpenses(
        propertyId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<ExpenseData>>

    fun observePropertyExpenses(
        propertyId: String
    ): Flow<Result<List<ExpenseData>>>

    suspend fun getUnitExpenses(
        unitId: String
    ): Result<List<ExpenseData>>

    suspend fun getLandlordExpenses(
        landlordId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<ExpenseData>>


    /*
     * ---------------------------------------------------------
     * EXPENSE CATEGORIES
     * ---------------------------------------------------------
     */

    suspend fun getExpenseCategories(): Result<List<ExpenseCategoryData>>

    suspend fun createExpenseCategory(
        userId: String,
        category: CreateExpenseCategoryData
    ): Result<String>

    suspend fun updateExpenseCategory(
        userId: String,
        categoryId: String,
        category: UpdateExpenseCategoryData
    ): Result<Unit>

    suspend fun deleteExpenseCategory(
        userId: String,
        categoryId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EXPENSE RECEIPTS
     * ---------------------------------------------------------
     */

    suspend fun uploadReceipt(
        userId: String,
        expenseId: String,
        filePath: String
    ): Result<String>

    suspend fun getReceipts(
        expenseId: String
    ): Result<List<ExpenseReceiptData>>

    suspend fun deleteReceipt(
        userId: String,
        receiptId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * APPROVAL WORKFLOW
     * ---------------------------------------------------------
     */

    suspend fun submitForApproval(
        userId: String,
        expenseId: String
    ): Result<Unit>

    suspend fun approveExpense(
        approverId: String,
        expenseId: String,
        comment: String?
    ): Result<Unit>

    suspend fun rejectExpense(
        approverId: String,
        expenseId: String,
        reason: String
    ): Result<Unit>

    suspend fun getPendingApprovals(
        userId: String
    ): Result<List<ExpenseData>>


    /*
     * ---------------------------------------------------------
     * CONTRACTOR PAYMENTS
     * ---------------------------------------------------------
     */

    suspend fun createContractorExpense(
        userId: String,
        contractorId: String,
        expense: ContractorExpenseData
    ): Result<String>

    suspend fun getContractorExpenses(
        contractorId: String
    ): Result<List<ExpenseData>>


    /*
     * ---------------------------------------------------------
     * RECURRING EXPENSES
     * ---------------------------------------------------------
     */

    suspend fun createRecurringExpense(
        userId: String,
        recurringExpense: CreateRecurringExpenseData
    ): Result<String>

    suspend fun updateRecurringExpense(
        userId: String,
        recurringExpenseId: String,
        expense: UpdateRecurringExpenseData
    ): Result<Unit>

    suspend fun pauseRecurringExpense(
        userId: String,
        recurringExpenseId: String
    ): Result<Unit>

    suspend fun resumeRecurringExpense(
        userId: String,
        recurringExpenseId: String
    ): Result<Unit>

    suspend fun cancelRecurringExpense(
        userId: String,
        recurringExpenseId: String
    ): Result<Unit>

    suspend fun getRecurringExpenses(
        propertyId: String
    ): Result<List<RecurringExpenseData>>


    /*
     * ---------------------------------------------------------
     * BUDGETS
     * ---------------------------------------------------------
     */

    suspend fun createBudget(
        userId: String,
        budget: CreateBudgetData
    ): Result<String>

    suspend fun updateBudget(
        userId: String,
        budgetId: String,
        budget: UpdateBudgetData
    ): Result<Unit>

    suspend fun getPropertyBudgets(
        propertyId: String
    ): Result<List<BudgetData>>

    suspend fun getBudgetProgress(
        budgetId: String
    ): Result<BudgetProgressData>


    /*
     * ---------------------------------------------------------
     * EXPENSE SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchExpenses(
        userId: String,
        query: String,
        propertyId: String? = null
    ): Result<List<ExpenseData>>


    /*
     * ---------------------------------------------------------
     * FINANCIAL SUMMARIES
     * ---------------------------------------------------------
     */

    suspend fun getExpenseSummary(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<ExpenseSummaryData>

    suspend fun getMonthlyExpenseSummary(
        propertyId: String,
        year: Int,
        month: Int
    ): Result<ExpenseSummaryData>

    suspend fun getYearlyExpenseSummary(
        propertyId: String,
        year: Int
    ): Result<ExpenseSummaryData>


    /*
     * ---------------------------------------------------------
     * PROFITABILITY
     * ---------------------------------------------------------
     */

    suspend fun getPropertyProfitability(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyProfitabilityData>

    suspend fun getLandlordProfitability(
        landlordId: String,
        startDate: String,
        endDate: String
    ): Result<LandlordProfitabilityData>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getExpenseAnalytics(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<ExpenseAnalyticsData>


    /*
     * ---------------------------------------------------------
     * EXPORT
     * ---------------------------------------------------------
     */

    suspend fun exportExpenses(
        userId: String,
        propertyId: String,
        startDate: String,
        endDate: String,
        format: ExpenseExportFormat
    ): Result<String>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateExpenseData(
    val propertyId: String,
    val unitId: String?,
    val categoryId: String,
    val title: String,
    val description: String?,
    val amount: Double,
    val currency: String = "KES",
    val expenseDate: String,
    val vendorName: String?,
    val vendorId: String?,
    val paymentMethod: ExpensePaymentMethod,
    val isRecurring: Boolean = false,
    val notes: String?
)

data class UpdateExpenseData(
    val categoryId: String?,
    val title: String?,
    val description: String?,
    val amount: Double?,
    val expenseDate: String?,
    val vendorName: String?,
    val vendorId: String?,
    val paymentMethod: ExpensePaymentMethod?,
    val notes: String?
)

data class ExpenseData(
    val id: String,
    val propertyId: String,
    val unitId: String?,
    val categoryId: String,
    val categoryName: String?,
    val title: String,
    val description: String?,
    val amount: Double,
    val currency: String,
    val expenseDate: String,
    val vendorName: String?,
    val vendorId: String?,
    val paymentMethod: ExpensePaymentMethod,
    val status: ExpenseStatus,
    val isRecurring: Boolean,
    val receiptCount: Int,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String
)

data class ExpenseCategoryData(
    val id: String,
    val name: String,
    val description: String?,
    val icon: String?,
    val active: Boolean = true
)

data class CreateExpenseCategoryData(
    val name: String,
    val description: String?,
    val icon: String?
)

data class UpdateExpenseCategoryData(
    val name: String?,
    val description: String?,
    val icon: String?,
    val active: Boolean?
)

data class ExpenseReceiptData(
    val id: String,
    val expenseId: String,
    val fileName: String,
    val fileUrl: String,
    val mimeType: String?,
    val uploadedAt: String
)

data class ContractorExpenseData(
    val propertyId: String,
    val unitId: String?,
    val title: String,
    val description: String?,
    val amount: Double,
    val workOrderId: String?,
    val paymentDate: String
)

data class CreateRecurringExpenseData(
    val propertyId: String,
    val unitId: String?,
    val categoryId: String,
    val title: String,
    val amount: Double,
    val frequency: RecurringExpenseFrequency,
    val nextDueDate: String,
    val vendorName: String?
)

data class UpdateRecurringExpenseData(
    val title: String?,
    val amount: Double?,
    val frequency: RecurringExpenseFrequency?,
    val nextDueDate: String?,
    val vendorName: String?
)

data class RecurringExpenseData(
    val id: String,
    val propertyId: String,
    val unitId: String?,
    val categoryId: String,
    val title: String,
    val amount: Double,
    val frequency: RecurringExpenseFrequency,
    val nextDueDate: String,
    val active: Boolean,
    val vendorName: String?
)

data class CreateBudgetData(
    val propertyId: String,
    val categoryId: String?,
    val name: String,
    val amount: Double,
    val period: BudgetPeriod,
    val startDate: String,
    val endDate: String
)

data class UpdateBudgetData(
    val name: String?,
    val amount: Double?,
    val period: BudgetPeriod?,
    val startDate: String?,
    val endDate: String?
)

data class BudgetData(
    val id: String,
    val propertyId: String,
    val categoryId: String?,
    val name: String,
    val amount: Double,
    val spent: Double,
    val remaining: Double,
    val period: BudgetPeriod,
    val startDate: String,
    val endDate: String
)

data class BudgetProgressData(
    val budgetId: String,
    val budgetAmount: Double,
    val spentAmount: Double,
    val remainingAmount: Double,
    val percentageUsed: Double,
    val overBudget: Boolean
)

data class ExpenseSummaryData(
    val totalExpenses: Double,
    val expenseCount: Int,
    val averageExpense: Double,
    val largestExpense: Double,
    val categoryBreakdown: List<ExpenseCategorySummaryData>
)

data class ExpenseCategorySummaryData(
    val categoryId: String,
    val categoryName: String,
    val amount: Double,
    val percentage: Double
)


data class LandlordProfitabilityData(
    val landlordId: String,
    val totalRentalIncome: Double,
    val totalOtherIncome: Double,
    val totalExpenses: Double,
    val netIncome: Double,
    val propertyCount: Int
)

data class ExpenseAnalyticsData(
    val totalExpenses: Double,
    val monthlyAverage: Double,
    val maintenanceExpenses: Double,
    val utilityExpenses: Double,
    val contractorExpenses: Double,
    val staffExpenses: Double,
    val taxExpenses: Double,
    val insuranceExpenses: Double,
    val otherExpenses: Double,
    val topExpenseCategory: String?,
    val topVendor: String?
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ExpenseStatus {

    DRAFT,

    PENDING_APPROVAL,

    APPROVED,

    REJECTED,

    PAID,

    CANCELLED
}

enum class ExpensePaymentMethod {

    CASH,

    MPESA,

    BANK_TRANSFER,

    CARD,

    CHEQUE,

    OTHER
}

enum class RecurringExpenseFrequency {

    DAILY,

    WEEKLY,

    MONTHLY,

    QUARTERLY,

    SEMI_ANNUALLY,

    ANNUALLY
}

enum class BudgetPeriod {

    MONTHLY,

    QUARTERLY,

    SEMI_ANNUALLY,

    ANNUALLY,

    CUSTOM
}

enum class ExpenseExportFormat {

    PDF,

    CSV,

    EXCEL
}