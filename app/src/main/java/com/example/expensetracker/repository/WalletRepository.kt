package com.example.expensetracker.repository

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.DatastoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val walletDao: WalletDao,
    private val datastoreManager: DatastoreManager
) {
     val initialAmount = datastoreManager.getInitialAmount
    suspend fun addWallet(wallet: Wallet){
        walletDao.addWallet(wallet)
    }

    fun showWallet(): Flow<List<Wallet>>{
        return walletDao.showWallet()
    }
    suspend fun saveInitialAmount(amount:String){
        datastoreManager.saveInitialAmount(amount)
    }

     fun totalBalance():Flow<Float> {
         return walletDao.totalBalance()
     }
}