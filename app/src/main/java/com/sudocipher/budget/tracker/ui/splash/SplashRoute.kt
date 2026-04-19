package com.sudocipher.budget.tracker.ui.splash

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute : NavKey

fun EntryProviderScope<NavKey>.splashRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToCurrencySelection: () -> Unit,
    onNavigateToInitialSetup: () -> Unit,
) {
    entry<SplashRoute> {
        val viewModel = hiltViewModel<SplashViewModel>()

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    SplashNavigationEvent.NavigateToCurrencySelection -> onNavigateToCurrencySelection()
                    SplashNavigationEvent.NavigateToInitialSetup -> onNavigateToInitialSetup()
                    SplashNavigationEvent.NavigateToMain -> onNavigateToMain()
                }
            }
        }

        LoadingBox()
    }
}

