package com.example.expensetracker.uiScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.listOfCategory
import com.example.expensetracker.viewModel.ExpenseViewModel
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addExpense(
   expenseViewModel: ExpenseViewModel,
){
   /* val expense = Expense(1,LocalTime.now(), LocalDate.now(),200.0F,"testing database",1,1)
    expenseViewModel.addExpense(expense)*/
   val prepopulateCategory = listOfCategory.fetchCategory()
   prepopulateCategory.forEach {
      Log.d("tag", it.categoryType.name)
   }
}