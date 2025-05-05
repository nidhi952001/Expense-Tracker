package com.example.expensetracker.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.viewModel.WalletViewModel

enum class TopLevelDestination(@StringRes val route: Int) {
    home(route = R.string.home),
    userName(route = R.string.add_name),
    initialAmount(route = R.string.initial_amount),
    transaction(route = R.string.transaction),
    income(route = R.string.income),
    expense(route = R.string.expense),
    showWallet(route = R.string.showWallet),
    addWallet(route = R.string.addWallet),
    pickWalletIcon(route =R.string.pickIcon)
}

val screenTitle = mapOf(
    "expense" to R.string.expense,
    "income" to R.string.income,
    "transaction" to R.string.transaction,
    "add" to R.string.add,
    "showWallet" to R.string.showWallet,
    "addWallet" to R.string.addWallet,
    "home" to R.string.home,
    "addName" to R.string.add_name,
    "addInitialAmount" to R.string.initial_amount,
    "numericInput" to R.string.numericInput,
    "showWalletIcon" to R.string.pickIcon
)
@Composable
fun getScreenName(currentRoute: String):String {
    val context = LocalContext.current
    return screenTitle[currentRoute]?.let(context::getString) ?: currentRoute
}


fun topBarAction(
    currentRoute: String,
    walletViewModel: WalletViewModel,
    walletUiState: WalletStateData,
    navController: NavController
) {
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            val newWallet = Wallet(
                walletId = 0,
                walletName = walletUiState.walletName,
                walletType = walletUiState.selectType,
                walletAmount = walletUiState.walletAmount.toFloat(),
                walletIcon = walletUiState.selectedIcon,
                walletIconDes = walletUiState.walletIconName)
            walletViewModel.addWallet(newWallet)
            navController.navigateUp()
            walletViewModel.resetWalletUiState()
        }
    }
}