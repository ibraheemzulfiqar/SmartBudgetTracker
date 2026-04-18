package com.sudocipher.budget.tracker.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sudocipher.budget.tracker.ui._feature.FeatureRoute
import com.sudocipher.budget.tracker.ui._feature.featureRoute

@Composable
fun MyApp() {
    val backStack = rememberNavBackStack(FeatureRoute(addLoadingDelay = true))

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {

            featureRoute(
                navigateUp = { backStack.removeIfLast() },
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