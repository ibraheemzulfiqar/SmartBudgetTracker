package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object StatisticsRoute : NavKey

fun NavBackStack<NavKey>.navigateToStatistics() {
    add(StatisticsRoute)
}

fun EntryProviderScope<NavKey>.statisticsRoute(
    navigateUp: () -> Unit,
) {
    entry<StatisticsRoute> {
        val viewModel = hiltViewModel<StatisticsViewModel>()
        val state by viewModel.state.collectAsState()

        StatisticsScreen(
            state = state,
            onNavigateUp = navigateUp
        )
    }
}
