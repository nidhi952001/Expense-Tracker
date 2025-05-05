package com.example.expensetracker.utils

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet

data class HomeStateData(
    var userName : String = "",
    var initialMoney:String="",

)

data class TopBarStateData(
    var selectedTopBar:String = TopLevelDestination.expense.name
)

data class WalletStateData(
    var walletName:String = "",
    var selectType :TypeOfWallet= TypeOfWallet.GENERAL,
    var walletAmount : String = "",
    var walletIconName:Int= R.string.bank,
    var listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon:Int = R.drawable.account_wallet_ic
)
