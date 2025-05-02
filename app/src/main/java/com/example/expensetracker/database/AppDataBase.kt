package com.example.expensetracker.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.ExpenseDao
import com.example.expensetracker.dao.IncomeDao
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.Expense
import com.example.expensetracker.entity.Income
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.TypeConvertor

@Database
    (
    entities =
    [Income::class,
        Expense::class,
        Category::class,
        Wallet::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(TypeConvertor::class)
abstract class AppDataBase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val walletDao: WalletDao
    abstract val incomeDao: IncomeDao
    abstract val expenseDao: ExpenseDao
}