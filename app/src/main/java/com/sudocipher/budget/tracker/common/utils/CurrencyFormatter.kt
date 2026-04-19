package com.sudocipher.budget.tracker.common.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    fun formatAmount(amount: Double): String {
        return formatter.format(amount)
    }

    fun formatWithSymbol(amount: Double, symbol: String): String {
        return "$symbol ${formatAmount(amount)}"
    }
}
