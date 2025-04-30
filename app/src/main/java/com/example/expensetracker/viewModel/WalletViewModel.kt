package com.example.expensetracker.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository
):ViewModel() {
    fun addWallet(wallet: Wallet){
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.addWallet(wallet)
        }
    }

    fun showWallet(): LiveData<List<Wallet>> {
        return walletRepository.showWallet()
    }

    fun saveInitialAmount(amount: String) {
        val amountFloat: Float = amount.toFloat()
        viewModelScope.launch {
            val wallet = Wallet(0,"cash", TypeOfWallet.GENERAL,amountFloat, Icons.Default.Add.name)
        }

    }
}