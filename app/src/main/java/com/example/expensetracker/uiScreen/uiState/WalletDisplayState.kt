package com.example.expensetracker.uiScreen.uiState

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.entity.Wallet
import com.example.transactionensetracker.entity.TransactionType

//data coming from database for wallet screen
data class WalletDisplayState(
    val userWallets: List<Wallet>,
    val totalWalletBalance:Float
)

/**
 *  wallet screen detail
 *  data coming from database
 **/
data class WalletDetailState(
    val selectedWalletDetails: Wallet?,
    val selectedWalletExpenseCount: Int,
    val selectedWalletIncomeCount: Int,
    val selectedWalletTransferCount:Int,
    val selectedWalletTransactions: List<selectedWalletTransactionState>

)

/**
* transaction screen overview
 **/
data class OverViewDisplayState(
    val totalExpense: Float=0F,
    val totalIncome: Float=0F,
    val total:Float=0F,
    val isLoading:Boolean = true,
    val showAll:Boolean = true
)

//transaction screen , data come from database and divide in 2 data class to display
data class TransactionDetailState(
    val transactionId: Int,
    val transactionDate: Long?,
    val transactionAmount: Float,
    val transactionDescription: String?,
    val transactionType: TransactionType,
    val fromWalletId:Int,
    val fromWalletName: String,
    val toWalletId:Int,
    val toWalletName:String?,
    val categoryId:Int,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color
)
//transaction data from database , divided for UI
data class TransactionByDateState(
    val transactionDate: String,
    val transactionDay:String,
    val transactionMonth:String,
    val transactionYear:String,
    val transactionTotalAmount: Float,
    val transactionList:List<TransactionListState>
)
//transaction data from database , divided for UI
data class TransactionListState(
    val transactionId: Int,
    val transactionAmount: String,
    val transactionDescription: String?,
    val transactionColor: Color,
    val walletName: String,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color
)
//statistic page
data class TransactionByCategory(
    val categoryId:Int,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color,
    /*val categoryExpTotalAmount:String,
    val categoryExpCount:String,*/
    val categoryExpPercentage:Float
)
//detail screen of wallet
data class selectedWalletTransactionState(
    val transactionId: Int,
    val transactionFromWalletId:Int?,
    val transactionToWalletId:Int?,
    val totalTransaction:Int?,
    val transactionTotalAmount:Float,
    val transactionType: TransactionType,
    val categoryId:Int,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color,
)
//detail screen of wallet
data class TransactionForSelectedWalletCategoryState(
    val selectedCategoryForWallet:Int=0
)
//user selected transaction
data class selectedTransaction(
    val selectedTransactionId:Int=0
)

