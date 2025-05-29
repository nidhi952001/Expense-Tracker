package com.example.expensetracker.repository

import com.example.expensetracker.dao.TransactionDao
import com.example.expensetracker.utils.DisplayUIState.transactionDetail
import com.example.expensetracker.utils.DisplayUIState.transactionDetailByWallet
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

    fun showTotalExpense(expense: TransactionType):Flow<Float>{
        return transactionDao.showTotalExpense(expense)
    }

    suspend fun addIncome(income: Transaction){
        transactionDao.addIncome(income)
    }

    fun showTotalIncome(income: TransactionType): Flow<Float> {
        return transactionDao.showTotalIncome(income)
    }

    fun showExpenseTransaction():Flow<List<transactionDetail>>{
        return transactionDao.showExpenseTransaction()
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
}