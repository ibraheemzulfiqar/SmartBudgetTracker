package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.DismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppButton
import com.sudocipher.budget.tracker.designsystem.components.AppOutlinedButton
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer
import com.sudocipher.budget.tracker.domain.model.SavingsGoal

@Composable
fun AddAmountDialog(
    state: DismissibleState,
    goal: SavingsGoal,
    onConfirm: (Double) -> Unit,
) {
    AppBasicAlertDialog(
        state = state
    ) {

        var amountText by remember { mutableStateOf("") }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Add to ${goal.name}",
            style = typography.titleLarge,
            color = colorScheme.onSurface,
        )

        VerticalSpacer(16.dp)

        Text(
            text = "Enter the amount you've saved.",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = typography.bodyLarge,
            color = colorScheme.onSurface,
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpacer(24.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppOutlinedButton(
                text = "Cancel",
                onClick = { state.dismiss() },
                modifier = Modifier.weight(1f),
            )

            AppButton(
                text = "Add",
                onClick = {
                    amountText.toDoubleOrNull()?.let { onConfirm(it) }
                    state.dismiss()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors()
            )
        }
    }

}
