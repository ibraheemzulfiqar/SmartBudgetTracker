package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.ui.add_account.toIcon
import com.sudocipher.budget.tracker.ui.dashboard.accountTypeString

@Composable
fun AccountTypePickerBottomSheet(
    state: BottomSheetDismissibleState,
    onTypeSelected: (AccountType) -> Unit,
) {
    AppBottomSheet(state) {
        BottomSheetHeader(
            title = "Account Type",
            onClose = { state.dismiss() }
        )

        LazyColumn {
            items(
                items = AccountType.entries,
            ) { type ->
                BottomSheetListItem(
                    title = accountTypeString(type),
                    icon = type.toIcon(),
                    onClick = {
                        onTypeSelected(type)
                        state.dismiss()
                    }
                )
            }
        }
    }
}
