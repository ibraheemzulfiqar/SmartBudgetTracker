package com.sudocipher.budget.tracker.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithAccount(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity
)