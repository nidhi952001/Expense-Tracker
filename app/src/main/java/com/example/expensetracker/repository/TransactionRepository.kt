package com.example.expensetracker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.uiScreen.uiState.FinanceInputState
import com.example.expensetracker.uiScreen.uiState.selectedWalletTransactionState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    //sharing across financeViewModel and walletViewModel
    val _tempFinanceState = MutableStateFlow(FinanceInputState())

    suspend fun insertTransaction(transaction: Transaction){
        transactionDao.insertTransaction(transaction)
    }

    fun getTransactionSummaryByType(type: TransactionType, startDate : Long, endDate: Long): Flow<Float> {
        return transactionDao.getTransactionSummaryByType(type,startDate ,endDate)
    }

    fun getTransactionsByDateRange(startDate : Long, endDate: Long):Flow<PagingData<TransactionDetailState>>{
        return Pager(
            config = PagingConfig(pageSize = 10)){
                    transactionDao.getTransactionsByDateRange(startDate ,endDate)
            }.flow
    }

    fun getTransactionCountByWalletAndType(walletId:Int, expense: TransactionType):Flow<Int>{
        return transactionDao.getTransactionCountByWalletAndType(walletId, expense)
    }
    fun getTransferCountById(walletId: Int,transfer:TransactionType):Flow<Int>{
        return transactionDao.getTransferCountById(walletId,transfer)
    }

    fun getWalletTransactionSummary(walletId: Int): Flow<List<selectedWalletTransactionState>> {
        return transactionDao.getWalletTransactionSummary(walletId)
    }

    fun getTransaction_selectedWallet_ByCat(categoryId: Int): Flow<List<TransactionDetailState>> {
        return transactionDao.getTransaction_selectedWallet_ByCat(categoryId)
    }

    fun getTotalAmountForCatByWallet(walletId: Int,categoryId: Int):Flow<Float>{
        return transactionDao.getTotalAmountForCatByWallet(walletId,categoryId)
    }

    fun getTransactionById(selectedTransactionId: Int):Flow<TransactionDetailState?> {
        return transactionDao.getTransactionById(selectedTransactionId)
    }

    suspend fun deleteTransaction(transactionId: Int) {
        transactionDao.deleteTransaction(transactionId)
    }

    fun getTransactionsByCategory(startDate: Long, endDate: Long, selectedCategoryId: Int):Flow<PagingData<TransactionDetailState>>{
        return Pager(
            config = PagingConfig(pageSize = 10)){
            transactionDao.getTransactionsByCategory(startDate,endDate,selectedCategoryId)
        }.flow
    }
}