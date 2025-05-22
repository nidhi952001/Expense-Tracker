package com.example.expensetracker.utils.DisplayUIState

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType

data class WalletDisplayState(
    val savedWallets: List<Wallet>,
    val totalBalance:Float
)


data class overViewDisplayState(
    val totalExpense: Float,
    val totalIncome: Float,
    val total:Float
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
    val categoryColor:Color,
)
