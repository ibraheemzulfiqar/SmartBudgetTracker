package com.sudocipher.budget.tracker.domain.model

import androidx.annotation.StringRes
import com.sudocipher.budget.tracker.R

data class CategoryItem(
    val category: Category,
    @StringRes val name: Int,
    val subCategories: List<CategoryItem> = emptyList(),
) {
    val hasSubCategories: Boolean
        get() = subCategories.isNotEmpty()
}

object CategoryData {

    fun getCategories(): List<CategoryItem> {
        val foodAndDrinks = CategoryItem(
            category = Category.FoodAndDrinks,
            name = R.string.category_food_drinks,
            subCategories = listOf(
                CategoryItem(Category.FoodAndDrinks.Bar, R.string.bar),
                CategoryItem(Category.FoodAndDrinks.Groceries, R.string.groceries),
                CategoryItem(Category.FoodAndDrinks.Restaurant, R.string.restaurant)
            )
        )
        val shopping = CategoryItem(
            category = Category.Shopping,
            name = R.string.shopping,
            subCategories = listOf(
                CategoryItem(Category.Shopping.ClothsAndShoes, R.string.clothes_shoes),
                CategoryItem(Category.Shopping.Gifts, R.string.gifts)
            )
        )

        val categories = listOf(
            foodAndDrinks,
            shopping,
            CategoryItem(Category.Transportation, R.string.transportation),
            CategoryItem(Category.Subscription, R.string.subscription),
            CategoryItem(Category.Income, R.string.income),
            CategoryItem(Category.Others, R.string.others)
        )

        return categories
    }

    fun getCategoryItemOf(category: Category): CategoryItem {
        val categories = getCategories()

        fun findRecursive(items: List<CategoryItem>): CategoryItem? {
            for (item in items) {
                if (item.category == category) return item

                val foundInSub = findRecursive(item.subCategories)

                if (foundInSub != null) return foundInSub
            }
            return null
        }

        return findRecursive(categories)
            ?: throw IllegalArgumentException("CategoryItem not found against category=$categories")
    }
}