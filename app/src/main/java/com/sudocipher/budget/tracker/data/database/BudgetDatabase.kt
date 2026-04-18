package com.sudocipher.budget.tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sudocipher.budget.tracker.data.database.converter.CategoryConverter
import com.sudocipher.budget.tracker.data.database.dao.AccountDao
import com.sudocipher.budget.tracker.data.database.dao.TransactionDao
import com.sudocipher.budget.tracker.data.database.entity.AccountEntity
import com.sudocipher.budget.tracker.data.database.entity.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(CategoryConverter::class)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun getAccountDao(): AccountDao
    abstract fun getTransactionDao(): TransactionDao

}