package com.sudocipher.budget.tracker.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.rememberAppPainter

object AppIcons {

    val ArrowBack: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.ArrowBack)

    val Check: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Check)

    val Add: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Add)

}