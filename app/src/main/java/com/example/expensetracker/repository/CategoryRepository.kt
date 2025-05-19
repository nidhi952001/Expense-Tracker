package com.example.expensetracker.repository

import android.util.Log
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.entity.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
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

     fun showCategory():Flow<List<Category>> {
        return categoryDao.showCategory()
    }

    fun getCategoryById(categoryId: Int): Flow<Category?> {
        return categoryDao.getCategoryById(categoryId)
    }

    fun updateselectedCategory(selectedCategory: Int) {
        Log.d("DEBUG", "Category Repo updated: $selectedCategory")
        _selectedCategory.value = selectedCategory
    }
}