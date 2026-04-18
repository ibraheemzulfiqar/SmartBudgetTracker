package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.CategoryData

@Composable
fun StatisticsScreen(
    state: StatisticsState,
    modifier: Modifier = Modifier,
    onNavigateUp: (() -> Unit)? = null,
) {
    if (onNavigateUp != null) {
        AppScaffold(
            title = "Statistics",
            onNavigateUp = onNavigateUp
        ) {
            StatisticsBaseContent(state, modifier)
        }
    } else {
        StatisticsBaseContent(state, modifier)
    }
}

@Composable
private fun StatisticsBaseContent(
    state: StatisticsState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (state) {
            StatisticsState.Loading -> LoadingBox()
            is StatisticsState.Success -> {
                StatisticsContent(state)
            }
        }
    }
}

@Composable
private fun StatisticsContent(state: StatisticsState.Success) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OverviewCards(state.totalIncome, state.totalExpense)
        }

        if (state.categoryStats.isNotEmpty()) {
            item {
                Text(
                    text = "Spending by Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.categoryStats) { stat ->
                CategoryStatRow(stat)
            }
        } else {
            item {
                Text(
                    text = "No spending data available for this period",
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun OverviewCards(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Income",
            amount = income,
            color = Color(0xFF4CAF50),
            icon = AppIcons.Income
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Expense",
            amount = expense,
            color = Color(0xFFF44336),
            icon = AppIcons.Expense
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    color: Color,
    icon: AppPainter
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIcon(icon = icon, tint = color)
                Spacer(Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "PKR ${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun CategoryStatRow(stat: CategoryStat) {
    val categoryItem = remember(stat.category) {
        CategoryData.getCategoryItemOf(stat.category)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(categoryItem.name),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "PKR ${String.format("%.2f", stat.amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(stat.percentage)
                    .height(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        Text(
            text = "${String.format("%.1f", stat.percentage * 100)}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.End)
        )
    }
}
