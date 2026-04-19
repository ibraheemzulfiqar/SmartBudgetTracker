package com.sudocipher.budget.tracker.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.R
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.icons.AppIcons

data class CategoryItem(
    val category: Category,
    @StringRes val name: Int,
    val icon: @Composable () -> AppPainter,
    val subCategories: List<CategoryItem> = emptyList(),
) {
    val hasSubCategories: Boolean
        get() = subCategories.isNotEmpty()
}

object CategoryData {

    fun getCategories(): List<CategoryItem> {
        return listOf(
            CategoryItem(
                category = Category.FoodAndDrinks,
                name = R.string.category_food_drinks,
                icon = { AppIcons.Food },
                subCategories = listOf(
                    CategoryItem(Category.Bar, R.string.bar, { AppIcons.Bar }),
                    CategoryItem(Category.Groceries, R.string.groceries, { AppIcons.Grocery }),
                    CategoryItem(Category.Restaurant, R.string.restaurant, { AppIcons.Restaurant })
                )
            ),
            CategoryItem(
                category = Category.Shopping,
                name = R.string.shopping,
                icon = { AppIcons.ShoppingCart },
                subCategories = listOf(
                    CategoryItem(Category.ClothsAndShoes, R.string.clothes_shoes, { AppIcons.Gadget }),
                    CategoryItem(Category.Gifts, R.string.gifts, { AppIcons.Gift })
                )
            ),
            CategoryItem(Category.Transportation, R.string.transportation, { AppIcons.Vehicle }),
            CategoryItem(Category.Subscription, R.string.subscription, { AppIcons.Gadget }),
            CategoryItem(Category.Income, R.string.income, { AppIcons.Income }),
            CategoryItem(Category.Others, R.string.others, { AppIcons.Others })
        )
    }

    fun getCategoryItemOf(category: Category): CategoryItem {
        val categories = getCategories()

        fun findRecursive(items: List<CategoryItem>): CategoryItem? {
            for (item in items) {
                if (item.category.id == category.id) return item
                val foundInSub = findRecursive(item.subCategories)
                if (foundInSub != null) return foundInSub
            }
            return null
        }

        return findRecursive(categories) ?: CategoryItem(Category.Others, R.string.others, { AppIcons.Others })
    }
}
