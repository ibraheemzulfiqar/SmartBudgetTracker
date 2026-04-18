package com.sudocipher.budget.tracker.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.rememberAppPainter

object AppIcons {

    val ArrowBack: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.ArrowBack)

}