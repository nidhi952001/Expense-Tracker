package com.example.expensetracker.utils

enum class StatisticCategory{
    INCOME, EXPENSE
}

data class selectedStatistics(
    val selectedStatisticBar:StatisticCategory = StatisticCategory.EXPENSE
)

data class selectedCategory(
    val selectedCategoryId:Int =0,
    val selectetedCategoryName:Int?=null
)