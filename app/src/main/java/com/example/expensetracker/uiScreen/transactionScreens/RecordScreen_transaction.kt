package com.example.expensetracker.uiScreen.transactionScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.viewModel.FinanceViewModel
import java.util.Locale

@Composable
fun View_single_transaction(financeViewModel: FinanceViewModel,onback:()->Unit) {
    BackHandler {
        financeViewModel.resetUserSelectedTransaction()
        onback()
    }

    val transactionData = financeViewModel.transactionSelectedByUser.collectAsState()
    transactionData.value?.let {

        Column(modifier = Modifier.fillMaxWidth().background(color = AppColors.inverseOnSurface).padding(top = 20.dp).wrapContentHeight()
            .background(color = AppColors.surface).padding(vertical = 20.dp, horizontal = 30.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(it.categoryIcon),
                    contentDescription = stringResource(it.categoryName),
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                        .background(color = it.categoryColor).padding(3.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
                Text(text = if(it.transactionDescription=="") stringResource(it.categoryName) else it.transactionDescription?:"",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(start = 5.dp))
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.width(100.dp)) {
                    Text(text = stringResource(R.string.category),
                        color = MaterialTheme.colorScheme.outline)
                }
                Text(text = stringResource(it.categoryName))
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.width(100.dp)) {
                    Text(text = stringResource(R.string.amount),
                        color = MaterialTheme.colorScheme.outline)
                }
                Text(text = it.transactionAmount.toString())
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.width(100.dp)) {
                    Text(text = stringResource(R.string.date),
                        color = MaterialTheme.colorScheme.outline)
                }
                Text(text = formatOnlyDate(it.transactionDate?:0L))
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.width(100.dp)) {
                    Text(text = stringResource(R.string.wallet),
                        color = MaterialTheme.colorScheme.outline)
                }
                Text(text = it.fromWalletName)
                if(it.toWalletName!=null){
                    Text(text = "-> ${it.toWalletName}")
                }
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.width(100.dp)) {
                    Text(text = stringResource(R.string.type),
                        color = MaterialTheme.colorScheme.outline)
                }
                Text(text = it.transactionType.name)
            }


        }

    }

}


fun formatOnlyDate(date:Long):String{
    if(date!=0L) {
        val formatDate = android.icu.text.SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return formatDate.format(date)

    }
    else return ""

}