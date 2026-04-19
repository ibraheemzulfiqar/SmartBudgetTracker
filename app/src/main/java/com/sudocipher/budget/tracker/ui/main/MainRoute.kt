package com.sudocipher.budget.tracker.ui.main

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.sudocipher.budget.tracker.ui.dashboard.DashboardViewModel
import com.sudocipher.budget.tracker.ui.goals.SavingsGoalsViewModel
import com.sudocipher.budget.tracker.ui.statistics.StatisticsViewModel
import kotlinx.serialization.Serializable

@Serializable
object MainRoute : NavKey

fun NavBackStack<NavKey>.navigateToMain() {
    add(MainRoute)
}

fun EntryProviderScope<NavKey>.mainRoute(
    navigateToAddAccount: (Long?) -> Unit,
    navigateToAddTransaction: (Long?) -> Unit,
    onNavigateToGoalDetail: (Long) -> Unit,
    onNavigateToAddGoal: () -> Unit,
) {
    entry<MainRoute> {
        val dashboardViewModel = hiltViewModel<DashboardViewModel>()
        val savingsGoalsViewModel = hiltViewModel<SavingsGoalsViewModel>()
        val statisticsViewModel = hiltViewModel<StatisticsViewModel>()

        val dashboardState by dashboardViewModel.state.collectAsState()
        val savingsGoalsState by savingsGoalsViewModel.state.collectAsState()
        val statisticsState by statisticsViewModel.state.collectAsState()
        val selectedParent by statisticsViewModel.selectedParentCategory.collectAsState()

        MainScreen(
            dashboardState = dashboardState,
            savingsGoalsState = savingsGoalsState,
            statisticsState = statisticsState,
            selectedStatisticsParent = selectedParent,
            onStatisticsCategoryClick = statisticsViewModel::selectParentCategory,
            onStatisticsBackToParent = { statisticsViewModel.selectParentCategory(null) },
            onNavigateToAddAccount = navigateToAddAccount,
            onNavigateToAddTransaction = navigateToAddTransaction,
            onNavigateToGoalDetail = { onNavigateToGoalDetail(it.id) },
            onNavigateToAddGoal = onNavigateToAddGoal,
            onAddSavingsAmount = savingsGoalsViewModel::addSavedAmount
        )
    }
}
