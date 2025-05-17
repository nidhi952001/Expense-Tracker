package com.example.expensetracker.viewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.ExpenseRepository
import com.example.expensetracker.utils.InputUIState.ExpenseInputState
import com.example.expensetracker.utils.StaticData.listOfCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
):ViewModel() {

    private val _expenseTempState = MutableStateFlow(ExpenseInputState())
    val expenseTempState = _expenseTempState.asStateFlow()

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

    fun updateDateDialogState(dateDialog: Boolean) {
        _expenseTempState.update {
            it.copy(showDateDialogUI = dateDialog)
        }

    }

    fun updateSelectedDate(date: Long?) {
        _expenseTempState.update {
            it.copy(selectedDate = date)
        }

    }

    fun updateExpenseAmount(expenseAmount: String) {
        _expenseTempState.update {
            it.copy(expAmount = expenseAmount)
        }
    }

    fun updateExpenseDes(description: String) {
        _expenseTempState.update {
            it.copy(expDescription = description)
        }

    }

}