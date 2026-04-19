package com.sudocipher.budget.tracker.ui.goal_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import kotlinx.coroutines.flow.combine
import java.util.Currency

@HiltViewModel(assistedFactory = SavingsGoalDetailViewModel.Factory::class)
class SavingsGoalDetailViewModel @AssistedInject constructor(
    private val repository: BudgetRepository,
    private val preferenceStore: PreferenceStore,
    @Assisted private val goalId: Long,
) : ViewModel() {

    val goal: StateFlow<GoalDetailState> = combine(
        repository.getSavingsGoal(goalId),
        preferenceStore.preference
    ) { goal, preference ->
        val currencySymbol = preference.currencyCode?.let {
            Currency.getInstance(it).symbol
        } ?: ""
        GoalDetailState.Success(goal, currencySymbol)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GoalDetailState.Loading,
        )

    fun addSavedAmount(amount: Double) {
        val currentGoal = (goal.value as? GoalDetailState.Success)?.goal ?: return

        repository.addOrUpdateSavingsGoal(
            currentGoal.copy(savedAmount = currentGoal.savedAmount + amount)
        )
    }

    fun markAsAchieved() {
        val currentGoal = (goal.value as? GoalDetailState.Success)?.goal ?: return

        repository.addOrUpdateSavingsGoal(
            currentGoal.copy(savedAmount = currentGoal.targetAmount)
        )
    }

    fun deleteGoal() {
        repository.deleteSavingsGoal(goalId)
    }

    @AssistedFactory
    interface Factory {
        fun create(goalId: Long): SavingsGoalDetailViewModel
    }
}

sealed interface GoalDetailState {

    data object Loading : GoalDetailState

    data class Success(
        val goal: SavingsGoal,
        val currencySymbol: String,
    ) : GoalDetailState

}