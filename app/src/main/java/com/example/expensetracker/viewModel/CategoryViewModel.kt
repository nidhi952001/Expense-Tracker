package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Category
import com.example.expensetracker.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
):ViewModel(){

    fun addCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.addCategory(category)
        }
    }

    fun showCategory() {
        categoryRepository.showCategory()
    }
}