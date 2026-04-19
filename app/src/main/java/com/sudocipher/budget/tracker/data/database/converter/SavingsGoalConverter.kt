package com.sudocipher.budget.tracker.data.database.converter

import com.sudocipher.budget.tracker.data.database.entity.SavingsGoalEntity
import com.sudocipher.budget.tracker.domain.model.ColorTag
import com.sudocipher.budget.tracker.domain.model.SavingsGoal
import com.sudocipher.budget.tracker.domain.model.SavingsGoalIcon
import kotlin.time.Instant

fun SavingsGoalEntity.asDomain(): SavingsGoal =
    SavingsGoal(
        id = id,
        name = name,
        targetAmount = targetAmount,
        savedAmount = savedAmount,
        desiredDate = desiredDate?.let { Instant.fromEpochMilliseconds(it) },
        colorTag = ColorTag.valueOf(colorTag),
        icon = SavingsGoalIcon.valueOf(icon)
    )

fun SavingsGoal.toEntity(): SavingsGoalEntity =
    SavingsGoalEntity(
        id = id,
        name = name,
        targetAmount = targetAmount,
        savedAmount = savedAmount,
        desiredDate = desiredDate?.toEpochMilliseconds(),
        colorTag = colorTag.name,
        icon = icon.name
    )
