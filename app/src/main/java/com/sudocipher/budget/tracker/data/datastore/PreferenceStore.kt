package com.sudocipher.budget.tracker.data.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceStore @Inject constructor(
    private val datastore: DataStore<Preference>,

) {
    private val externalScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob())

    val preference: StateFlow<Preference> = datastore.data.stateIn(
        scope = externalScope,
        started = SharingStarted.Eagerly,
        initialValue = Preference.DEFAULT,
    )

    fun showWelcomeMessage(): Flow<Boolean> {
        return getPreference { showWelcomeMessage }
    }

    fun setShowWelcomeMessage(show: Boolean): Job {
        return setPreference { copy(showWelcomeMessage = show) }
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

