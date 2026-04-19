package com.sudocipher.budget.tracker.ui.goals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.dialogs.AddAmountDialog
import com.sudocipher.budget.tracker.common.ui.rememberDismissible
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.domain.model.SavingsGoal

@Composable
fun SavingsGoalsScreen(
    state: SavingsGoalsState,
    onNavigateToGoalDetail: (SavingsGoal) -> Unit,
    onAddAmount: (SavingsGoal, Double) -> Unit,
) {
    when (state) {
        SavingsGoalsState.Loading -> {
            LoadingBox()
        }

        is SavingsGoalsState.Success -> {
            if (state.goals.isEmpty()) {
                EmptyGoalsContent()
            } else {
                GoalsList(
                    goals = state.goals,
                    currencySymbol = state.currencySymbol,
                    onNavigateToGoalDetails = onNavigateToGoalDetail,
                    onAddAmount = onAddAmount,
                )
            }
        }
    }
}

@Composable
private fun EmptyGoalsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No savings goals yet.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun GoalsList(
    goals: List<SavingsGoal>,
    currencySymbol: String,
    onNavigateToGoalDetails: (SavingsGoal) -> Unit,
    onAddAmount: (SavingsGoal, Double) -> Unit,
) {
    val addAmountDialog = rememberDismissible()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(goals) { goal ->
            GoalCard(
                goal = goal,
                currencySymbol = currencySymbol,
                onClick = { onNavigateToGoalDetails(goal) },
                onAddAmount = { addAmountDialog.show() }
            )

            AddAmountDialog(
                state = addAmountDialog,
                goal = goal,
                onConfirm = { onAddAmount(goal, it) }
            )
        }
    }
}

@Composable
private fun GoalCard(
    goal: SavingsGoal,
    currencySymbol: String,
    onClick: () -> Unit,
    onAddAmount: () -> Unit,
) {
    val color = Color(goal.colorTag.hex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(60.dp)
            ) {
                CircularProgressIndicator(
                    progress = { goal.progress },
                    modifier = Modifier.fillMaxSize(),
                    color = color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 6.dp
                )
                AppIcon(
                    icon = goal.icon.toIcon(),
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${CurrencyFormatter.formatWithSymbol(goal.savedAmount, currencySymbol)} / ${CurrencyFormatter.formatAmount(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = onAddAmount) {
                Text("Add")
            }
        }
    }
}
