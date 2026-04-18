package com.sudocipher.budget.tracker.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import com.sudocipher.budget.tracker.ui.home.HomeState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    val state: StateFlow<HomeState> = combine(
        budgetRepository.getAllAccounts(),
        budgetRepository.getAllTransactions()
    ) { accounts, transactions ->
        HomeState.Success(
            accounts = accounts,
            transactions = transactions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Loading
    )

}


sealed interface HomeState {

    data object Loading : HomeState

    @Immutable
    data class Success(
        val accounts: List<Account>,
        val transactions: List<Transaction>,
    ) : HomeState
}