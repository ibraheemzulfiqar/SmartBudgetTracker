package com.sudocipher.budget.tracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "account",
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val number: String,
    val type: String,
    val color: String,
    val balance: Double,
    val dateCreated: Long,
)