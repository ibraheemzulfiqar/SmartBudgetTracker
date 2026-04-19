package com.sudocipher.budget.tracker.ui.currency_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    private val preferenceStore: PreferenceStore
) : ViewModel() {

    private val _currencies = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencies: StateFlow<List<CurrencyInfo>> = _currencies.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        val availableCurrencies =
            listOf("PKR", "USD", "EUR", "GBP", "JPY", "CNY", "INR", "SAR", "CAD", "AUD")
                .map { code ->
                    val currency = Currency.getInstance(code)

                    CurrencyInfo(
                        code = code,
                        name = currency.getDisplayName(Locale.getDefault()),
                        symbol = currency.symbol
                    )
                }

        _currencies.value = availableCurrencies
    }

    fun selectCurrency(code: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            preferenceStore.setCurrencyCode(code).join()
            onComplete()
        }
    }
}

data class CurrencyInfo(
    val code: String,
    val name: String,
    val symbol: String
)
