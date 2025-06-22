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

//wallet screen temp ui state
data class WalletInputState(
    val walletId: Int = 0,
    val walletName: String = "",
    val isWalletNameValid: Boolean = false,
    val isWalletTypeExpanded: Boolean = false,
    val selectType: TypeOfWallet = TypeOfWallet.General,
    val walletAmount: String = "",
    val isWalletAmountValid: Boolean = false, //for add wallet
    val showListOfColor: Map<String, Color> = listOfWalletColor.coloCodeToColor,
    val selectedColors: Color = listOfWalletColor.coloCodeToColor.getValue("sky_blue"),
    val showColorPicker: Boolean = false,
    val walletIconName: Int = R.string.bank,
    val listOfIcon: List<ListOfIcons> = listOfWalletIcon.iconData,
    val selectedIcon: Int = R.drawable.account_wallet_ic,

    val hideBalance: Boolean = true,


    //wallet expense / income
    val selectedFinanceFromWalletId: Int = 1,
    //wallet transfer
    val selectedFinanceToWalletId: Int = 0,

    //selected wallet for details screen
    val selectedwalletIdDetail: Int = 0,
    val isEditWalletClicked: Boolean = false
)

//finance screen second top bar
data class SelectedTopBar(
    val selectedFinance: Int = R.string.expense
)

//transaction screen top bar
data class SelectedMonthAndYear(
    val currentMonthYear: Calendar = Calendar.getInstance()
)

//finance screen
data class FinanceInputState(
    val showDateDialogUI: Boolean = false,
    val selectedDate: Long? = System.currentTimeMillis(),
    val financeDescription: String = "",
    val financeAmount: String = "",
    val isFinanceAmountValid: Boolean = false,
    val isFromWalletValid:Boolean = true,
    val isToWalletValid: Boolean = false,
)

data class CategoryInputState(
    val selectedExpCategoryId: Int = 0,
    val isExpenseCategoryValid: Boolean = false,
    var selectedIncCategoryId: Int = 0,
    val isIncomeCategoryValid: Boolean = false
)





