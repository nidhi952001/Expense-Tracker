package com.example.expensetracker.utils.StaticData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType

object listOfCategory {
    fun fetchCategory(): MutableList<Category> {
        return  mutableListOf(
            Category(1,"Food", Icons.Default.Favorite.hashCode(), CategoryType.INCOME),
            Category(2,"Food", Icons.Default.List.hashCode(), CategoryType.EXPENSE)
        )
    }
}