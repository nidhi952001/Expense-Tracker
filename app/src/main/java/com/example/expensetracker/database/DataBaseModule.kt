package com.example.expensetracker.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.dao.CategoryDao
import com.example.expensetracker.dao.TransactionDao
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
        @ApplicationScope appScope: CoroutineScope
    ): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // use the db created via Room.builder by providing a lambda to the builder
                appScope.launch(Dispatchers.IO) {
                    // this is safe now; will be the correct db instance
                    AppDataBase.INSTANCE?.categoryDao?.insertAll(listOfCategory.categoryList())
                }
            }
        }
    }


    @Provides
    @Singleton
    fun provideDatabaseAccess(
        @ApplicationContext context: Context,
        @ApplicationScope appScope: CoroutineScope
    ): AppDataBase {
        val db = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "app_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    appScope.launch(Dispatchers.IO) {
                        AppDataBase.INSTANCE?.let { database ->
                            if(database.categoryDao.getAllCategories().isEmpty())
                                database.categoryDao.insertAll(listOfCategory.categoryList())
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

        AppDataBase.INSTANCE = db
        return db
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
    fun provideTransactionDao(appDataBase: AppDataBase): TransactionDao {
        return appDataBase.transactionDao
    }

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
