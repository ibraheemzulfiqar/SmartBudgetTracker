package com.sudocipher.budget.tracker.ui.add_transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.sudocipher.budget.tracker.common.ui.dialogs.AccountPickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.dialogs.CategoryPickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.rememberBottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.CategoryItem
import com.sudocipher.budget.tracker.domain.model.TransactionType

@Composable
fun AddTransactionScreen(
    fetchState: TransactionFetchState,
    accounts: List<Account>,
    amount: TextFieldState,
    category: CategoryItem,
    transactionType: TransactionType,
    onAccountChange: (acc: Account) -> Unit,
    onTransactionTypeChange: (type: TransactionType) -> Unit,
    onCategoryChange: (cat: CategoryItem) -> Unit,
    onSaveChanges: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    AppScaffold(
        title = "Add Transaction",
        onNavigateUp = onNavigateUp,
        actions = {
            IconButton(
                onClick = onSaveChanges,
            ) {
                AppIcon(AppIcons.Check)
            }
        }
    ) {
        when(fetchState) {
            TransactionFetchState.Loading -> {
                LoadingBox()
            }
            is TransactionFetchState.Success -> {
                AddTransactionContent(
                    amount = amount,
                    accounts = accounts,
                    selectedAccount = fetchState.selectedAccount,
                    category = category,
                    transactionType = transactionType,
                    onTransactionTypeChange = onTransactionTypeChange,
                    onAccountChange = onAccountChange,
                    onCategoryChange = onCategoryChange
                )
            }
        }
    }
}

@Composable
private fun AddTransactionContent(
    amount: TextFieldState,
    accounts: List<Account>,
    selectedAccount: Account,
    category: CategoryItem,
    transactionType: TransactionType,
    onTransactionTypeChange: (type: TransactionType) -> Unit,
    onAccountChange: (acc: Account) -> Unit,
    onCategoryChange: (cat: CategoryItem) -> Unit,
) {
    val transactionTypes = remember { TransactionType.entries }
    val categoryPickerBottomSheet = rememberBottomSheetDismissibleState()
    val accountPickerBottomSheet = rememberBottomSheetDismissibleState()

    CategoryPickerBottomSheet(
        state = categoryPickerBottomSheet,
        onCategorySelected = { onCategoryChange(it) },
    )

    AccountPickerBottomSheet(
        state = accountPickerBottomSheet,
        accounts = accounts,
        onAccountSelected = onAccountChange,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            transactionTypes.fastForEach {
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = it == transactionType,
                    onClick = { onTransactionTypeChange(it) },
                    label = { Text(it.name) },
                )
            }
        }

        OutlinedTextField(
            state = amount,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Amount")
            }
        )

        Text("Account")

        Button(onClick = {
            accountPickerBottomSheet.show()
        }) {
            Text(selectedAccount.name)
        }

        Text("Category")

        Button(onClick = {
            categoryPickerBottomSheet.show()
        }) {
            Text(stringResource(category.name))
        }
    }
}

