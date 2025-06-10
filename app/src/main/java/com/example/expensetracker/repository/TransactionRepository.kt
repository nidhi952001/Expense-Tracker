package com.example.expensetracker.repository

import androidx.room.Query
import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.utils.DisplayUIState.transactionByDate
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.expensetracker.utils.DisplayUIState.transactionDetailByWallet
import com.example.transactionensetracker.entity.Transaction
import com.example.transactionensetracker.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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

    fun showExpenseTransaction(firstDayOfMonth: Long, lastDayOfMonth: Long):Flow<List<transactionDetail>>{
        return transactionDao.showExpenseTransaction(firstDayOfMonth,lastDayOfMonth)
    }

    fun getExpenseCountById(walletId:Int,expense: TransactionType):Flow<Int>{
        return transactionDao.getExpenseCountById(walletId, expense)
    }

    fun getIncomeCountById(walletId:Int,income: TransactionType):Flow<Int>{
        return transactionDao.getIncomeCountById(walletId, income)
    }

    fun showTransactionByWallet(walletId: Int): Flow<List<transactionDetailByWallet>> {
        return transactionDao.showTransactionByWallet(walletId)
    }

    fun getTransaction_selectedWallet_ByCat(categoryId: Int):StateFlow<List<transactionDetail>> {
        return transactionDao.getTransaction_selectedWallet_ByCat(categoryId)
    }

    suspend fun getTotalAmountForCatByWallet(walletId: Int,categoryId: Int):Float{
        return transactionDao.getTotalAmountForCatByWallet(walletId,categoryId)
    }
}