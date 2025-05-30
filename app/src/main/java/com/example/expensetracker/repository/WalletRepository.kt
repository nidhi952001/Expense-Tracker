package com.example.expensetracker.repository

import com.example.expensetracker.dao.WalletDao
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.DatastoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(
    private val walletDao: WalletDao,
    private val datastoreManager: DatastoreManager
) {
     val initialAmount = datastoreManager.getInitialAmount

    private val _selectedWallet = MutableStateFlow(1)
    val selectedWallet = _selectedWallet.asStateFlow()
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

     fun getWalletDataById(walletID: Int): Flow<Wallet?> {
        return walletDao.getWalletDataById(walletID)
    }

    fun updateSelectedWallet(walletId: Int) {
        _selectedWallet.value = walletId
    }

    suspend fun updateWalletBalance(updateWalletAmount: Float, walletId: Int) {
        walletDao.updateWalletAmount(updateWalletAmount,walletId)
    }

    suspend fun getWalletAmountById(walletId: Int): Float {
        return walletDao.getWalletAmountById(walletId)
    }

    suspend fun editWallet(wallet: Wallet) {
        walletDao.updateWallet(wallet)
    }

}