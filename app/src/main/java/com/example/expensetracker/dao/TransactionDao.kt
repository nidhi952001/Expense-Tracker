package com.example.expensetracker.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.uiScreen.uiState.selectedWalletTransactionState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    private companion object {
        const val BASE_TRANSACTION_DETAIL_QUERY = """
            SELECT 
                t.transaction_id as transactionId,
                t.transaction_date as transactionDate,
                t.transaction_amount as transactionAmount,
                t.transaction_description as transactionDescription,
                t.transaction_type as transactionType,
                t.transaction_from_wallet_id as fromWalletId,
                w1.walletName as fromWalletName,
                t.transaction_to_wallet_id as toWalletId,
                w2.walletName as toWalletName,
                c.categoryId as categoryId,
                c.categoryName,
                c.categoryIcon,
                c.categoryColor
            FROM `transaction` as t
            INNER JOIN wallet as w1 ON t.transaction_from_wallet_id = w1.walletId
            LEFT JOIN wallet as w2 ON t.transaction_to_wallet_id = w2.walletId
            LEFT JOIN category as c ON t.transaction_category_id = c.categoryId
        """
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("select sum(transaction_amount) from `Transaction` where transaction_type=:type and " +
                "transaction_date between :startDate and :endDate")
    fun getTransactionSummaryByType(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<Float>

    @Query("$BASE_TRANSACTION_DETAIL_QUERY WHERE t.transaction_date BETWEEN :startDate AND :endDate ORDER BY t.transaction_date DESC")
    fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): PagingSource<Int, TransactionDetailState>

    @Query("select count(transaction_id) from `transaction` where transaction_from_wallet_id=:walletId and transaction_type=:type")
    fun getTransactionCountByWalletAndType(walletId: Int, type: TransactionType): Flow<Int>

    @Query("select count(transaction_id) from `transaction` where (transaction_to_wallet_id=:walletId or transaction_from_wallet_id=:walletId) and transaction_type=:transfer")
    fun getTransferCountById(walletId: Int, transfer: TransactionType): Flow<Int>

    @Query(
        """
        select t.transaction_id as transactionId , 
            t.transaction_from_wallet_id as transactionFromWalletId, 
            t.transaction_to_wallet_id as transactionToWalletId,
            count(t.transaction_id) as totalTransaction,
            sum(t.transaction_amount) as transactionTotalAmount,
            t.transaction_type as transactionType,
            c.categoryId,c.categoryName,c.categoryIcon,c.categoryColor
            from `transaction` as t 
            inner join category as c ON t.transaction_category_id=c.categoryId
            where (t.transaction_from_wallet_id=:walletId or t.transaction_to_wallet_id=:walletId)
            group by c.categoryName,t.transaction_type"""
    )
    fun getWalletTransactionSummary(walletId: Int): Flow<List<selectedWalletTransactionState>>

    @Query("$BASE_TRANSACTION_DETAIL_QUERY where c.categoryId=:category_id order by t.transaction_date desc")
    fun getTransaction_selectedWallet_ByCat(category_id: Int): Flow<List<TransactionDetailState>>

    @Query("select sum(transaction_amount) from `transaction` where transaction_from_wallet_id=:walletId and transaction_category_id=:category_id")
    fun getTotalAmountForCatByWallet(walletId: Int, category_id: Int): Flow<Float>

    @Query("$BASE_TRANSACTION_DETAIL_QUERY where t.transaction_id=:selectedTransactionId")
    fun getTransactionById(selectedTransactionId: Int): Flow<TransactionDetailState?>

    @Query("delete from `transaction` where transaction_id=:transactionId")
    suspend fun deleteTransaction(transactionId: Int?)


    @Query("""$BASE_TRANSACTION_DETAIL_QUERY where t.transaction_date between :startDate and :endDate 
        and c.categoryId=:selectedCategoryId order by t.transaction_date desc"""
    )
    fun getTransactionsByCategory(
        startDate: Long,
        endDate: Long,
        selectedCategoryId: Int
    ): PagingSource<Int, TransactionDetailState>
}