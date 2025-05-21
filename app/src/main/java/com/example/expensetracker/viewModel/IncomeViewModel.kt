package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Income
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.IncomeRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _incomeTempState = MutableStateFlow(ExpenseIncomeInputState())
    val incomeTempState = _incomeTempState.asStateFlow()

    val selectedIncCategory = categoryRepository.selectedCategory
    val selectedIncWallet = walletRepository.selectedWallet

    fun addIncome(income: Income){
        viewModelScope.launch((Dispatchers.IO)) {
            incomeRepository.addIncome(income)
        }
    }

    fun showIncome(): Flow<List<Income>> {
        return incomeRepository.showIncome()
    }
    fun updateDateDialogState(dateDialog: Boolean) {
        _incomeTempState.update {
            it.copy(showDateDialogUI = dateDialog)
        }

    }

    fun updateSelectedDate(date: Long?) {
        _incomeTempState.update {
            it.copy(selectedDate = date)
        }

    }

    fun updateExpenseAmount(expenseAmount: String) {
        _incomeTempState.update {
            it.copy(expIncAmount = expenseAmount)
        }
        Log.d("TAG", "updateExpenseAmount: $expenseAmount")
        if(expenseAmount.isNotEmpty() && expenseAmount.isNotBlank()) _incomeTempState.update { it.copy(validExpIncAmount = true) }
        else  _incomeTempState.update { it.copy(validExpIncAmount = false) }
    }

    fun updateExpenseDes(description: String) {
        _incomeTempState.update {
            it.copy(expIncDescription = description)
        }

    }
    fun saveIntoIncome() {
        Log.d("DEBUG", "Current state: ${_incomeTempState.value}")

         val income = Income(
             incId = 0,
             incDate = _incomeTempState.value.selectedDate,
             incTime = 0L,
             incAmount = _incomeTempState.value.expIncAmount.toFloat(),
             incDescription = _incomeTempState.value.expIncDescription,
             incCategory = selectedIncCategory.value,
             incWallet = selectedIncWallet.value
         )
         viewModelScope.launch {
             incomeRepository.addIncome(income)
             //fetch current wallet amount
             val currentWalletAmount = walletRepository.getWalletAmountById(selectedIncWallet.value)
             //update the wallet
             if(!_incomeTempState.value.expIncAmount.isNullOrEmpty()) {
                 val updateWalletAmount = currentWalletAmount +
                         _incomeTempState.value.expIncAmount.toFloat()

                 walletRepository.updateWalletBalance(updateWalletAmount, selectedIncWallet.value)

                 //now reset the value foe UI
                 resetIncomeUiState()
             }
         }
    }
    fun resetIncomeUiState(){
        _incomeTempState.update {
            it.copy(
                showDateDialogUI = false,
                expIncDescription = "",
                expIncAmount= "",
                validExpIncAmount= false
            )
        }
    }
}