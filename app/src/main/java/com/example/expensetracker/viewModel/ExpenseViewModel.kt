package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.listOfCategory
import com.example.expensetracker.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
):ViewModel() {

    init {
        val prepopulateCategory = listOfCategory.fetchCategory()
    }
    fun addExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.addExpense(expense)
        }
    }

    fun showExpense(){
        expenseRepository.showExpense()
    }
}