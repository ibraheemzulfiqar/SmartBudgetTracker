package com.sudocipher.budget.tracker.ui.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import com.sudocipher.budget.tracker.common.utils.CurrencyFormatter
import com.sudocipher.budget.tracker.designsystem.DimenTokens
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.HorizontalSpacer
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.designsystem.theme.Green
import com.sudocipher.budget.tracker.designsystem.theme.Red
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.domain.model.AccountType
import com.sudocipher.budget.tracker.domain.model.CategoryData
import com.sudocipher.budget.tracker.domain.model.Transaction
import com.sudocipher.budget.tracker.domain.model.TransactionType
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
    Column(modifier = modifier.fillMaxSize()) {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        AccountSection(
            accounts = state.accounts,
            currencySymbol = state.currencySymbol,
            onNavigateToAddAccount = onNavigateToAddAccount,
        )

        VerticalSpacer(16.dp)

        if (state.transactions.isNotEmpty()) {
            Text(
                text = "Last records overview",
                fontSize = 16.sp,
            )

            VerticalSpacer(24.dp)

            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
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
            }

        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("No Transaction found!")
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            AppIcon(categoryItem.icon())
        }

        Column {
            Text(stringResource(categoryItem.name))
            Text(
                text = transaction.account.name.uppercase(),
                color = colorScheme.outline,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            val sign = if (transaction.type == TransactionType.EXPENSE) "-" else ""

            Text(
                text = "${sign}${CurrencyFormatter.formatWithSymbol(transaction.amount, currencySymbol)}",
                color = if (transaction.type == TransactionType.EXPENSE) Red else Green
            )

            Text(
                text = dateFormatted(transaction.timestamp),
                color = colorScheme.outline,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun AccountSection(
    accounts: List<Account>,
    currencySymbol: String,
    onNavigateToAddAccount: (Long?) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Accounts",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )

        Spacer(Modifier.weight(1f))


        TextButton(onClick = { onNavigateToAddAccount(null) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(AppIcons.AddCircle, Modifier.size(16.dp))
                HorizontalSpacer(4.dp)
                Text("New")
            }
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
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

@Composable
private fun AccountCard(
    account: Account,
    currencySymbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .size(DimenTokens.AccountCardWidth, DimenTokens.AccountCardHeight)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = dropUnlessResumed { onClick() })
            .background(Color(account.colorTag.hex))
            .padding(12.dp)
    ) {
        Text(
            text = account.name.uppercase(),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White.copy(alpha = 0.9f)
        )

        VerticalSpacer(2.dp)

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = CurrencyFormatter.formatWithSymbol(account.balance, currencySymbol),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )

        VerticalSpacer(1.dp)

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = accountTypeString(account.type),
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun AddAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = colorScheme.onSurface

    Box(
        modifier = modifier
            .size(DimenTokens.AccountCardWidth, DimenTokens.AccountCardHeight)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(colorScheme.surfaceContainer)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val cornerRadius = 8.dp.toPx()

                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(10f, 10f),
                            phase = 0f
                        )
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AppIcon(
            icon = AppIcons.Add,
        )
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
