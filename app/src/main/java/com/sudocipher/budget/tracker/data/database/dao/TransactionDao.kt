package com.sudocipher.budget.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sudocipher.budget.tracker.data.database.entity.TransactionEntity
import com.sudocipher.budget.tracker.data.database.entity.TransactionWithAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): Flow<TransactionWithAccount?>

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionWithAccount>>

}
