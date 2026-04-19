package com.sudocipher.budget.tracker.data.repository

import androidx.compose.ui.util.fastMap
import com.sudocipher.budget.tracker.data.database.converter.asDomain
import com.sudocipher.budget.tracker.data.database.converter.toEntity
import com.sudocipher.budget.tracker.data.database.dao.AccountDao
import com.sudocipher.budget.tracker.data.database.dao.SavingsGoalDao
import com.sudocipher.budget.tracker.data.database.dao.TransactionDao
import com.sudocipher.budget.tracker.data.database.entity.AccountEntity
import com.sudocipher.budget.tracker.data.database.entity.SavingsGoalEntity
import com.sudocipher.budget.tracker.data.database.entity.TransactionWithAccount
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val savingsGoalDao: SavingsGoalDao,
) : BudgetRepository {

    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getAccount(id: Long): Flow<Account> {
        return accountDao.getAccountById(id).map(AccountEntity::asDomain)
    }

    override fun addOrUpdateAccount(account: Account) {
        externalScope.launch {
            accountDao.upsert(account.toEntity())
        }
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts().map {
            it.fastMap(AccountEntity::asDomain)
        }
    }

    override fun getDefaultAccount(): Flow<Account> {
        return accountDao.getFirstAccount().map(AccountEntity::asDomain)
    }

    override fun getTransaction(id: Long): Flow<Transaction> {
        return transactionDao.getTransactionById(id).map(TransactionWithAccount::asDomain)
    }

    override fun addOrUpdateTransaction(new: Transaction, old: Transaction?) {
        externalScope.launch {
            transactionDao.upsert(new.toEntity())

            if (old != null && old.account.id != new.account.id) {
                accountDao.updateBalance(old.account.id, -old.signedAmount)
                accountDao.updateBalance(new.account.id, new.signedAmount)
            } else {
                val oldAmount = old?.signedAmount ?: 0.0
                val delta = new.signedAmount - oldAmount
                accountDao.updateBalance(new.account.id, delta)
            }
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map {
            it.fastMap(TransactionWithAccount::asDomain)
        }
    }

    override fun getAllSavingsGoals(): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getAllSavingsGoals().map {
            it.fastMap(SavingsGoalEntity::asDomain)
        }
    }

    override fun getSavingsGoal(id: Long): Flow<SavingsGoal> {
        return savingsGoalDao.getSavingsGoal(id).map(SavingsGoalEntity::asDomain)
    }

    override fun addOrUpdateSavingsGoal(goal: SavingsGoal) {
        externalScope.launch {
            savingsGoalDao.insertOrUpdateSavingsGoal(goal.toEntity())
        }
    }

    override fun deleteSavingsGoal(id: Long) {
        externalScope.launch {
            savingsGoalDao.deleteSavingsGoal(id)
        }
    }
}