package com.sudocipher.budget.tracker.data.database.converter

import com.sudocipher.budget.tracker.data.database.entity.TransactionEntity
import com.sudocipher.budget.tracker.data.database.entity.TransactionWithAccount
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionType
import kotlin.time.Instant

fun Transaction.toEntity(): TransactionEntity =
    TransactionEntity(
        id = id,
        amount = amount,
        type = type.name,
        accountId = account.id,
        category = category,
        note = note,
        timestamp = timestamp.toEpochMilliseconds(),
    )

fun TransactionWithAccount.asDomain(): Transaction =
    Transaction(
        id = transaction.id,
        amount = transaction.amount,
        type = TransactionType.valueOf(transaction.type),
        account = account.asDomain(),
        category = transaction.category,
        note = transaction.note,
        timestamp = Instant.fromEpochMilliseconds(transaction.timestamp)
    )