package com.sudocipher.budget.tracker.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEachIndexed
import com.sudocipher.budget.tracker.R
import com.sudocipher.budget.tracker.designsystem.components.AppIcon
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.Category
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.ui.dashboard.DashboardScreen
import com.sudocipher.budget.tracker.ui.dashboard.DashboardState
import com.sudocipher.budget.tracker.ui.goals.SavingsGoalsScreen
import com.sudocipher.budget.tracker.ui.goals.SavingsGoalsState
import com.sudocipher.budget.tracker.ui.main.BottomNavigations.DASHBOARD
import com.sudocipher.budget.tracker.ui.main.BottomNavigations.GOALS
import com.sudocipher.budget.tracker.ui.main.BottomNavigations.STATS
import com.sudocipher.budget.tracker.ui.statistics.StatisticsScreen
import com.sudocipher.budget.tracker.ui.statistics.StatisticsState
import kotlinx.coroutines.launch

enum class BottomNavigations(
    @StringRes val label: Int
) {
    DASHBOARD(R.string.dashboard),
    GOALS(R.string.saving_goals),
    STATS(R.string.statistics)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    dashboardState: DashboardState,
    savingsGoalsState: SavingsGoalsState,
    statisticsState: StatisticsState,
    selectedStatisticsParent: Category?,
    onStatisticsCategoryClick: (Category) -> Unit,
    onStatisticsBackToParent: () -> Unit,
    onNavigateToAddAccount: (Long?) -> Unit,
    onNavigateToAddTransaction: (Long?) -> Unit,
    onNavigateToGoalDetail: (SavingsGoal) -> Unit,
    onNavigateToAddGoal: () -> Unit,
    onAddSavingsAmount: (SavingsGoal, Double) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val navigationEntries = remember { BottomNavigations.entries }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(navigationEntries[pagerState.currentPage].label))
                }
            )
        },
        bottomBar = {
            NavigationBar(
            ) {
                navigationEntries.fastForEachIndexed { i, nav ->
                    val isSelected = pagerState.currentPage == i
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { scope.launch { pagerState.animateScrollToPage(i) } },
                        icon = { NavigationIcon(nav) },
                        label = { Text(stringResource(nav.label)) },

                    )
                }
            }
        },
        floatingActionButton = {
            if (pagerState.currentPage == DASHBOARD.ordinal ||
                pagerState.currentPage == GOALS.ordinal
            ) {
                FloatingActionButton(
                    onClick = {
                        if (pagerState.currentPage == DASHBOARD.ordinal) {
                            onNavigateToAddTransaction(null)
                        } else {
                            onNavigateToAddGoal()
                        }
                    },
                ) {
                    AppIcon(AppIcons.Add)
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 3,
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    DASHBOARD.ordinal -> DashboardScreen(
                        state = dashboardState,
                        onNavigateToAddAccount = onNavigateToAddAccount,
                        onNavigateToAddTransaction = onNavigateToAddTransaction
                    )

                    GOALS.ordinal -> SavingsGoalsScreen(
                        state = savingsGoalsState,
                        onNavigateToGoalDetail = onNavigateToGoalDetail,
                        onAddAmount = onAddSavingsAmount
                    )

                    STATS.ordinal -> StatisticsScreen(
                        state = statisticsState,
                        selectedParent = selectedStatisticsParent,
                        onCategoryClick = onStatisticsCategoryClick,
                        onBackToParent = onStatisticsBackToParent
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationIcon(nav: BottomNavigations) {
    val icon = when (nav) {
        DASHBOARD -> AppIcons.Dashboard
        GOALS -> AppIcons.Savings
        STATS -> AppIcons.Statistics
    }

    AppIcon(icon)
}
