package com.sudocipher.budget.tracker.domain.model

sealed interface Category {

    data object FoodAndDrinks : Category {
        data object Bar : Category
        data object Groceries : Category
        data object Restaurant : Category
    }

    data object Shopping : Category {
        data object ClothsAndShoes : Category
        data object Gifts : Category
    }

    data object Transportation : Category

    data object Subscription : Category

    data object Income : Category

    data object Others : Category

}