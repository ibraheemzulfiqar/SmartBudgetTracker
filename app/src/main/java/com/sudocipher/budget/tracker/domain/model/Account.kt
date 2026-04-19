package com.sudocipher.budget.tracker.domain.model

import kotlin.time.Clock
import kotlin.time.Instant

data class Account(
    val id: Long,
    val name: String,
    val number: String,
    val type: AccountType,
    val colorTag: ColorTag,
    val balance: Double,
    val dateCreated: Instant,
) {
    companion object {
        fun getEmpty(): Account = Account(
            id = 0,
            name = "",
            number = "",
            type = AccountType.GENERAL,
            colorTag = ColorTag.BLACK,
            balance = 0.0,
            dateCreated = Clock.System.now()
        )
    }
}

enum class AccountType {
    CASH,
    BANK,
    WALLET,
    GENERAL
}