package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.expensetracker.utils.DisplayUIState.transactionDetailByWallet
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun addIncome(income: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expense: Transaction)

    @Query("select sum(transaction_amount) from `Transaction` where transaction_type=:income")
    fun showTotalIncome(income: TransactionType): Flow<Float>

    @Query("Select sum(transaction_amount) from `Transaction` where transaction_type=:expense")
    fun showTotalExpense(expense: TransactionType): Flow<Float>

    @Query("select t.transaction_id , t.transaction_date , t.transaction_amount , t.transaction_description , t.transaction_type ," +
            "w.walletName , c.categoryName , c.categoryIcon , c.categoryColor" +
            " from `transaction` as t inner join wallet as w inner join category as c where " +
            "t.transaction_wallet_id=w.walletId and t.transaction_category_id=c.categoryId " +
            "and t.transaction_date between :firstDayOfMonth and :lastDayOfMonth " +
            "order by t.transaction_date desc")
    fun showExpenseTransaction(firstDayOfMonth: Long, lastDayOfMonth: Long):Flow<List<transactionDetail>>
    @Query("select count(transaction_id) from `transaction` where transaction_wallet_id=:walletId and transaction_type=:expense")
    fun getExpenseCountById(walletId: Int,expense:TransactionType): Flow<Int>

    @Query("select count(transaction_id) from `transaction` where transaction_wallet_id=:walletId and transaction_type=:income")
    fun getIncomeCountById(walletId: Int,income:TransactionType): Flow<Int>
    @Query("select t.transaction_id,count(t.transaction_id) as total_transaction," +
            "sum(t.transaction_amount) as transaction_total_amount," +
            "t.transaction_type,c.categoryName,c.categoryIcon,c.categoryColor" +
            " from `transaction` as t inner join category as c " +
            "where t.transaction_category_id=c.categoryId and t.transaction_wallet_id=:walletId group by c.categoryName")
    fun showTransactionByWallet(walletId: Int): Flow<List<transactionDetailByWallet>>

}