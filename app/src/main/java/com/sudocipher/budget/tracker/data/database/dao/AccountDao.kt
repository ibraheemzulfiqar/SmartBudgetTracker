package com.sudocipher.budget.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sudocipher.budget.tracker.data.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Upsert
    suspend fun upsert(acc: AccountEntity)

    @Delete
    suspend fun delete(acc: AccountEntity)

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id: Long): Flow<AccountEntity>

    @Query("SELECT * FROM account ORDER BY dateCreated DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>


    @Query("SELECT * FROM account ORDER BY dateCreated DESC LIMIT 1")
    fun getFirstAccount(): Flow<AccountEntity>

}