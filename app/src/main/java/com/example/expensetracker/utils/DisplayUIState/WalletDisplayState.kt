package com.example.expensetracker.utils.DisplayUIState

import com.example.expensetracker.entity.Wallet
import kotlinx.coroutines.flow.Flow

data class WalletDisplayState(
    val savedWallets: List<Wallet>,
    val totalBalance:Float
)
