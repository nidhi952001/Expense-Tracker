package com.example.expensetracker.entity

import androidx.compose.material.icons.Icons
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallet (
    @PrimaryKey(autoGenerate = true)
    val walletId:Int,
    val walletName:String,
    val walletType: TypeOfWallet,
    val initalAmount:Float,
    val walletIcon: Int
)

enum class TypeOfWallet{
   GENERAL, CREDITCARD
}
