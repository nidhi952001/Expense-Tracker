package com.example.expensetracker.entity

import androidx.compose.material.icons.Icons
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