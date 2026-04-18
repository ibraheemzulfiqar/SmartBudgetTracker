package com.sudocipher.budget.tracker.ui._feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox

@Composable
fun FeatureScreen(
    state: FeatureState,
    onNavigateUp: () -> Unit,
) {
    AppScaffold(
        title = "Feature",
        onNavigateUp = onNavigateUp,
    ) {
        when(state) {
            FeatureState.Loading -> {
                LoadingBox()
            }
            is FeatureState.Success -> {
                if (state.showWelcomeMessage) {
                    Text("Welcome to the App!")
                } else {
                    Text("This is not a welcome message")
                }
            }
        }
    }
}