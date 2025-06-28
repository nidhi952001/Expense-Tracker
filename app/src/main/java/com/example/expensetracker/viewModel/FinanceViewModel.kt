package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.uiScreen.uiState.OverViewDisplayState
import com.example.expensetracker.uiScreen.uiState.SelectedMonthAndYear
import com.example.expensetracker.uiScreen.uiState.selectedTransaction
import com.example.expensetracker.utils.StaticData.listOfCategory.categoryList
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
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
) : ViewModel() {

    private val _financeTempState = transactionRepository._tempFinanceState
    val tempFinanceState = _financeTempState.asStateFlow()

    val currentCategory = categoryRepository.selectedCategory
    val currentFromWallet = walletRepository.selectedFromWalletId
    val currentToWallet = walletRepository.selectedToWalletId

    private val _currentMonthYear = MutableStateFlow(SelectedMonthAndYear())
    val currentMonthYear = _currentMonthYear.asStateFlow()


    private val _selectedTransactionId = MutableStateFlow(selectedTransaction())

    var lastDayOfMonth: Long
    var firstDayOfMonth: Long

    init {
        val mapValue = getSelectedMonthRange(_currentMonthYear.value.currentMonthYear)
        firstDayOfMonth = mapValue.keys.first()
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
            firstDayOfMonth = mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showTotalExpense(
                TransactionType.Expense,
                firstDayOfMonth,
                lastDayOfMonth
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val showIncomeTotal =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth = mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showTotalIncome(
                TransactionType.Income,
                firstDayOfMonth,
                lastDayOfMonth
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val _showTransaction =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            firstDayOfMonth = mapValue.keys.first()
            lastDayOfMonth = mapValue.entries.first().value
            transactionRepository.showTransaction(firstDayOfMonth, lastDayOfMonth)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.cachedIn(viewModelScope)

    fun previousMonthYear(currentMonthYear: Calendar) {
        val updatedCalendar = currentMonthYear.clone() as Calendar
        updatedCalendar.add(Calendar.MONTH, -1)
        Log.d("the current Month year changed now ", updatedCalendar.toString())
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
        OverViewDisplayState(expense, income, (income - expense), isLoading = false)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        OverViewDisplayState(isLoading = true)
    )

    fun updateDateDialogState(dateDialog: Boolean) {
        _financeTempState.update {
            it.copy(showDateDialogUI = dateDialog)
        }

    }

    fun updateSelectedDate(date: Long?) {
        _financeTempState.update {
            it.copy(selectedDate = date)
        }

    }

    fun updateFinanceAmount(amount: String) {
        _financeTempState.update {
            it.copy(financeAmount = amount)
        }
        Log.d("TAG", "updateExpenseAmount: $amount")
        if (amount.isNotEmpty() && amount.isNotBlank()) _financeTempState.update {
            it.copy(
                isFinanceAmountValid = true
            )
        }
        else _financeTempState.update { it.copy(isFinanceAmountValid = false) }
    }

    fun updateFinanceDes(description: String) {
        _financeTempState.update {
            it.copy(financeDescription = description)
        }

    }


    fun dataOfExpense() {
        val expense = Transaction(
            transactionId = if(_selectedTransactionId.value.selectedTransactionId!=0)
                _selectedTransactionId.value.selectedTransactionId
            else 0,
            transactionTime = 0L,
            transactionDate = _financeTempState.value.selectedDate,
            transactionAmount = _financeTempState.value.financeAmount.toFloat(),
            transactionDescription = _financeTempState.value.financeDescription,
            transactionType = TransactionType.Expense,
            transactionCategory = currentCategory.value,
            transactionFromWallet = currentFromWallet.value,
            transactionToWallet = null
        )
        saveExpense(expense)
    }

    private fun saveExpense(expense: Transaction) {
        viewModelScope.launch {
            transactionRepository.addExpense(expense)
            if(_selectedTransactionId.value.selectedTransactionId!=0 && expense.transactionType == TransactionType.Expense) ExpenseEdit(expense)
            else ExpenseSave(expense)
        }
    }

    private suspend fun ExpenseSave(expense: Transaction){
        //fetch current wallet amount
        val currentWalletAmount =
            walletRepository.fetchWalletAmountById(currentFromWallet.value)
        //update the wallet
        if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
            val updateWalletAmount = currentWalletAmount -
                    _financeTempState.value.financeAmount.toFloat()

            walletRepository.updateWalletAmount(updateWalletAmount, currentFromWallet.value)
            if (expense.transactionType == TransactionType.Expense)
                resetUiState()
        }
    }
    private suspend fun ExpenseEdit(expense: Transaction) {
        var updateWalletId:Int=currentFromWallet.value
        var updateWalletAmount:Float =0.0F

        //if transaction values are not different
        if (transactionSelectedByUser.value?.transactionAmount != _financeTempState.value.financeAmount.toFloat() &&
            transactionSelectedByUser.value?.fromWalletId == currentFromWallet.value)
        {
            println("coming first condition")
            //fetch current wallet amount
            val currentWalletAmount =
                walletRepository.fetchWalletAmountById(currentFromWallet.value)
            //update the wallet amount
            if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
                updateWalletAmount = if (transactionSelectedByUser.value != null) {
                    val difference = transactionSelectedByUser.value!!.transactionAmount -
                            _financeTempState.value.financeAmount.toFloat()
                    if (difference > 0) {
                        currentWalletAmount + difference
                    } else {
                        currentWalletAmount + difference
                    }
                } else {
                    currentWalletAmount -
                            _financeTempState.value.financeAmount.toFloat()
                }
            }
            walletRepository.updateWalletAmount(updateWalletAmount, updateWalletId)

        } //if wallet are different
        if(transactionSelectedByUser.value?.fromWalletId != currentFromWallet.value){
            println("coming first condition")

            //user previously selected wallet amount
            val userSelectedWalletAmount =
                transactionSelectedByUser.value?.fromWalletId?.let {
                    walletRepository.fetchWalletAmountById(
                        it
                    )
                }
            if (userSelectedWalletAmount != null) {
                updateWalletAmount = userSelectedWalletAmount + (transactionSelectedByUser.value?.transactionAmount
                    ?: 0.0F)
                updateWalletId = transactionSelectedByUser.value!!.fromWalletId
                walletRepository.updateWalletAmount(updateWalletAmount, updateWalletId)
            }

            //user currently selected wallet amount
            val currentWalletAmount = walletRepository.fetchWalletAmountById(currentFromWallet.value)
            val updateCurrentWalletAmount = currentWalletAmount - _financeTempState.value.financeAmount.toFloat()
            walletRepository.updateWalletAmount(updateCurrentWalletAmount, currentFromWallet.value)
        }
        if (expense.transactionType == TransactionType.Expense)
            resetUiState()
    }


    fun dataOfIncome() {
        Log.d("DEBUG", "Current state: ${_financeTempState.value}")

        val income = Transaction(
            transactionId = if(_selectedTransactionId.value.selectedTransactionId!=0){
                _selectedTransactionId.value.selectedTransactionId
            }
            else 0,
            transactionDate = _financeTempState.value.selectedDate,
            transactionTime = 0L,
            transactionAmount = _financeTempState.value.financeAmount.toFloat(),
            transactionDescription = _financeTempState.value.financeDescription,
            transactionType = TransactionType.Income,
            transactionCategory = currentCategory.value,
            transactionFromWallet = currentFromWallet.value,
            transactionToWallet = null
        )
        saveIncome(income)
    }

    private fun saveIncome(income: Transaction) {
        if(income.transactionType == TransactionType.Income)
            viewModelScope.launch {
                transactionRepository.addIncome(income) // only one transaction should add into table if it is transfer
            }
        if(_selectedTransactionId.value.selectedTransactionId!=0) IncomeEdit(income)
        else IncomeSave(income)
    }

    private fun IncomeSave(income: Transaction){
        viewModelScope.launch {
            //fetch current wallet amount
            val currentWalletAmount = if(income.transactionType == TransactionType.TRANSFER){
                walletRepository.fetchWalletAmountById(currentToWallet.value)
            }
            else{
                walletRepository.fetchWalletAmountById(currentFromWallet.value)
            }
            //update the wallet
            if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount +
                        _financeTempState.value.financeAmount.toFloat()
                if(income.transactionType == TransactionType.TRANSFER) {
                    walletRepository.updateWalletAmount(updateWalletAmount, currentToWallet.value)
                    resetUiState()
                }
                else{
                    walletRepository.updateWalletAmount(updateWalletAmount, currentFromWallet.value)
                    resetUiState()
                }

            }
        }
    }
    private fun IncomeEdit(income: Transaction){
        var currentFromWalletAmount:Float=0.0F
        var currentToWalletAmount:Float=0.0F

        var updateWalletId:Int=currentFromWallet.value
        var updateWalletAmount:Float =0.0F
        viewModelScope.launch {
            //fetch current wallet amount
             if (income.transactionType == TransactionType.TRANSFER) {
                // only one transaction should add into table if it is transfer
                 currentToWalletAmount = walletRepository.fetchWalletAmountById(currentToWallet.value)
            } else {
                 currentFromWalletAmount = walletRepository.fetchWalletAmountById(currentFromWallet.value)
            }
            //only amount change
            if(transactionSelectedByUser.value?.transactionAmount != _financeTempState.value.financeAmount.toFloat() &&
                transactionSelectedByUser.value?.fromWalletId == currentFromWallet.value &&
                transactionSelectedByUser.value?.toWalletId == currentToWallet.value) {
                //update the wallet
                if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
                    if(transactionSelectedByUser.value!=null){
                        val difference = transactionSelectedByUser.value!!.transactionAmount -
                                _financeTempState.value.financeAmount.toFloat()
                        currentFromWalletAmount -= difference
                        currentToWalletAmount-=difference
                    }
                    else {
                        currentFromWalletAmount +=
                                _financeTempState.value.financeAmount.toFloat()
                    }
                    if (income.transactionType == TransactionType.TRANSFER) {
                        walletRepository.updateWalletAmount(currentToWalletAmount, currentToWallet.value)
                    } else {
                        walletRepository.updateWalletAmount(currentFromWalletAmount, currentFromWallet.value)
                    }

                }
            } //only wallet changes
            if(transactionSelectedByUser.value?.fromWalletId != currentFromWallet.value &&
                transactionSelectedByUser.value?.transactionType == TransactionType.Income){

                //user previously selected wallet amount
                val userSelectedWalletAmount =
                    transactionSelectedByUser.value?.fromWalletId?.let {
                        walletRepository.fetchWalletAmountById(
                            it
                        )
                    }
                if (userSelectedWalletAmount != null) {
                    updateWalletAmount = userSelectedWalletAmount - (transactionSelectedByUser.value?.transactionAmount
                        ?: 0.0F)
                    updateWalletId = transactionSelectedByUser.value!!.fromWalletId
                    walletRepository.updateWalletAmount(updateWalletAmount, updateWalletId)
                }

                //user currently selected wallet amount
                val currentWalletAmount = walletRepository.fetchWalletAmountById(currentFromWallet.value)
                val updateCurrentWalletAmount = currentWalletAmount + _financeTempState.value.financeAmount.toFloat()
                walletRepository.updateWalletAmount(updateCurrentWalletAmount, currentFromWallet.value)
            }
            if(transactionSelectedByUser.value?.transactionType == TransactionType.TRANSFER){
                if(transactionSelectedByUser.value?.fromWalletId != currentFromWallet.value){
                    //user previously selected wallet amount
                    val userSelectedWalletAmount =
                        transactionSelectedByUser.value?.fromWalletId?.let {
                            walletRepository.fetchWalletAmountById(
                                it
                            )
                        }
                    if (userSelectedWalletAmount != null) {
                        updateWalletAmount = userSelectedWalletAmount + (transactionSelectedByUser.value?.transactionAmount
                            ?: 0.0F)
                        updateWalletId = transactionSelectedByUser.value!!.fromWalletId
                        walletRepository.updateWalletAmount(updateWalletAmount, updateWalletId)
                    }

                    //user currently selected wallet amount
                    val currentWalletAmount = walletRepository.fetchWalletAmountById(currentFromWallet.value)
                    val updateCurrentWalletAmount = currentWalletAmount - _financeTempState.value.financeAmount.toFloat()
                    walletRepository.updateWalletAmount(updateCurrentWalletAmount, currentFromWallet.value)
                }
                else {
                    //user previously selected to wallet amount is not equal to currently selected from wallet
                    if(transactionSelectedByUser.value?.toWalletId != currentFromWallet.value) {
                        val userSelectedWalletAmount =
                            transactionSelectedByUser.value?.toWalletId?.let {
                                walletRepository.fetchWalletAmountById(
                                    it
                                )
                            }
                        if (userSelectedWalletAmount != null) {
                            updateWalletAmount =
                                userSelectedWalletAmount - (transactionSelectedByUser.value?.transactionAmount
                                    ?: 0.0F)
                            updateWalletId = transactionSelectedByUser.value!!.toWalletId
                            walletRepository.updateWalletAmount(updateWalletAmount, updateWalletId)
                        }
                    }

                    //user currently selected wallet amount
                    val currentWalletAmount =
                        walletRepository.fetchWalletAmountById(currentToWallet.value)
                    val updateCurrentWalletAmount =
                        currentWalletAmount + _financeTempState.value.financeAmount.toFloat()
                    walletRepository.updateWalletAmount(
                        updateCurrentWalletAmount,
                        currentToWallet.value
                    )
                }
            }
            resetUiState()

        }
    }
    fun resetUiState() {
        _financeTempState.update {
            it.copy(
                showDateDialogUI = false,
                financeDescription = "",
                financeAmount = "",
                isFinanceAmountValid = false,
                isToWalletValid = false,
                isFromWalletValid = true
            )
        }
        _selectedTransactionId.update {
            it.copy(
                selectedTransactionId = 0
            )
        }
    }

    fun saveIntoTransfer() {
        val transfer = Transaction(
            transactionId = if(_selectedTransactionId.value.selectedTransactionId!=0)
                _selectedTransactionId.value.selectedTransactionId
            else 0,
            transactionTime = 0L,
            transactionDate = _financeTempState.value.selectedDate,
            transactionAmount = _financeTempState.value.financeAmount.toFloat(),
            transactionDescription = _financeTempState.value.financeDescription,
            transactionType = TransactionType.TRANSFER,
            transactionCategory = categoryList()[1].categoryId,
            transactionFromWallet = currentFromWallet.value,
            transactionToWallet = currentToWallet.value

        )
        saveExpense(transfer)
        saveIncome(transfer)
    }


    fun userSelectedTransaction(id: Int) {
        _selectedTransactionId.update {
            it.copy(selectedTransactionId = id)
        }
    }

    val transactionSelectedByUser = _selectedTransactionId.map {
        it.selectedTransactionId
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.getTransactionById(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    //when user click for edit transaction
    fun populateFinanceData() {
        if (transactionSelectedByUser.value?.transactionType == TransactionType.Income ||
            transactionSelectedByUser.value?.transactionType == TransactionType.Expense
        ) {
            _financeTempState.update {
                it.copy(
                    showDateDialogUI = false,
                    selectedDate = transactionSelectedByUser.value?.transactionDate,
                    financeDescription = transactionSelectedByUser.value?.transactionDescription?:"",
                    financeAmount = transactionSelectedByUser.value?.transactionAmount.toString(),
                    isFinanceAmountValid = true,
                    isFromWalletValid = true,
                    isToWalletValid = false
                )
            }
        } else {
            _financeTempState.update {
                it.copy(
                    showDateDialogUI = false,
                    selectedDate = transactionSelectedByUser.value?.transactionDate,
                    financeDescription = transactionSelectedByUser.value?.transactionDescription?:"",
                    financeAmount = transactionSelectedByUser.value?.transactionAmount.toString(),
                    isFinanceAmountValid = true,
                    isFromWalletValid = true,
                    isToWalletValid = true
                )
            }
        }
    }
}