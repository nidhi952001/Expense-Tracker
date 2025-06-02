package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.DisplayUIState.overViewDisplayState
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
import com.example.expensetracker.utils.InputUIState.SelectedMonthAndYear
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import com.google.common.collect.Multimaps
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpenseIncomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
):ViewModel() {

    private val _expenseTempState = MutableStateFlow(ExpenseIncomeInputState())
    val tempExpeIncState = _expenseTempState.asStateFlow()

    val currentCategory = categoryRepository.selectedCategory
    val currentWallet = walletRepository.selectedWallet

    private val _currentMonthYear = MutableStateFlow(SelectedMonthAndYear())
    val currentMonthYear = _currentMonthYear.asStateFlow()

    // Your formatted string
    val format = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val date = format.parse(_currentMonthYear.value.selectedMonthYear)
    val timestamp = date.date.inc()


    private val showExpenseTotal = transactionRepository.showTotalExpense(TransactionType.Expense)
    private val showIncomeTotal = transactionRepository.showTotalIncome(TransactionType.Income)

    val _showTransaction = transactionRepository.showExpenseTransaction().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )


    val showOverView = combine(showExpenseTotal,showIncomeTotal){
        expense , income ->
        overViewDisplayState(expense,income,(income-expense))
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(),
        overViewDisplayState(0F,0F,0F))

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
        val expense = Transaction(
            transactionId = 0,
            transactionTime = 0L,
            transactionDate = _expenseTempState.value.selectedDate,
            transactionAmount = _expenseTempState.value.expIncAmount.toFloat(),
            transactionDescription = _expenseTempState.value.expIncDescription,
            transactionType = TransactionType.Expense,
            transactionCategory = currentCategory.value,
            transactionWallet = currentWallet.value
        )
        viewModelScope.launch {
            transactionRepository.addExpense(expense)
            //fetch current wallet amount
            val currentWalletAmount = walletRepository.getWalletAmountById(currentWallet.value)
            //update the wallet
            if(!_expenseTempState.value.expIncAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount -
                        _expenseTempState.value.expIncAmount.toFloat()

                walletRepository.updateWalletBalance(updateWalletAmount, currentWallet.value)

                //now reset the value foe UI
                resetUiState()
            }
        }


    }



    fun saveIntoIncome() {
        Log.d("DEBUG", "Current state: ${_expenseTempState.value}")

        val income = Transaction(
            transactionId = 0,
            transactionDate = _expenseTempState.value.selectedDate,
            transactionTime = 0L,
            transactionAmount = _expenseTempState.value.expIncAmount.toFloat(),
            transactionDescription = _expenseTempState.value.expIncDescription,
            transactionType = TransactionType.Income,
            transactionCategory = currentCategory.value,
            transactionWallet = currentWallet.value
        )
        viewModelScope.launch {
            transactionRepository.addIncome(income)
            //fetch current wallet amount
            val currentWalletAmount = walletRepository.getWalletAmountById(currentWallet.value)
            //update the wallet
            if(!_expenseTempState.value.expIncAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount +
                        _expenseTempState.value.expIncAmount.toFloat()

                walletRepository.updateWalletBalance(updateWalletAmount, currentWallet.value)

                //now reset the value foe UI
                resetUiState()
            }
        }
    }

    fun resetUiState(){
        _expenseTempState.update {
            it.copy(
                showDateDialogUI = false,
                expIncDescription = "",
                expIncAmount= "",
                validExpIncAmount= false
            )
        }
    }


    fun fetchTotalExpenseCountById(walletId:Int){

    }
}