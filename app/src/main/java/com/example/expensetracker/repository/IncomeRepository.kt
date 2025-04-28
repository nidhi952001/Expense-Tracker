package com.example.expensetracker.repository

import androidx.room.Query
import com.example.expensetracker.dao.IncomeDao
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.Income
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao
) {
    suspend fun addIncome(income: Income){
        incomeDao.addIncome(income)
    }

    fun showIncome(): Flow<List<Income>>{
        return incomeDao.showIncome()
    }
}