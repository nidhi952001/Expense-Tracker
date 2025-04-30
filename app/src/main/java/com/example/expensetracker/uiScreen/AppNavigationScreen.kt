package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.utils.DatastoreManager
import com.example.expensetracker.viewModel.ExpenseViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

enum class TopLevelDestination(@StringRes val route: Int){
    expense(route = R.string.expense),
    income(route = R.string.income),
    transaction(route = R.string.transaction),
    add(route = R.string.add),
    wallet(route = R.string.wallet),
    home(route = R.string.home),
    addName(route = R.string.add_name),
    addInitialAmount(route = R.string.initial_amount)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationScreen(userName:String){
    println("userName $userName")
    //navigation
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //view model
    val expViewModel: ExpenseViewModel = hiltViewModel()
    val walletViewModel: WalletViewModel = hiltViewModel()
    val homeViewModel:HomeViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            AppTopBar(userName,currentRoute,navController)
        },
        bottomBar = {
            AppBottomBar(navController,currentRoute)
        }
    ) { padding->
        NavHost(
            navController = navController,
            startDestination = if(userName.equals("User")) TopLevelDestination.home.name else TopLevelDestination.transaction.name,
            modifier = Modifier.padding(padding)
        ){
            composable(route = TopLevelDestination.home.name){
                homeScreen(onGetStarted = { navController.navigate(TopLevelDestination.addName.name) },
                    modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp))
            }
            composable(route = TopLevelDestination.addName.name){
                addNameScreen(modifier = Modifier.fillMaxSize() , saveUserName = {
                    homeViewModel.saveUserNamee(it)
                    navController.navigate(TopLevelDestination.addInitialAmount.name)
                })
            }
            composable(route = TopLevelDestination.addInitialAmount.name){
                saveInitalMoneyScreen(modifier = Modifier.fillMaxSize() , saveInitialMoney = {
                    walletViewModel.saveInitialAmount(it)
                    navController.navigate(TopLevelDestination.transaction.name)
                })
            }
            composable(route = TopLevelDestination.transaction.name){
                TransactionScreen()
            }
            composable(route = TopLevelDestination.expense.name){
                addExpense(expenseViewModel = expViewModel)
            }
            composable(route = TopLevelDestination.wallet.name){
                addWallet(walletViewModel = walletViewModel)
            }
        }
    }
}



