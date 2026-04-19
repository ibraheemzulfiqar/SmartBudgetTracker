package com.sudocipher.budget.tracker.ui.add_goal

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.sudocipher.budget.tracker.R
import kotlinx.serialization.Serializable

@Serializable
data class AddGoal(
    val goalId: Long? = null,
) : NavKey

fun NavBackStack<NavKey>.navigateToAddGoal(goalId: Long? = null) {
    add(AddGoal(goalId = goalId))
}

@SuppressLint("LocalContextGetResourceValueCall")
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

        val context = LocalContext.current

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
                val target = viewModel.targetAmountState.text.toString().toDoubleOrNull() ?: 0.0

                if (viewModel.nameState.text.isEmpty()) {
                    Toast.makeText(
                        context.applicationContext,
                        context.getString(R.string.please_enter_goal_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (target <= 0.0) {
                    Toast.makeText(
                        context.applicationContext,
                        context.getString(R.string.target_should_be_greater_than_0),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.saveGoal(onSuccess = onNavigateUp)
                }
            },
            onDelete = {
                viewModel.deleteGoal(onSuccess = onNavigateUp)
            },
            onNavigateUp = onNavigateUp
        )
    }
}
