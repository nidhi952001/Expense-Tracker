package com.example.expensetracker.utils.InputUIState

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R
import com.example.expensetracker.utils.StaticData.ListOfIcons
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.expensetracker.utils.StaticData.listOfWalletColor
import com.example.expensetracker.utils.StaticData.listOfWalletIcon

data class HomeStateData(
    val userName: String = "",
    val initialMoney: String = "",

    )


data class WalletInputState(
    val walletName: String = "",
    val isWalletNameValid: Boolean = false,
    val isWalletTypeExpanded: Boolean = false,
    val selectType: TypeOfWallet = TypeOfWallet.General,
    val walletAmount: String = "",
    val isWalletAmountValid: Boolean = false,
    val showListOfColor: Map<String, Color> = listOfWalletColor.coloCodeToColor,
    val selectedColors: Color = listOfWalletColor.coloCodeToColor.getValue("sky_blue"),
    val showColorPicker: Boolean = false,
    val walletIconName: Int = R.string.bank,
    val listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon: Int = R.drawable.account_wallet_ic,

    //wallet expense
    val selectedExpWalletId:Int=1
)
data class SelectedTopBar(
    val selectedExpInc:Int = R.string.expense
)

data class ExpenseIncomeInputState(
    val showDateDialogUI: Boolean = false,
    val selectedDate: Long? = System.currentTimeMillis(),
    val expIncDescription: String = "",
    val expIncAmount:String = "",
    val validExpIncAmount:Boolean = false,
)

data class CategoryInputState(
    val selectedExpCategoryId:Int=0,
    val validExpCategory:Boolean = false,
    var selectedIncCategoryId:Int=0,
    val validIncCategory:Boolean=false
)



