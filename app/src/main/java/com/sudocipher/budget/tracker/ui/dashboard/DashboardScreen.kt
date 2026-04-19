package com.sudocipher.budget.tracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.designsystem.theme.Green
import com.sudocipher.budget.tracker.designsystem.theme.Red
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.CategoryData
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionType
import com.sudocipher.budget.tracker.ui.add_account.toIcon
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun DashboardScreen(
    state: DashboardState,
    onNavigateToAddAccount: (id: Long?) -> Unit,
    onNavigateToAddTransaction: (id: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            DashboardState.Loading -> {
                LoadingBox()
            }

            is DashboardState.Success -> {
                DashboardLoadedContent(
                    state = state,
                    onNavigateToAddAccount = onNavigateToAddAccount,
                    onNavigateToAddTransaction = onNavigateToAddTransaction,
                )
            }
        }
    }
}

@Composable
private fun DashboardLoadedContent(
    state: DashboardState.Success,
    onNavigateToAddAccount: (id: Long?) -> Unit,
    onNavigateToAddTransaction: (id: Long?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AccountSection(
                accounts = state.accounts,
                currencySymbol = state.currencySymbol,
                onNavigateToAddAccount = onNavigateToAddAccount,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (state.transactions.isNotEmpty()) {
            items(
                items = state.transactions,
                key = { it.id }
            ) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    currencySymbol = state.currencySymbol,
                    onClick = {
                        onNavigateToAddTransaction(transaction.id)
                    }
                )
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No transactions yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.outline,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    currencySymbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoryItem = remember(transaction) {
        CategoryData.getCategoryItemOf(transaction.category)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AppIcon(
                        icon = categoryItem.icon(),
                        modifier = Modifier.size(24.dp),
                        tint = colorScheme.primary
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(categoryItem.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.account.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                val isExpense = transaction.type == TransactionType.EXPENSE
                val sign = if (isExpense) "-" else "+"
                
                Text(
                    text = "${sign}${CurrencyFormatter.formatWithSymbol(transaction.amount, currencySymbol)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpense) Red else Green,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )

                Text(
                    text = dateFormatted(transaction.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.outline,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun AccountSection(
    accounts: List<Account>,
    currencySymbol: String,
    onNavigateToAddAccount: (Long?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Your Accounts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(onClick = { onNavigateToAddAccount(null) }) {
                AppIcon(AppIcons.AddCircle, tint = colorScheme.primary)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(
                items = accounts,
                key = { it.id },
            ) { account ->
                AccountCard(
                    account = account,
                    currencySymbol = currencySymbol,
                    onClick = { onNavigateToAddAccount(account.id) },
                )
            }
            item {
                AddAccountButton(
                    onClick = { onNavigateToAddAccount(null) }
                )
            }
        }
    }
}

@Composable
private fun AccountCard(
    account: Account,
    currencySymbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accountColor = Color(account.colorTag.hex)
    
    Card(
        modifier = modifier
            .size(width = 160.dp, height = 110.dp)
            .clickable(onClick = dropUnlessResumed { onClick() }),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = accountColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.1f)
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    AppIcon(
                        icon = account.type.toIcon(),
                        modifier = Modifier.size(20.dp),
                        tint = Color.White.copy(alpha = 0.9f)
                    )
                    
                    Text(
                        text = account.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false).padding(start = 8.dp)
                    )
                }

                Column {
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(account.balance, currencySymbol),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = accountTypeString(account.type),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AddAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(width = 160.dp, height = 110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = null
    ) {
        val color = colorScheme.outline
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val cornerRadius = 20.dp.toPx()

                    drawRoundRect(
                        color = color.copy(alpha = 0.3f),
                        style = Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(12f, 8f),
                                phase = 0f
                            )
                        ),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppIcon(
                    icon = AppIcons.Add,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Add Account",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun dateFormatted(publishDate: Instant): String = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.MEDIUM)
    .withLocale(Locale.getDefault())
    .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
    .format(publishDate.toJavaInstant())

@Composable
fun accountTypeString(type: AccountType): String {
    return when (type) {
        AccountType.CASH -> "Cash"
        AccountType.WALLET -> "Wallet"
        AccountType.BANK -> "Bank"
        AccountType.GENERAL -> "General"
    }
}
