package com.example.expensetracker.repository

import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun addCategory(category: Category){
        categoryDao.addCategory(category)
    }

     fun showCategory():Flow<List<Category>> {
        return categoryDao.showCategory()
    }

    fun getCategoryById(categoryId: Int): Flow<Category?> {
        return categoryDao.getCategoryById(categoryId)
    }
}