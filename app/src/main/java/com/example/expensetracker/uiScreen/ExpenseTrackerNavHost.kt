package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.topBarAction
import com.example.expensetracker.utils.topBarBackAction
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.ExpenseIncomeViewModel
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
    val expIncViewModel: ExpenseIncomeViewModel = hiltViewModel()
    val walletViewModel: WalletViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val categoryViewModel:CategoryViewModel = hiltViewModel()

    //uiState
    val homeUiState by homeViewModel.homeUiStateData.collectAsState()
    val selectedExpInc by homeViewModel.selectedExpInc.collectAsState()
    val walletDatabaseState by walletViewModel.persistedWalletState.collectAsState()
    val inputWalletState by walletViewModel.tempWalletState.collectAsState()
    val inputExpeIncState by expIncViewModel.tempExpeIncState.collectAsState()
    val selectedExpenseWallet by walletViewModel.selectedWallet.collectAsState(
            initial = null
    )
    val selectedWallet_detail by walletViewModel.getSelectedWalletDetails.collectAsState(null)
    val overViewState by expIncViewModel.showOverView.collectAsState()
    val showTransaction by expIncViewModel._showTransaction.collectAsState()
    val listOfExpCategory by categoryViewModel.listOfExpCategory.collectAsState()
    val listOfIncCategory by categoryViewModel.listOfIncCategory.collectAsState()
    val inputCategoryState by categoryViewModel.tempCategoryState.collectAsState()
    val selectedExpCategory by categoryViewModel.currentExpCategory.collectAsState(initial = null)
    val selectedIncCategory by categoryViewModel.currentIncCategory.collectAsState(initial = null)

    var userName by rememberSaveable { mutableStateOf("User") }
    var initialAmount by rememberSaveable { mutableStateOf("not_decided") }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var walletScrollableState = rememberScrollState()

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
                    localWallet = inputWalletState,
                    localExpIncState = inputExpeIncState,
                    localCatState = inputCategoryState,
                    selectedExpInc = selectedExpInc,
                    onSelectExpInc = {homeViewModel.updateSelectedInExp(it)},
                    onActionClick = {
                        topBarAction(
                            currentRoute = currentRoute,
                            walletViewModel = walletViewModel,
                            expViewModel = expIncViewModel,
                            catViewModel = categoryViewModel,
                            navController = navController,
                            selectedExpInc = selectedExpInc,
                            )
                    },
                    onBackClick = {
                        topBarBackAction(
                            currentRoute = currentRoute,
                            walletViewModel = walletViewModel,
                            navController = navController,
                            onBackClick = true
                        )
                    },
                    onEditWallet = {
                        topBarBackAction(
                            currentRoute = currentRoute,
                            walletViewModel = walletViewModel,
                            navController = navController,
                            onBackClick = false
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
                modifier = Modifier.padding(padding).background(color =  surface)
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
                    TransactionScreen(
                        modifier = Modifier.fillMaxSize().background(color = inverseOnSurface),
                        overviewUiState = overViewState,
                        showTransaction = showTransaction
                    )
                }
                composable(route = TopLevelDestination.expenseIncome.name) {
                    ExpenseIncomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        inputUiState = inputExpeIncState,
                        showDateDialogUI = {expIncViewModel.updateDateDialogState(it)},
                        onDateSelected = {expIncViewModel.updateSelectedDate(it)},
                        onExpenseAmountChanged = {expIncViewModel.updateExpenseAmount(it)},
                        onDescriptionChanged = {expIncViewModel.updateExpenseDes(it)},
                        selectedExpWallet = selectedExpenseWallet,
                        onClickListOfWallet = {navController.navigate(TopLevelDestination.selectWallet.name)},
                        selectedCategory =
                        if(selectedExpInc.selectedExpInc == R.string.expense)
                            selectedExpCategory
                        else
                            selectedIncCategory,
                        onClickListOfCategory = {navController.navigate(TopLevelDestination.selectCategory.name)},
                        onBack = {
                            expIncViewModel.resetUiState()
                            categoryViewModel.resetIncCategory()
                            categoryViewModel.resetExpCategory()
                            navController.navigateUp()
                        }
                    )
                } //todo need to do code review
                composable(route = TopLevelDestination.selectWallet.name){
                    SelectWallet(
                        walletDatabaseState = walletDatabaseState,
                        selectedWallet = selectedExpenseWallet,
                        onSelectWallet = {wallet->
                            walletViewModel.updateSelectedExpWallet(wallet)
                            navController.navigateUp()
                        }
                    )
                }
                composable(route = TopLevelDestination.selectCategory.name){
                    if(selectedExpInc.selectedExpInc == R.string.expense) {
                        ShowCategoryList(
                            listOfExpCategory = listOfExpCategory,
                            selectedCategory = inputCategoryState,
                            onSelectCategory = {
                                categoryViewModel.updateSelectedCategory(it)
                                navController.navigateUp()
                            }
                        )
                    }
                    else{
                        ShowCategoryList(
                            listOfExpCategory = listOfIncCategory,
                            onSelectCategory = {
                                categoryViewModel.updateSelectedIncCategory(it)
                                navController.navigateUp()
                            },
                            selectedCategory = inputCategoryState
                        )
                    }
                }
                //wallet screen
                composable(route = TopLevelDestination.showWallet.name) {
                    viewWalletWithBalance(
                        modifier = Modifier.scrollable(state = walletScrollableState, orientation = Orientation.Vertical).fillMaxSize().background(color =  inverseOnSurface),
                        walletDatabaseState = walletDatabaseState,
                        addWallet = {navController.navigate(TopLevelDestination.addWallet.name)},
                        onViewWalletDetail = {walletId->
                            walletViewModel.getSelectedWalletData(walletId)
                            navController.navigate(TopLevelDestination.showDetailOfWallet.name)
                        },
                        amountVisibility = inputWalletState,
                        onClickVisibility = {walletViewModel.updateVisibility(it)}
                    )
                }
                composable(route = TopLevelDestination.addWallet.name) {
                    addWallet(
                        walletUiState = inputWalletState,
                        onSelectType = { walletViewModel.updateWalletType(it) },
                        onWalletAmountChanged = { walletViewModel.updateWalletAmount(it) },
                        onNameChanged = { walletViewModel.updateWalletName(it) },
                        onIconClick = {
                            navController.navigate(TopLevelDestination.pickWalletIcon.name)
                        },
                        onClickColorPicker= {walletViewModel.updateColorPicker(it)},
                        onSelectedColor = {walletViewModel.updateSelectedColor(it)},
                        expandDropDown = {walletViewModel.updateDropDown(it)},
                        onBackClick = {
                            navController.navigateUp()
                            walletViewModel.resetWalletUiState()
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
                composable(route = TopLevelDestination.showDetailOfWallet.name)
                {
                        showWalletDetail(
                            walletData = selectedWallet_detail,
                            modifier = Modifier.fillMaxSize().background(color = inverseOnSurface)
                        )

                }
            }
        }
    }
}





