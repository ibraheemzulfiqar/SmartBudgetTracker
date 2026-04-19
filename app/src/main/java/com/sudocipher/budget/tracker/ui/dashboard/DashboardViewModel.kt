package com.sudocipher.budget.tracker.ui.dashboard

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import com.sudocipher.budget.tracker.ui.dashboard.DashboardState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import java.util.Currency

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferenceStore: PreferenceStore,
) : ViewModel() {

    val state: StateFlow<DashboardState> = combine(
        budgetRepository.getAllAccounts(),
        budgetRepository.getAllTransactions(),
        preferenceStore.preference
    ) { accounts, transactions, preference ->
        val currencySymbol = preference.currencyCode?.let {
            Currency.getInstance(it).symbol
        } ?: ""
        DashboardState.Success(
            accounts = accounts,
            transactions = transactions,
            currencySymbol = currencySymbol
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Loading
    )

}


sealed interface DashboardState {

    data object Loading : DashboardState

    @Immutable
    data class Success(
        val accounts: List<Account>,
        val transactions: List<Transaction>,
        val currencySymbol: String,
    ) : DashboardState
}