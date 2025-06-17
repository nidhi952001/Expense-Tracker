package com.example.expensetracker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.uiScreen.uiState.TransactionDetailSelectedWalletState
import com.example.expensetracker.uiScreen.uiState.TransactionDetailState
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    suspend fun addExpense(transaction: Transaction){
        transactionDao.addExpense(transaction)
    }

    fun showTotalExpense(expense: TransactionType, firstDayOfMonth: Long, lastDayOfMonth: Long):Flow<Float>{
        return transactionDao.showTotalExpense(expense,firstDayOfMonth,lastDayOfMonth)
    }

    suspend fun addIncome(income: Transaction){
        transactionDao.addIncome(income)
    }

    fun showTotalIncome(income: TransactionType, firstDayOfMonth: Long, lastDayOfMonth: Long): Flow<Float> {
        return transactionDao.showTotalIncome(income,firstDayOfMonth,lastDayOfMonth)
    }

    fun showTransaction(firstDayOfMonth: Long, lastDayOfMonth: Long):Flow<PagingData<TransactionDetailState>>{
        return Pager(
            config = PagingConfig(pageSize = 10)){
                    transactionDao.showExpenseTransaction(firstDayOfMonth,lastDayOfMonth)
            }.flow
    }

    fun getExpenseCountById(walletId:Int,expense: TransactionType):Flow<Int>{
        return transactionDao.getExpenseCountById(walletId, expense)
    }

    fun getIncomeCountById(walletId:Int,income: TransactionType):Flow<Int>{
        return transactionDao.getIncomeCountById(walletId, income)
    }

    fun showTransactionByWallet(walletId: Int): Flow<List<TransactionDetailSelectedWalletState>> {
        return transactionDao.showTransactionByWallet(walletId)
    }

    fun getTransaction_selectedWallet_ByCat(categoryId: Int): Flow<List<TransactionDetailState>> {
        return transactionDao.getTransaction_selectedWallet_ByCat(categoryId)
    }

    fun getTotalAmountForCatByWallet(walletId: Int,categoryId: Int):Flow<Float>{
        return transactionDao.getTotalAmountForCatByWallet(walletId,categoryId)
    }
}