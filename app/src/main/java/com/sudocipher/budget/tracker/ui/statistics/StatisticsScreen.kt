package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.designsystem.theme.Green
import com.sudocipher.budget.tracker.designsystem.theme.Red
import com.sudocipher.budget.tracker.domain.model.Category
import com.sudocipher.budget.tracker.domain.model.CategoryData

@Composable
fun StatisticsScreen(
    state: StatisticsState,
    selectedParent: Category?,
    onCategoryClick: (Category) -> Unit,
    onBackToParent: () -> Unit,
) {
    when (state) {
        StatisticsState.Loading -> {
            LoadingBox()
        }

        is StatisticsState.Success -> {
            StatisticsContent(
                state = state,
                selectedParent = selectedParent,
                onCategoryClick = onCategoryClick,
                onBackToParent = onBackToParent
            )
        }
    }
}

@Composable
private fun StatisticsContent(
    state: StatisticsState.Success,
    selectedParent: Category?,
    onCategoryClick: (Category) -> Unit,
    onBackToParent: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OverviewCards(state.totalIncome, state.totalExpense, state.currencySymbol)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedParent == null) "Spending by Category" else "Spending in ${stringResource(CategoryData.getCategoryItemOf(selectedParent).name)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (selectedParent != null) {
                    TextButton(onClick = onBackToParent) {
                        Text("Back to All")
                    }
                }
            }
        }

        if (state.categoryStats.isNotEmpty()) {
            items(state.categoryStats) { stat ->
                CategoryStatRow(
                    stat = stat,
                    currencySymbol = state.currencySymbol,
                    onClick = { if (selectedParent == null) onCategoryClick(stat.category) }
                )
            }
        } else {
            item {
                Text(
                    text = "No spending data available",
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun OverviewCards(income: Double, expense: Double, currencySymbol: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Income",
            amount = income,
            currencySymbol = currencySymbol,
            color = Green,
            icon = AppIcons.Income
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Expense",
            amount = expense,
            currencySymbol = currencySymbol,
            color = Red,
            icon = AppIcons.Expense
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    currencySymbol: String,
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
                text = CurrencyFormatter.formatWithSymbol(amount, currencySymbol),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun CategoryStatRow(
    stat: CategoryStat,
    currencySymbol: String,
    onClick: () -> Unit
) {
    val categoryItem = CategoryData.getCategoryItemOf(stat.category)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                text = CurrencyFormatter.formatWithSymbol(stat.amount, currencySymbol),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { stat.percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        
        Text(
            text = "${String.format("%.1f", stat.percentage * 100)}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.End)
        )
    }
}
