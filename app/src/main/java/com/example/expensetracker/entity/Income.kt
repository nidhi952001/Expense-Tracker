package com.example.expensetracker.entity

import android.icu.text.DateFormat
import androidx.datastore.preferences.protobuf.Timestamp
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys =
    [ForeignKey
        (
        entity = Category::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("income_category_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.RESTRICT
    ),
        ForeignKey
            (
            entity = Wallet::class,
            parentColumns = arrayOf("walletId"),
            childColumns = arrayOf("income_wallet_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        )]
)
data class Income(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "income_id")
    val incId: Int,
    @ColumnInfo(name = "income_time")
    val incTime: Long?,
    @ColumnInfo(name = "income_date")
    val incDate: Long?,
    @ColumnInfo(name = "income_amount")
    val incAmount: Float,
    @ColumnInfo(name = "income_description")
    val incDescription: String?,
    @ColumnInfo(name = "income_category_id")
    val incCategory: Int,
    @ColumnInfo(name = "income_wallet_id")
    val incWallet: Int
)
