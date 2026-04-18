package com.sudocipher.budget.tracker.data.database.converter

import androidx.room.TypeConverter
import com.sudocipher.budget.tracker.domain.model.TransactionCategory

class CategoryConverter {

    @TypeConverter
    fun fromCategory(value: TransactionCategory): String = when (value) {
        is TransactionCategory.FoodAndDrinks.Bar -> "food_bar"
        is TransactionCategory.FoodAndDrinks.Groceries -> "food_groceries"
        is TransactionCategory.FoodAndDrinks.Restaurant -> "food_restaurant"
        is TransactionCategory.Shopping.ClothsAndShoes -> "shop_clothes"
        is TransactionCategory.Shopping.Gifts -> "shop_gifts"
        is TransactionCategory.Transportation -> "transport"
        is TransactionCategory.FoodAndDrinks -> "FoodAndDrinks"
        is TransactionCategory.Income -> "Income"
        is TransactionCategory.Shopping -> "Shopping"
        is TransactionCategory.Subscription -> "Subscription"
        is TransactionCategory.Others -> "others"
    }

    @TypeConverter
    fun toCategory(value: String): TransactionCategory = when (value) {
        "food_bar" -> TransactionCategory.FoodAndDrinks.Bar
        "food_groceries" -> TransactionCategory.FoodAndDrinks.Groceries
        "food_restaurant" -> TransactionCategory.FoodAndDrinks.Restaurant
        "shop_clothes" -> TransactionCategory.Shopping.ClothsAndShoes
        "shop_gifts" -> TransactionCategory.Shopping.Gifts
        "transport" -> TransactionCategory.Transportation
        "FoodAndDrinks" -> TransactionCategory.FoodAndDrinks
        "Income" -> TransactionCategory.Income
        "Shopping" -> TransactionCategory.Shopping
        "Subscription" -> TransactionCategory.Subscription
        else -> TransactionCategory.Others
    }
}