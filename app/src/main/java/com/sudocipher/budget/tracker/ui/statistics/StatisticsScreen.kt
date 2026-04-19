package com.sudocipher.budget.tracker.ui.statistics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.Pie
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

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
    val chartColors = listOf(
        Color(0xFF2E7D32), // Green
        Color(0xFF1565C0), // Blue
        Color(0xFFC62828), // Red
        Color(0xFFF9A825), // Yellow
        Color(0xFF6A1B9A), // Purple
        Color(0xFFEF6C00), // Orange
        Color(0xFF00838F), // Cyan
        Color(0xFFAD1457), // Pink
        Color(0xFF283593), // Indigo
        Color(0xFF37474F), // Blue Grey
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            OverviewCards(state.totalIncome, state.totalExpense, state.currencySymbol)
        }

        if (state.monthlyStats.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Income vs Expenses",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        val monthLabels = state.monthlyStats.map { stat ->
                            val parts = stat.month.split("-")
                            val month = Month.of(parts[1].toInt())
                            month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        }

                        LineChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            data = listOf(
                                Line(
                                    label = "Income",
                                    values = state.monthlyStats.map { it.income },
                                    color = Brush.verticalGradient(listOf(Green.copy(alpha = 0.8f), Green)),
                                    curvedEdges = true
                                ),
                                Line(
                                    label = "Expense",
                                    values = state.monthlyStats.map { it.expense },
                                    color = Brush.verticalGradient(listOf(Red.copy(alpha = 0.8f), Red)),
                                    curvedEdges = true
                                )
                            ),
                            labelProperties = LabelProperties(
                                enabled = true,
                                labels = monthLabels,
                                textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.outline)
                            )
                        )
                    }
                }
            }
        }

        if (state.categoryStats.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Spending Breakdown",
                            modifier = Modifier.align(Alignment.Start),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(16.dp))
                        PieChart(
                            modifier = Modifier.size(200.dp),
                            data = state.categoryStats.mapIndexed { index, stat ->
                                Pie(
                                    label = stringResource(CategoryData.getCategoryItemOf(stat.category).name),
                                    data = stat.amount,
                                    color = chartColors[index % chartColors.size],
                                    selectedColor = chartColors[index % chartColors.size].copy(alpha = 0.8f)
                                )
                            }
                        )
                    }
                }
            }
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
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                if (selectedParent != null) {
                    TextButton(onClick = onBackToParent) {
                        Text(
                            text = "Back to All", 
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun OverviewCards(income: Double, expense: Double, currencySymbol: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AppIcon(icon = icon, tint = color, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title, 
                    style = MaterialTheme.typography.labelMedium, 
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = CurrencyFormatter.formatWithSymbol(amount, currencySymbol),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AppIcon(icon = categoryItem.icon(), modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
            
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(categoryItem.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                LinearProgressIndicator(
                    progress = { stat.percentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            
            Spacer(Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.formatWithSymbol(stat.amount, currencySymbol),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
                Text(
                    text = "${String.format("%.1f", stat.percentage * 100)}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1
                )
            }
        }
    }
}
