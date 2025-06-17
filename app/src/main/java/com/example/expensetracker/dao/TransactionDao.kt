package com.example.expensetracker.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.uiScreen.uiState.TransactionDetailSelectedWalletState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun addIncome(income: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expense: Transaction)

    @Query("select sum(transaction_amount) from `Transaction` where transaction_type=:income and " +
            "transaction_date between :firstDayOfMonth and :lastDayOfMonth")
    fun showTotalIncome(income: TransactionType, firstDayOfMonth: Long, lastDayOfMonth: Long): Flow<Float>

    @Query("Select sum(transaction_amount) from `Transaction` where transaction_type=:expense and " +
            "transaction_date between :firstDayOfMonth and :lastDayOfMonth")
    fun showTotalExpense(expense: TransactionType, firstDayOfMonth: Long, lastDayOfMonth: Long): Flow<Float>

    @Query("select t.transaction_id as transactionId, t.transaction_date as transactionDate , " +
            "t.transaction_amount as transactionAmount , t.transaction_description as transactionDescription , " +
            "t.transaction_type as transactionType," +
            "w.walletName , c.categoryName , c.categoryIcon , c.categoryColor" +
            " from `transaction` as t inner join wallet as w inner join category as c where " +
            "t.transaction_wallet_id=w.walletId and t.transaction_category_id=c.categoryId " +
            "and t.transaction_date between :firstDayOfMonth and :lastDayOfMonth " +
            "order by t.transaction_date desc")
    fun showExpenseTransaction(firstDayOfMonth: Long, lastDayOfMonth: Long): PagingSource<Int, TransactionDetailState>
    @Query("select count(transaction_id) from `transaction` where transaction_wallet_id=:walletId and transaction_type=:expense")
    fun getExpenseCountById(walletId: Int,expense:TransactionType): Flow<Int>

    @Query("select count(transaction_id) from `transaction` where transaction_wallet_id=:walletId and transaction_type=:income")
    fun getIncomeCountById(walletId: Int,income:TransactionType): Flow<Int>
    @Query("select t.transaction_id as transactionId,count(t.transaction_id) as totalTransaction," +
            "sum(t.transaction_amount) as transactionTotalAmount," +
            "t.transaction_type as transactionType,c.categoryId,c.categoryName,c.categoryIcon,c.categoryColor" +
            " from `transaction` as t inner join category as c " +
            "where t.transaction_category_id=c.categoryId and t.transaction_wallet_id=:walletId group by c.categoryName,t.transaction_type")
    fun showTransactionByWallet(walletId: Int): Flow<List<TransactionDetailSelectedWalletState>>

    @Query("select t.transaction_id as transactionId, t.transaction_date as transactionDate , t.transaction_amount as transactionAmount , t.transaction_description , t.transaction_type as transactionType ," +
        "w.walletName , c.categoryName , c.categoryIcon , c.categoryColor" +
                " from `transaction` as t inner join wallet as w inner join category as c where " +
                "t.transaction_wallet_id=w.walletId and t.transaction_category_id=c.categoryId " +
                "and c.categoryId=:category_id order by t.transaction_date desc")
     fun getTransaction_selectedWallet_ByCat(category_id: Int):Flow<List<TransactionDetailState>>

    @Query("select sum(transaction_amount) from `transaction` where transaction_wallet_id=:walletId and transaction_category_id=:category_id")
    fun getTotalAmountForCatByWallet(walletId: Int,category_id: Int):Flow<Float>
}