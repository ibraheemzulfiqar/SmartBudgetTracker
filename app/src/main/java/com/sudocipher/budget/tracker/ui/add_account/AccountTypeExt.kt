package com.sudocipher.budget.tracker.ui.add_account

import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.AccountType

@Composable
fun AccountType.toIcon(): AppPainter {
    return when (this) {
        AccountType.CASH -> AppIcons.Cash
        AccountType.BANK -> AppIcons.Bank
        AccountType.WALLET -> AppIcons.Wallet
        AccountType.GENERAL -> AppIcons.Dashboard
    }
}
