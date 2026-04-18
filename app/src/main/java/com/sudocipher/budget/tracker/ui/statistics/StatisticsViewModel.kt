package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: BudgetRepository,
) : ViewModel() {

    val state: StateFlow<StatisticsState> = repository.getAllTransactions()
        .map { transactions ->
            if (transactions.isEmpty()) {
                StatisticsState.Success(
                    totalIncome = 0.0,
                    totalExpense = 0.0,
                    categoryStats = emptyList(),
                    monthlyStats = emptyList()
                )
            } else {
                val income = transactions.fastFilter {
                    it.type == TransactionType.INCOME
                }.sumOf { it.amount }

                val expense = transactions.fastFilter {
                    it.type == TransactionType.EXPENSE
                }.sumOf { it.amount }

                val categoryGroups = transactions.fastFilter { it.type == TransactionType.EXPENSE }
                    .groupBy { it.category }
                    .map { (category, categoryTransactions) ->
                        val amount = categoryTransactions.sumOf { it.amount }
                        CategoryStat(
                            category = category,
                            amount = amount,
                            percentage = if (expense > 0) (amount / expense).toFloat() else 0f
                        )
                    }
                    .sortedByDescending { it.amount }

                // For simplicity, we just group by month string for now
                // In a real app we would use proper date formatting
                val monthlyStats = transactions
                    .groupBy { 
                        // Simplified month grouping
                        "All" 
                    }
                    .map { (month, monthTransactions) ->
                        MonthlyStat(
                            month = month,
                            income = monthTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                            expense = monthTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                        )
                    }

                StatisticsState.Success(
                    totalIncome = income,
                    totalExpense = expense,
                    categoryStats = categoryGroups,
                    monthlyStats = monthlyStats
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatisticsState.Loading
        )
}
