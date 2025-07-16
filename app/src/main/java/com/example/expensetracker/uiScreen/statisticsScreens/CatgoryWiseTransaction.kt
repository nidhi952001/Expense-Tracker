package com.example.expensetracker.uiScreen.statisticsScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.transactionScreens.AllTransaction
import com.example.expensetracker.uiScreen.transactionScreens.TransactionDate
import com.example.expensetracker.uiScreen.transactionScreens.TransactionSummary
import com.example.expensetracker.uiScreen.uiState.TransactionByDateState
import com.example.expensetracker.utils.getTotalAmount
import com.example.expensetracker.utils.transformByDate
import com.example.expensetracker.viewModel.FinanceViewModel

@Composable
fun showCategoryTransaction(financeViewModel: FinanceViewModel,showRecord: () -> Unit) {

    val transaction = financeViewModel._showTransactionByCategory.collectAsLazyPagingItems().itemSnapshotList.items
    val transactionGroupByDate = remember(transaction) {
        transformByDate(transaction)
    }
    val categoryTotalAmount = getTotalAmount(transaction)
    if(!categoryTotalAmount.equals(0.0F)) {
        Column(
            Modifier.fillMaxSize()
                .background(color = AppColors.inverseOnSurface)
        ) {
            summary(
                modifier = Modifier.padding(top = 15.dp).fillMaxWidth()
                    .background(color = AppColors.surface).padding(vertical = 10.dp, horizontal = 10.dp),
                categoryTotal = categoryTotalAmount.toString()
            )
            detail(transactionGroupByDate, showRecord = {
                financeViewModel.userSelectedTransaction(it)
                showRecord()
            })
        }
    }

}

@Composable
fun detail(showTransaction: List<TransactionByDateState>,showRecord: (Int) -> Unit) {
    LazyColumn(modifier= Modifier) {
        items(items = showTransaction) { transaction ->
            TransactionDate(
                transactionDate = transaction.transactionDate,
                transactionDay = transaction.transactionDay,
                transactionMonth = transaction.transactionMonth,
                transactionYear = transaction.transactionYear,
                totalOfTheDay = transaction.transactionTotalAmount)
            transaction.transactionList.forEach { transactions->
                AllTransaction(transactions, modifier = Modifier, showRecord = {showRecord(it)})
            }
        }
    }
}

@Composable
fun summary(modifier: Modifier, categoryTotal: String?) {
    Column(modifier) {
        Row {
            Text(
                text = stringResource(R.string.overview),
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.inverseSurface,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.all)
            )
            Text(
                text = "100.0%"
            )
            if (categoryTotal != null) {
                val annotedTotal = buildAnnotatedString {
                    append("-")
                    append(categoryTotal)
                }
                Text(text = annotedTotal)
            }
        }
    }
}
