package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon
import com.sudocipher.budget.tracker.ui.goals.toIcon

@Composable
fun IconPickerBottomSheet(
    state: BottomSheetDismissibleState,
    onIconSelected: (SavingsGoalIcon) -> Unit,
) {
    AppBottomSheet(state) {
        BottomSheetHeader(
            title = "Select Icon",
            onClose = { state.dismiss() }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SavingsGoalIcon.entries) { icon ->
                BottomSheetListItem(
                    title = "", // No title needed for grid
                    icon = icon.toIcon(),
                    onClick = {
                        onIconSelected(icon)
                        state.dismiss()
                    }
                )
            }
        }
    }
}
