package com.example.expensetracker.utils

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.uiScreen.uiState.TransactionByDateState
import com.example.expensetracker.uiScreen.uiState.TransactionListState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.transactionensetracker.entity.TransactionType
import java.util.Calendar
import java.util.Locale

fun transformByDate(transactions: List<TransactionDetailState>): List<TransactionByDateState> {
    val listOfTransaction = mutableListOf<TransactionByDateState>()
    val transaction = transactions.groupBy {
        getNormalizedDate(it.transactionDate)
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
    transactionDetailStates: List<TransactionDetailState>
): TransactionByDateState {
    val formatDate = android.icu.text.SimpleDateFormat("dd", Locale.getDefault())
    val formatDay = android.icu.text.SimpleDateFormat("eeee", Locale.getDefault())
    val formatMonth = android.icu.text.SimpleDateFormat("MMM ", Locale.getDefault())
    val formatYear = android.icu.text.SimpleDateFormat("YYYY ", Locale.getDefault())
    return TransactionByDateState(
        transactionDate = formatDate.format(date),
        transactionDay = formatDay.format(date),
        transactionMonth = formatMonth.format(date),
        transactionYear = formatYear.format(date),
        transactionTotalAmount = totalForDay(transactionDetailStates),
        transactionList = formatTransaction(transactionDetailStates)
    )
}


private fun formatTransaction(transactionDetailStates: List<TransactionDetailState>): List<TransactionListState> {
    val transaction = mutableListOf<TransactionListState>()
    transactionDetailStates.forEach {
        val transactions = TransactionListState(
            transactionAmount =
            if (it.transactionType == TransactionType.Expense)
                "-${it.transactionAmount}"
            else
                it.transactionAmount.toString(),
            transactionDescription = it.transactionDescription,
            transactionColor =
            if (it.transactionType == TransactionType.Expense)
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

private fun totalForDay(transactionDetailStates: List<TransactionDetailState>): Float {
    var dayTotal = 0F
    transactionDetailStates.forEach {
        dayTotal += if (it.transactionType == TransactionType.Expense)
            -it.transactionAmount
        else
            it.transactionAmount
    }
    return dayTotal
}

