package com.sudocipher.budget.tracker.ui.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudocipher.budget.tracker.common.ui.dialogs.AddAmountDialog
import com.sudocipher.budget.tracker.common.ui.rememberDismissible
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.ui.goals.toIcon

@Composable
fun SavingsGoalsScreen(
    state: SavingsGoalsState,
    onNavigateToGoalDetail: (SavingsGoal) -> Unit,
    onAddAmount: (SavingsGoal, Double) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
}

@Composable
private fun EmptyGoalsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No savings goals yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.outline,
                maxLines = 1
            )
        }
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
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
    val goalColor = Color(goal.colorTag.hex)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = goalColor.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AppIcon(
                            icon = goal.icon.toIcon(),
                            tint = goalColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${(goal.progress * 100).toInt()}% achieved",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = onAddAmount,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    AppIcon(com.sudocipher.budget.tracker.designsystem.icons.AppIcons.Add, modifier = Modifier.size(20.dp))
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = { goal.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = goalColor,
                    trackColor = goalColor.copy(alpha = 0.1f),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(goal.savedAmount, currencySymbol),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = "of ${CurrencyFormatter.formatAmount(goal.targetAmount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
