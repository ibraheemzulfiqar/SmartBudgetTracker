package com.sudocipher.budget.tracker.ui.statistics

import com.sudocipher.budget.tracker.domain.model.Category

sealed interface StatisticsState {

    data object Loading : StatisticsState

    data class Success(
        val totalIncome: Double,
        val totalExpense: Double,
        val categoryStats: List<CategoryStat>,
        val monthlyStats: List<MonthlyStat>,
        val currencySymbol: String,
    ) : StatisticsState

}

data class CategoryStat(
    val category: Category,
    val amount: Double,
    val percentage: Float,
)

data class MonthlyStat(
    val month: String,
    val income: Double,
    val expense: Double,
)
