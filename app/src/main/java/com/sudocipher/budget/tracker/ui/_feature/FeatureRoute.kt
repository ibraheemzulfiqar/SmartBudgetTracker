package com.sudocipher.budget.tracker.ui._feature

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FeatureRoute(
    val addLoadingDelay: Boolean,
) : NavKey

fun NavBackStack<NavKey>.navigateToFeature(addLoadingDelay: Boolean) {
    add(FeatureRoute(addLoadingDelay = addLoadingDelay))
}

fun EntryProviderScope<NavKey>.featureRoute(
    navigateUp: FeatureRoute.() -> Unit,
) {
    entry<FeatureRoute> { route ->

        val viewModel = hiltViewModel<FeatureViewModel, FeatureViewModel.Factory> {
            it.create(route.addLoadingDelay)
        }

        val state by viewModel.state.collectAsState()

        FeatureScreen(
            state = state,
            onNavigateUp = { navigateUp(route) },
        )
    }
}