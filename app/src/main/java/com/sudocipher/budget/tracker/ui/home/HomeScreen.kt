package com.sudocipher.budget.tracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.dropUnlessResumed
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.components.AppScaffold
import com.sudocipher.budget.tracker.designsystem.components.LoadingBox
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.designsystem.theme.BudgetTheme
import com.sudocipher.budget.tracker.domain.model.Account
import com.sudocipher.budget.tracker.mock.states.MockStatesData

@Preview
@Composable
private fun HomeScreenPrev() {
    BudgetTheme {
        HomeScreen(
            state = MockStatesData.getMockHomeState(),
            onNavigateToAddAccount = {},
            onNavigateToAddTransaction = {},
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeState,
    onNavigateToAddAccount: (id: Long?) -> Unit,
    onNavigateToAddTransaction: (id: Long?) -> Unit,
) {
    AppScaffold(
        title = "Home",
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { onNavigateToAddTransaction(null) }
            ) {
                AppIcon(AppIcons.Add)
            }
        }
    ) {
        when (state) {
            HomeState.Loading -> {
                LoadingBox()
            }

            is HomeState.Success -> {
                HomeLoadedContent(
                    state = state,
                    onNavigateToAddAccount = onNavigateToAddAccount,
                    onNavigateToAddTransaction = onNavigateToAddTransaction,
                )
            }
        }
    }
}

@Composable
private fun HomeLoadedContent(
    state: HomeState.Success,
    onNavigateToAddAccount: (id: Long?) -> Unit,
    onNavigateToAddTransaction: (id: Long?) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text("List of accounts")

        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            state.accounts.fastForEach { account ->
                AccountCard(
                    account = account,
                    onClick = { onNavigateToAddAccount(account.id) },
                    modifier = Modifier
                        .weight(1f)
                )
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = dropUnlessResumed { onNavigateToAddAccount(null) },
            ) {
                Text("Add Account")
            }
        }

        LazyColumn {
            items(
                items = state.transactions,
                key = { it.id }
            ) { transaction ->
                ListItem(
                    modifier = Modifier.clickable(
                        onClick = { onNavigateToAddTransaction(transaction.id) }
                    ),
                    headlineContent = {
                        Text(transaction.amount.toString())
                    },
                    supportingContent = {
                        Text(transaction.category::class.simpleName.toString())
                    },
                    overlineContent = {
                        Text(transaction.account.name)
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountCard(
    account: Account,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = dropUnlessResumed { onClick() })
            .background(Color(account.colorTag.hex))
            .padding(8.dp)
    ) {
        Text(account.name)

        Text("PKR ${account.balance}")
    }
}