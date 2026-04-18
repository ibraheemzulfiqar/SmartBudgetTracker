package com.sudocipher.budget.tracker.data._di

import com.sudocipher.budget.tracker.data.repository.BudgetRepositoryImpl
import com.sudocipher.budget.tracker.domain.repository.BudgetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

}