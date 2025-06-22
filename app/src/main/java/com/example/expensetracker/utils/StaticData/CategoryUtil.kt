package com.example.expensetracker.utils.StaticData

import com.example.expensetracker.R
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.utils.StaticData.listOfWalletColor.coloCodeToColor

object listOfCategory {
    val categoryList = mutableListOf<Category>()
    fun categoryList(): List<Category> {
        val c1= Category(0,R.string.transfer,R.drawable.transfer_ic, coloCodeToColor.values.last(),CategoryType.TRANSFER)
        val c2= Category(1,R.string.transfer_in,R.drawable.transfer_in_ic, coloCodeToColor.values.last(),CategoryType.TRANSFER)
        val c3= Category(2,R.string.transfer_out,R.drawable.transfer_out_ic, coloCodeToColor.values.last(),CategoryType.TRANSFER)

        val expenseCategoryList = baseExpCategories.mapIndexed { index, (id, nameRes, iconRes) ->
            val color = colors.getOrNull(index) ?: colors.last() // fallback to last color if not enough
            Category(id, nameRes, iconRes, color, CategoryType.EXPENSE)
        }
         val incomeCategoryList = baseIncCategories.mapIndexed { index, (id, nameRes, iconRes) ->
            val color = colors.getOrNull(index) ?: colors.last() // fallback to last color if not enough
            Category(id, nameRes, iconRes, color, CategoryType.INCOME)
        }
        categoryList.add(0,c1)
        categoryList.add(1,c2)
        categoryList.add(2,c3)
        categoryList.addAll(expenseCategoryList)
        categoryList.addAll(incomeCategoryList)
        return categoryList

    }
}
val colors = coloCodeToColor.values.toList()
val baseExpCategories = listOf(
    Triple(3, R.string.bills, R.drawable.bill_ic),
    Triple(4, R.string.clothing, R.drawable.clothing_ic),
    Triple(5, R.string.education, R.drawable.education_ic),
    Triple(6, R.string.entertainment, R.drawable.entertainment_ic),
    Triple(7, R.string.fitness, R.drawable.fitness_ic),
    Triple(8, R.string.food, R.drawable.food_ic),
    Triple(9, R.string.furniture, R.drawable.furniture_ic),
    Triple(10, R.string.gifts, R.drawable.gifts_ic),
    Triple(11, R.string.health, R.drawable.health_ic),
    Triple(12, R.string.pet, R.drawable.pet_ic),
    Triple(13, R.string.shopping, R.drawable.shopping_ic),
    Triple(14, R.string.transportation, R.drawable.transport_ic),
    Triple(15, R.string.travel, R.drawable.travel_ic),
    Triple(16, R.string.others, R.drawable.others_ic),
)
val baseIncCategories = listOf(
    Triple(17, R.string.allowance, R.drawable.allowance_ic),
    Triple(18, R.string.award, R.drawable.award_ic),
    Triple(19, R.string.bonus, R.drawable.bonus),
    Triple(20, R.string.dividend, R.drawable.dividend_ic),
    Triple(21, R.string.investment, R.drawable.investment_ic),
    Triple(22, R.string.lottery, R.drawable.lottery_ic),
    Triple(23, R.string.salary, R.drawable.salary_ic),
    Triple(24, R.string.tips, R.drawable.tips_ic),
    Triple(25,R.string.others,R.drawable.others_ic)


)