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
    val userName : String = "",
    val initialMoney:String="",

)

data class TopBarStateData(
    val selectedTopBar:String = TopLevelDestination.expense.name
)

data class WalletInputState(
    val walletName:String = "",
    val isWalletNameValid:Boolean= false,
    val selectType :TypeOfWallet= TypeOfWallet.GENERAL,
    val walletAmount : String = "",
    val isWalletAmountValid:Boolean=false,
    val walletIconName:Int= R.string.bank,
    val listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon:Int = R.drawable.account_wallet_ic,
    val showListOfColor:List<ListOfColors> = listOfColor.listOfColors,
    val selectedColors: Color = listOfColor.listOfColors.get(0).colors,
    val showColorPicker:Boolean = false
)
