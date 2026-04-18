package com.sudocipher.budget.tracker.ui._feature

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = FeatureViewModel.Factory::class)
class FeatureViewModel @AssistedInject constructor(
    private val preferenceStore: PreferenceStore,
    @Assisted private val addLoadingDelay: Boolean,
) : ViewModel() {

    val state: StateFlow<FeatureState> = preferenceStore.showWelcomeMessage()
        .onStart {
            if (addLoadingDelay) delay(2.seconds)
        }
        .mapLatest {
            FeatureState.Success(showWelcomeMessage = it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FeatureState.Loading,
        )


    @AssistedFactory
    interface Factory {
        fun create(addLoadingDelay: Boolean): FeatureViewModel
    }

}


sealed interface FeatureState {

    data object Loading : FeatureState

    @Immutable
    data class Success(
        val showWelcomeMessage: Boolean,
    ) : FeatureState
}