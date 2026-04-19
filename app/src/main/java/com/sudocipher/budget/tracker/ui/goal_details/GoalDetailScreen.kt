package com.sudocipher.budget.tracker.ui.goal_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.dialogs.AddAmountDialog
import com.sudocipher.budget.tracker.common.ui.rememberDismissible
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppButton
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.ui.dashboard.dateFormatted
import com.sudocipher.budget.tracker.ui.goals.toIcon

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
        title = "Goal Detail",
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
    val goalColor = Color(goal.colorTag.hex)

    AddAmountDialog(
        state = addAmountDialog,
        goal = goal,
        onConfirm = { onAddAmount(it) },
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        GoalProgressBar(goal, goalColor)

        GoalInfoCard(goal, currencySymbol)

        GoalDetailCard(goal, currencySymbol)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                text = "Add Savings",
                onClick = { addAmountDialog.show() },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onMarkAsAchieved,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Text("Achieved", color = colorScheme.onSurface)
                }

                IconButton(
                    onClick = onDeleteGoal,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(colorScheme.errorContainer.copy(alpha = 0.2f)),
                ) {
                    AppIcon(
                        icon = AppIcons.Delete,
                        tint = colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GoalDetailCard(
    goal: SavingsGoal,
    currencySymbol: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (goal.desiredDate != null) {
                DetailRow(
                    label = "Target Date",
                    value = dateFormatted(goal.desiredDate),
                    icon = AppIcons.CalendarMonth
                )
            }

            val remaining = (goal.targetAmount - goal.savedAmount).coerceAtLeast(0.0)
            DetailRow(
                label = "Remaining",
                value = CurrencyFormatter.formatWithSymbol(remaining, currencySymbol),
                icon = AppIcons.Savings
            )
        }
    }
}

@Composable
private fun GoalInfoCard(
    goal: SavingsGoal,
    currencySymbol: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = goal.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = CurrencyFormatter.formatWithSymbol(goal.savedAmount, currencySymbol),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            color = colorScheme.primary
        )

        Text(
            text = "Target: ${
                CurrencyFormatter.formatWithSymbol(
                    goal.targetAmount,
                    currencySymbol
                )
            }",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.outline
        )
    }
}

@Composable
private fun GoalProgressBar(
    goal: SavingsGoal,
    goalColor: Color
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
        CircularProgressIndicator(
            progress = { goal.progress },
            modifier = Modifier.fillMaxSize(),
            color = goalColor,
            trackColor = goalColor.copy(alpha = 0.1f),
            strokeWidth = 14.dp,
            strokeCap = StrokeCap.Round
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = goalColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AppIcon(
                        icon = goal.icon.toIcon(),
                        tint = goalColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            VerticalSpacer(8.dp)
            Text(
                text = "${(goal.progress * 100).toInt()}%",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    icon: AppPainter
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(10.dp),
            color = colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.Center) {
                AppIcon(icon = icon, modifier = Modifier.size(20.dp), tint = colorScheme.primary)
            }
        }
        
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}
