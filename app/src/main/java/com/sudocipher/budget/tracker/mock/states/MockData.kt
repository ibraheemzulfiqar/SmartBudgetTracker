package com.sudocipher.budget.tracker.mock.states

import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionCategory
import com.sudocipher.budget.tracker.domain.model.TransactionType
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

object MockData {

    fun getAccounts(): List<Account> = listOf(
        Account(
            id = 1L,
            name = "Main Savings",
            number = "**** 4920",
            type = AccountType.BANK,
            colorTag = ColorTag.BLACK,
            balance = 12500.0,
            dateCreated = Instant.parse("2022-01-15T10:00:00Z")
        ),
        Account(
            id = 2L,
            name = "Cash",
            number = "",
            type = AccountType.CASH,
            colorTag = ColorTag.ORANGE,
            balance = 450.0,
            dateCreated = Instant.parse("2023-05-20T14:30:00Z")
        ),
    )

    fun getAccount() = getAccounts()[0]

    fun getMockTransactions(): List<Transaction> {
        val account = getAccount()

        return listOf(
            Transaction(
                id = 101L,
                amount = 12.50, // $12.50
                type = TransactionType.EXPENSE,
                account = account,
                category = TransactionCategory.FoodAndDrinks,
                note = "Starbucks Coffee",
                timestamp = Clock.System.now().minus(2.hours)
            ),
            Transaction(
                id = 102L,
                amount = 14.99,
                type = TransactionType.EXPENSE,
                account = account,
                category = TransactionCategory.Subscription,
                note = "Netflix Monthly",
                timestamp = Clock.System.now().minus(1.days)
            ),
            Transaction(
                id = 103L,
                amount = 4500.0,
                type = TransactionType.INCOME,
                account = account,
                category = TransactionCategory.Income,
                note = "Monthly Salary",
                timestamp = Clock.System.now().minus(3.days)
            )
        )
    }

}