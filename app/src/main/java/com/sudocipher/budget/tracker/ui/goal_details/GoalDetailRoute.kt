package com.sudocipher.budget.tracker.ui.goal_details

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class GoalDetailRoute(val goalId: Long) : NavKey

fun NavBackStack<NavKey>.navigateToGoalDetail(goalId: Long) {
    add(GoalDetailRoute(goalId))
}

fun EntryProviderScope<NavKey>.savingsGoalDetailRoute(
    navigateToEditGoal: (id: Long) -> Unit,
    navigateUp: () -> Unit,
) {
    entry<GoalDetailRoute> { route ->
        val viewModel =
            hiltViewModel<SavingsGoalDetailViewModel, SavingsGoalDetailViewModel.Factory> { factory ->
                factory.create(route.goalId)
            }

        val state by viewModel.goal.collectAsState()

        GoalDetailScreen(
            state = state,
            onNavigateUp = navigateUp,
            onNavigateToEditGoal = navigateToEditGoal,
            onAddAmount = viewModel::addSavedAmount,
            onMarkAsAchieved = viewModel::markAsAchieved,
            onDeleteGoal = {
                viewModel.deleteGoal()
                navigateUp()
            }
        )
    }
}
