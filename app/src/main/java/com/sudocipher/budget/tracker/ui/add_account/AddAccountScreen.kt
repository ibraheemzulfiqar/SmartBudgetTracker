package com.sudocipher.budget.tracker.ui.add_account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.designsystem.theme.BudgetTheme
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.mock.states.MockData
import com.sudocipher.budget.tracker.ui.add_account.components.AccountTypeDropdown
import com.sudocipher.budget.tracker.ui.add_account.components.ColorTagDropdown

@Preview
@Composable
private fun AddAccountContentPrev() {
    BudgetTheme {
        val account = MockData.getAccount()

        AddAccountScreen(
            isLoading = false,
            accountName = TextFieldState(account.name),
            accountNumber = TextFieldState(account.number),
            accountBalance = TextFieldState("2000"),
            accountType = account.type,
            colorTag = account.colorTag,
            onTypeChange = {},
            onColorChange = {},
            onSaveChanges = {},
            onNavigateUp = {},
        )
    }
}

@Composable
fun AddAccountScreen(
    isLoading: Boolean,
    accountName: TextFieldState,
    accountNumber: TextFieldState,
    accountBalance: TextFieldState,
    accountType: AccountType,
    colorTag: ColorTag,
    onTypeChange: (AccountType) -> Unit,
    onColorChange: (ColorTag) -> Unit,
    onSaveChanges: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    AppScaffold(
        title = "Add Account",
        onNavigateUp = onNavigateUp,
        actions = {
            IconButton(
                onClick = onSaveChanges,
            ) {
                AppIcon(AppIcons.Check)
            }
        }
    ) {
        AddAccountContent(
            isLoading = isLoading,
            accountName = accountName,
            accountNumber = accountNumber,
            accountBalance = accountBalance,
            accountType = accountType,
            colorTag = colorTag,
            onTypeChange = onTypeChange,
            onColorChange = onColorChange,
        )
    }
}

@Composable
private fun AddAccountContent(
    isLoading: Boolean,
    accountName: TextFieldState,
    accountNumber: TextFieldState,
    accountBalance: TextFieldState,
    accountType: AccountType,
    onTypeChange: (AccountType) -> Unit,
    onColorChange: (ColorTag) -> Unit,
    colorTag: ColorTag,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = accountName,
            readOnly = isLoading,
            label = {
                Text("Account Name")
            },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = accountNumber,
            readOnly = isLoading,
            label = {
                Text("Account Number")
            },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = accountBalance,
            readOnly = isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            label = {
                Text("Initial Balance")
            },
        )

        AccountTypeDropdown(
            selectedType = accountType,
            onTypeSelected = onTypeChange,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        ColorTagDropdown(
            selectedColor = colorTag,
            onColorSelected = onColorChange,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

