package com.sudocipher.budget.tracker.ui.add_account

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AddAccount(
    val accountId: Long?,
) : NavKey

fun NavBackStack<NavKey>.navigateToAddAccount(accountId: Long?) {
    add(AddAccount(accountId = accountId))
}

fun EntryProviderScope<NavKey>.addAccountRoute(
    navigateUp: AddAccount.() -> Unit,
) {
    entry<AddAccount> { route ->

        val viewModel = hiltViewModel<AddAccountViewModel, AddAccountViewModel.Factory> {
            it.create(route.accountId)
        }

        val fetchState by viewModel.fetchState.collectAsState()
        val accountType by viewModel.accountType.collectAsState()
        val accountColor by viewModel.accountColor.collectAsState()

        AddAccountScreen(
            isLoading = fetchState is AccountFetchState.Loading,
            accountName = viewModel.accountName,
            accountNumber = viewModel.accountNumber,
            accountBalance = viewModel.accountBalance,
            accountType = accountType,
            colorTag = accountColor,
            onTypeChange = viewModel::setAccountType,
            onColorChange = viewModel::setAccountColor,
            onSaveChanges = {
                viewModel.saveAccount()
                navigateUp(route)
            },
            onNavigateUp = { navigateUp(route) },
        )
    }
}