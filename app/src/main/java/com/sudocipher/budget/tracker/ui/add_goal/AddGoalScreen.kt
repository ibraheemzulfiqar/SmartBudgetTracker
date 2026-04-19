package com.sudocipher.budget.tracker.ui.add_goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.dialogs.IconPickerBottomSheet
import com.sudocipher.budget.tracker.common.ui.rememberBottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppButton
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.ColorTagDropdown
import com.sudocipher.budget.tracker.designsystem.components.SelectableItem
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon
import com.sudocipher.budget.tracker.ui.dashboard.dateFormatted
import com.sudocipher.budget.tracker.ui.goals.toIcon
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    nameState: TextFieldState,
    targetAmountState: TextFieldState,
    savedAmountState: TextFieldState,
    selectedIcon: SavingsGoalIcon,
    selectedColor: ColorTag,
    desiredDate: Instant?,
    goalToEdit: SavingsGoal?,
    isLoading: Boolean,
    onIconSelected: (SavingsGoalIcon) -> Unit,
    onColorSelected: (ColorTag) -> Unit,
    onDateSelected: (Instant?) -> Unit,
    onSaveGoal: () -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val iconPickerState = rememberBottomSheetDismissibleState()

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = desiredDate?.toEpochMilliseconds() ?: Clock.System.now().toEpochMilliseconds(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val today = Clock.System.now().toEpochMilliseconds()
                    return utcTimeMillis >= today - (24 * 60 * 60 * 1000)
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis?.let { Instant.fromEpochMilliseconds(it) })
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    IconPickerBottomSheet(
        state = iconPickerState,
        onIconSelected = onIconSelected
    )

    AppScaffold(
        title = if (goalToEdit != null) "Edit Goal" else "New Goal",
        onNavigateUp = onNavigateUp,
        actions = {
            if (goalToEdit != null) {
                AppIconButton(
                    icon = AppIcons.Delete,
                    onClick = onDelete,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            AppIconButton(
                icon = AppIcons.Check,
                onClick = onSaveGoal
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Target Amount Input
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Target Amount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                VerticalSpacer(8.dp)
                
                OutlinedTextField(
                    state = targetAmountState,
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
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }

            // Goal Details Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    state = nameState,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Goal Name") },
                    placeholder = { Text("e.g. Dream Wedding") },
                    shape = MaterialTheme.shapes.large,
                    lineLimits = TextFieldLineLimits.SingleLine
                )

                OutlinedTextField(
                    state = savedAmountState,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Initial Saved") },
                    placeholder = { Text("0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.large,
                    lineLimits = TextFieldLineLimits.SingleLine
                )

                SelectableItem(
                    label = "Goal Icon",
                    value = selectedIcon.name.lowercase().replaceFirstChar { it.uppercase() },
                    icon = selectedIcon.toIcon(),
                    iconContainerColor = Color(selectedColor.hex).copy(alpha = 0.15f),
                    iconContentColor = Color(selectedColor.hex),
                    onClick = { iconPickerState.show() }
                )

                SelectableItem(
                    label = "Target Date",
                    value = desiredDate?.let { dateFormatted(it) } ?: "Select a date",
                    icon = AppIcons.CalendarMonth,
                    onClick = { showDatePicker = true }
                )

                ColorTagDropdown(
                    selectedColor = selectedColor,
                    onColorSelected = onColorSelected,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            VerticalSpacer(16.dp)

            AppButton(
                onClick = onSaveGoal,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                text = if (goalToEdit != null) "Save Changes" else "Create Goal",
            )
        }
    }
}
