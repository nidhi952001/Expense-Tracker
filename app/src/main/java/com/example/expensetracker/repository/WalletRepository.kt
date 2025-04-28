package com.example.expensetracker.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Wallet
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val walletDao: WalletDao
) {
    suspend fun addWallet(wallet: Wallet){
        walletDao.addWallet(wallet)
    }

    fun showWallet(): LiveData<List<Wallet>>{
        return walletDao.showWallet()
    }
}