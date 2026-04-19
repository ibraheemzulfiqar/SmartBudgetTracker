package com.sudocipher.budget.tracker.ui.add_transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudocipher.budget.tracker.common.ui.dialogs.AccountPickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.dialogs.CategoryPickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.rememberBottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.*
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.CategoryData
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
        title = "New Transaction",
        onNavigateUp = onNavigateUp,
        actions = {
            AppIconButton(
                icon = AppIcons.Check,
                onClick = onSaveChanges,
            )
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
                    onCategoryChange = onCategoryChange,
                    onSaveChanges = onSaveChanges
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
    onSaveChanges: () -> Unit,
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Transaction Type Selector
        AppSegmentedControl(
            options = transactionTypes,
            selectedOption = transactionType,
            onOptionSelected = onTransactionTypeChange,
            labelProvider = { it.name.lowercase().replaceFirstChar { it.uppercase() } }
        )

        // Amount Input
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Amount",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            VerticalSpacer(8.dp)
            
            OutlinedTextField(
                state = amount,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = {
                    Text(
                        "0.00",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        // Account Selector
        SelectableItem(
            label = "Account",
            value = selectedAccount.name,
            icon = AppIcons.Dashboard, // Using Dashboard as a generic account icon
            iconContainerColor = Color(selectedAccount.colorTag.hex).copy(alpha = 0.2f),
            iconContentColor = Color(selectedAccount.colorTag.hex),
            onClick = { accountPickerBottomSheet.show() }
        )

        // Category Selector
        SelectableItem(
            label = "Category",
            value = stringResource(category.name),
            icon = category.icon(),
            onClick = { categoryPickerBottomSheet.show() }
        )

        VerticalSpacer(16.dp)

        AppButton(
            text = "Save Transaction",
            onClick = onSaveChanges,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
