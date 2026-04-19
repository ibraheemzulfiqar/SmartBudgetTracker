package com.sudocipher.budget.tracker.domain.model

sealed class Category(
    val id: String,
    val parent: Category? = null
) {
    // Root Categories
    data object FoodAndDrinks : Category("FOOD_AND_DRINKS")
    data object Shopping : Category("SHOPPING")
    data object Transportation : Category("TRANSPORTATION")
    data object Subscription : Category("SUBSCRIPTION")
    data object Income : Category("INCOME")
    data object Others : Category("OTHERS")

    // Food & Drinks Subcategories
    data object Bar : Category("BAR", FoodAndDrinks)
    data object Groceries : Category("GROCERIES", FoodAndDrinks)
    data object Restaurant : Category("RESTAURANT", FoodAndDrinks)

    // Shopping Subcategories
    data object ClothsAndShoes : Category("CLOTHS_AND_SHOES", Shopping)
    data object Gifts : Category("GIFTS", Shopping)

    val rootParent: Category
        get() = parent?.rootParent ?: this

    val isRoot: Boolean
        get() = parent == null

    companion object {
        fun fromId(id: String): Category {
            return when (id) {
                "FOOD_AND_DRINKS" -> FoodAndDrinks
                "SHOPPING" -> Shopping
                "TRANSPORTATION" -> Transportation
                "SUBSCRIPTION" -> Subscription
                "INCOME" -> Income
                "OTHERS" -> Others
                "BAR" -> Bar
                "GROCERIES" -> Groceries
                "RESTAURANT" -> Restaurant
                "CLOTHS_AND_SHOES" -> ClothsAndShoes
                "GIFTS" -> Gifts
                else -> Others
            }
        }

        fun getAll(): List<Category> = listOf(
            FoodAndDrinks, Bar, Groceries, Restaurant,
            Shopping, ClothsAndShoes, Gifts,
            Transportation,
            Subscription,
            Income,
            Others
        )
    }
}
