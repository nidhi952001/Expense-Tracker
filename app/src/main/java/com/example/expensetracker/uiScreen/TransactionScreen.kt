package com.example.expensetracker.uiScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.walletScreens.formatWalletAmount
import com.example.expensetracker.utils.DisplayUIState.overViewDisplayState
import com.example.expensetracker.utils.DisplayUIState.transactionByDate
import com.example.expensetracker.utils.DisplayUIState.transactionByList
import com.example.expensetracker.utils.transformByDate
import com.example.expensetracker.viewModel.ExpenseIncomeViewModel

@Composable
fun TransactionScreenRoute(expenseIncomeViewModel: ExpenseIncomeViewModel)
{
    val context = LocalContext.current
    val activity = context as Activity
    val scrollableState = rememberScrollState()
    BackHandler(enabled = true){
        activity.finish()
    }
    val overViewState by expenseIncomeViewModel.showOverView.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = expenseIncomeViewModel._showTransaction.collectAsLazyPagingItems().itemSnapshotList.items
    val transactionGroupByDate = remember(transaction) {
        transformByDate(transaction)
    }
    if(!overViewState.isLoading){
        if(overViewState.total!=0F /*&& transactionGroupByDate.isNotEmpty()*/){
            TransactionScreen(
                modifier = modifier,
                scrollableState = scrollableState,
                overViewState = overViewState,
                showTransaction = transactionGroupByDate
            )
        }
        else{
            NoTransactionScreen(modifier)
        }
    }


}

@Composable
fun NoTransactionScreen(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(R.drawable.empty_transaction_ic),
            contentDescription = stringResource(R.string.no_record)
        )
        Text(text = stringResource(R.string.no_record),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 2.dp))

    }
}

@Composable
fun TransactionScreen(
    modifier: Modifier,
    scrollableState: ScrollState,
    overViewState: overViewDisplayState,
    showTransaction: List<transactionByDate>
) {
    Column(
        modifier = modifier.scrollable(scrollableState, orientation = Orientation.Vertical)
    ) {
        TransactionDetail(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            overviewUiState = overViewState,
            showTransaction = showTransaction
        )
    }
}

@Composable
fun TransactionSummary(modifier: Modifier, overviewUiState: overViewDisplayState){
    Column(modifier= modifier.padding(horizontal = 10.dp, vertical = 15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.overview),
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.inverseSurface,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.padding(end = 3.dp)
            )
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.info),
            )
        }
        if(overviewUiState.showAll) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val annotedString = buildAnnotatedString {
                    append(stringResource(R.string.ruppes_icon))
                    append(" ")
                    append(formatWalletAmount(overviewUiState.totalIncome.toString()))
                }
                Text(text = stringResource(R.string.income))
                Text(
                    text = annotedString,
                    color = Color.Blue.copy(alpha = 0.5F)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val annotedString = buildAnnotatedString {
                    append(stringResource(R.string.minus_icon))
                    append(stringResource(R.string.ruppes_icon))
                    append(" ")
                    append(formatWalletAmount(overviewUiState.totalExpense.toString()))
                }
                Text(text = stringResource(R.string.expense))
                Text(
                    text = annotedString,
                    color = Color.Red.copy(alpha = 0.5F)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            val annotedString = buildAnnotatedString {
                append(stringResource( R.string.ruppes_icon))
                append(" ")
                append(formatWalletAmount(overviewUiState.total.toString()))
            }
            val annotedString1 = buildAnnotatedString {
                append(stringResource(R.string.minus_icon))
                append(stringResource( R.string.ruppes_icon))
                append(" ")
                append(formatWalletAmount(overviewUiState.total.toString()))
            }
            Text(text = stringResource(R.string.total))
            Text(text =
                if(overviewUiState.total>0) annotedString
                else annotedString1
            )
        }
    }
}

@Composable
fun TransactionDetail(modifier: Modifier,overviewUiState: overViewDisplayState, showTransaction: List<transactionByDate>){
    LazyColumn(modifier= modifier) {
        item {
            TransactionSummary(modifier = Modifier.fillMaxWidth()
                .background(color = AppColors.surface),
                overviewUiState = overviewUiState)
        }
            items(items = showTransaction) { transaction ->
                TransactionDate(
                    transactionDate = transaction.transaction_date,
                    transactionDay = transaction.transaction_day,
                    transactionMonth = transaction.transaction_month,
                    transactionYear = transaction.transaction_year,
                    totalOfTheDay = transaction.transaction_total_amount)
                transaction.transaction_list.forEach {transactions->
                    AllTransaction(transactions, modifier = Modifier)
                }
            }
    }
}

@Composable
private fun TransactionDate(
    transactionDate: String,
    transactionDay: String,
    transactionMonth: String,
    transactionYear: String,
    totalOfTheDay: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = AppColors.surface
            ).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = transactionDate,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.inverseSurface,
            fontWeight = FontWeight.ExtraBold,
            )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = transactionDay)
            Text(text = "$transactionMonth $transactionYear")
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(text = totalOfTheDay.toString())
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
fun AllTransaction(transaction: transactionByList, modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth().background(
        color = AppColors.surface).padding(10.dp) , verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(transaction.categoryIcon),
                    contentDescription = stringResource(transaction.categoryName),
                    modifier = Modifier.size(35.dp)
                        .clip(CircleShape)
                        .background(color = transaction.categoryColor).padding(3.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text =
                    (if(transaction.transaction_description!="")
                        transaction.transaction_description
                    else
                        stringResource(transaction.categoryName))!!,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = transaction.walletName)
                }
            }
        }
        val annotedString = buildAnnotatedString {
            append(stringResource( R.string.ruppes_icon))
            append(" ")
            append(formatWalletAmount(transaction.transaction_amount))
        }
        Text(
            text = annotedString,
            color = transaction.transaction_color,
            textAlign = TextAlign.End
        )
    }
}


