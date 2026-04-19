package com.sudocipher.budget.tracker.domain.model

data class GoalTemplate(
    val icon: SavingsGoalIcon,
    val defaultName: String,
) {
    companion object {
        val presets = listOf(
            GoalTemplate(SavingsGoalIcon.VEHICLE, "New Vehicle"),
            GoalTemplate(SavingsGoalIcon.HOME, "New Home"),
            GoalTemplate(SavingsGoalIcon.VACATION, "Vacation"),
            GoalTemplate(SavingsGoalIcon.EDUCATION, "Education"),
            GoalTemplate(SavingsGoalIcon.GADGET, "New Gadget"),
            GoalTemplate(SavingsGoalIcon.OTHER, "Savings")
        )
    }
}
