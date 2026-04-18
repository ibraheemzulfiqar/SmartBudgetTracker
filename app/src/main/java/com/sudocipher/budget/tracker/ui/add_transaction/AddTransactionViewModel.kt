package com.sudocipher.budget.tracker.ui.add_transaction

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionCategory
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import com.sudocipher.budget.tracker.ui.add_transaction.TransactionFetchState.Loading
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Clock

@HiltViewModel(assistedFactory = AddTransactionViewModel.Factory::class)
class AddTransactionViewModel @AssistedInject constructor(
    private val budgetRepository: BudgetRepository,
    @Assisted private val transactionId: Long?,
) : ViewModel() {

    val fetchState: StateFlow<TransactionFetchState>
        field = MutableStateFlow<TransactionFetchState>(Loading)

    val amount = TextFieldState("0")

    val category: StateFlow<TransactionCategory>
        field = MutableStateFlow<TransactionCategory>(TransactionCategory.Others)

    val type: StateFlow<TransactionType>
        field = MutableStateFlow(TransactionType.EXPENSE)


    init {
        viewModelScope.launch {
            if (transactionId != null) {
                val transaction = budgetRepository.getTransaction(transactionId).first()

                fetchState.value = TransactionFetchState.Success(
                    account = transaction.account,
                    transaction = transaction,
                )

                category.value = transaction.category
                type.value = transaction.type
                amount.setTextAndPlaceCursorAtEnd(transaction.amount.toString())
            } else {
                val account = budgetRepository.getDefaultAccount().first()

                fetchState.value = TransactionFetchState.Success(
                    account = account,
                    transaction = null,
                )
            }
        }
    }

    fun setAccount(account: Account) {
        val state = fetchState.value

        if (state !is TransactionFetchState.Success) return

        fetchState.value = state.copy(account = account)
    }

    fun setCategory(category: TransactionCategory) {
        this.category.value = category
    }

    fun setType(type: TransactionType) {
        this.type.value = type
    }

    fun saveTransaction() {
        val state = fetchState.value

        if (state !is TransactionFetchState.Success) return

        var transaction = state.transaction ?: Transaction.getEmpty()

        transaction = transaction.copy(
            amount = amount.text.toString().toDouble(),
            type = type.value,
            account = state.account,
            category = category.value,
            note = null,
            timestamp = Clock.System.now(),
        )

        budgetRepository.addOrUpdateTransaction(transaction)
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
        val account: Account,
    ) : TransactionFetchState

}