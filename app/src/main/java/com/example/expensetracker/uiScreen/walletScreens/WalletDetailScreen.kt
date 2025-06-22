package com.example.expensetracker.uiScreen.walletScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.TransactionScreen
import com.example.expensetracker.uiScreen.uiState.WalletDetailState
import com.example.expensetracker.uiScreen.uiState.selectedWalletTransactionState
import com.example.expensetracker.utils.formatWalletAmount
import com.example.expensetracker.viewModel.WalletViewModel
import com.example.transactionensetracker.entity.TransactionType

//wallet detail screen
@Composable
fun showWalletDetailRoute(
    walletViewModel: WalletViewModel,
    onClickTransactionFromWallet: () -> Unit
) {
    val scrollableState = rememberScrollState()
    val walletDetailUiState by walletViewModel.walletDetailUiState.collectAsState()
    val selectedwalletIdDetail by walletViewModel.walletInputState.collectAsState()
    println("trnasaction by wallet ${walletDetailUiState}")
    showWalletDetail(walletData = walletDetailUiState,
        selectedWalletId = selectedwalletIdDetail.selectedwalletIdDetail,
        scrollableState = scrollableState,
        onClickTransactionFromWallet = {
        walletViewModel.selectedCategoryForWallet(it)
        onClickTransactionFromWallet()
    })
}


@Composable
fun showWalletDetail(
    walletData: WalletDetailState,
    scrollableState: ScrollState,
    onClickTransactionFromWallet: (Int) -> Unit,
    selectedWalletId: Int
) {
    if (walletData.selectedWalletDetails != null) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
                .padding(top = 20.dp).scrollable(scrollableState, Orientation.Vertical)
        ) {
            Column {
                walletDetail(selectedWallet_detail = walletData.selectedWalletDetails!!, walletData)
                if (walletData.selectedWalletTransactions.isNotEmpty())
                    walletTransaction(
                        walletData.selectedWalletTransactions,
                        selectedWalletId = selectedWalletId,
                        onClickTransactionFromWallet = onClickTransactionFromWallet
                    )
            }
        }
    }
}
//wallet detail screen - wallet info
@Composable
private fun walletDetail(
    selectedWallet_detail: Wallet,
    walletData: WalletDetailState
) {
    Column(
        modifier = Modifier.background(color = AppColors.surface)
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(selectedWallet_detail.walletIcon),
                contentDescription = stringResource(selectedWallet_detail.walletIconDes),
                colorFilter = ColorFilter.tint(
                    color = Color.White
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = selectedWallet_detail.walletColor)
                    .padding(8.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedWallet_detail.walletName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.ruppes_icon) + formatWalletAmount(
                    selectedWallet_detail.walletAmount.toString()
                ),
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.income))
            Text(
                text = walletData.selectedWalletIncomeCount.toString() + "  " +
                        stringResource(R.string.transactions),
                color = Color.Blue.copy(alpha = 0.5F)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.expense))
            Text(
                text = walletData.selectedWalletExpenseCount.toString() + "  " +
                        stringResource(R.string.transactions),
                color = Color.Red.copy(alpha = 0.5F)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.transfer))
            Text(
                text = walletData.selectedWalletTransferCount.toString() + "  " +
                        stringResource(R.string.transactions),
                color = Color.Black.copy(alpha = 0.5F)
            )
        }
    }
}
@Composable
fun walletTransaction(
    transaction: List<selectedWalletTransactionState>,
    onClickTransactionFromWallet: (Int) -> Unit,
    selectedWalletId: Int
) {
    LazyColumn(
        modifier = Modifier.wrapContentHeight().padding(top = 20.dp).background(
            color = AppColors.surface
        ).padding(horizontal = 10.dp)
    ) {
        item {
            Text(
                text = "Transaction list",
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)
            )
        }
        items(transaction) {
            walletAllTransaction(
                it,
                onClickTransactionFromWallet = onClickTransactionFromWallet,
                selectedWalletId = selectedWalletId,
                modifier = Modifier
            )
        }
    }
}


@Composable
fun walletAllTransaction(
    transaction: selectedWalletTransactionState,
    onClickTransactionFromWallet: (Int) -> Unit,
    modifier: Modifier.Companion,
    selectedWalletId: Int
) {
    val categoryIcon = if(selectedWalletId == transaction.transactionFromWalletId && transaction.transactionType == TransactionType.TRANSFER){
        R.drawable.transfer_out_ic
    }
    else if(selectedWalletId == transaction.transactionToWalletId){
        R.drawable.transfer_in_ic
    }
    else{
        transaction.categoryIcon
    }
    val categoryName = if(selectedWalletId == transaction.transactionFromWalletId && transaction.transactionType == TransactionType.TRANSFER){
        R.string.transfer_out
    }
    else if(selectedWalletId == transaction.transactionToWalletId){
        R.string.transfer_in
    }
    else{
        transaction.categoryName
    }
    val categoryColor = if(selectedWalletId == transaction.transactionFromWalletId && transaction.transactionType == TransactionType.TRANSFER){
        Color(0XFF5d6d7e)
    }
    else if(selectedWalletId == transaction.transactionToWalletId){
        Color(0XFF5d6d7e)
    }
    else{
        transaction.categoryColor
    }
    Row(
        modifier = modifier.fillMaxWidth()
            .clickable(onClick = { onClickTransactionFromWallet(transaction.categoryId) })
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(categoryIcon),
                    contentDescription = stringResource(categoryName),
                    modifier = Modifier.size(35.dp)
                        .clip(CircleShape)
                        .background(color = categoryColor).padding(3.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text =
                        stringResource(categoryName),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = transaction.totalTransaction.toString() + " " + stringResource(R.string.transactions))
                }
            }
        }
        val annotedString = buildAnnotatedString {
            append(stringResource(R.string.ruppes_icon))
            append(" ")
            append(formatWalletAmount(transaction.transactionTotalAmount.toString()))
        }
        val annotedString1 = buildAnnotatedString {
            append(stringResource(R.string.minus_icon))
            append(stringResource(R.string.ruppes_icon))
            append(" ")
            append(formatWalletAmount(transaction.transactionTotalAmount.toString()))
        }
        Text(
            text =
            if (transaction.transactionType == TransactionType.Income) annotedString
            else annotedString1,
            color =
            if (transaction.transactionType == TransactionType.Income)
                Color.Blue.copy(alpha = 0.5F)
            else
                Color.Red.copy(alpha = 0.5F),
            textAlign = TextAlign.End
        )
    }
}

//wallet transaction detail,
@Composable
fun showWalletTransaction(walletViewModel: WalletViewModel) {
    val modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface)
    val scrollableState = rememberScrollState()
    val overViewState by walletViewModel.walletOverviewState.collectAsState()
    val transactionGroupByDate by walletViewModel.transactionGroupByDate.collectAsState()
    TransactionScreen(
        modifier = modifier,
        scrollableState = scrollableState,
        overViewState = overViewState,
        showTransaction = transactionGroupByDate
    )
}