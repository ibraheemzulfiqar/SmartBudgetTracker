package com.sudocipher.budget.tracker.domain.model

import kotlin.time.Clock
import kotlin.time.Instant

data class Transaction(
    val id: Long,
    val amount: Double, // always positive
    val type: TransactionType,
    val account: Account,
    val category: TransactionCategory,
    val note: String? = null,
    val timestamp: Instant,
) {

    companion object {

        fun getEmpty(): Transaction = Transaction(
            id = 0,
            amount = 0.0,
            type = TransactionType.EXPENSE,
            account = Account.getEmpty(),
            category = TransactionCategory.Others,
            note = null,
            timestamp = Clock.System.now()
        )

    }

}

enum class TransactionType {
    INCOME,
    EXPENSE,
}