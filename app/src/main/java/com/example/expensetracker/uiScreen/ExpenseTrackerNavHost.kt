package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.uiScreen.financeScreens.FinanceScreenRoute
import com.example.expensetracker.uiScreen.statisticsScreens.StatisticScreenRoute
import com.example.expensetracker.uiScreen.statisticsScreens.StructureScreenRoute
import com.example.expensetracker.uiScreen.statisticsScreens.showCategoryTransaction
import com.example.expensetracker.uiScreen.transactionScreens.TransactionScreenRoute
import com.example.expensetracker.uiScreen.transactionScreens.View_single_transaction
import com.example.expensetracker.uiScreen.walletScreens.SelectWalletRoute
import com.example.expensetracker.uiScreen.walletScreens.ShowIconScreen
import com.example.expensetracker.uiScreen.walletScreens.WalletScreenEntry
import com.example.expensetracker.uiScreen.walletScreens.addWalletScreenEntry
import com.example.expensetracker.uiScreen.walletScreens.showWalletDetailRoute
import com.example.expensetracker.uiScreen.walletScreens.showWalletTransaction
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.topBarAction
import com.example.expensetracker.utils.topBarBackAction
import com.example.expensetracker.utils.topBarDeleteAction
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTrackerNavHost() {
    //navigation
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //view model
    val financeViewModel: FinanceViewModel = hiltViewModel()
    val walletViewModel: WalletViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    //uiState
    val selectedFinanceType by homeViewModel.selectedFinance.collectAsState()
    val inputFinanceState by financeViewModel.tempFinanceState.collectAsState()



    val inputCategoryState by categoryViewModel.tempCategoryState.collectAsState()


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
        walletViewModel.initialAmount.collect {
            initialAmount = it
            isLoading = false
        }
    }

    if (!isLoading) {
        Scaffold(topBar = {
            AppTopBar(
                userName = userName,
                currentRoute = currentRoute, //todo need to rewrite this logic , it can crash the app
                localFinanceState = inputFinanceState,
                localCatState = inputCategoryState,
                selectedFinanceType = selectedFinanceType,
                onSelectFinance = { homeViewModel.updateSelectedFinance(it) },
                onActionClick = {
                    topBarAction(
                        currentRoute = currentRoute,
                        walletViewModel = walletViewModel,
                        financeViewModel = financeViewModel,
                        catViewModel = categoryViewModel,
                        navController = navController,
                        selectedFinanceType = selectedFinanceType,
                    )
                },
                onBackClick = {
                    topBarBackAction(
                        currentRoute = currentRoute,
                        walletViewModel = walletViewModel,
                        financeViewModel = financeViewModel,
                        categoryViewModel = categoryViewModel,
                        navController = navController,
                        onBackClick = true,
                        homeViewModel = homeViewModel
                    )
                },
                onEditWallet = {
                    topBarBackAction(
                        currentRoute = currentRoute,
                        walletViewModel = walletViewModel,
                        financeViewModel = financeViewModel,
                        navController = navController,
                        onBackClick = false,
                        homeViewModel = homeViewModel,
                        categoryViewModel = categoryViewModel
                    )
                },
                onDeleteTransaction = {
                    topBarDeleteAction(
                        financeViewModel = financeViewModel,
                        navController = navController,
                    )
                },
                walletViewModel = walletViewModel,
                onSelectStructure = {financeViewModel.updateStatistics(it)},
                selectedCategory = financeViewModel._userSelectedCategory.collectAsState()
            )
        }, bottomBar = {
            appBottomBar(navController, currentRoute)
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination =   //todo this also need to rewrite
                if (initialAmount == "not_decided") TopLevelDestination.home.name
                else TopLevelDestination.transaction.name,
                modifier = Modifier.padding(padding).background(color = surface)
            ) {
                composable(route = TopLevelDestination.home.name) {
                    WelcomeScreenRoute(
                        onGetStarted = { navController.navigate(TopLevelDestination.userName.name) }
                    )
                }
                composable(route = TopLevelDestination.userName.name) {
                    userNameScreenRoute(
                        onNavigate = {
                            navController.navigate(TopLevelDestination.initialAmount.name)
                        },homeViewModel = homeViewModel)
                }
                composable(route = TopLevelDestination.initialAmount.name) {
                    initialMoneyScreenRoute(
                        onNavigate = {
                            navController.navigate(TopLevelDestination.transaction.name)
                        },
                        homeViewModel = homeViewModel,
                        walletViewModel = walletViewModel
                    )
                }
                composable(route = TopLevelDestination.transaction.name) {
                    TransactionScreenRoute(
                        financeViewModel = financeViewModel,
                        showRecord = {
                            navController.navigate(TopLevelDestination.Record.name)
                        }
                    )
                }
                composable(route = TopLevelDestination.Record.name){
                    View_single_transaction(financeViewModel= financeViewModel)
                }
                composable(route = TopLevelDestination.Finance.name) {
                    FinanceScreenRoute(
                        onClickListOfWallet = {
                            navController.navigate(TopLevelDestination.selectWallet.name +"/$it") },
                        homeViewModel = homeViewModel,
                        onClickListOfCategory = { navController.navigate(TopLevelDestination.selectCategory.name) },
                        onBack = {
                            navController.navigateUp()
                        },
                        financeViewModel = financeViewModel,
                        walletViewModel = walletViewModel,
                        categoryViewModel = categoryViewModel
                    )
                }
                composable(route = TopLevelDestination.statictis.name){
                    StatisticScreenRoute(
                        financeViewModel = financeViewModel,
                        showTransactionScreen = {navController.navigate(TopLevelDestination.statisticsTransaction.name)},
                        showStructureScreen = {navController.navigate(TopLevelDestination.structureScreen.name)}
                    )
                }
                composable(route = TopLevelDestination.statisticsTransaction.name){
                    TransactionScreenRoute(
                        financeViewModel = financeViewModel,
                        showRecord = {
                            navController.navigate(TopLevelDestination.Record.name)
                        }
                    )
                }
                composable(route = TopLevelDestination.structureScreen.name){
                    StructureScreenRoute(
                        financeViewModel = financeViewModel,
                        showSelectedCategory = {navController.navigate(TopLevelDestination.transactionsForSelectedCategory.name)},
                        navigateback = {navController.navigateUp()}
                    )

                }
                composable(route = TopLevelDestination.transactionsForSelectedCategory.name){
                    showCategoryTransaction(
                        financeViewModel = financeViewModel,
                        showRecord = {
                            navController.navigate(TopLevelDestination.Record.name)
                        }
                    )
                }
                composable(route = TopLevelDestination.selectWallet.name + "/{walletSelectFor}") {
                    val walletShowingFor = it.arguments?.getString("walletSelectFor")
                    SelectWalletRoute(
                        walletShowing = walletShowingFor,
                        navigateUp = {
                        navController.popBackStack()
                    },walletViewModel = walletViewModel)
                }
                composable(route = TopLevelDestination.selectCategory.name) {
                    ShowCategoryListRoute(
                        navigateUp = {
                            navController.popBackStack()
                        },
                        homeViewModel = homeViewModel,
                        categoryViewModel = categoryViewModel
                    )
                }
                //wallet screen
                composable(route = TopLevelDestination.showWallet.name) {
                    WalletScreenEntry(
                        addWallet = {
                            navController.navigate(TopLevelDestination.addWallet.name)
                        },
                        onWalletClick = {
                            navController.navigate(TopLevelDestination.showDetailOfWallet.name)
                        }, walletViewModel = walletViewModel
                    )
                }
                composable(route = TopLevelDestination.addWallet.name) {
                    addWalletScreenEntry(onIconClick = {
                        navController.navigate(TopLevelDestination.pickWalletIcon.name)
                    }, onBackClick = {
                        navController.popBackStack()
                    },walletViewModel = walletViewModel
                    )
                }
                composable(route = TopLevelDestination.pickWalletIcon.name) {
                    ShowIconScreen(onSelectedIconNavigate = { navController.popBackStack() }, walletViewModel = walletViewModel)
                }
                composable(route = TopLevelDestination.showDetailOfWallet.name) {
                    showWalletDetailRoute(walletViewModel = walletViewModel,
                        onClickTransactionFromWallet = {
                            navController.navigate(TopLevelDestination.transactionsForSelectedWallet.name)
                        })
                }
                composable(route = TopLevelDestination.transactionsForSelectedWallet.name){
                    showWalletTransaction(walletViewModel = walletViewModel,
                        financeViewModel = financeViewModel,
                        showRecord = {navController.navigate(TopLevelDestination.Record.name)}
                    )
                }
            }
        }
    }
}






