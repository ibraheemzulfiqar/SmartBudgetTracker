package com.sudocipher.budget.tracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Category
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import java.util.Currency

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val preferenceStore: PreferenceStore,
) : ViewModel() {

    private val _selectedParentCategory = MutableStateFlow<Category?>(null)
    val selectedParentCategory: StateFlow<Category?> = _selectedParentCategory

    val state: StateFlow<StatisticsState> = combine(
        repository.getAllTransactions(),
        _selectedParentCategory,
        preferenceStore.preference
    ) { transactions, selectedParent, preference ->
        val currencySymbol = preference.currencyCode?.let {
            Currency.getInstance(it).symbol
        } ?: ""
        if (transactions.isEmpty()) {
            StatisticsState.Success(
                totalIncome = 0.0,
                totalExpense = 0.0,
                categoryStats = emptyList(),
                monthlyStats = emptyList(),
                currencySymbol = currencySymbol
            )
        } else {
            val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

            val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
            
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

            StatisticsState.Success(
                totalIncome = income,
                totalExpense = expense,
                categoryStats = categoryGroups,
                monthlyStats = emptyList(), // Simplified for now
                currencySymbol = currencySymbol
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading
    )

    fun selectParentCategory(category: Category?) {
        _selectedParentCategory.value = category
    }
}
