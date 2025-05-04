package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.ExpenseViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel

enum class TopLevelDestination(@StringRes val route: Int) {
    expense(route = R.string.expense),
    income(route = R.string.income),
    transaction(route = R.string.transaction),
    add(
        route = R.string.add
    ),
    showWallet(route = R.string.showWallet),
    addWallet(route = R.string.addWallet),
    home(route = R.string.home),
    addName(route = R.string.add_name),
    addInitialAmount(route = R.string.initial_amount),
    numericInput(route = R.string.numericInput)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationScreen() {
    //navigation
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //view model
    val expViewModel: ExpenseViewModel = hiltViewModel()
    val walletViewModel: WalletViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    //uiState
    val homeScreenUiState by homeViewModel.uiStateData.collectAsState()
    val walletDatabaseState by walletViewModel.wallets.collectAsState()
    val walletUiState by walletViewModel.walletUiState.collectAsState()

    var userName by rememberSaveable { mutableStateOf("User") }
    var initialAmount by rememberSaveable { mutableStateOf("not_decided") }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        homeViewModel.savedUsername.collect { newUserName ->
            userName = newUserName
        }
    }

    LaunchedEffect(true) {
        walletViewModel.initialAmount.collect { it ->
            initialAmount = it
            isLoading = false
        }
    }

    if (!isLoading) {
        Scaffold(topBar = {
            AppTopBar(
                userName = userName,
                currentRoute = currentRoute,
                navHostController = navController,
                selected = homeScreenUiState.selected,
                newSelectedRoute = {
                    homeViewModel.changeToNewRoute(it)
                    navController.navigate(it)
                }
            )
        }, bottomBar = {
            AppBottomBar(navController, currentRoute)
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination =
                    if (initialAmount.equals("not_decided")) TopLevelDestination.home.name
                    else TopLevelDestination.transaction.name,
                modifier = Modifier.padding(padding)
            ) {
                composable(route = TopLevelDestination.home.name) {
                    WelcomeScreen(
                        onGetStarted = { navController.navigate(TopLevelDestination.addName.name) },
                        modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp)
                    )
                }
                composable(route = TopLevelDestination.addName.name) {
                    addNameScreen(
                        uiState = homeScreenUiState,
                        userNameChanged = { homeViewModel.updateUserName(it) },
                        modifier = Modifier.fillMaxSize(),
                        saveUserName = {
                            homeViewModel.saveUserName(it)
                            navController.navigate(TopLevelDestination.addInitialAmount.name)
                        })
                }
                composable(route = TopLevelDestination.addInitialAmount.name) {
                    SaveInitalMoneyScreen(
                        modifier = Modifier.fillMaxSize(),
                        initialMoneyState = homeScreenUiState,
                        updateInitialMoney = { homeViewModel.updateInitalMoney(it) },
                        saveInitialMoney = {
                            walletViewModel.saveInitialAmount(it)
                            navController.navigate(TopLevelDestination.transaction.name)
                        })
                }
                composable(route = TopLevelDestination.transaction.name) {
                    TransactionScreen()
                }
                composable(route = TopLevelDestination.expense.name) {
                    ExpenseScreen(expenseViewModel = expViewModel , onClick = {
                        navController.navigate(TopLevelDestination.numericInput.name)
                    })
                }
                composable (route = TopLevelDestination.numericInput.name){
                    showNumberKeyBoard()
                }
                composable(route = TopLevelDestination.showWallet.name) {
                    showWallet(
                        modifier = Modifier.fillMaxSize(),
                        listOfWallet = walletDatabaseState,
                        addWallet = {navController.navigate(TopLevelDestination.addWallet.name)}
                    )
                }
                composable(route = TopLevelDestination.addWallet.name) {
                    addWallet(
                        walletUiState = walletUiState,
                        onSelectType = {},
                        saveAmount = {},
                        onValueChange = {walletViewModel.updateWalletName(it)},
                    )
                }
            }
        }
    }
}




