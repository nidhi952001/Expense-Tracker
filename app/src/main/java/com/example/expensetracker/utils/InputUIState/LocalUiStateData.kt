package com.example.expensetracker.utils.InputUIState

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.utils.ListOfColors
import com.example.expensetracker.utils.ListOfIcons
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.listOfColor
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
    val selectedIcon:Int = R.drawable.account_wallet_ic,
    val showListOfColor:List<ListOfColors> = listOfColor.listOfColors,
    var selectedColors: Color = listOfColor.listOfColors.get(0).colors,
    var showColorPicker:Boolean  = false
)
