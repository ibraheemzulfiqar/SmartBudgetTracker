package com.sudocipher.budget.tracker.data.database.converter

import androidx.room.TypeConverter
import com.sudocipher.budget.tracker.domain.model.Category

class CategoryConverter {

    @TypeConverter
    fun fromCategory(value: Category): String = when (value) {
        is Category.FoodAndDrinks.Bar -> "food_bar"
        is Category.FoodAndDrinks.Groceries -> "food_groceries"
        is Category.FoodAndDrinks.Restaurant -> "food_restaurant"
        is Category.Shopping.ClothsAndShoes -> "shop_clothes"
        is Category.Shopping.Gifts -> "shop_gifts"
        is Category.Transportation -> "transport"
        is Category.FoodAndDrinks -> "FoodAndDrinks"
        is Category.Income -> "Income"
        is Category.Shopping -> "Shopping"
        is Category.Subscription -> "Subscription"
        is Category.Others -> "others"
    }

    @TypeConverter
    fun toCategory(value: String): Category = when (value) {
        "food_bar" -> Category.FoodAndDrinks.Bar
        "food_groceries" -> Category.FoodAndDrinks.Groceries
        "food_restaurant" -> Category.FoodAndDrinks.Restaurant
        "shop_clothes" -> Category.Shopping.ClothsAndShoes
        "shop_gifts" -> Category.Shopping.Gifts
        "transport" -> Category.Transportation
        "FoodAndDrinks" -> Category.FoodAndDrinks
        "Income" -> Category.Income
        "Shopping" -> Category.Shopping
        "Subscription" -> Category.Subscription
        else -> Category.Others
    }
}