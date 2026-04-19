package com.sudocipher.budget.tracker.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sudocipher.budget.tracker.ui.add_account.addAccountRoute
import com.sudocipher.budget.tracker.ui.add_account.navigateToAddAccount
import com.sudocipher.budget.tracker.ui.add_goal.addGoalRoute
import com.sudocipher.budget.tracker.ui.add_goal.navigateToAddGoal
import com.sudocipher.budget.tracker.ui.add_transaction.addTransactionRoute
import com.sudocipher.budget.tracker.ui.add_transaction.navigateToAddTransaction
import com.sudocipher.budget.tracker.ui.currency_selection.CurrencySelectionRoute
import com.sudocipher.budget.tracker.ui.currency_selection.currencySelectionRoute
import com.sudocipher.budget.tracker.ui.goal_details.navigateToGoalDetail
import com.sudocipher.budget.tracker.ui.goal_details.savingsGoalDetailRoute
import com.sudocipher.budget.tracker.ui.initial_setup.InitialSetupRoute
import com.sudocipher.budget.tracker.ui.initial_setup.initialSetupRoute
import com.sudocipher.budget.tracker.ui.main.MainRoute
import com.sudocipher.budget.tracker.ui.main.mainRoute
import com.sudocipher.budget.tracker.ui.splash.SplashRoute
import com.sudocipher.budget.tracker.ui.splash.splashRoute
import com.sudocipher.budget.tracker.ui.statistics.statisticsRoute

@Composable
fun MyApp() {
    val backStack = rememberNavBackStack(SplashRoute)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {

            splashRoute(
                onNavigateToMain = {
                    backStack.clear()
                    backStack.add(MainRoute)
                },
                onNavigateToCurrencySelection = {
                    backStack.clear()
                    backStack.add(CurrencySelectionRoute)
                },
                onNavigateToInitialSetup = {
                    backStack.clear()
                    backStack.add(InitialSetupRoute)
                }
            )

            currencySelectionRoute(
                onComplete = {
                    backStack.clear()
                    backStack.add(SplashRoute)
                }
            )

            initialSetupRoute(
                navigateToMain = {
                    backStack.clear()
                    backStack.add(MainRoute)
                }
            )

            mainRoute(
                navigateToAddAccount = backStack::navigateToAddAccount,
                navigateToAddTransaction = backStack::navigateToAddTransaction,
                onNavigateToGoalDetail = backStack::navigateToGoalDetail,
                onNavigateToAddGoal = { backStack.navigateToAddGoal() },
            )

            addGoalRoute(
                onNavigateUp = { backStack.removeLastOrNull() }
            )

            savingsGoalDetailRoute(
                navigateToEditGoal = { id -> backStack.navigateToAddGoal(goalId = id) },
                navigateUp = { backStack.removeLastOrNull() },
            )

            addAccountRoute(
                navigateUp = { backStack.removeLastOrNull() }
            )

            addTransactionRoute(
                navigateUp = { backStack.removeLastOrNull() }
            )

            statisticsRoute(
                navigateUp = { backStack.removeLastOrNull() }
            )

        }
    )
}
