package com.sudocipher.budget.tracker.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sudocipher.budget.tracker.ui.add_account.addAccountRoute
import com.sudocipher.budget.tracker.ui.add_account.navigateToAddAccount
import com.sudocipher.budget.tracker.ui.add_transaction.addTransactionRoute
import com.sudocipher.budget.tracker.ui.add_transaction.navigateToAddTransaction
import com.sudocipher.budget.tracker.ui.home.HomeRoute
import com.sudocipher.budget.tracker.ui.home.homeRoute

@Composable
fun MyApp() {
    val backStack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {

            homeRoute(
                navigateToAddAccount = backStack::navigateToAddAccount,
                navigateToAddTransaction = backStack::navigateToAddTransaction,
            )

            addAccountRoute(
                navigateUp = { backStack.removeIfLast() }
            )

            addTransactionRoute(
                navigateUp = { backStack.removeIfLast() }
            )

        }
    )
}

inline fun <reified T : NavKey> NavBackStack<T>.removeIfLast() {
    if (size == 1) return

    if (lastOrNull() is T) {
        removeLastOrNull()
    }
}