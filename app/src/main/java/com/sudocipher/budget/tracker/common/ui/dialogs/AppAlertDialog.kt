package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudocipher.budget.tracker.common.ui.DismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppButton
import com.sudocipher.budget.tracker.designsystem.components.AppImage
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.VerticalSpacer

@Composable
fun AppAlertDialog(
    state: DismissibleState,
    title: String? = null,
    body: String? = null,
    icon: AppPainter? = null,
    positionActionText: String? = null,
    onPositionActionClick: (() -> Unit)? = null,
    negativeActionText: String? = null,
    onNegativeActionClick: (() -> Unit)? = null,
    isErrorDialog: Boolean = false,
) {
    val centerAlign = icon != null

    val horizontalAlignment = if (centerAlign) {
        Alignment.CenterHorizontally
    } else {
        Alignment.Start
    }

    AppBasicAlertDialog(
        state = state,
        horizontalAlignment = horizontalAlignment,
    ) {

        if (icon != null) {
            AppImage(icon)
            VerticalSpacer(24.dp)
        }

        if (title != null) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title,
                style = typography.titleLarge,
                color = colorScheme.onSurface,
            )
            VerticalSpacer(16.dp)
        }

        if (body != null) {
            Text(
                text = body,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = typography.bodyLarge,
                color = colorScheme.onSurface,
                textAlign = TextAlign.Center.takeIf { centerAlign },
            )

            VerticalSpacer(24.dp)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (negativeActionText != null) {
                AppButton(
                    text = negativeActionText,
                    onClick = onNegativeActionClick ?: {},
                    enabled = onNegativeActionClick != null,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.onSurfaceVariant,
                        contentColor = colorScheme.onSurface,
                        disabledContainerColor = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        disabledContentColor = colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                )
            }

            if (positionActionText != null) {
                AppButton(
                    text = positionActionText,
                    onClick = onPositionActionClick ?: {},
                    enabled = onPositionActionClick != null,
                    modifier = Modifier.weight(1f),
                    colors = if (isErrorDialog) {
                        ButtonDefaults.buttonColors(
                            containerColor = colorScheme.error,
                            contentColor = colorScheme.onError,
                            disabledContainerColor = colorScheme.error.copy(alpha = 0.5f),
                            disabledContentColor = colorScheme.onError.copy(alpha = 0.5f),
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                )
            }
        }
    }
}