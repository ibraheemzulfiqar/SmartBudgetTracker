package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons
import com.sudocipher.budget.tracker.domain.model.CategoryData
import com.sudocipher.budget.tracker.domain.model.CategoryItem

@Composable
fun CategoryPickerBottomSheet(
    state: BottomSheetDismissibleState,
    onCategorySelected: (CategoryItem) -> Unit,
) {
    AppBottomSheet(state) {
        val categoryItems = remember { CategoryData.getCategories() }
        var subCategory by remember { mutableStateOf<CategoryItem?>(null) }

        BottomSheetHeader(
            title = if (subCategory == null) "Category" else stringResource(subCategory!!.name),
            onClose = { state.dismiss() },
            onBack = if (subCategory != null) { { subCategory = null } } else null
        )

        LazyColumn {
            subCategory?.let { category ->
                item {
                    BottomSheetListItem(
                        title = "General ${stringResource(category.name)}",
                        icon = category.icon(),
                        iconContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        iconContentColor = MaterialTheme.colorScheme.primary,
                        onClick = {
                            onCategorySelected(category)
                            state.dismiss()
                        }
                    )
                }
            }

            items(
                items = subCategory?.subCategories ?: categoryItems,
            ) { category ->
                BottomSheetListItem(
                    title = stringResource(category.name),
                    icon = category.icon(),
                    iconContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    iconContentColor = MaterialTheme.colorScheme.primary,
                    trailingIcon = if (category.hasSubCategories && subCategory == null) AppIcons.ArrowForward else null,
                    onClick = {
                        if (subCategory == null && category.hasSubCategories) {
                            subCategory = category
                        } else {
                            onCategorySelected(category)
                            state.dismiss()
                        }
                    }
                )
            }
        }
    }
}
