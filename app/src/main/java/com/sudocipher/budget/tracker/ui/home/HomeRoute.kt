package com.sudocipher.budget.tracker.ui.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : NavKey

fun NavBackStack<NavKey>.navigateToHome() {
    add(HomeRoute)
}

fun EntryProviderScope<NavKey>.homeRoute(
    navigateToAddAccount: (id: Long?) -> Unit,
    navigateToAddTransaction: (id: Long?) -> Unit,
) {
    entry<HomeRoute> { route ->

        val viewModel = hiltViewModel<HomeViewModel>()

        val state by viewModel.state.collectAsState()

        HomeScreen(
            state = state,
            onNavigateToAddAccount = navigateToAddAccount,
            onNavigateToAddTransaction = navigateToAddTransaction,
        )
    }
}