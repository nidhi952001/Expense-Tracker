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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWallet(wallet: Wallet)

    @Query("select * from wallet")
    fun showWallet():Flow<List<Wallet>>

    @Query("select SUM(walletAmount) from wallet")
    fun totalBalance():Flow<Float>
    @Query("select * from wallet where walletId=:walletID")
    fun getWalletDataById(walletID: Int): Flow<Wallet?>

    @Query("update wallet set walletAmount=:updateWalletAmount where walletId=:walletId")
    suspend fun updateWalletAmount(updateWalletAmount: Float, walletId: Int)
    @Query("select walletAmount from wallet where walletId=:walletId")
    suspend fun getWalletAmountById(walletId: Int): Float

    @Update
    suspend fun updateWallet(wallet:Wallet)
}