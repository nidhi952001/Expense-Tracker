package com.example.expensetracker.repository

import android.util.Log
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    //state management
    private val _selectedCategory  = MutableStateFlow(0)
    val selectedCategory = _selectedCategory.asStateFlow()


    suspend fun insertCategory(category: Category){
        categoryDao.insertCategory(category)
    }

     fun getCategoriesByType(category:CategoryType):Flow<List<Category>> {
         return categoryDao.getCategoriesByType(category)
             .catch { e ->
                 emit(emptyList())
             }
    }

    fun getCategoryNameById(categoryId: Int): Flow<Int?> {
        return categoryDao.getCategoryNameById(categoryId)
            .catch { e ->
                Log.e("CategoryRepo", "Error fetching category name", e)
                emit(null)
            }
    }

    //this is for expense & income from finance screen
    fun selectCategory(categoryId: Int) {
        //if (_selectedCategory.value != categoryId) {
            _selectedCategory.value = categoryId
       // }
    }
}