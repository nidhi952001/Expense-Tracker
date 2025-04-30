package com.example.expensetracker.uiScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.viewModel.CategoryViewModel

@Composable
fun addCategory(categoryViewModel: CategoryViewModel = hiltViewModel()){
    val category = Category(1,"Food", Icons.Default.ArrowBack.name.length, CategoryType.EXPENSE)
    categoryViewModel.addCategory(category)
}