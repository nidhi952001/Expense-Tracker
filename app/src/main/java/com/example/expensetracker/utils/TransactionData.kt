package com.example.expensetracker.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.utils.DisplayUIState.transactionByDate
import com.example.expensetracker.utils.DisplayUIState.transactionByList
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.Locale

fun transformByDate(transactions: List<transactionDetail>): List<transactionByDate> {
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

