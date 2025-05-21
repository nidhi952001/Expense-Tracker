package com.example.expensetracker.utils.StaticData

import com.example.expensetracker.R
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.utils.StaticData.listOfWalletColor.coloCodeToColor

object listOfCategory {
    fun categoryList(): List<Category> {
        val categoryList = mutableListOf<Category>()
        val expenseCategoryList = baseExpCategories.mapIndexed { index, (id, nameRes, iconRes) ->
            val color = colors.getOrNull(index) ?: colors.last() // fallback to last color if not enough
            Category(id, nameRes, iconRes, color, CategoryType.EXPENSE)
        }
         val incomeCategoryList = baseIncCategories.mapIndexed { index, (id, nameRes, iconRes) ->
            val color = colors.getOrNull(index) ?: colors.last() // fallback to last color if not enough
            Category(id, nameRes, iconRes, color, CategoryType.INCOME)
        }
        categoryList.addAll(expenseCategoryList)
        categoryList.addAll(incomeCategoryList)
        return categoryList

    }
}
val colors = coloCodeToColor.values.toList()
val baseExpCategories = listOf(
    Triple(1, R.string.bills, R.drawable.bill_ic),
    Triple(2, R.string.clothing, R.drawable.clothing_ic),
    Triple(3, R.string.education, R.drawable.education_ic),
    Triple(4, R.string.entertainment, R.drawable.entertainment_ic),
    Triple(5, R.string.fitness, R.drawable.fitness_ic),
    Triple(6, R.string.food, R.drawable.food_ic),
    Triple(7, R.string.furniture, R.drawable.furniture_ic),
    Triple(8, R.string.gifts, R.drawable.gifts_ic),
    Triple(9, R.string.health, R.drawable.health_ic),
    Triple(10, R.string.pet, R.drawable.pet_ic),
    Triple(11, R.string.shopping, R.drawable.shopping_ic),
    Triple(12, R.string.transportation, R.drawable.transport_ic),
    Triple(13, R.string.travel, R.drawable.travel_ic),
    Triple(14, R.string.others, R.drawable.others_ic)
)
val baseIncCategories = listOf(
    Triple(15, R.string.allowance, R.drawable.allowance_ic),
    Triple(16, R.string.award, R.drawable.award_ic),
    Triple(17, R.string.bonus, R.drawable.bonus),
    Triple(18, R.string.dividend, R.drawable.dividend_ic),
    Triple(19, R.string.investment, R.drawable.investment_ic),
    Triple(20, R.string.lottery, R.drawable.lottery_ic),
    Triple(21, R.string.salary, R.drawable.salary_ic),
    Triple(22, R.string.tips, R.drawable.tips_ic),
    Triple(23,R.string.others,R.drawable.others_ic)


)