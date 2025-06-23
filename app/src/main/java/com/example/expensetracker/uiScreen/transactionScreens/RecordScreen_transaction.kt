package com.example.expensetracker.uiScreen.transactionScreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.expensetracker.viewModel.FinanceViewModel

@Composable
fun View_single_transaction( financeViewModel: FinanceViewModel) {
    val transactionData = financeViewModel.transactionSelectedByUser.collectAsState()
    transactionData.value?.let {
        Text(text = it.transactionAmount.toString())

        LaunchedEffect(it.transactionId) {
            println("transaction selected by ${it.transactionAmount}")
        }
    }

}