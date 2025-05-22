package com.example.expensetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.TypeConvertor
import com.example.transactionensetracker.entity.Transaction

@Database
    (
    entities =
    [Transaction::class,
        Category::class,
        Wallet::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(TypeConvertor::class)
abstract class AppDataBase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val walletDao: WalletDao
    abstract val transactionDao: TransactionDao
}