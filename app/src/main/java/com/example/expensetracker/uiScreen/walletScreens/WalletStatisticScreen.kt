package com.example.expensetracker.uiScreen.walletScreens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.statisticsScreens.StatiscticScreen
import com.example.expensetracker.uiScreen.statisticsScreens.showCharts
import com.example.expensetracker.uiScreen.statisticsScreens.showDetailData
import com.example.expensetracker.uiScreen.transactionScreens.NoTransactionScreen
import com.example.expensetracker.uiScreen.transactionScreens.TransactionScreen
import com.example.expensetracker.utils.StatisticCategory
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.dataForCharts
import com.example.expensetracker.utils.transformByDate
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.transactionensetracker.entity.TransactionType

@Composable
fun StatisticScreenWalletRoute(financeViewModel: FinanceViewModel,showTransactionScreen: () -> Unit,showStructureScreen :()->Unit,onBack:()->Unit){
    val scrollableState = rememberScrollState()
    val overViewState by financeViewModel.showOverViewByWallet.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = financeViewModel._showTransactionByWallet.collectAsLazyPagingItems().itemSnapshotList.items
    val chartsData = remember(transaction){
        dataForCharts(transaction,overViewState,TransactionType.Expense)
    }
    BackHandler {
        financeViewModel.resetStatisticsSelection()
        onBack()
    }

    if(!overViewState.isLoading){
        if(overViewState.total!=0F){
            StatiscticScreen(
                modifier = modifier,
                scrollableState = scrollableState,
                overviewUiState = overViewState,
                chartsData = chartsData.take(5),
                showTransactionScreen = showTransactionScreen,
                showStructureScreen = showStructureScreen
            )
        }
        else{
            NoTransactionScreen(modifier)
        }
    }
}

@Composable
fun statisticsTransactionScreenWalletRoute(financeViewModel: FinanceViewModel,showRecord: () -> Unit)
{
    val scrollableState = rememberScrollState()
    val overViewState by financeViewModel.showOverViewByWallet.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = financeViewModel._showTransactionByWallet.collectAsLazyPagingItems().itemSnapshotList.items
    val transactionGroupByDate = remember(transaction) {
        transformByDate(transaction)
    }
    if(!overViewState.isLoading){
        if(/*overViewState.total!=0F && */transactionGroupByDate.isNotEmpty()){
            TransactionScreen(
                modifier = modifier,
                scrollableState = scrollableState,
                overViewState = overViewState,
                showTransaction = transactionGroupByDate,
                showRecord = {
                    financeViewModel.userSelectedTransaction(it)
                    showRecord()
                }
            )
        }
        else{
            NoTransactionScreen(modifier)
        }
    }

}


@Composable
fun StructureScreenWalletRoute(financeViewModel: FinanceViewModel,showSelectedCategory:(String)->Unit,navigateback:()->Unit) {
    var validChart by remember { mutableStateOf(false)}
    val selectedStatistics by financeViewModel._selectedStatistics.collectAsState()

    val scrollableState = rememberScrollState()
    val overViewState by financeViewModel.showOverViewByWallet.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = financeViewModel._showTransactionByWallet.collectAsLazyPagingItems().itemSnapshotList.items

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
