package com.sudocipher.budget.tracker.data.database.converter

import androidx.room.TypeConverter
import com.sudocipher.budget.tracker.domain.model.Category

class CategoryConverter {

    @TypeConverter
    fun fromCategory(value: Category): String = value.id

    @TypeConverter
    fun toCategory(value: String): Category = Category.fromId(value)
}
