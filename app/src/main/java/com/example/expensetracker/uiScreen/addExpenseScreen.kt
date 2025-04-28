package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.ExpenseViewModel
import com.example.expensetracker.viewModel.WalletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addExpense(expViewModel: ExpenseViewModel = hiltViewModel(),
               categoryViewModel: CategoryViewModel= hiltViewModel(),
               walletViewModel: WalletViewModel = hiltViewModel()
){
    val wallet = Wallet(1,"cash",TypeOfWallet.GENERAL,500F,Icons.Default.Add.name.length)
    walletViewModel.addWallet(wallet)
    val category = Category(1,"Food", Icons.Default.ArrowBack.name.length,CategoryType.EXPENSE)
    categoryViewModel.addCategory(category)
    val expense = Expense(1,LocalTime.now(), LocalDate.now(),200.0F,"testing database",category.categoryId,wallet.walletId)
    expViewModel.addExpense(expense)
}