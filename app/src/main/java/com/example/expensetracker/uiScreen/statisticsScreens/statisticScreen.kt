package com.example.expensetracker.uiScreen.statisticsScreens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.transactionScreens.TransactionSummary
import com.example.expensetracker.uiScreen.uiState.OverViewDisplayState
import com.example.expensetracker.uiScreen.uiState.TransactionByCategory
import com.example.expensetracker.utils.dataForCharts
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.transactionensetracker.entity.TransactionType

@Composable
fun StatisticScreenRoute(financeViewModel: FinanceViewModel,showTransactionScreen: () -> Unit,showStructureScreen :()->Unit){

    val scrollableState = rememberScrollState()
    val overViewState by financeViewModel.showOverView.collectAsState()
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val transaction = financeViewModel._showTransaction.collectAsLazyPagingItems().itemSnapshotList.items
    val chartsData = remember(transaction){
        dataForCharts(transaction,overViewState,TransactionType.Expense)
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
            NoStatisticScreen(modifier)
        }
    }
}

@Composable
fun NoStatisticScreen(modifier: Modifier){

}

@Composable
fun StatiscticScreen(
    modifier: Modifier,
    scrollableState: ScrollState,
    overviewUiState: OverViewDisplayState,
    chartsData: List<TransactionByCategory>,
    showTransactionScreen: () -> Unit,
    showStructureScreen: () -> Unit
) {
    Column(
        modifier = modifier.scrollable(scrollableState, orientation = Orientation.Vertical)
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
            item {
                TransactionSummary(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = AppColors.surface),
                    overviewUiState = overviewUiState
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp
                )
                Row(modifier = Modifier.fillMaxWidth()
                    .background(color = AppColors.surface)
                    .clickable(onClick = {showTransactionScreen()})
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.show_more))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.previous)
                    )
                }
                ExpenseChart(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                        .background(color = AppColors.surface),
                    chartsData = chartsData,
                    overviewUiState = overviewUiState
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp
                )
                Row(modifier = Modifier.fillMaxWidth().background(color = AppColors.surface)
                    .clickable(onClick = { showStructureScreen() })
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.show_more))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.previous)
                    )
                }
            }
        }
    }
}


@Composable
fun ExpenseChart(
    modifier: Modifier,
    chartsData: List<TransactionByCategory>,
    overviewUiState: OverViewDisplayState
) {
    Column(modifier= modifier.padding(horizontal = 10.dp, vertical = 15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.expense_structure),
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.inverseSurface,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.padding(end = 3.dp)
            )
        }
        Row(modifier = Modifier.padding(vertical = 15.dp , horizontal = 15.dp)) {
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                showCharts(chartsData,overviewUiState)
            }
            Column {
                showData(chartsData)
            }
        }
    }
}

@Composable

fun showCharts(chartsData: List<TransactionByCategory>, overviewUiState: OverViewDisplayState,selectedStatistics: TransactionType = TransactionType.Expense) {
    var radiusOuter = 140.dp
    val chartBarWidth = 25.dp
    var expenseForChart = mutableListOf<Float>()

    //expense data - charts data
    chartsData.forEach {
        expenseForChart.add(360f*it.categoryExpPercentage /100.0F)
    }
    //animation
    var animationPlayed by remember { mutableStateOf(true) }
    var lastValue = 0F

    val animateSize by animateFloatAsState(
        targetValue = radiusOuter.value,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )
    Box(modifier = Modifier.size(animateSize.dp), contentAlignment = Alignment.Center){
        Canvas(
            modifier = Modifier.size(radiusOuter *2f).rotate(animateRotation)
        ){
            expenseForChart.forEachIndexed { index, value ->
                drawArc(
                    color = chartsData[index].categoryColor,
                    lastValue,
                    value,
                    useCenter = false,
                    style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                )
                lastValue += value
            }
        }
        val annotedExpString = buildAnnotatedString {
            append("Expense")
            append("\n")
            append("- ")
            append(overviewUiState.totalExpense.toString())
        }
        val annotedIncomeString = buildAnnotatedString {
            append("Income")
            append("\n")
            append(overviewUiState.totalIncome.toString())
        }
        Text(
            text = if(selectedStatistics == TransactionType.Expense) annotedExpString else annotedIncomeString
        )
    }


}

@Composable
fun showData(chartsData: List<TransactionByCategory>) {
        chartsData.forEach {
            Row(modifier = Modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(
                    color = it.categoryColor , shape = CircleShape)
                ) {

                }
                Column(modifier = Modifier.padding(start = 5.dp)) {
                    Text(text = stringResource(it.categoryName) ,
                        modifier = Modifier.padding(end = 5.dp))
                }
                val annotedString = buildAnnotatedString {
                    append("(")
                    append(it.categoryExpPercentage.toString())
                    append("%")
                    append(")")
                }
                Text(text = annotedString)
            }

        }
}
