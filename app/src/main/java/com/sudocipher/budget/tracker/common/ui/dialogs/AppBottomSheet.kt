package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.common.ui.dismissed

@Composable
fun AppBottomSheet(
    state: BottomSheetDismissibleState,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (state.dismissed) return

    ModalBottomSheet(
        onDismissRequest = state::dismiss,
        sheetState = state.sheetState,
        containerColor = colorScheme.surfaceContainer,
        contentColor = colorScheme.onSurface,
        dragHandle = dragHandle,
        content = content,
    )
}