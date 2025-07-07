package com.example.expensetracker.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import com.example.expensetracker.uiScreen.uiState.OverViewDisplayState
import com.example.expensetracker.uiScreen.uiState.TransactionByCategory
import com.example.expensetracker.uiScreen.uiState.TransactionByDateState
import com.example.expensetracker.uiScreen.uiState.TransactionListState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.expensetracker.utils.StaticData.listOfCategory.categoryList
import com.example.transactionensetracker.entity.TransactionType
import java.math.BigDecimal
import java.math.RoundingMode
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

        val annotedString =
            if (it.transactionType == TransactionType.TRANSFER) {
                buildAnnotatedString {
                    append(it.fromWalletName)
                    append("->")
                    append(it.toWalletName)
                }
            } else {
                buildAnnotatedString {
                    append(it.fromWalletName)
                }
            }

        val transactions = TransactionListState(
            transactionId = it.transactionId,
            transactionAmount =
            if (it.transactionType == TransactionType.Expense)
                "-${it.transactionAmount}"
            else
                it.transactionAmount.toString(),
            transactionDescription = it.transactionDescription,
            transactionColor =
            if (it.transactionType == TransactionType.Expense)
                Color.Red.copy(alpha = 0.5F)
            else if (it.transactionType == TransactionType.Income)
                Color.Blue.copy(alpha = 0.5F)
            else
                Color.Black,
            walletName = annotedString.text,
            categoryName = if (it.transactionType == TransactionType.TRANSFER) categoryList()[0].categoryName else it.categoryName,
            categoryIcon = if (it.transactionType == TransactionType.TRANSFER) categoryList()[0].categoryIcon else it.categoryIcon,
            categoryColor = if (it.transactionType == TransactionType.TRANSFER) categoryList()[0].categoryColor else it.categoryColor
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
        else if (it.transactionType == TransactionType.Income)
            +it.transactionAmount
        else
            0.0F
    }
    return dayTotal
}


fun dataForCharts(
    transaction: List<TransactionDetailState>,
    overViewState: OverViewDisplayState
): List<TransactionByCategory> {

    val listOfData = mutableListOf<TransactionByCategory>()
    val listOfCategory = mutableListOf<Int>()

    val transactions = transaction.groupBy {
        it.categoryId
    }

    transactions.forEach {
        if (it.value.first().transactionType == TransactionType.Expense) {
            println("the data ${it.key}  and ${it.value}")
            listOfCategory.add(it.key)
            listOfData.add(
                TransactionByCategory(
                    categoryId = it.key,
                    categoryName = it.value.first().categoryName,
                    categoryIcon = it.value.first().categoryIcon,
                    categoryColor = it.value.first().categoryColor,
                    categoryExpPercentage = findExpensePercentage(
                        it.value,
                        overViewState.totalExpense
                    )
                )
            )
        }
    }
    return listOfData
}

fun findExpensePercentage(value: List<TransactionDetailState>, totalExpense: Float): Float {
    var totalAmount = 0.0F
    value.forEach {
        totalAmount += it.transactionAmount
    }
    return roundFloatToTwoDecimals(((totalAmount / totalExpense) * 100))
}

fun roundFloatToTwoDecimals(value: Float): Float {
    return BigDecimal(value.toString())
        .setScale(1, RoundingMode.HALF_UP)
        .toFloat()
}

fun getTransactionCount(
    data: TransactionDetailState
): Pair<Int, Int> {
    var expenseCount = 0
    var incomeCount = 0
    if (data.transactionType == TransactionType.Expense) {
        expenseCount++
    }
    if (data.transactionType == TransactionType.Income) {
        incomeCount++
    }
    return Pair(expenseCount, incomeCount)
}

