package com.example.expensetracker.repository

import androidx.room.Insert
import androidx.room.Query
import com.example.expensetracker.dao.ExpenseDao
import com.example.expensetracker.entity.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun addExpense(expense: Expense){
        expenseDao.addExpense(expense)
    }

    fun showExpense(){
        expenseDao.showExpense()
    }
}