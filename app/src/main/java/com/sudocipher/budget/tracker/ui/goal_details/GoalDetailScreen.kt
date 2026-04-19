package com.sudocipher.budget.tracker.ui.goal_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudocipher.budget.tracker.common.ui.dialogs.AddAmountDialog
import com.sudocipher.budget.tracker.common.ui.rememberDismissible
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.SavingsGoal

@Composable
fun GoalDetailScreen(
    state: GoalDetailState,
    onAddAmount: (Double) -> Unit,
    onMarkAsAchieved: () -> Unit,
    onDeleteGoal: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToEditGoal: (id: Long) -> Unit,
) {
    AppScaffold(
        title = "Goal Details",
        onNavigateUp = onNavigateUp,
        actions = {
            if (state is GoalDetailState.Success) {
                AppIconButton(
                    icon = AppIcons.Edit,
                    onClick = { onNavigateToEditGoal(state.goal.id) },
                )
            }
        }
    ) {
        when (state) {
            GoalDetailState.Loading -> {
                LoadingBox()
            }

            is GoalDetailState.Success -> {
                GoalDetailsContent(
                    goal = state.goal,
                    currencySymbol = state.currencySymbol,
                    onAddAmount = onAddAmount,
                    onMarkAsAchieved = onMarkAsAchieved,
                    onDeleteGoal = onDeleteGoal,
                )
            }
        }
    }
}

@Composable
private fun GoalDetailsContent(
    goal: SavingsGoal,
    currencySymbol: String,
    onAddAmount: (Double) -> Unit,
    onMarkAsAchieved: () -> Unit,
    onDeleteGoal: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val addAmountDialog = rememberDismissible()

    AddAmountDialog(
        state = addAmountDialog,
        goal = goal,
        onConfirm = { onAddAmount(it) },
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = goal.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        GoalCircularProgressBar(goal)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${CurrencyFormatter.formatWithSymbol(goal.savedAmount, currencySymbol)} saved",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Target: ${CurrencyFormatter.formatWithSymbol(goal.targetAmount, currencySymbol)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { addAmountDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Amount")
            }

            Button(
                onClick = onMarkAsAchieved,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Done")
            }
        }

        Button(
            onClick = onDeleteGoal,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Delete Goal")
        }
    }
}

@Composable
fun GoalCircularProgressBar(
    goal: SavingsGoal,
    modifier: Modifier = Modifier,
) {
    val color = Color(goal.colorTag.hex)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
    ) {
        CircularProgressIndicator(
            progress = { goal.progress },
            modifier = Modifier.fillMaxSize(),
            color = color,
            trackColor = colorScheme.surfaceVariant,
            strokeWidth = 12.dp,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(goal.progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp),
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}
