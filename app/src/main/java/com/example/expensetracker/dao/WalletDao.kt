package com.example.expensetracker.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    fun getWalletDataById(walletID: Int): Flow<Wallet>
}