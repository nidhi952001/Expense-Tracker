package com.example.expensetracker.repository

import android.util.Log
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    private val _selectedCategory  = MutableStateFlow(0)
    val selectedCategory = _selectedCategory.asStateFlow()
    suspend fun addCategory(category: Category){
        categoryDao.addCategory(category)
    }

     fun showCategoryByType(category:CategoryType):Flow<List<Category>> {
        return categoryDao.showCategoryByType(category)
    }

    fun getCategoryNameById(categoryId: Int): Flow<Int?> {
        return categoryDao.getCategoryNameById(categoryId)
    }

    //this is for expense screen
    fun updateselectedCategory(selectedCategory: Int) {
        Log.d("DEBUG", "Category Repo updated: $selectedCategory")
        _selectedCategory.value = selectedCategory
    }
}