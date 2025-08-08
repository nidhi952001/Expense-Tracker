package com.example.expensetracker.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.uiScreen.uiState.SelectedTopBar
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel

enum class TopLevelDestination(@StringRes val route: Int) {
    home(route = R.string.home),
    userName(route = R.string.add_name),
    initialAmount(route = R.string.initial_amount),
    transaction(route = R.string.transaction),
    Record(route = R.string.record),
    Finance(route = R.string.finance),
    statictis(route = R.string.statistic),
    statisticsTransaction(route = R.string.statisticTransaction),
    structureScreen(route = R.string.statistic),
    transactionsForSelectedCategory(route = R.string.transactionsForSelectedCategory),
    showWallet(route = R.string.showWallet),
    addWallet(route = R.string.addWallet),
    pickWalletIcon(route = R.string.pickIcon),
    showDetailOfWallet(route = R.string.show_detail_of_wallet),
    selectWallet(route = R.string.select_wallet),
    selectCategory(route = R.string.selectCategory),
    transactionsByWallet(route = R.string.transactionsByWallet),
    statisticsByWallet(route = R.string.statisticsByWallet),
    statisticsTransactionByWallet(route = R.string.statisticsTransactionByWallet),
    structureByWallet(route=R.string.structureByWallet)
}

@Composable
fun getScreenName(
    currentRoute: String,
    selectedFinanceType: SelectedTopBar,
    selectedCategoryStatistics: Int?
): String {
    return if (currentRoute == TopLevelDestination.userName.name) {
        stringResource(R.string.add_name)
    } else if (currentRoute == TopLevelDestination.initialAmount.name) {
        stringResource(R.string.initial_amount)
    } else if (currentRoute == TopLevelDestination.Finance.name) {
        if (selectedFinanceType.selectedFinance == R.string.expense)
            stringResource(R.string.expense)
        else if (selectedFinanceType.selectedFinance == R.string.income)
            stringResource(R.string.income)
        else
            stringResource(R.string.transfer)
    } else if (currentRoute == TopLevelDestination.addWallet.name) {
        stringResource(R.string.addWallet)
    } else if (currentRoute == TopLevelDestination.pickWalletIcon.name) {
        stringResource(R.string.pickIcon)
    } else if (currentRoute == TopLevelDestination.selectCategory.name) {
        stringResource(R.string.selectCategory)
    }
    else if(currentRoute.contains(TopLevelDestination.selectWallet.name)){
        stringResource(R.string.select_wallet)
    }
    else if(currentRoute == TopLevelDestination.statisticsTransaction.name || currentRoute == TopLevelDestination.statisticsTransactionByWallet.name){
        stringResource(R.string.transaction)
    }
    else if(currentRoute == TopLevelDestination.structureScreen.name || currentRoute == TopLevelDestination.structureByWallet.name){
        stringResource(R.string.statistic)
    }
    else if(currentRoute ==TopLevelDestination.transactionsForSelectedCategory.name || currentRoute==TopLevelDestination.transactionsByWallet.name){
        if (selectedCategoryStatistics != null) {
            stringResource(selectedCategoryStatistics)
        }
        else ""
    }
    else if(currentRoute == TopLevelDestination.statisticsByWallet.name){
        stringResource(R.string.statistic)
    }
    else {
        currentRoute

    }
}


fun topBarAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    navController: NavController,
    financeViewModel: FinanceViewModel,
    selectedFinanceType: SelectedTopBar,
    catViewModel: CategoryViewModel
) {
    when (currentRoute) {
        TopLevelDestination.addWallet.name -> {
            if (walletViewModel.walletInputState.value.isEditWalletClicked) {
                walletViewModel.editWallet()
            } else {
                walletViewModel.saveNewWallet()
            }
            navController.navigateUp()
        }

        TopLevelDestination.Finance.name -> {
            if (selectedFinanceType.selectedFinance == R.string.expense) {
                financeViewModel.dataOfExpense()
                navController.navigate(TopLevelDestination.transaction.name)
                catViewModel.resetExpCategory()
            } else if (selectedFinanceType.selectedFinance == R.string.income) {
                financeViewModel.dataOfIncome()
                navController.navigate(TopLevelDestination.transaction.name)
                catViewModel.resetIncCategory()
            } else {
                financeViewModel.saveIntoTransfer()
                navController.navigate(TopLevelDestination.transaction.name)
            }
        }
    }
}

fun topBarBackAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    financeViewModel: FinanceViewModel,
    navController: NavController,
    onBackClick: Boolean,
    homeViewModel: HomeViewModel,
    categoryViewModel: CategoryViewModel
) {
    when (currentRoute) {
        TopLevelDestination.addWallet.name -> {
            walletViewModel.clearWalletInputState()
            navController.navigateUp()
        }

        TopLevelDestination.showDetailOfWallet.name -> {
            if (onBackClick) {
                walletViewModel.clearEditMode()
                navController.navigateUp()
            } else {
                walletViewModel.populateWalletEditState()
                navController.navigate(TopLevelDestination.addWallet.name)
            }
        }

        TopLevelDestination.Record.name->{
            if (onBackClick) {
                walletViewModel.clearEditMode()
                financeViewModel.resetUserSelectedTransaction()
                navController.navigateUp()
            } else {
                val transactionData = financeViewModel.transactionSelectedByUser.value
                if(transactionData!=null) {
                    if(transactionData.transactionType.name.contains("Income")) {
                        homeViewModel.updateSelectedFinance(R.string.income)
                        walletViewModel.updateSelectedFromWallet(transactionData.fromWalletId)
                        categoryViewModel.updateSelectedIncCategory(transactionData.categoryId)
                        financeViewModel.populateFinanceData()
                        navController.navigate(TopLevelDestination.Finance.name)
                    }
                    else if(transactionData.transactionType.name.contains("Expense")) {
                        homeViewModel.updateSelectedFinance(R.string.expense)
                        walletViewModel.updateSelectedFromWallet(transactionData.fromWalletId)
                        categoryViewModel.updateSelectedCategory(transactionData.categoryId)
                        financeViewModel.populateFinanceData()
                        navController.navigate(TopLevelDestination.Finance.name)
                    }
                    else{
                        homeViewModel.updateSelectedFinance(R.string.transfer)
                        walletViewModel.updateSelectedFromWallet(transactionData.fromWalletId)
                        walletViewModel.updateSelectedToWallet(transactionData.toWalletId)
                        financeViewModel.populateFinanceData()
                        navController.navigate(TopLevelDestination.Finance.name)
                    }
                }
            }
        }

        TopLevelDestination.structureScreen.name->{
            if(onBackClick){
                financeViewModel.resetStatisticsSelection()
                navController.navigateUp()
            }
        }
        TopLevelDestination.Finance.name->{
            if(onBackClick){
                homeViewModel.updateSelectedFinance(R.string.expense)
                navController.navigateUp()
            }
        }

        else -> {
            navController.navigateUp()
        }
    }
}

fun topBarDeleteAction(financeViewModel: FinanceViewModel, navController: NavController) {
    val transactionData = financeViewModel.transactionSelectedByUser.value
    financeViewModel.deleteTransaction(transactionData,null)
    financeViewModel.resetUserSelectedTransaction()
    navController.navigate(TopLevelDestination.transaction.name)

}
