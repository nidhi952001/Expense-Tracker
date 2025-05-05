package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.utils.ListOfIcons
import com.example.expensetracker.utils.WalletStateData
import com.example.expensetracker.utils.listOfWalletIcon
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
            val initialWallet = Wallet(
                walletId = 0,
                walletName = "Cash",
                walletType = TypeOfWallet.GENERAL,
                walletAmount = amountFloat,
                walletIcon = R.drawable.account_wallet_ic,
                walletIconDes = R.string.bank)
            addWallet(initialWallet)
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
    fun updateWalletType(walletType:TypeOfWallet){
        _walletUiState.update {
            it.copy(
                selectType = walletType
            )
        }
    }
    fun updateWalletAmount(walletAmount: String){
        _walletUiState.update {
            it.copy(
                walletAmount = walletAmount
            )
        }
    }

    fun updateSelectedIcon(selectedIcon: Int) {
        _walletUiState.update {
            it.copy(selectedIcon = selectedIcon)
        }
    }

    fun resetWalletUiState() {
        _walletUiState.update {
            it.copy(
                walletName = "",
                 selectType = walletUiState.value.selectType,
                 walletAmount = "",
                 walletIconName =  walletUiState.value.walletIconName,
                 listOfIcon = listOfWalletIcon.iconData,
                 selectedIcon = walletUiState.value.selectedIcon
            )
        }
    }
}