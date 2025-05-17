package com.example.expensetracker.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.utils.InputUIState.WalletInputState
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
    pickWalletIcon(route =R.string.pickIcon),
    showDetailOfWallet(route = R.string.show_detail_of_wallet),
    selectWallet(route = R.string.select_wallet)
}

val screenTitle = mapOf(
    "addName" to R.string.add_name,
    "addInitialAmount" to R.string.initial_amount,
    "expense" to R.string.expense,
    "income" to R.string.income,
    "addWallet" to R.string.addWallet,
    "pickWalletIcon" to R.string.pickIcon,
    "selectWallet" to R.string.select_wallet
)
@Composable
fun getScreenName(currentRoute: String):String {
    val context = LocalContext.current
    return screenTitle[currentRoute]?.let(context::getString) ?: currentRoute
}


fun topBarAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    walletUiState: WalletInputState,
    navController: NavController
) {
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            walletViewModel.saveIntoWallet(walletUiState)
            navController.navigateUp()
        }
    }
}


fun topBarBackAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    navController: NavController
){
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            walletViewModel.resetWalletUiState()
            navController.navigateUp()
        }
        else->{
            navController.navigateUp()
        }
    }
}