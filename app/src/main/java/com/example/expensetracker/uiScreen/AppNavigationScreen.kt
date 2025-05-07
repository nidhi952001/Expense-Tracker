package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.topBarAction
import com.example.expensetracker.viewModel.ExpenseViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel



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
    val homeUiState by homeViewModel.homeUiStateData.collectAsState()
    val walletDatabaseState by walletViewModel.persistedWalletState.collectAsState()
    val inputWalletState by walletViewModel.tempWalletState.collectAsState()
    val topBarUiState by homeViewModel.topBarUiState.collectAsState()

    var userName by rememberSaveable { mutableStateOf("User") }
    var initialAmount by rememberSaveable { mutableStateOf("not_decided") }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    //todo can add this into data class
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
                    currentRoute = currentRoute, //todo need to rewrite this logic , it can crash the app
                    navHostController = navController,
                    localWallet = inputWalletState,
                    selected = topBarUiState.selectedTopBar,
                    newSelectedRoute = {
                        homeViewModel.changeToNewRoute(it)
                        navController.navigate(it)
                    },
                    onActionClick = {
                        topBarAction(
                            currentRoute = currentRoute,
                            walletViewModel = walletViewModel,
                            walletUiState = inputWalletState,
                            navController = navController
                            )
                    }
                )
        }, bottomBar = {
            appBottomBar(navController, currentRoute)
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination =   //todo this also need to rewrite
                    if (initialAmount.equals("not_decided"))
                        TopLevelDestination.home.name
                    else
                        TopLevelDestination.transaction.name,
                modifier = Modifier.padding(padding).background(color = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                composable(route = TopLevelDestination.home.name) {
                    WelcomeScreen(
                        onGetStarted = { navController.navigate(TopLevelDestination.userName.name) },
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                    )
                }
                composable(route = TopLevelDestination.userName.name) {
                    userNameScreen(
                        uiState = homeUiState,
                        onUserNameChange = { homeViewModel.updateUserName(it) },
                        modifier = Modifier.fillMaxSize(),
                        saveUserName = {
                            homeViewModel.saveUserName(it)
                            navController.navigate(TopLevelDestination.initialAmount.name)
                        })
                }
                composable(route = TopLevelDestination.initialAmount.name) {
                    initialMoneyScreen(
                        modifier = Modifier.fillMaxSize(),
                        initialMoneyState = homeUiState,
                        onInitialMoneyChange = { homeViewModel.updateInitalMoney(it) },
                        saveInitialMoney = {
                            walletViewModel.saveInitialAmount(it)
                            navController.navigate(TopLevelDestination.transaction.name)
                        })
                }
                composable(route = TopLevelDestination.transaction.name) {
                    TransactionScreen()
                }
                composable(route = TopLevelDestination.expense.name) {
                    ExpenseScreen(
                        modifier = Modifier.fillMaxSize(),
                        expenseViewModel = expViewModel,
                        onClick = {
                    })
                } //todo need to do code review
                composable(route = TopLevelDestination.income.name){
                    incomeScreen()
                }
                composable(route = TopLevelDestination.showWallet.name) {
                    viewWalletWithBalance(
                        modifier = Modifier.fillMaxSize(),
                        walletDatabaseState = walletDatabaseState,
                        addWallet = {navController.navigate(TopLevelDestination.addWallet.name)}
                    )
                }
                composable(route = TopLevelDestination.addWallet.name) {
                    addWallet(
                        walletUiState = inputWalletState,
                        onSelectType = { walletViewModel.updateWalletType(it) },
                        onValueChange = { walletViewModel.updateWalletAmount(it) },
                        onNameChanged = { walletViewModel.updateWalletName(it) },
                        onIconClick = {
                            navController.navigate(TopLevelDestination.pickWalletIcon.name)
                        }
                    )
                }
                composable(route = TopLevelDestination.pickWalletIcon.name){
                        showIcon(walletUiState = inputWalletState,
                            modifier = Modifier.fillMaxSize(),
                            onSelectedIcon = {
                                walletViewModel.updateSelectedIcon(it)
                                navController.navigateUp()
                            })
                }
            }
        }
    }
}




