package com.sudocipher.budget.tracker.data._di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /*@Singleton
    @Provides
    fun getMyDatabase(
        @ApplicationContext context: Context,
    ): MyDatabase = Room.databaseBuilder(
        context,
        MyDatabase::class.java,
        "my_db"
    )
        .build()*/

}
