package com.sudocipher.budget.tracker.ui.add_goal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.designsystem.components.AppButton
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.ColorTagDropdown
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon
import com.sudocipher.budget.tracker.ui.goals.toIcon
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    onNavigateUp: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = desiredDate?.toEpochMilliseconds() ?: Clock.System.now().toEpochMilliseconds(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Only allow today and future dates
                    val today = Clock.System.now().toEpochMilliseconds()
                    // Start of today (approximate by using 24h window or just comparing millis)
                    // Better approach: allow dates from start of today
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

    AppScaffold(
        title = if (goalToEdit != null) "Edit Goal" else "Create Goal",
        onNavigateUp = onNavigateUp,
        actions = {
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(selectedColor.hex).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(
                        icon = selectedIcon.toIcon(),
                        modifier = Modifier.size(48.dp),
                        tint = Color(selectedColor.hex)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Icon",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SavingsGoalIcon.entries.forEach { icon ->
                        val isSelected = selectedIcon == icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                                .clickable { onIconSelected(icon) },
                            contentAlignment = Alignment.Center
                        ) {
                            AppIcon(
                                icon = icon.toIcon(),
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                state = nameState,
                label = { Text("Goal Name") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Dream Wedding") }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    state = targetAmountState,
                    label = { Text("Target Amount") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    state = savedAmountState,
                    label = { Text("Initial Saved") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Desired Date (Optional)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedCard(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 14.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = desiredDate?.formatDate() ?: "Select a date",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (desiredDate == null) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                        )
                        AppIcon(AppIcons.CalendarMonth, modifier = Modifier.size(20.dp))
                    }
                }
            }

            ColorTagDropdown(
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            AppButton(
                onClick = onSaveGoal,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                text = if (goalToEdit != null) "Save Changes" else "Create Goal"
            )
        }
    }
}

fun Instant.formatDate(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
    val javaDateTime = java.time.LocalDateTime.of(
        dateTime.year,
        dateTime.monthNumber,
        dateTime.dayOfMonth,
        dateTime.hour,
        dateTime.minute,
        dateTime.second
    )
    return javaDateTime.format(formatter)
}
