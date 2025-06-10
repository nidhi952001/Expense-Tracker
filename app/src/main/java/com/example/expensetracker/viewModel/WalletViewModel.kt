package com.example.expensetracker.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.uiScreen.formatAmount
import com.example.expensetracker.utils.DisplayUIState.WalletDetailState
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.DisplayUIState.transactionByCatForSelectedWallet
import com.example.expensetracker.utils.DisplayUIState.transactionByDate
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.expensetracker.utils.StaticData.listOfWalletColor
import com.example.expensetracker.utils.StaticData.listOfWalletColor.coloCodeToColor
import com.example.expensetracker.utils.StaticData.listOfWalletIcon
import com.example.transactionensetracker.entity.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    val initialAmount = walletRepository.initialAmount

    //from database
    val savedWallets: StateFlow<List<Wallet>> = walletRepository.showWallet()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val showBalance: StateFlow<Float> = walletRepository.totalBalance().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0F
    )

    val persistedWalletState: StateFlow<WalletDisplayState> =
        combine(savedWallets, showBalance) { wallet, balance ->
            WalletDisplayState(wallet, balance)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            WalletDisplayState(emptyList(), 0F)
        )


    fun addWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.addWallet(wallet)
        }
    }

    fun saveInitialAmount(amount: String) {
        val amountFloat: Float = amount.toFloat()
        viewModelScope.launch {
            //saved in datastore
            walletRepository.saveInitialAmount(amount)
            val initialWallet = Wallet(
                walletId = 0,
                walletName = "Cash",
                walletType = TypeOfWallet.General,
                walletAmount = amountFloat,
                walletIcon = R.drawable.account_wallet_ic,
                walletIconDes = R.string.bank,
                walletColor = coloCodeToColor.getValue("sky_blue")
            )
            addWallet(initialWallet)
        }

    }

    //for the Ui state
    private val _tempWalletState = MutableStateFlow(WalletInputState())
    val tempWalletState = _tempWalletState.asStateFlow()

    fun updateWalletName(newWalletName: String) {
        if (!newWalletName.isNullOrEmpty() || newWalletName.isNotBlank()) {
            _tempWalletState.update {
                it.copy(
                    isWalletNameValid = true
                )
            }
        } else {
            _tempWalletState.update {
                it.copy(
                    isWalletNameValid = false
                )
            }
        }
        _tempWalletState.update {
            it.copy(
                walletName = newWalletName
            )
        }
    }

    fun updateWalletType(walletType: TypeOfWallet) {
        _tempWalletState.update {
            it.copy(
                selectType = walletType
            )
        }
    }

    fun updateWalletAmount(walletAmount: String) {
        if (!walletAmount.isNullOrEmpty() || walletAmount.isNotBlank()) {
            _tempWalletState.update {
                it.copy(
                    isWalletAmountValid = true
                )
            }
        } else {
            _tempWalletState.update {
                it.copy(
                    isWalletAmountValid = false
                )
            }
        }
        _tempWalletState.update {
            it.copy(
                walletAmount = walletAmount
            )
        }
    }

    fun updateSelectedIcon(selectedIcon: Int) {
        _tempWalletState.update {
            it.copy(selectedIcon = selectedIcon)
        }
    }

    fun updateSelectedColor(selectedColor: Color) {
        _tempWalletState.update {
            it.copy(selectedColors = selectedColor, showColorPicker = false)
        }
    }

    fun updateColorPicker(colorPicker: Boolean) {
        _tempWalletState.update {
            it.copy(showColorPicker = colorPicker)
        }
    }

    fun resetWalletUiState() {
        _tempWalletState.update {
            it.copy(
                walletName = "",
                isWalletNameValid = false,
                selectType = TypeOfWallet.General,
                isWalletTypeExpanded = false,
                walletAmount = "",
                isWalletAmountValid = false,
                walletIconName = R.string.bank,
                listOfIcon = listOfWalletIcon.iconData,
                selectedIcon = R.drawable.account_wallet_ic,
                showListOfColor = coloCodeToColor,
                selectedColors = coloCodeToColor.getValue("sky_blue"),
                showColorPicker = false,
                onEditWalletClick = false
            )
        }
    }


    fun saveIntoWallet() {
        val newWallet = Wallet(
            walletId = 0,
            walletName = _tempWalletState.value.walletName,
            walletType = _tempWalletState.value.selectType,
            walletAmount = _tempWalletState.value.walletAmount.toFloat(),
            walletIcon = _tempWalletState.value.selectedIcon,
            walletIconDes = _tempWalletState.value.walletIconName,
            walletColor = _tempWalletState.value.selectedColors
        )
        addWallet(newWallet)
        resetWalletUiState()
    }

    fun updateDropDown(dropDownState: Boolean) {
        _tempWalletState.update {
            it.copy(isWalletTypeExpanded = dropDownState)
        }

    }

    fun updateSelectedExpWallet(walletId: Int) {
        _tempWalletState.update {
            it.copy(selectedExpWalletId = walletId)
        }
        walletRepository.updateSelectedWallet(walletId)
    }

    val selectedWallet = _tempWalletState.map {
        it.selectedExpWalletId
    }.distinctUntilChanged().flatMapLatest {
        walletRepository.getWalletDataById(it)
    }

    //this is for the wallet screen , in wallet screen , click on one wallet
    fun getSelectedWalletData(walletId: Int) {
        _tempWalletState.update {
            it.copy(
                selectedWalletId_detail = walletId
            )
        }
    }

    //based on select wallet from wallet screen , show the details of wallet
    val selectedWallet_detail = _tempWalletState.map {
        it.selectedWalletId_detail
    }.distinctUntilChanged().flatMapLatest {
        walletRepository.getWalletDataById(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val countSelectedWallet_expense = _tempWalletState.map {
        it.selectedWalletId_detail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.getExpenseCountById(it, TransactionType.Expense)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)


    val countselectedwalletIncome = _tempWalletState.map {
        it.selectedWalletId_detail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.getIncomeCountById(it, TransactionType.Income)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val transactionByWallet = _tempWalletState.map {
        it.selectedWalletId_detail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.showTransactionByWallet(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val getSelectedWalletDetails: StateFlow<WalletDetailState> =
        combine(
            selectedWallet_detail,
            countSelectedWallet_expense,
            countselectedwalletIncome,
            transactionByWallet
        )
        { wallet, countExp, countInc, transaction ->
            WalletDetailState(wallet, countExp, countInc, transaction)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            WalletDetailState(null, 0, 0, emptyList())
        )

    fun showWallet_edit() {
        _tempWalletState.update {
            it.copy(
                walletId = selectedWallet_detail.value?.walletId ?: 0,
                walletName = selectedWallet_detail.value?.walletName.toString(),
                walletAmount = formatAmount(selectedWallet_detail.value?.walletAmount.toString()),
                isWalletNameValid = true,
                isWalletAmountValid = true,
                showListOfColor = coloCodeToColor,
                selectedColors = selectedWallet_detail.value!!.walletColor,
                showColorPicker = false,
                walletIconName = selectedWallet_detail.value!!.walletIconDes,
                listOfIcon = listOfWalletIcon.iconData,
                selectedIcon = selectedWallet_detail.value!!.walletIcon,
                onEditWalletClick = true
            )
        }
    }

    fun resetOnEditClick() {
        _tempWalletState.update {
            it.copy(
                onEditWalletClick = false
            )
        }
    }

    fun editWallet() {
        resetOnEditClick()
        val wallet = Wallet(
            walletId = _tempWalletState.value.walletId,
            walletName = _tempWalletState.value.walletName,
            walletType = _tempWalletState.value.selectType,
            walletAmount = _tempWalletState.value.walletAmount.toFloat(),
            walletIcon = _tempWalletState.value.selectedIcon,
            walletIconDes = _tempWalletState.value.walletIconName,
            walletColor = _tempWalletState.value.selectedColors
        )
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.editWallet(wallet)

        }
    }

    fun updateVisibility(visibility: Boolean) {
        _tempWalletState.update {
            it.copy(
                hideBalance = visibility
            )
        }
    }

    private val _tempCatForWallet = MutableStateFlow(transactionByCatForSelectedWallet())
    val tempCatForWallet = _tempCatForWallet.asStateFlow()
    fun selectedCategoryForWallet(categoryId: Int) {
        _tempCatForWallet.update {
            it.copy(selectedCatForWallet = categoryId)
        }
    }

    val _catTransactionForWallet =
        _tempCatForWallet.map {
            it.selectedCatForWallet
            transactionRepository.getTransaction_selectedWallet_ByCat(it.selectedCatForWallet)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    /*val transactionGroupByDate: StateFlow<List<transactionByDate>> =
        _catTransactionForWallet.map { transactionRepository.transformByDate(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())*/
}