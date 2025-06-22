package com.example.transactionensetracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.Wallet

@Entity(
    foreignKeys =
    [ForeignKey
        (
        entity = Category::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("transaction_category_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.RESTRICT
    ),
        ForeignKey
            (
            entity = Wallet::class,
            parentColumns = arrayOf("walletId"),
            childColumns = arrayOf("transaction_from_wallet_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey
            (
            entity = Wallet::class,
            parentColumns = arrayOf("walletId"),
            childColumns = arrayOf("transaction_to_wallet_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        )]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Int,
    @ColumnInfo(name = "transaction_time")
    val transactionTime: Long?,
    @ColumnInfo(name = "transaction_date")
    val transactionDate: Long?,
    @ColumnInfo(name = "transaction_amount")
    val transactionAmount: Float,
    @ColumnInfo(name = "transaction_description")
    val transactionDescription: String?,
    @ColumnInfo(name = "transaction_type")
    val transactionType: TransactionType,
    @ColumnInfo(name = "transaction_category_id")
    val transactionCategory: Int?,
    @ColumnInfo(name = "transaction_from_wallet_id")
    val transactionFromWallet: Int,
    @ColumnInfo(name = "transaction_to_wallet_id")
    val transactionToWallet: Int?
)

enum class TransactionType{
    Income,Expense,TRANSFER
}
