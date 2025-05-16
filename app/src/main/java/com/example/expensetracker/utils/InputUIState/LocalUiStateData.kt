package com.example.expensetracker.utils.InputUIState

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.ListOfIcons
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.listOfColor
import com.example.expensetracker.utils.listOfWalletIcon

data class HomeStateData(
    val userName: String = "",
    val initialMoney: String = "",

    )

data class TopBarStateData(
    val selectedTopBar: String = TopLevelDestination.expense.name
)

data class WalletInputState(
    val walletName: String = "",
    val isWalletNameValid: Boolean = false,
    val isWalletTypeExpanded: Boolean = false,
    val selectType: TypeOfWallet = TypeOfWallet.General,
    val walletAmount: String = "",
    val isWalletAmountValid: Boolean = false,
    val showListOfColor: Map<String, Color> = listOfColor.coloCodeToColor,
    val selectedColors: Color = listOfColor.coloCodeToColor.getValue("sky_blue"),
    val showColorPicker: Boolean = false,
    val walletIconName: Int = R.string.bank,
    val listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon: Int = R.drawable.account_wallet_ic,
)

data class ExpenseInputState(
    val showDateDialogUI: Boolean = false,
    val selectedDate: Long? = System.currentTimeMillis(),
    val showTimeDialogUI:Boolean = false,
    val expDescription: String = "",
    val expAmount:String = "",
    val expSelectedCategory:String="",
)


