package com.sudocipher.budget.tracker.domain.model

import kotlin.time.Instant

data class SavingsGoal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val desiredDate: Instant?,
    val colorTag: ColorTag,
    val icon: SavingsGoalIcon,
) {
    val progress: Float
        get() = if (targetAmount > 0) (savedAmount / targetAmount).coerceIn(0.0, 1.0).toFloat() else 0f
}

enum class SavingsGoalIcon {
    VEHICLE,
    HOME,
    VACATION,
    EDUCATION,
    GADGET,
    OTHER
}
