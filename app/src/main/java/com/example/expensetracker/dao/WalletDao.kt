package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.entity.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    /**
     * Retrieves all wallet entries from the database.
     *
     * @return A Flow emitting the list of wallets.
     */
    @Query("select * from wallet")
    fun getAllWallets():Flow<List<Wallet>>

    /**
     * Calculates the total sum of all wallet balances.
     *
     * @return A Flow emitting the sum as a Float.
     */
    @Query("select IFNULL(SUM(walletAmount),0) from wallet")
    fun getTotalBalance():Flow<Float>

    /**
     * add wallet into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    @Query("select * from wallet where walletId=:walletID")
    fun getWalletById(walletID: Int): Flow<Wallet?>

    @Query("update wallet set walletAmount=:updateWalletAmount where walletId=:walletId")
    suspend fun updateWalletAmount(updateWalletAmount: Float, walletId: Int)
    @Query("select walletAmount from wallet where walletId=:walletId")
    suspend fun getWalletAmount(walletId: Int): Float

    @Update
    suspend fun updateWallet(wallet:Wallet)

}