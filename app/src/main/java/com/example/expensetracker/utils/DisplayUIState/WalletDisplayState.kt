package com.example.expensetracker.utils.DisplayUIState

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.entity.Wallet
import com.example.transactionensetracker.entity.TransactionType

data class WalletDisplayState(
    val savedWallets: List<Wallet>,
    val totalBalance:Float
)

data class WalletDetailState(
    val selectedWallet_detail: Wallet?,
    val countSelectedWallet_expense: Int,
    val countSelectedWallet_income: Int,
    val transaction: List<transactionDetailByWallet>

)

data class overViewDisplayState(
    val totalExpense: Float=0F,
    val totalIncome: Float=0F,
    val total:Float=0F,
    val isLoading:Boolean = true,
    val showAll:Boolean = true
)

data class transactionDetail(
    val transaction_id: Int,
    val transaction_date: Long?,
    val transaction_amount: Float,
    val transaction_description: String?,
    val transaction_type: TransactionType,
    val walletName: String,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color
)

data class transactionByDate(
    val transaction_date: String,
    val transaction_day:String,
    val transaction_month:String,
    val transaction_year:String,
    val transaction_total_amount: Float,
    val transaction_list:List<transactionByList>
)

data class transactionByList(
    val transaction_amount: String,
    val transaction_description: String?,
    val transaction_color: Color,
    val walletName: String,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color
)

data class transactionDetailByWallet(
    val transaction_id: Int,
    val total_transaction:Int?,
    val transaction_total_amount:Float,
    val transaction_type: TransactionType,
    val categoryId:Int,
    val categoryName:Int,
    val categoryIcon:Int,
    val categoryColor:Color,
)

data class transactionByCatForSelectedWallet(
    val selectedCatForWallet:Int=0
)

