package com.example.expensetracker.uiScreen.uiState

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R
import com.example.expensetracker.utils.StaticData.ListOfIcons
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.expensetracker.utils.StaticData.listOfWalletColor
import com.example.expensetracker.utils.StaticData.listOfWalletIcon
import java.util.Calendar

//initial setup
data class HomeStateData(
    val userName: String = "",
    val initialMoney: String = "",

    )


data class WalletInputState(
    val walletId: Int = 0,
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

    val hideBalance: Boolean = true,


    //wallet expense
    val selectedFinanceWalletId: Int = 1,

    //selected wallet for details screen
    val selectedwalletIdDetail: Int = 0,
    val isEditWalletClicked: Boolean = false
)

data class SelectedTopBar(
    val selectedFinance: Int = R.string.expense
)

data class SelectedMonthAndYear(
    val currentMonthYear: Calendar = Calendar.getInstance()
)

data class FinanceInputState(
    val showDateDialogUI: Boolean = false,
    val selectedDate: Long? = System.currentTimeMillis(),
    val financeDescription: String = "",
    val financeAmount: String = "",
    val isFinanceAmountValid: Boolean = false,
)

data class CategoryInputState(
    val selectedExpCategoryId: Int = 0,
    val isExpenseCategoryValid: Boolean = false,
    var selectedIncCategoryId: Int = 0,
    val isIncomeCategoryValid: Boolean = false
)



