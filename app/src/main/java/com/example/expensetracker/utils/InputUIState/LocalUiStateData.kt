package com.example.expensetracker.utils.InputUIState

import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.utils.ListOfIcons
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.listOfWalletIcon

data class HomeStateData(
    var userName : String = "",
    var initialMoney:String="",

)

data class TopBarStateData(
    var selectedTopBar:String = TopLevelDestination.expense.name
)

data class WalletInputState(
    var walletName:String = "",
    var isWalletNameValid:Boolean= false,
    var selectType :TypeOfWallet= TypeOfWallet.GENERAL,
    var walletAmount : String = "",
    var isWalletAmountValid:Boolean=false,
    var walletIconName:Int= R.string.bank,
    var listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon:Int = R.drawable.account_wallet_ic
)
