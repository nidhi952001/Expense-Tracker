package com.example.expensetracker.entity

import androidx.datastore.preferences.protobuf.Timestamp
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys =
    [ForeignKey
        (
        entity = Category::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("expense_category_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.RESTRICT
    ),
        ForeignKey
            (
            entity = Wallet::class,
            parentColumns = arrayOf("walletId"),
            childColumns = arrayOf("expense_wallet_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        )]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    val expId: Int,
    @ColumnInfo(name = "expense_time")
    val expTime: Long?,
    @ColumnInfo(name = "expense_date")
    val expDate: Long?,
    @ColumnInfo(name = "expense_amount")
    val expAmount: Float,
    @ColumnInfo(name = "expense_description")
    val expDescription: String?,
    @ColumnInfo(name = "expense_category_id")
    val expCategory: Int,
    @ColumnInfo(name = "expense_wallet_id")
    val expWallet: Int
)
