package com.sudocipher.budget.tracker.mock.states

import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.ui.home.HomeState
import kotlin.time.Instant

object MockStatesData {

    fun getMockHomeState(): HomeState.Success {
        return HomeState.Success(
            transactions = MockData.getMockTransactions(),
            accounts = listOf(
                Account(
                    id = 1L,
                    name = "NayaPay",
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
        )
    }

}