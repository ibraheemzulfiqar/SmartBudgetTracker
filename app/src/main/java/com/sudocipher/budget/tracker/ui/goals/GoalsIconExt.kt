package com.sudocipher.budget.tracker.ui.goals

import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon

@Composable
fun SavingsGoalIcon.toIcon(): AppPainter {
    return when (this) {
        SavingsGoalIcon.VEHICLE -> AppIcons.Vehicle
        SavingsGoalIcon.HOME -> AppIcons.Home
        SavingsGoalIcon.VACATION -> AppIcons.Vacation
        SavingsGoalIcon.EDUCATION -> AppIcons.Education
        SavingsGoalIcon.GADGET -> AppIcons.Gadget
        SavingsGoalIcon.OTHER -> AppIcons.Savings
    }
}
