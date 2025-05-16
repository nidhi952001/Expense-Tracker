package com.example.expensetracker.entity

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val walletId: Int,
    val walletName: String,
    val walletType: TypeOfWallet,
    val walletAmount: Float,
    @DrawableRes val walletIcon: Int,
    val walletIconDes:Int,
    val walletColor:Color
)

enum class TypeOfWallet {
    General, CreditCard
}
