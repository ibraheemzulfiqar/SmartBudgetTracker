package com.sudocipher.budget.tracker.domain.model

sealed interface TransactionCategory {

    data object FoodAndDrinks : TransactionCategory {
        data object Bar : TransactionCategory
        data object Groceries : TransactionCategory
        data object Restaurant : TransactionCategory
    }

    data object Shopping : TransactionCategory {
        data object ClothsAndShoes : TransactionCategory
        data object Gifts : TransactionCategory
    }

    data object Transportation : TransactionCategory

    data object Subscription : TransactionCategory

    data object Income : TransactionCategory

    data object Others : TransactionCategory

}