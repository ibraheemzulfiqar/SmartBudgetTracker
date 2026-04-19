package com.sudocipher.budget.tracker.ui.add_transaction

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.Category
import com.sudocipher.budget.tracker.domain.model.CategoryData
import com.sudocipher.budget.tracker.domain.model.CategoryItem
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import com.sudocipher.budget.tracker.ui.add_transaction.TransactionFetchState.Loading
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock

@HiltViewModel(assistedFactory = AddTransactionViewModel.Factory::class)
class AddTransactionViewModel @AssistedInject constructor(
    private val budgetRepository: BudgetRepository,
    @Assisted private val transactionId: Long?,
) : ViewModel() {

    val accounts: StateFlow<List<Account>> = budgetRepository
        .getAllAccounts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val fetchState = MutableStateFlow<TransactionFetchState>(Loading)

    val amount = TextFieldState()

    val categoryItem = MutableStateFlow<CategoryItem>(CategoryData.getCategoryItemOf(Category.Others))

    val type = MutableStateFlow(TransactionType.EXPENSE)


    init {
        viewModelScope.launch {
            if (transactionId != null) {
                val transaction = budgetRepository.getTransaction(transactionId).first()

                fetchState.value = TransactionFetchState.Success(
                    selectedAccount = transaction.account,
                    transaction = transaction,
                )

                categoryItem.value = CategoryData.getCategoryItemOf(transaction.category)
                type.value = transaction.type
                amount.setTextAndPlaceCursorAtEnd(transaction.amount.toString())
            } else {
                val account = budgetRepository.getDefaultAccount().first()

                fetchState.value = TransactionFetchState.Success(
                    selectedAccount = account,
                    transaction = null,
                )
            }
        }
    }

    fun setAccount(account: Account) {
        val state = fetchState.value

        if (state !is TransactionFetchState.Success) return

        fetchState.value = state.copy(selectedAccount = account)
    }

    fun setCategory(category: CategoryItem) {
        this.categoryItem.value = category
    }

    fun setType(type: TransactionType) {
        this.type.value = type
    }

    fun saveTransaction() {
        val state = fetchState.value

        if (state !is TransactionFetchState.Success) return

        val oldTransaction = state.transaction
        var transaction = oldTransaction ?: Transaction.getEmpty()

        transaction = transaction.copy(
            amount = amount.text.toString().toDoubleOrNull() ?: 0.0,
            type = type.value,
            account = state.selectedAccount,
            category = categoryItem.value.category,
            note = null,
            timestamp = Clock.System.now(),
        )

        budgetRepository.addOrUpdateTransaction(new = transaction, old = oldTransaction)
    }

    fun deleteTransaction() {
        if (transactionId != null) {
            budgetRepository.deleteTransaction(transactionId)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(transactionId: Long?): AddTransactionViewModel
    }

}

sealed interface TransactionFetchState {

    data object Loading : TransactionFetchState

    @Immutable
    data class Success(
        val transaction: Transaction?,
        val selectedAccount: Account,
    ) : TransactionFetchState

}
