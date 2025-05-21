package com.example.expensetracker.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.ExpenseViewModel
import com.example.expensetracker.viewModel.IncomeViewModel
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
    selectWallet(route = R.string.select_wallet),
    selectExpCategory(route = R.string.selectCategory),
    selectIncCategory(route = R.string.selectCategory)
}

val screenTitle = mapOf(
    "addName" to R.string.add_name,
    "addInitialAmount" to R.string.initial_amount,
    "income" to R.string.income,
    "addWallet" to R.string.addWallet,
    "pickWalletIcon" to R.string.pickIcon,
    "selectWallet" to R.string.select_wallet,
    "selectExpCategory" to R.string.selectCategory,
    "selectIncCategory" to R.string.selectCategory
)
@Composable
fun getScreenName(currentRoute: String):String {
    val context = LocalContext.current
    return screenTitle[currentRoute]?.let(context::getString) ?: currentRoute
}


fun topBarAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    navController: NavController,
    expViewModel: ExpenseViewModel,
    incViewModel: IncomeViewModel
) {
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            walletViewModel.saveIntoWallet()
            navController.navigateUp()
        }
        TopLevelDestination.expense.name->{
            expViewModel.saveIntoExpense()
            navController.navigate(navController.graph.findStartDestination().id)
        }
        TopLevelDestination.income.name->{
            incViewModel.saveIntoIncome()
            navController.navigate(navController.graph.findStartDestination().id)
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