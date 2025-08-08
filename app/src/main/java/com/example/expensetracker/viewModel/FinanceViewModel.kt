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
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.expensetracker.uiScreen.uiState.selectedTransaction
import com.example.expensetracker.utils.StaticData.listOfCategory.defaultCategories
import com.example.expensetracker.utils.StatisticCategory
import com.example.expensetracker.utils.selectedCategory
import com.example.expensetracker.utils.selectedStatistics
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
    private val categoryRepository: CategoryRepository,
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

    var endDate: Long
    var startDate : Long

    init {
        val mapValue = getSelectedMonthRange(_currentMonthYear.value.currentMonthYear)
        startDate  = mapValue.keys.first()
        endDate = mapValue.entries.first().value
    }

    private fun getSelectedMonthRange(currentMonthYear: Calendar): Map<Long, Long> {
        val selectedCalendar = currentMonthYear.clone() as Calendar

        val firstDay = selectedCalendar.clone() as Calendar
        firstDay.set(Calendar.DAY_OF_MONTH, 1)
        firstDay.set(Calendar.HOUR_OF_DAY, 0)
        firstDay.set(Calendar.MINUTE, 0)
        firstDay.set(Calendar.SECOND, 0)
        firstDay.set(Calendar.MILLISECOND, 0)
        startDate  = firstDay.timeInMillis

        val lastDay = selectedCalendar.clone() as Calendar
        lastDay.set(Calendar.DAY_OF_MONTH, 1)
        lastDay.add(Calendar.MONTH, 1)
        lastDay.set(Calendar.HOUR_OF_DAY, 0)
        lastDay.set(Calendar.MINUTE, 0)
        lastDay.set(Calendar.SECOND, 0)
        lastDay.set(Calendar.MILLISECOND, 0)
        endDate = lastDay.timeInMillis

        return mapOf(startDate  to endDate)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val showExpenseTotal =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            startDate  = mapValue.keys.first()
            endDate = mapValue.entries.first().value
            transactionRepository.getTransactionSummaryByType(
                TransactionType.Expense,
                startDate ,
                endDate
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val showIncomeTotal =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            startDate  = mapValue.keys.first()
            endDate = mapValue.entries.first().value
            transactionRepository.getTransactionSummaryByType(
                TransactionType.Income,
                startDate ,
                endDate
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val _showTransaction =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            startDate  = mapValue.keys.first()
            endDate = mapValue.entries.first().value
            transactionRepository.getTransactionsByDateRange(startDate , endDate)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.cachedIn(viewModelScope)

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
            if(_selectedTransactionId.value.selectedTransactionId!=0) ExpenseEdit(expense)
            else ExpenseSave(expense)
        }
    }

    private suspend fun ExpenseSave(expense: Transaction){
        transactionRepository.insertTransaction(expense)
        //fetch current wallet amount
        val currentWalletAmount =
            walletRepository.getWalletAmount(currentFromWallet.value)
        //update the wallet
        if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
            val updateWalletAmount = currentWalletAmount -
                    _financeTempState.value.financeAmount.toFloat()

            walletRepository.updateWalletAmount(updateWalletAmount, currentFromWallet.value)
            if (expense.transactionType == TransactionType.Expense) {
                resetUiState()
                resetUserSelectedTransaction()
            }
        }
    }

    private fun ExpenseEdit(expense:Transaction){
        if(expense.transactionType== TransactionType.Expense) //for transfer no need to delete here
            deleteTransaction(transactionSelectedByUser.value,expense)
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
        if(_selectedTransactionId.value.selectedTransactionId!=0) IncomeEdit(income)
        else IncomeSave(income)
    }

    private fun IncomeSave(income: Transaction){
        viewModelScope.launch {
            // save into transaction table only if it is income
            if(income.transactionType == TransactionType.Income)
                transactionRepository.insertTransaction(income)

            //fetch current wallet amount
            val currentWalletAmount = if(income.transactionType == TransactionType.TRANSFER){
                walletRepository.getWalletAmount(currentToWallet.value)
            }
            else{
                walletRepository.getWalletAmount(currentFromWallet.value)
            }
            //update the wallet
            if (!_financeTempState.value.financeAmount.isNullOrEmpty()) {
                val updateWalletAmount = currentWalletAmount +
                        _financeTempState.value.financeAmount.toFloat()
                if(income.transactionType == TransactionType.Income)
                    walletRepository.updateWalletAmount(updateWalletAmount, currentFromWallet.value)
                else
                    walletRepository.updateWalletAmount(updateWalletAmount, currentToWallet.value)
                resetUiState()
                resetUserSelectedTransaction()
            }
        }
    }
    private fun IncomeEdit(income: Transaction){
        deleteTransaction(transactionSelectedByUser.value,income)
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
    }

    fun resetUserSelectedTransaction(){
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
            transactionCategory = defaultCategories()[1].categoryId,
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

    fun deleteTransaction(transaction: TransactionDetailState?,expense: Transaction?) {
        var updateWalletAmount:Float
        viewModelScope.launch {
            if(transaction!=null) {
                    val currentWalletAmount = walletRepository.getWalletAmount(transaction.fromWalletId)
                    if(transaction.transactionType==TransactionType.Expense) {
                        updateWalletAmount = currentWalletAmount + transaction.transactionAmount
                    }
                    else if(transaction.transactionType == TransactionType.Income) {
                        updateWalletAmount =currentWalletAmount - transaction.transactionAmount
                    }
                    else {
                        updateWalletAmount = currentWalletAmount + transaction.transactionAmount
                        val currentToWalletAmount = walletRepository.getWalletAmount(transaction.toWalletId)
                        //update to wallet
                        walletRepository.updateWalletAmount(currentToWalletAmount-transaction.transactionAmount,transaction.toWalletId)
                    }
                walletRepository.updateWalletAmount(updateWalletAmount,transaction.fromWalletId)
                transactionRepository.deleteTransaction(transaction.transactionId)

                //in case of edit with existing id update the transaction table
                if(expense!=null) {
                    if(expense.transactionType == TransactionType.Expense)
                        ExpenseSave(expense)
                    else if(expense.transactionType == TransactionType.Income)
                        IncomeSave(expense)
                    else {
                        ExpenseSave(expense)
                        IncomeSave(expense)
                    }
                }
            }
        }
    }

    //statistic screen - user selected the top bar
    private var selectedStatistics = MutableStateFlow(selectedStatistics())
    val _selectedStatistics = selectedStatistics.asStateFlow()

    fun updateStatistics(selected: StatisticCategory) {
        selectedStatistics.update {
            it.copy(selectedStatisticBar = selected)
        }
    }

    private var userSelectedCategory = MutableStateFlow(selectedCategory())
    val _userSelectedCategory = userSelectedCategory.asStateFlow()
    fun updateSelectedCategory(id: Int) {
        userSelectedCategory.update {
            it.copy(
                selectedCategoryId =  id
            )
        }
        viewModelScope.launch {
            val selectedCategoryName = userSelectedCategory.map {
                it.selectedCategoryId
            }.distinctUntilChanged().flatMapLatest {
                categoryRepository.getCategoryNameById(it)
            }.stateIn(viewModelScope)
            userSelectedCategory.update {
                it.copy(
                    selectetedCategoryName = selectedCategoryName.value
                )
            }
        }

    }

    fun resetStatisticsSelection(){
        selectedStatistics.update {
            it.copy(
                selectedStatisticBar = StatisticCategory.EXPENSE
            )
        }
    }

    val _showTransactionByCategory = combine(
        _currentMonthYear,
        userSelectedCategory.asStateFlow()
    ) { currentMonthYearState, selectedCategoryState ->
        val monthRange = getSelectedMonthRange(currentMonthYearState.currentMonthYear)
        val selectedCatId = selectedCategoryState.selectedCategoryId
        val startDate  = monthRange.keys.first()
        val endDate = monthRange.values.first()

        Triple(startDate , endDate, selectedCatId)
    }.distinctUntilChanged()
        .flatMapLatest { (firstDay, lastDay, selectedCatId) ->
            transactionRepository.getTransactionsByCategory(firstDay, lastDay, selectedCatId)
        }.cachedIn(viewModelScope)


    //wallet - statistic screen


    @OptIn(ExperimentalCoroutinesApi::class)
    private val showExpenseTotalByWallet =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            startDate  = mapValue.keys.first()
            endDate = mapValue.entries.first().value
            val selectedWallet = walletRepository.walletSelected.value
            transactionRepository.getTransactionSummaryByWallet(
                TransactionType.Expense,
                startDate ,
                endDate,
                selectedWallet
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val showIncomeTotalByWallet =
        _currentMonthYear.map {
            it.currentMonthYear
            val mapValue = getSelectedMonthRange(it.currentMonthYear)
            startDate  = mapValue.keys.first()
            endDate = mapValue.entries.first().value
            val selectedWallet = walletRepository.walletSelected.value
            transactionRepository.getTransactionSummaryByWallet(
                TransactionType.Income,
                startDate ,
                endDate,
                selectedWallet
            )
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    val showOverViewByWallet = combine(showExpenseTotalByWallet, showIncomeTotalByWallet) { expense, income ->
        OverViewDisplayState(expense, income, (income - expense), isLoading = false)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        OverViewDisplayState(isLoading = true)
    )

    val _showTransactionByWallet = combine(_currentMonthYear, walletRepository.walletSelected)
    { monthYearData, selectedWallet ->
            val mapValue = getSelectedMonthRange(monthYearData.currentMonthYear)
            startDate = mapValue.keys.first()
            endDate = mapValue.entries.first().value

            transactionRepository.getWalletTransactionsByDateRange(
                startDate, endDate, selectedWallet
            )

    }.distinctUntilChanged()
        .flatMapLatest { it }
        .cachedIn(viewModelScope)


}