package com.example.expensetracker.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.expensetracker.entity.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert
    suspend fun addIncome(income: Income)

    @Query("select * from Income")
    fun showIncome(): Flow<List<Income>>
}