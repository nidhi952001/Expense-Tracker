package com.example.expensetracker.uiScreen.statisticsScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.uiState.TransactionByCategory
import com.example.expensetracker.utils.StatisticCategory
import com.example.expensetracker.utils.dataForCharts
import com.example.expensetracker.utils.selectedCategory
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.transactionensetracker.entity.TransactionType

@Composable
fun StructureScreenRoute(financeViewModel: FinanceViewModel,showSelectedCategory:(String)->Unit,navigateback:()->Unit) {
    var validChart by remember { mutableStateOf(false)}
    val selectedStatistics by financeViewModel._selectedStatistics.collectAsState()

    val scrollableState = rememberScrollState()
    val overViewState by financeViewModel.showOverView.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = financeViewModel._showTransaction.collectAsLazyPagingItems().itemSnapshotList.items

    BackHandler {
        financeViewModel.resetStatisticsSelection()
        navigateback()
    }

    val chartsData = remember(selectedStatistics,transaction){
        dataForCharts(transaction,overViewState,
            if(selectedStatistics.selectedStatisticBar == StatisticCategory.INCOME)
                TransactionType.Income
            else
            TransactionType.Expense
        )
    }

    Column(modifier = modifier
        .scrollable(scrollableState, orientation = Orientation.Vertical)
        .padding(top = 20.dp)
        .background(color = AppColors.surface)
        .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val selectedChartType:TransactionType =
            if(selectedStatistics.selectedStatisticBar == StatisticCategory.INCOME) {
                if(overViewState.totalIncome!=0.0F)
                    validChart = true
                else validChart =false
                TransactionType.Income
            }
            else {
                if(overViewState.totalExpense!=0.0F)
                    validChart = true
                else validChart =false
                TransactionType.Expense
            }
        if(validChart) {
            showCharts(chartsData, overViewState, selectedChartType)
            showDetailData(
                chartsData
            ) { id, total ->
                financeViewModel.updateSelectedCategory(id)
                showSelectedCategory(total)
            }
        }
    }

}

@Composable
fun showDetailData(detailData: List<TransactionByCategory>,showSelectedCategory:(Int,String)->Unit) {
    LazyColumn {
        items(detailData){transaction->
            Row(modifier = Modifier.fillMaxWidth().padding(10.dp).clickable(onClick = {showSelectedCategory(transaction.categoryId,transaction.categoryExpTotalAmount)}), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Image(
                        painter = painterResource(transaction.categoryIcon),
                        contentDescription = stringResource(transaction.categoryName),
                        modifier = Modifier.size(35.dp)
                            .clip(CircleShape)
                            .background(color = transaction.categoryColor).padding(3.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(transaction.categoryName))
                    val annotedPercentage = buildAnnotatedString {
                        append(transaction.categoryExpPercentage.toString())
                        append("%")
                    }
                        Text(text = annotedPercentage)
                }
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    val annotedTotal = buildAnnotatedString {
                        append("- ")
                        append(transaction.categoryExpTotalAmount)
                    }
                    Text(text = annotedTotal)
                    val annotedCount = buildAnnotatedString {
                        append(transaction.categoryExpCount)
                        append(" transactions")
                    }
                    Text(text = annotedCount)
                }

            }
        }
    }
}
