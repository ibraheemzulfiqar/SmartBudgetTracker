package com.sudocipher.budget.tracker.ui.add_transaction

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AddTransaction(
    val transactionId: Long?,
) : NavKey

fun NavBackStack<NavKey>.navigateToAddTransaction(transactionId: Long?) {
    add(AddTransaction(transactionId = transactionId))
}

fun EntryProviderScope<NavKey>.addTransactionRoute(
    navigateUp: AddTransaction.() -> Unit,
) {
    entry<AddTransaction> { route ->

        val viewModel = hiltViewModel<AddTransactionViewModel, AddTransactionViewModel.Factory> {
            it.create(route.transactionId)
        }

        val fetchState by viewModel.fetchState.collectAsState()

        val category by viewModel.category.collectAsState()

        val type by viewModel.type.collectAsState()

        AddTransactionScreen(
            fetchState = fetchState,
            amount = viewModel.amount,
            category = category,
            transactionType = type,
            onAccountChange = viewModel::setAccount,
            onTransactionTypeChange = viewModel::setType,
            onCategoryChange = viewModel::setCategory,
            onSaveChanges = {
                viewModel.saveTransaction()
                navigateUp(route)
            },
            onNavigateUp = { navigateUp(route) }
        )
    }
}