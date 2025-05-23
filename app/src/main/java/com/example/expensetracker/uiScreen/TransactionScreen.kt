package com.example.expensetracker.uiScreen

import android.app.Activity
import android.icu.text.SimpleDateFormat
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.utils.DisplayUIState.overViewDisplayState
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.transactionensetracker.entity.TransactionType
import java.util.Date
import java.util.Locale

@Composable
fun TransactionScreen(
    modifier: Modifier,
    overviewUiState: overViewDisplayState,
    showTransaction: List<transactionDetail>
){
    val context = LocalContext.current
    val activity = context as Activity
    val scrollableState = rememberScrollState()

    BackHandler(enabled = true){
        activity.finish()
    }


    Column(
        modifier = modifier.scrollable(scrollableState , orientation = Orientation.Vertical)
    ) {
        TransactionDetail(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            overviewUiState = overviewUiState,
            showTransaction = showTransaction)
    }

}

@Composable
fun TransactionSummary(modifier: Modifier, overviewUiState: overViewDisplayState){
    Column(modifier= modifier.padding(horizontal = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.overview),
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.inverseSurface,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.info)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.income))
            Text(text = "₹ "+overviewUiState.totalIncome.toString(),
                color = Color.Blue.copy(alpha = 0.5F))
        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.expense))
            Text(text = "-₹ "+overviewUiState.totalExpense.toString(),
                color = Color.Red.copy(alpha = 0.5F))
        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.total))
            Text(text =
                if(overviewUiState.total>0)
                    "₹ "+overviewUiState.total.toString()
                else
                    "-₹ "+overviewUiState.total.toString()
            )
        }
    }
}

@Composable
fun TransactionDetail(modifier: Modifier,overviewUiState: overViewDisplayState, showTransaction: List<transactionDetail>){
    val groupByDate = showTransaction.groupBy { it.transaction_date }
    var uniqueDate = emptySet<String>()
    val format = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
    val formatDate = SimpleDateFormat("dd", Locale.getDefault())
    val formatDay = SimpleDateFormat("eeee", Locale.getDefault())
    val formatMonthYear = SimpleDateFormat("MMM yyyy",Locale.getDefault())

    LazyColumn(modifier= modifier) {
        item {
            TransactionSummary(modifier = Modifier.fillMaxWidth()
                .background(color = AppColors.surface),
                overviewUiState = overviewUiState)
        }
        groupByDate.forEach { date, transactionDetails ->
            items(items = transactionDetails) { transaction ->
                    if (!uniqueDate.contains(format.format(date))) {
                        TransactionDate(
                            date,
                            formatDate,
                            formatDay,
                            formatMonthYear,
                            addUniqueDate = { uniqueDate = setOf(format.format(date)) }
                        )
                    }
                    TransactionByDate(transaction, modifier = Modifier)
            }
        }
    }
}

@Composable
private fun TransactionDate(
    date: Long?,
    formatDate: SimpleDateFormat,
    formatDay: SimpleDateFormat,
    formatMonthYear: SimpleDateFormat,
    addUniqueDate:(Date)->Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = AppColors.surface
            ).padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val date = date?.let { Date(it) }
        Text(text = formatDate.format(date))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = formatDay.format(date))
            Text(text = formatMonthYear.format(date))
        }
        addUniqueDate(date!!)
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
fun TransactionByDate(transaction: transactionDetail,modifier: Modifier) {
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
                        stringResource(transaction.categoryName))!!
                    )
                    Text(text = transaction.walletName)
                }
            }
        }
        Text(
            text =
            if(transaction.transaction_type == TransactionType.Income)
                "₹ "+transaction.transaction_amount.toString()
            else
                "-₹ "+transaction.transaction_amount.toString(),
            color =
            if(transaction.transaction_type == TransactionType.Income)
                Color.Blue.copy(alpha = 0.5F)
            else
                Color.Red.copy(alpha = 0.5F),
            textAlign = TextAlign.End
        )
    }
}


