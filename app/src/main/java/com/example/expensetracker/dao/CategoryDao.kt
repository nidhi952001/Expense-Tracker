package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)


    @Query("Select * From Category where categoryType=:category")
    fun showCategoryByType(category: CategoryType): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)
    @Query("select categoryName from Category where categoryId=:categoryID")
    fun getCategoryNameById(categoryID: Int): Flow<Int?>


    @Query("select * from category")
    fun getAllCategories():List<Category>
}