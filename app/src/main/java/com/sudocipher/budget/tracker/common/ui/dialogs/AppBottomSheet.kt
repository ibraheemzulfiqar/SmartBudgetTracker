package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.common.ui.dismissed

@Composable
fun AppBottomSheet(
    state: BottomSheetDismissibleState,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (state.dismissed) return

    ModalBottomSheet(
        onDismissRequest = state::dismiss,
        sheetState = state.sheetState,
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = dragHandle,
        tonalElevation = 0.dp,
        content = {
            content()
            Spacer(
                modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())
            )
        },
    )
}
