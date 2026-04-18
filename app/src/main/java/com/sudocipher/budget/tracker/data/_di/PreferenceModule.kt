package com.sudocipher.budget.tracker.data._di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.sudocipher.budget.tracker.data.datastore.Preference
import com.sudocipher.budget.tracker.data.datastore.PreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preference> = DataStoreFactory.create(
        serializer = PreferenceSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { Preference.DEFAULT },
        produceFile = {
            context.dataStoreFile("preference.json")
        }
    )

}