package com.sudocipher.budget.tracker.data._di

import android.content.Context
import androidx.room.Room
import com.sudocipher.budget.tracker.data.database.BudgetDatabase
import com.sudocipher.budget.tracker.data.database.dao.AccountDao
import com.sudocipher.budget.tracker.data.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun getAccountDao(db: BudgetDatabase): AccountDao {
        return db.getAccountDao()
    }

    @Provides
    fun getTransactionDao(db: BudgetDatabase): TransactionDao {
        return db.getTransactionDao()
    }

    @Singleton
    @Provides
    fun getBudgetDatabase(
        @ApplicationContext context: Context,
    ): BudgetDatabase = Room.databaseBuilder(
        context,
        BudgetDatabase::class.java,
        "budget_db"
    ).build()

}
