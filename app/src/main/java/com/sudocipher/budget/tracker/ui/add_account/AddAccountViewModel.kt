package com.sudocipher.budget.tracker.ui.add_account

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import com.sudocipher.budget.tracker.ui.add_account.AccountFetchState.Loading
import com.sudocipher.budget.tracker.ui.add_account.AccountFetchState.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddAccountViewModel.Factory::class)
class AddAccountViewModel @AssistedInject constructor(
    private val budgetRepository: BudgetRepository,
    @Assisted private val accountId: Long?,
) : ViewModel() {

    val fetchState: StateFlow<AccountFetchState>
        field = MutableStateFlow<AccountFetchState>(Loading)

    val accountName: TextFieldState = TextFieldState()

    val accountNumber: TextFieldState = TextFieldState()

    val accountBalance: TextFieldState = TextFieldState()

    val accountType: StateFlow<AccountType>
        field = MutableStateFlow(AccountType.GENERAL)

    val accountColor: StateFlow<ColorTag>
        field = MutableStateFlow(ColorTag.BLACK)

    init {
        if (accountId != null) {
            viewModelScope.launch {
                val account = budgetRepository
                    .getAccount(accountId)
                    .first()

                fetchState.value = Success(account)

                accountName.setTextAndPlaceCursorAtEnd(account.name)
                accountNumber.setTextAndPlaceCursorAtEnd(account.number)
                accountBalance.setTextAndPlaceCursorAtEnd(account.balance.toString())
                accountType.value = account.type
                accountColor.value = account.colorTag
            }
        } else {
            fetchState.value = Success(account = null)
        }
    }

    fun setAccountType(type: AccountType) {
        accountType.value = type
    }

    fun setAccountColor(color: ColorTag) {
        accountColor.value = color
    }

    fun saveAccount() {
        var account = (fetchState.value as? Success)?.account ?: Account.getEmpty()

        account = account.copy(
            name = accountName.text.toString(),
            number = accountNumber.text.toString(),
            balance = accountBalance.text.toString().toDouble(),
            type = accountType.value,
            colorTag = accountColor.value,
        )

        budgetRepository.addOrUpdateAccount(account)
    }

    @AssistedFactory
    interface Factory {
        fun create(accountId: Long?): AddAccountViewModel
    }

}


sealed interface AccountFetchState {

    data object Loading : AccountFetchState

    @Immutable
    data class Success(
        val account: Account?,
    ) : AccountFetchState

}