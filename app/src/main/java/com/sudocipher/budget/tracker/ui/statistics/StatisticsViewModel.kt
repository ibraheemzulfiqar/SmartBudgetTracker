package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import com.sudocipher.budget.tracker.domain.model.Category
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.util.Currency
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    repository: BudgetRepository,
    preferenceStore: PreferenceStore,
) : ViewModel() {

    val selectedParentCategory: StateFlow<Category?>
        field  = MutableStateFlow<Category?>(null)

    val state: StateFlow<StatisticsState> = combine(
        repository.getAllTransactions(),
        selectedParentCategory,
        preferenceStore.preference
    ) { transactions, selectedParent, preference ->
        val currencySymbol = preference.currencyCode?.let {
            Currency.getInstance(it).symbol
        } ?: ""
        
        // Generate last 6 months labels
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val last6Months = (0..5).map { i ->
            var year = now.year
            var month = now.month.number - (5 - i)

            while (month <= 0) {
                month += 12
                year -= 1
            }

            "${year}-${month.toString().padStart(2, '0')}"
        }

        if (transactions.isEmpty()) {
            val emptyMonthlyStats = last6Months.map { MonthlyStat(it, 0.0, 0.0) }
            StatisticsState.Success(
                totalIncome = 0.0,
                totalExpense = 0.0,
                categoryStats = emptyList(),
                monthlyStats = emptyMonthlyStats,
                currencySymbol = currencySymbol
            )
        } else {

            val expenseTransactions = transactions.fastFilter {
                it.type == TransactionType.EXPENSE
            }


            val income = transactions.fastFilter {
                it.type == TransactionType.INCOME
            }.sumOf { it.amount }

            val expense = expenseTransactions.sumOf { it.amount }


            val categoryGroups = if (selectedParent == null) {
                // Show root-level categories
                expenseTransactions
                    .groupBy { it.category.rootParent }
                    .map { (root, rootTransactions) ->
                        val amount = rootTransactions.sumOf { it.amount }
                        CategoryStat(
                            category = root,
                            amount = amount,
                            percentage = if (expense > 0) (amount / expense).toFloat() else 0f
                        )
                    }
            } else {
                // Show subcategories of selected parent
                val parentExpense = expenseTransactions
                    .filter { it.category.rootParent.id == selectedParent.id }
                    .sumOf { it.amount }

                expenseTransactions
                    .filter { it.category.rootParent.id == selectedParent.id }
                    .groupBy { it.category }
                    .map { (category, categoryTransactions) ->
                        val amount = categoryTransactions.sumOf { it.amount }
                        CategoryStat(
                            category = category,
                            amount = amount,
                            percentage = if (parentExpense > 0) (amount / parentExpense).toFloat() else 0f
                        )
                    }
            }.sortedByDescending { it.amount }

            // Group existing transactions by month
            val groupedTransactions = transactions.groupBy { 
                val date = it.timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
                "${date.year}-${date.monthNumber.toString().padStart(2, '0')}"
            }

            // Map to last 6 months ensuring no gaps
            val monthlyStats = last6Months.map { monthKey ->
                val monthTransactions = groupedTransactions[monthKey] ?: emptyList()
                MonthlyStat(
                    month = monthKey,
                    income = monthTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                    expense = monthTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                )
            }

            StatisticsState.Success(
                totalIncome = income,
                totalExpense = expense,
                categoryStats = categoryGroups,
                monthlyStats = monthlyStats,
                currencySymbol = currencySymbol
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading
    )

    fun selectParentCategory(category: Category?) {
        selectedParentCategory.value = category
    }
}
