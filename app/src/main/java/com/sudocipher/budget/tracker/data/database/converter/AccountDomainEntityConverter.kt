package com.sudocipher.budget.tracker.data.database.converter

import com.sudocipher.budget.tracker.data.database.entity.AccountEntity
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import kotlin.time.Instant

fun AccountEntity.asDomain(): Account =
    Account(
        id = id,
        name = name,
        number = number,
        type = AccountType.valueOf(type),
        colorTag = ColorTag.valueOf(color),
        balance = balance,
        dateCreated = Instant.fromEpochMilliseconds(dateCreated),
    )

fun Account.toEntity(): AccountEntity =
    AccountEntity(
        id = id,
        name = name,
        number = number,
        type = type.name,
        color = colorTag.name,
        balance = balance,
        dateCreated = dateCreated.toEpochMilliseconds(),
    )
