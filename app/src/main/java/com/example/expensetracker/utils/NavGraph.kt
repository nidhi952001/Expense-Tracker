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
    pickWalletIcon(route =R.string.pickIcon),
    showDetailOfWallet(route = R.string.show_detail_of_wallet),
    selectWallet(route = R.string.select_wallet),
    selectCategory(route = R.string.selectCategory),
    transactionsForSelectedWallet(route = R.string.transactionsForSelectedWallet)
}

@Composable
fun getScreenName(currentRoute: String, selectedFinanceType: SelectedTopBar):String {
    return when(currentRoute){
        TopLevelDestination.userName.name -> stringResource(R.string.add_name)
        TopLevelDestination.initialAmount.name -> stringResource(R.string.initial_amount)
        TopLevelDestination.Finance.name ->
            if(selectedFinanceType.selectedFinance == R.string.expense)
                stringResource(R.string.expense)
            else
                stringResource(R.string.income)
        TopLevelDestination.addWallet.name-> stringResource(R.string.addWallet)
        TopLevelDestination.pickWalletIcon.name -> stringResource(R.string.pickIcon)
        TopLevelDestination.selectCategory.name -> stringResource(R.string.selectCategory)
        else -> currentRoute
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
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            if(walletViewModel.walletInputState.value.isEditWalletClicked){
                walletViewModel.editWallet()
            }
            else {
                walletViewModel.saveNewWallet()
            }
            navController.navigateUp()
        }
        TopLevelDestination.Finance.name->{
            if(selectedFinanceType.selectedFinance == R.string.expense) {
                financeViewModel.saveIntoExpense()
                navController.navigateUp()
                catViewModel.resetExpCategory()
            }else{
                financeViewModel.saveIntoIncome()
                navController.navigateUp()
                catViewModel.resetIncCategory()
            }
        }
    }
}


fun topBarBackAction(
    currentRoute: String?,
    walletViewModel: WalletViewModel,
    navController: NavController,
    onBackClick: Boolean
){
    when(currentRoute){
        TopLevelDestination.addWallet.name->{
            walletViewModel.clearWalletInputState()
            navController.navigateUp()
        }
        TopLevelDestination.showDetailOfWallet.name->{
            if(onBackClick) {
                walletViewModel.clearEditMode()
                navController.navigateUp()
            }
            else{
                walletViewModel.populateWalletEditState()
                navController.navigate(TopLevelDestination.addWallet.name)
            }
        }
        else->{
            navController.navigateUp()
        }
    }
}