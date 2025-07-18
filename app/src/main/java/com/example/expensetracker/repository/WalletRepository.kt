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
    val initialAmountFlow = datastoreManager.getInitialAmount

    private val _selectedWalletId = MutableStateFlow(1)
    val selectedFromWalletId = _selectedWalletId.asStateFlow()

    private val _selectedToWalletId = MutableStateFlow(0)
    val selectedToWalletId = _selectedToWalletId.asStateFlow()
    suspend fun addWallet(wallet: Wallet) {
        walletDao.addWallet(wallet)
    }

    /**
     * Returns a Flow emitting all wallets stored in the database.
     */
    fun getAllWallets(): Flow<List<Wallet>> {
        return walletDao.showWallet()
    }

    /**
     * Returns a Flow emitting the sum of all wallet balances.
     */
    fun totalBalance(): Flow<Float> {
        return walletDao.totalBalance()
    }

    /**
     * Saves the initial wallet amount to DataStore on first app launch.
     */
    suspend fun initializeWalletWithAmount(amount: String) {
        datastoreManager.initializeWalletWithAmount(amount)
    }

    /**
     * Returns a Flow emitting the wallet matching the given ID.
     */
    fun getWalletDataById(walletId: Int): Flow<Wallet?> {
        return walletDao.getWalletDataById(walletId)
    }
    /**
     * Updates the selected wallet ID used in UI state flows.
     */
    fun selectFromWalletById(walletId: Int) {
        _selectedWalletId.value = walletId
    }
    //to wallet from transfer screen
    fun selectToWalletById(walletId: Int){
        _selectedToWalletId.value = walletId
    }
    /**
     * Updates the balance of a wallet with the specified ID.
     */
    suspend fun updateWalletAmount(updateWalletAmount: Float, walletId: Int) {
        walletDao.updateWalletAmount(updateWalletAmount, walletId)
    }

    /**
     * Retrieves the current amount of the wallet by ID.
     */
    suspend fun fetchWalletAmountById(walletId: Int): Float {
        return walletDao.fetchWalletAmountById(walletId)
    }
    /**
     * Updates an existing wallet in the database.
     */
    suspend fun editWallet(wallet: Wallet) {
        walletDao.editWallet(wallet)
    }


}