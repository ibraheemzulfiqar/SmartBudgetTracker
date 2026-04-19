package com.sudocipher.budget.tracker.domain.repository

import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun getAccount(id: Long): Flow<Account>

    fun addOrUpdateAccount(account: Account)

    fun getAllAccounts(): Flow<List<Account>>

    fun getDefaultAccount(): Flow<Account>

    fun getTransaction(id: Long): Flow<Transaction>

    fun addOrUpdateTransaction(new: Transaction, old: Transaction?)

    fun getAllTransactions(): Flow<List<Transaction>>

    fun getAllSavingsGoals(): Flow<List<SavingsGoal>>

    fun getSavingsGoal(id: Long): Flow<SavingsGoal>

    fun addOrUpdateSavingsGoal(goal: SavingsGoal)

    fun deleteSavingsGoal(id: Long)
}