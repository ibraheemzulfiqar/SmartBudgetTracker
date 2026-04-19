package com.sudocipher.budget.tracker.data.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceStore @Inject constructor(
    private val datastore: DataStore<Preference>,

) {
    private val externalScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob())

    val preference = datastore.data

    fun setCurrencyCode(code: String): Job {
        return setPreference { copy(currencyCode = code) }
    }

    fun setInitialAccountSet(isSet: Boolean): Job {
        return setPreference { copy(isInitialAccountSet = isSet) }
    }

    private fun setPreference(
        transform: suspend Preference.() -> Preference,
    ): Job = externalScope.launch {
        datastore.updateData { it.transform() }
    }

    private inline fun <T> getPreference(
        crossinline transform: suspend Preference.() -> T
    ): Flow<T> = datastore.data.map { it.transform() }

}

