package com.sudocipher.budget.tracker.ui.add_goal

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AddGoal(
    val goalId: Long? = null,
) : NavKey

fun NavBackStack<NavKey>.navigateToAddGoal(goalId: Long? = null) {
    add(AddGoal(goalId = goalId))
}

fun EntryProviderScope<NavKey>.addGoalRoute(
    onNavigateUp: () -> Unit,
) {
    entry<AddGoal> { route ->
        val viewModel = hiltViewModel<AddGoalViewModel, AddGoalViewModel.Factory> {
            it.create(route.goalId)
        }

        val selectedIcon by viewModel.selectedIcon.collectAsState()
        val selectedColor by viewModel.selectedColor.collectAsState()
        val desiredDate by viewModel.desiredDate.collectAsState()
        val goalToEdit by viewModel.goalToEdit.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        AddGoalScreen(
            nameState = viewModel.nameState,
            targetAmountState = viewModel.targetAmountState,
            savedAmountState = viewModel.savedAmountState,
            selectedIcon = selectedIcon,
            selectedColor = selectedColor,
            desiredDate = desiredDate,
            goalToEdit = goalToEdit,
            isLoading = isLoading,
            onIconSelected = viewModel::onIconSelected,
            onColorSelected = viewModel::onColorSelected,
            onDateSelected = viewModel::onDateSelected,
            onSaveGoal = {
                viewModel.saveGoal(onSuccess = onNavigateUp)
            },
            onDelete = {
                viewModel.deleteGoal(onSuccess = onNavigateUp)
            },
            onNavigateUp = onNavigateUp
        )
    }
}
