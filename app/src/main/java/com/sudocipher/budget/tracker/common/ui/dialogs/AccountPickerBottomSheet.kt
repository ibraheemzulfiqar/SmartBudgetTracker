package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.Account

@Composable
fun AccountPickerBottomSheet(
    state: BottomSheetDismissibleState,
    accounts: List<Account>,
    onAccountSelected: (Account) -> Unit,
) {
    AppBottomSheet(state) {

        CenterAlignedTopAppBar(
            title = {
                Text("Select Account")
            },
            navigationIcon = {
                AppIconButton(
                    icon = AppIcons.ArrowBack,
                    onClick = { state.dismiss() }
                )
            }
        )

        LazyColumn {

            items(
                items = accounts,
            ) { account ->
                ListItem(
                    modifier = Modifier.clickable(
                        onClick = {
                            onAccountSelected(account)
                            state.dismiss()
                        }
                    ),
                    headlineContent = {
                        Text(account.name)
                    },
                    supportingContent = {
                        Text(account.type.name)
                    }
                )
            }

        }
    }
}