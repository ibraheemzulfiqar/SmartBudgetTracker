package com.sudocipher.budget.tracker.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudocipher.budget.tracker.data.datastore.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceStore: PreferenceStore
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SplashNavigationEvent>(1)
    val navigationEvent: SharedFlow<SplashNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val preference = preferenceStore.prefFlow().first()

            when {
                preference.currencyCode == null -> {
                    _navigationEvent.emit(SplashNavigationEvent.NavigateToCurrencySelection)
                }

                !preference.isInitialAccountSet -> {
                    _navigationEvent.emit(SplashNavigationEvent.NavigateToInitialSetup)
                }

                else -> {
                    _navigationEvent.emit(SplashNavigationEvent.NavigateToMain)
                }
            }
        }
    }

}

sealed interface SplashNavigationEvent {
    data object NavigateToCurrencySelection : SplashNavigationEvent
    data object NavigateToInitialSetup : SplashNavigationEvent
    data object NavigateToMain : SplashNavigationEvent
}
