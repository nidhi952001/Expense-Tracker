package com.example.expensetracker.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int,
    val categoryName:String,
    val categoryIcon:Int,
    val categoryType: CategoryType
)

enum class CategoryType{
    INCOME , EXPENSE
}

object listOfCategory {
    fun fetchCategory(): MutableList<Category> {
        return  mutableListOf(
            Category(1,"Food",Icons.Default.Favorite.hashCode(),CategoryType.INCOME),
            Category(2,"Food",Icons.Default.List.hashCode(),CategoryType.EXPENSE)
            )
    }
}