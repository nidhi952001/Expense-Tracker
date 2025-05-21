package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.ExpenseRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository
):ViewModel() {

    private val _expenseTempState = MutableStateFlow(ExpenseIncomeInputState())
    val tempExpenseState = _expenseTempState.asStateFlow()



    val currentCategory = categoryRepository.selectedCategory
    val currentWallet = walletRepository.selectedWallet



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
            it.copy(expIncAmount = expenseAmount)
        }
        Log.d("TAG", "updateExpenseAmount: $expenseAmount")
        if(expenseAmount.isNotEmpty() && expenseAmount.isNotBlank()) _expenseTempState.update { it.copy(validExpIncAmount = true) }
        else  _expenseTempState.update { it.copy(validExpIncAmount = false) }
    }

    fun updateExpenseDes(description: String) {
        _expenseTempState.update {
            it.copy(expIncDescription = description)
        }

    }

    fun saveIntoExpense() {
        val expense = Expense(
            expId = 0,
            expTime = 0L,
            expDate = _expenseTempState.value.selectedDate,
            expAmount = _expenseTempState.value.expIncAmount.toFloat(),
            expDescription = _expenseTempState.value.expIncDescription,
            expCategory = currentCategory.value,
            expWallet = currentWallet.value
        )
        viewModelScope.launch {
            expenseRepository.addExpense(expense)
            //fetch current wallet amount
            val currentWalletAmount = walletRepository.getWalletAmountById(currentWallet.value)
            //update the wallet
            if(!_expenseTempState.value.expIncAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount -
                        _expenseTempState.value.expIncAmount.toFloat()

                walletRepository.updateWalletBalance(updateWalletAmount, currentWallet.value)

                //now reset the value foe UI
                resetExpenseUiState()
            }
        }


    }

    fun resetExpenseUiState(){
        _expenseTempState.update {
            it.copy(
                showDateDialogUI = false,
                expIncDescription = "",
                expIncAmount= "",
                validExpIncAmount= false
            )
        }
    }

}