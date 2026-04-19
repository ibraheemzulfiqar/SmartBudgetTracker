package com.sudocipher.budget.tracker.ui.add_account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.dialogs.AccountTypePickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.rememberBottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.*
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.ui.dashboard.accountTypeString

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
        title = "New Account",
        onNavigateUp = onNavigateUp,
        actions = {
            AppIconButton(
                icon = AppIcons.Check,
                onClick = onSaveChanges,
            )
        }
    ) {
        if (isLoading) {
            LoadingBox()
        } else {
            AddAccountContent(
                accountName = accountName,
                accountNumber = accountNumber,
                accountBalance = accountBalance,
                accountType = accountType,
                onTypeChange = onTypeChange,
                onColorChange = onColorChange,
                colorTag = colorTag,
                onSaveChanges = onSaveChanges
            )
        }
    }
}

@Composable
private fun AddAccountContent(
    accountName: TextFieldState,
    accountNumber: TextFieldState,
    accountBalance: TextFieldState,
    accountType: AccountType,
    onTypeChange: (AccountType) -> Unit,
    onColorChange: (ColorTag) -> Unit,
    colorTag: ColorTag,
    onSaveChanges: () -> Unit,
) {
    val accountTypePickerState = rememberBottomSheetDismissibleState()

    AccountTypePickerBottomSheet(
        state = accountTypePickerState,
        onTypeSelected = onTypeChange
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Balance Input
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Initial Balance",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            VerticalSpacer(8.dp)
            
            OutlinedTextField(
                state = accountBalance,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
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

        // Account Details
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                state = accountName,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Account Name") },
                placeholder = { Text("e.g. Personal Savings") },
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                state = accountNumber,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Account Number (Optional)") },
                placeholder = { Text("e.g. **** 1234") },
                shape = MaterialTheme.shapes.large
            )

            SelectableItem(
                label = "Account Type",
                value = accountTypeString(accountType),
                icon = AppIcons.Dashboard,
                onClick = { accountTypePickerState.show() }
            )

            ColorTagDropdown(
                selectedColor = colorTag,
                onColorSelected = onColorChange,
                isLoading = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        VerticalSpacer(16.dp)

        AppButton(
            text = "Save Account",
            onClick = onSaveChanges,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
