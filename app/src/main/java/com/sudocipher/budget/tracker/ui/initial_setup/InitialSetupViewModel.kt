package com.sudocipher.budget.tracker.ui.initial_setup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class InitialSetupViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferenceStore: PreferenceStore
) : ViewModel() {

    val balanceState = TextFieldState("0")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun completeSetup() {
        val balance = balanceState.text.toString().toDoubleOrNull() ?: 0.0


        val cashAccount = Account(
            id = 0,
            name = "Cash",
            number = "",
            type = AccountType.CASH,
            colorTag = ColorTag.ORANGE,
            balance = balance,
            dateCreated = Clock.System.now(),
        )

        budgetRepository.addOrUpdateAccount(cashAccount)
        preferenceStore.setInitialAccountSet(true)
    }
}
