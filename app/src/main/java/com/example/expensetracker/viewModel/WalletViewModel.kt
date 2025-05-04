package com.example.expensetracker.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.WalletStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
):ViewModel() {
    val initialAmount = walletRepository.initialAmount

    //from database
    val wallets: StateFlow<List<Wallet>> = walletRepository.showWallet()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(),emptyList())


    fun addWallet(wallet: Wallet){
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.addWallet(wallet)
        }
    }

    fun saveInitialAmount(amount: String) {
        val amountFloat: Float = amount.toFloat()
        viewModelScope.launch {
            //saved in datastore
            walletRepository.saveInitialAmount(amount)
            //saved in database
            addWallet(Wallet(0,"Cash", TypeOfWallet.GENERAL,amountFloat,R.drawable.account_wallet_ic,
                R.string.bank_wallet))
        }

    }

    //for the Ui state
    private val _walletUiState = MutableStateFlow(WalletStateData())
    val walletUiState = _walletUiState.asStateFlow()

    fun updateWalletName(newWalletName:String){
        _walletUiState.update {
            it.copy(
                walletName = newWalletName
            )
        }
    }
}