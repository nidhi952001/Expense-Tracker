package com.example.expensetracker.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.uiScreen.uiState.SelectedTopBar
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.expensetracker.viewModel.WalletViewModel

enum class TopLevelDestination(@StringRes val route: Int) {
    home(route = R.string.home),
    userName(route = R.string.add_name),
    initialAmount(route = R.string.initial_amount),
    transaction(route = R.string.transaction),
    Finance(route = R.string.finance),
    showWallet(route = R.string.showWallet),
    addWallet(route = R.string.addWallet),
    pickWalletIcon(route = R.string.pickIcon),
    showDetailOfWallet(route = R.string.show_detail_of_wallet),
    selectWallet(route = R.string.select_wallet),
    selectCategory(route = R.string.selectCategory),
    transactionsForSelectedWallet(route = R.string.transactionsForSelectedWallet)
}

@Composable
fun getScreenName(currentRoute: String, selectedFinanceType: SelectedTopBar): String {
    return if (currentRoute == TopLevelDestination.userName.name) {
        stringResource(R.string.add_name)
    } else if (currentRoute == TopLevelDestination.initialAmount.name) {
        stringResource(R.string.initial_amount)
    } else if (currentRoute == TopLevelDestination.Finance.name) {
        if (selectedFinanceType.selectedFinance == R.string.expense)
            stringResource(R.string.expense)
        else if (selectedFinanceType.selectedFinance == R.string.expense)
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
    }else {
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
                navController.navigateUp()
                catViewModel.resetExpCategory()
            } else if (selectedFinanceType.selectedFinance == R.string.income) {
                financeViewModel.dataOfIncome()
                navController.navigateUp()
                catViewModel.resetIncCategory()
            } else {
                financeViewModel.saveIntoTransfer()
                navController.navigateUp()
            }
        }
    }
}


fun topBarBackAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    navController: NavController,
    onBackClick: Boolean
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

        else -> {
            navController.navigateUp()
        }
    }
}