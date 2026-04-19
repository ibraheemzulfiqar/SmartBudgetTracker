package com.sudocipher.budget.tracker.ui.currency_selection

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object CurrencySelectionRoute : NavKey

fun EntryProviderScope<NavKey>.currencySelectionRoute(
    onComplete: () -> Unit
) {
    entry<CurrencySelectionRoute> {
        val viewModel = hiltViewModel<CurrencySelectionViewModel>()
        val currencies by viewModel.currencies.collectAsState()

        CurrencySelectionScreen(
            currencies = currencies,
            onCurrencySelected = { code ->
                viewModel.selectCurrency(code, onComplete)
            }
        )
    }
}
