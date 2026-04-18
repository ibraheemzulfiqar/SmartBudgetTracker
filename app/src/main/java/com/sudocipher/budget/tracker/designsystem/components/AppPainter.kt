package com.sudocipher.budget.tracker.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AppIcon(
    icon: AppPainter,
    modifier: Modifier = Modifier,
    description: String? = null,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = icon.painter,
        contentDescription = description ?: icon.contentDescription,
        modifier = modifier,
        tint = tint,
    )
}

@Composable
fun AppImage(
    painter: AppPainter,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    description: String? = null,
    tint: Color? = null,
) {
    Image(
        painter = painter.painter,
        contentDescription = description ?: painter.contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        colorFilter = tint?.let { ColorFilter.tint(it) },
    )
}

@Composable
fun rememberAppPainter(
    @DrawableRes id: Int,
    description: String? = null,
): AppPainter {
    return rememberAppPainter(
        painter = painterResource(id),
        description = description,
    )
}

@Composable
fun rememberAppPainter(
    painter: Painter,
    description: String? = null,
): AppPainter {
    return remember(painter, description) {
        AppPainter(painter, description)
    }
}

@Composable
fun rememberAppPainter(
    imageVector: ImageVector,
    description: String? = null,
): AppPainter {
    val painter = rememberVectorPainter(imageVector)

    return remember(painter, description) {
        AppPainter(painter, description)
    }
}

@Immutable
data class AppPainter(
    val painter: Painter,
    val contentDescription: String?,
)