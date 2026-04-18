package com.sudocipher.budget.tracker.common.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sudocipher.budget.tracker.common.ui.BottomSheetDismissibleState
import com.sudocipher.budget.tracker.designsystem.components.AppIconButton
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

        CenterAlignedTopAppBar(
            title = {
                Text("Select Category")
            },
            navigationIcon = {
                AppIconButton(
                    icon = AppIcons.ArrowBack,
                    onClick = {
                        if (subCategory != null) {
                            subCategory = null
                        } else {
                            state.dismiss()
                        }
                    }
                )
            }
        )

        LazyColumn {
            subCategory?.let { category ->
                item {
                    ListItem(
                        modifier = Modifier.clickable(
                            onClick = {
                                onCategorySelected(category)
                                state.dismiss()
                            }
                        ),
                        headlineContent = {
                            Text(stringResource(category.name))
                        },
                    )
                }
            }

            items(
                items = subCategory?.subCategories ?: categoryItems,
            ) { category ->
                ListItem(
                    modifier = Modifier.clickable(
                        onClick = {
                            if (subCategory == null && category.hasSubCategories) {
                                subCategory = category
                            } else {
                                onCategorySelected(category)
                                state.dismiss()
                            }
                        }
                    ),
                    headlineContent = {
                        Text(stringResource(category.name))
                    },
                    trailingContent = {
                        if (category.hasSubCategories) {
                            Icon(Icons.AutoMirrored.Filled.ArrowRight, null)
                        }
                    },
                )
            }
        }
    }
}