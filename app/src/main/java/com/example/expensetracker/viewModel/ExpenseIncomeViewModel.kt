package com.example.expensetracker.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.DisplayUIState.overViewDisplayState
import com.example.expensetracker.utils.DisplayUIState.transactionByDate
import com.example.expensetracker.utils.DisplayUIState.transactionByList
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
import com.example.expensetracker.utils.InputUIState.SelectedMonthAndYear
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpenseIncomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
) : ViewModel() {

    private val _expenseTempState = MutableStateFlow(ExpenseIncomeInputState())
    val tempExpeIncState = _expenseTempState.asStateFlow()

    val currentCategory = categoryRepository.selectedCategory
    val currentWallet = walletRepository.selectedWallet

    private val _currentMonthYear = MutableStateFlow(SelectedMonthAndYear())
    val currentMonthYear = _currentMonthYear.asStateFlow()

    var lastDayOfMonth:Long
    var firstDayOfMonth:Long
    init {
        val mapValue = getSelectedMonthRange(_currentMonthYear.value.currentMonthYear)
        firstDayOfMonth= mapValue.keys.first()
        lastDayOfMonth = mapValue.entries.first().value
    }

    private fun getSelectedMonthRange(currentMonthYear: Calendar): Map<Long, Long> {
        val selectedCalendar = currentMonthYear.clone() as Calendar

        val firstDay = selectedCalendar.clone() as Calendar
        firstDay.set(Calendar.DAY_OF_MONTH, 1)
        firstDay.set(Calendar.HOUR_OF_DAY, 0)
        firstDay.set(Calendar.MINUTE, 0)
        firstDay.set(Calendar.SECOND, 0)
        firstDay.set(Calendar.MILLISECOND, 0)
        firstDayOfMonth = firstDay.timeInMillis

        val lastDay = selectedCalendar.clone() as Calendar
        lastDay.set(Calendar.DAY_OF_MONTH, 1)
        lastDay.add(Calendar.MONTH, 1)
        lastDay.set(Calendar.HOUR_OF_DAY, 0)
        lastDay.set(Calendar.MINUTE, 0)
        lastDay.set(Calendar.SECOND, 0)
        lastDay.set(Calendar.MILLISECOND, 0)
        lastDayOfMonth = lastDay.timeInMillis

        return mapOf(firstDayOfMonth to lastDayOfMonth)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val showExpenseTotal =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth= mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showTotalExpense(TransactionType.Expense,firstDayOfMonth, lastDayOfMonth)
        }.distinctUntilChanged().flatMapLatest {
            it
        }
    @OptIn(ExperimentalCoroutinesApi::class)
    private val showIncomeTotal =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth= mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showTotalIncome(TransactionType.Income,firstDayOfMonth, lastDayOfMonth)
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val _showTransaction =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth= mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showExpenseTransaction(firstDayOfMonth, lastDayOfMonth)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.cachedIn(viewModelScope)


    fun previousMonthYear(currentMonthYear: Calendar) {
        val updatedCalendar = currentMonthYear.clone() as Calendar
        updatedCalendar.add(Calendar.MONTH, -1)
        Log.d("the current Month year changed now ",updatedCalendar.toString())
        _currentMonthYear.update {
            it.copy(currentMonthYear = updatedCalendar)
        }
    }

    fun nextMonthYear(currentMonthYear: Calendar) {
        val updatedCalendar = currentMonthYear.clone() as Calendar
        updatedCalendar.add(Calendar.MONTH, +1)

        _currentMonthYear.update {
            it.copy(currentMonthYear = updatedCalendar)
        }
    }

    val showOverView = combine(showExpenseTotal, showIncomeTotal) { expense, income ->
        overViewDisplayState(expense, income, (income - expense), isLoading = false)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        overViewDisplayState(isLoading = true)
    )

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
        if (expenseAmount.isNotEmpty() && expenseAmount.isNotBlank()) _expenseTempState.update {
            it.copy(
                validExpIncAmount = true
            )
        }
        else _expenseTempState.update { it.copy(validExpIncAmount = false) }
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
            if (!_expenseTempState.value.expIncAmount.isNullOrEmpty()) {
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
            if (!_expenseTempState.value.expIncAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount +
                        _expenseTempState.value.expIncAmount.toFloat()

                walletRepository.updateWalletBalance(updateWalletAmount, currentWallet.value)

                //now reset the value foe UI
                resetUiState()
            }
        }
    }

    fun resetUiState() {
        _expenseTempState.update {
            it.copy(
                showDateDialogUI = false,
                expIncDescription = "",
                expIncAmount = "",
                validExpIncAmount = false
            )
        }
    }


    fun fetchTotalExpenseCountById(walletId:Int){

    }
}