package com.example.expensetracker.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    private fun getSelectedMonthRange(currentMonthYear: Calendar):Map<Long,Long> {
        val selectedCalendar = currentMonthYear.clone() as Calendar

        val firstDay = selectedCalendar.clone() as Calendar
        firstDay.set(Calendar.DAY_OF_MONTH, 1)
        firstDayOfMonth = firstDay.timeInMillis

        val lastDay = selectedCalendar.clone() as Calendar
        lastDay.add(Calendar.MONTH, 1)
        lastDay.set(Calendar.DAY_OF_MONTH, 1)
        lastDayOfMonth = lastDay.timeInMillis
        return mapOf(firstDayOfMonth to lastDayOfMonth)
    }


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

    val _showTransaction =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth= mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showExpenseTransaction(firstDayOfMonth, lastDayOfMonth)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
            )

    val transactionGroupByDate: StateFlow<List<transactionByDate>> =
        _showTransaction.map { transformByDate(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private fun transformByDate(transactions: List<transactionDetail>): List<transactionByDate> {
        val listOfTransaction = mutableListOf<transactionByDate>()
        val transaction = transactions.groupBy {
            getNormalizedDate(it.transaction_date)
        }
        transaction.forEach { date, transactionDetails ->
            listOfTransaction.add(formatDate(date, transactionDetails))
        }
        return listOfTransaction
    }

    private fun getNormalizedDate(timestamp: Long?): Long {
        if (timestamp == null) return 0L
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    private fun formatDate(
        date: Long?,
        transactionDetails: List<transactionDetail>
    ): transactionByDate {
        val formatDate = android.icu.text.SimpleDateFormat("dd", Locale.getDefault())
        val formatDay = android.icu.text.SimpleDateFormat("eeee", Locale.getDefault())
        val formatMonth = android.icu.text.SimpleDateFormat("MMM ", Locale.getDefault())
        val formatYear = android.icu.text.SimpleDateFormat("YYYY ", Locale.getDefault())
        return transactionByDate(
            transaction_date = formatDate.format(date),
            transaction_day = formatDay.format(date),
            transaction_month = formatMonth.format(date),
            transaction_year = formatYear.format(date),
            transaction_total_amount = totalForDay(transactionDetails),
            transaction_list = formatTransaction(transactionDetails)
        )
    }


    private fun formatTransaction(transactionDetails: List<transactionDetail>): List<transactionByList> {
        val transaction = mutableListOf<transactionByList>()
        transactionDetails.forEach {
            val transactions = transactionByList(
                transaction_amount =
                if (it.transaction_type == TransactionType.Expense)
                    "-${it.transaction_amount}"
                else
                    it.transaction_amount.toString(),
                transaction_description = it.transaction_description,
                transaction_color =
                if (it.transaction_type == TransactionType.Expense)
                    Color.Red.copy(alpha = 0.5F)
                else
                    Color.Blue.copy(alpha = 0.5F),
                walletName = it.walletName,
                categoryName = it.categoryName,
                categoryIcon = it.categoryIcon,
                categoryColor = it.categoryColor
            )
            transaction.add(transactions)
        }
        return transaction
    }

    private fun totalForDay(transactionDetails: List<transactionDetail>): Float {
        var dayTotal = 0F
        transactionDetails.forEach {
            dayTotal += if (it.transaction_type == TransactionType.Expense)
                -it.transaction_amount
            else
                it.transaction_amount
        }
        return dayTotal
    }

    fun previousMonthYear(currentMonthYear: Calendar) {
        val updatedCalendar = currentMonthYear.clone() as Calendar
        updatedCalendar.add(Calendar.MONTH, -1)
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