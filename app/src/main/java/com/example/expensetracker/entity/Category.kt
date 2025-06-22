package com.example.expensetracker.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color,
    val categoryType: CategoryType
)

enum class CategoryType{
    INCOME , EXPENSE , TRANSFER
}
