package com.example.expensetracker.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.ExpenseDao
import com.example.expensetracker.dao.IncomeDao
import com.example.expensetracker.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun  provideDatabaseAccess(@ApplicationContext context: Context):AppDataBase{
       return Room.databaseBuilder(
           context,AppDataBase::class.java,"app_database")
           .fallbackToDestructiveMigration(true)
           .build()
    }
    @Provides
    @Singleton
    fun provideCategoryDao(appDataBase: AppDataBase):CategoryDao{
        return appDataBase.categoryDao
    }

    @Provides
    @Singleton
    fun provideWalletDao(appDataBase: AppDataBase): WalletDao {
        return appDataBase.walletDao
    }

    @Provides
    @Singleton
    fun provideIncomeDao(appDataBase: AppDataBase):IncomeDao{
        return appDataBase.incomeDao
    }

    @Provides
    @Singleton
    fun provideExpenseDao(appDataBase: AppDataBase): ExpenseDao {
        return appDataBase.expenseDao
    }
}