package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.ui.dashboard.accountTypeString

@Composable
fun AccountPickerBottomSheet(
    state: BottomSheetDismissibleState,
    accounts: List<Account>,
    onAccountSelected: (Account) -> Unit,
) {
    AppBottomSheet(state) {
        BottomSheetHeader(
            title = "Select Account",
            onClose = { state.dismiss() }
        )

        LazyColumn {
            items(
                items = accounts,
            ) { account ->
                BottomSheetListItem(
                    title = account.name,
                    subtitle = accountTypeString(account.type),
                    icon = AppIcons.Dashboard,
                    iconContainerColor = Color(account.colorTag.hex).copy(alpha = 0.15f),
                    iconContentColor = Color(account.colorTag.hex),
                    onClick = {
                        onAccountSelected(account)
                        state.dismiss()
                    }
                )
            }
        }
    }
}
