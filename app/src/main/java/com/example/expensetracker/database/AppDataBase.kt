package com.example.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.TypeConvertor
import com.example.transactionensetracker.entity.Transaction

@Database(
    entities = [
        Transaction::class,
        Category::class,
        Wallet::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(TypeConvertor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "expense_tracker.db"
            )
                .fallbackToDestructiveMigration() // Remove in production after schema stabilization
                .build()
        }
    }
}