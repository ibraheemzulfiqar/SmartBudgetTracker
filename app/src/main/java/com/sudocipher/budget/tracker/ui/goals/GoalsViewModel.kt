package com.sudocipher.budget.tracker.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import kotlinx.coroutines.flow.combine
import java.util.Currency

@HiltViewModel
class SavingsGoalsViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val preferenceStore: PreferenceStore,
) : ViewModel() {

    val state: StateFlow<SavingsGoalsState> = combine(
        repository.getAllSavingsGoals(),
        preferenceStore.preference
    ) { goals, preference ->
        val currencySymbol = preference.currencyCode?.let {
            Currency.getInstance(it).symbol
        } ?: ""
        SavingsGoalsState.Success(goals, currencySymbol)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavingsGoalsState.Loading
    )

    fun addSavedAmount(goal: SavingsGoal, amount: Double) {
        repository.addOrUpdateSavingsGoal(
            goal.copy(savedAmount = goal.savedAmount + amount)
        )
    }
}


sealed interface SavingsGoalsState {

    data object Loading : SavingsGoalsState

    data class Success(
        val goals: List<SavingsGoal>,
        val currencySymbol: String,
    ) : SavingsGoalsState

}