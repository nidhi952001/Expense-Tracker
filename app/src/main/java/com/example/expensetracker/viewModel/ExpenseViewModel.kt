package com.example.expensetracker.viewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.ExpenseRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.InputUIState.ExpenseInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    categoryRepository: CategoryRepository,
    walletRepository: WalletRepository
):ViewModel() {

    private val _expenseTempState = MutableStateFlow(ExpenseInputState())
    val tempExpenseState = _expenseTempState.asStateFlow()



    val currentCategory = categoryRepository.selectedCategory
    val currentWallet = walletRepository.selectedWallet

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
        Log.d("TAG", "updateExpenseAmount: $expenseAmount")
        if(expenseAmount.isNotEmpty() && expenseAmount.isNotBlank()) _expenseTempState.update { it.copy(validExpAmount = true) }
        else  _expenseTempState.update { it.copy(validExpAmount = false) }
    }

    fun updateExpenseDes(description: String) {
        _expenseTempState.update {
            it.copy(expDescription = description)
        }

    }

    fun saveIntoExpense() {
        val expense = Expense(
            expId = 0,
            expTime = _expenseTempState.value.selectedDate,
            expDate = 0L,
            expAmount = _expenseTempState.value.expAmount.toFloat(),
            expDescription = _expenseTempState.value.expDescription,
            expCategory = currentCategory.value,
            expWallet = currentWallet.value
        )
        viewModelScope.launch {
            expenseRepository.addExpense(expense)
        }
    }

}