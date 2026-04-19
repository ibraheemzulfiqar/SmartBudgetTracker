package com.sudocipher.budget.tracker.ui.add_goal

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.model.GoalTemplate
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

@HiltViewModel(assistedFactory = AddGoalViewModel.Factory::class)
class AddGoalViewModel @AssistedInject constructor(
    private val repository: BudgetRepository,
    @Assisted private val goalId: Long?,
) : ViewModel() {

    val nameState = TextFieldState("")
    val targetAmountState = TextFieldState("")
    val savedAmountState = TextFieldState("0")

    private val _selectedIcon = MutableStateFlow(SavingsGoalIcon.OTHER)
    val selectedIcon: StateFlow<SavingsGoalIcon> = _selectedIcon.asStateFlow()

    private val _selectedColor = MutableStateFlow(ColorTag.BLACK)
    val selectedColor: StateFlow<ColorTag> = _selectedColor.asStateFlow()

    private val _desiredDate = MutableStateFlow<Instant?>(null)
    val desiredDate: StateFlow<Instant?> = _desiredDate.asStateFlow()

    private val _goalToEdit = MutableStateFlow<SavingsGoal?>(null)
    val goalToEdit: StateFlow<SavingsGoal?> = _goalToEdit.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        if (goalId != null) {
            viewModelScope.launch {
                _isLoading.value = true
                val goal = repository.getSavingsGoal(goalId).first()

                _goalToEdit.value = goal
                _selectedIcon.value = goal.icon
                _selectedColor.value = goal.colorTag
                _desiredDate.value = goal.desiredDate

                nameState.setTextAndPlaceCursorAtEnd(goal.name)
                targetAmountState.setTextAndPlaceCursorAtEnd(goal.targetAmount.toString())
                savedAmountState.setTextAndPlaceCursorAtEnd(goal.savedAmount.toString())
                _isLoading.value = false
            }
        }
    }

    fun onTemplateSelected(template: GoalTemplate) {
        if (nameState.text.isEmpty()) {
            nameState.setTextAndPlaceCursorAtEnd(template.defaultName)
        }
        _selectedIcon.value = template.icon
    }

    fun onIconSelected(icon: SavingsGoalIcon) {
        _selectedIcon.value = icon
    }

    fun onColorSelected(color: ColorTag) {
        _selectedColor.value = color
    }

    fun onDateSelected(instant: Instant?) {
        _desiredDate.value = instant
    }

    fun saveGoal(onSuccess: () -> Unit) {
        val name = nameState.text.toString()
        val target = targetAmountState.text.toString().toDoubleOrNull() ?: 0.0
        val saved = savedAmountState.text.toString().toDoubleOrNull() ?: 0.0

        if (name.isNotEmpty() && target > 0) {
            viewModelScope.launch {
                _isLoading.value = true
                val goal = (goalToEdit.value ?: SavingsGoal(
                    name = name,
                    targetAmount = target,
                    savedAmount = saved,
                    desiredDate = _desiredDate.value,
                    colorTag = _selectedColor.value,
                    icon = _selectedIcon.value
                )).copy(
                    name = name,
                    targetAmount = target,
                    savedAmount = saved,
                    desiredDate = _desiredDate.value,
                    colorTag = _selectedColor.value,
                    icon = _selectedIcon.value
                )
                repository.addOrUpdateSavingsGoal(goal)
                _isLoading.value = false
                onSuccess()
            }
        }
    }

    fun deleteGoal(onSuccess: () -> Unit) {
        if (goalId != null) {
            viewModelScope.launch {
                _isLoading.value = true
                repository.deleteSavingsGoal(goalId)
                _isLoading.value = false
                onSuccess()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(goalId: Long?): AddGoalViewModel
    }
}
