package com.sudocipher.budget.tracker.ui.initial_setup

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object InitialSetupRoute : NavKey

fun EntryProviderScope<NavKey>.initialSetupRoute(
    navigateToMain: () -> Unit
) {
    entry<InitialSetupRoute> {
        val viewModel = hiltViewModel<InitialSetupViewModel>()
        val isLoading by viewModel.isLoading.collectAsState()

        InitialSetupScreen(
            balanceState = viewModel.balanceState,
            onComplete = {
                viewModel.completeSetup()
                navigateToMain()
            },
            isLoading = isLoading
        )
    }
}
