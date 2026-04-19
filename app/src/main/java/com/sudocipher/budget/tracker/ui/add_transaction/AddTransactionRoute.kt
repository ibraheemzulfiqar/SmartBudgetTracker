package com.sudocipher.budget.tracker.ui.add_transaction

import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    navigateUp: () -> Unit,
) {
    entry<AddTransaction> { route ->

        val viewModel = hiltViewModel<AddTransactionViewModel, AddTransactionViewModel.Factory> {
            it.create(route.transactionId)
        }

        val fetchState by viewModel.fetchState.collectAsState()

        val accounts by viewModel.accounts.collectAsState()

        val category by viewModel.categoryItem.collectAsState()

        val type by viewModel.type.collectAsState()

        val context = LocalContext.current

        AddTransactionScreen(
            fetchState = fetchState,
            accounts = accounts,
            amount = viewModel.amount,
            category = category,
            transactionType = type,
            onAccountChange = viewModel::setAccount,
            onTransactionTypeChange = viewModel::setType,
            onCategoryChange = viewModel::setCategory,
            onSaveChanges = {
                val amountInput = viewModel.amount.text.toString().toDoubleOrNull() ?: 0.0

                if (amountInput > 0) {
                    viewModel.saveTransaction()
                    navigateUp()
                } else {
                    Toast.makeText(context.applicationContext, "Please enter amount", Toast.LENGTH_SHORT).show()
                }
            },
            onDelete = {
                viewModel.deleteTransaction()
                navigateUp()
            },
            onNavigateUp = { navigateUp() },
        )
    }
}
