package com.example.expensetracker.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.ExpenseDao
import com.example.expensetracker.dao.IncomeDao
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.utils.StaticData.listOfCategory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @ApplicationScope
     fun provideApplicationScope():CoroutineScope = CoroutineScope(SupervisorJob() )

    @Provides
    fun provideCallback(
        @ApplicationContext context: Context,
        @ApplicationScope appScope: CoroutineScope
    )= object : RoomDatabase.Callback(){
        override fun onCreate(connection: SQLiteConnection) {
            super.onCreate(connection)
            appScope.launch(Dispatchers.IO) {
                val tempDb = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "app_database"
                ).build()

                val categoryDao = tempDb.categoryDao
                categoryDao.insertAll(listOfCategory.categoryList())
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabaseAccess(@ApplicationContext context: Context,callback:RoomDatabase.Callback): AppDataBase {
        return Room.databaseBuilder(
            context, AppDataBase::class.java, "app_database"
        )
            .fallbackToDestructiveMigration(true)
            .addCallback(callback = callback)
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDataBase: AppDataBase): CategoryDao {
        return appDataBase.categoryDao
    }

    @Provides
    @Singleton
    fun provideWalletDao(appDataBase: AppDataBase): WalletDao {
        return appDataBase.walletDao
    }

    @Provides
    @Singleton
    fun provideIncomeDao(appDataBase: AppDataBase): IncomeDao {
        return appDataBase.incomeDao
    }

    @Provides
    @Singleton
    fun provideExpenseDao(appDataBase: AppDataBase): ExpenseDao {
        return appDataBase.expenseDao
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
