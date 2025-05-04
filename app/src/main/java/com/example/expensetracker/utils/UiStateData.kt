package com.example.expensetracker.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.expensetracker.R
import com.example.expensetracker.uiScreen.TopLevelDestination

data class UiStateData(
    var userName : String = "",
    var initialMoney:String="",
    var selected:String = TopLevelDestination.expense.name
)

data class WalletStateData(
    var walletName:String = "",
    var selectType :String="Select Category",
    var initialAmount :String = "",
    @DrawableRes
    var walletIcon:Int= R.drawable.account_wallet_ic
)
