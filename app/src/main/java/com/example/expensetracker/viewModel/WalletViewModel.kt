package com.example.expensetracker.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.repository.WalletRepository
import com.example.expensetracker.uiScreen.walletScreens.formatWalletAmount
import com.example.expensetracker.uiScreen.uiState.WalletDetailState
import com.example.expensetracker.uiScreen.uiState.WalletDisplayState
import com.example.expensetracker.uiScreen.uiState.OverViewDisplayState
import com.example.expensetracker.uiScreen.uiState.TransactionForSelectedWalletCategoryState
import com.example.expensetracker.uiScreen.uiState.TransactionByDateState
import com.example.expensetracker.uiScreen.uiState.WalletInputState
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.expensetracker.utils.StaticData.listOfWalletColor.coloCodeToColor
import com.example.expensetracker.utils.StaticData.listOfWalletIcon
import com.example.expensetracker.utils.transformByDate
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
/**
 * ViewModel for managing Wallet data and exposing UI state.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val initialAmount = walletRepository.initialAmountFlow
    //saved initial amount when user first time open the app
    fun initializeWalletWithAmount(amount: String) {
        val amountFloat: Float = amount.toFloat()
        viewModelScope.launch {
            //saved in datastore
            walletRepository.initializeWalletWithAmount(amount)
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

    //Flow of wallet list retrieved from the database via the repository.
    val allWallets: StateFlow<List<Wallet>> = walletRepository.getAllWallets()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    //Flow of total balance across all wallets.
    val totalAllWalletBalance: StateFlow<Float> = walletRepository.totalBalance().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0F
    )

    //Combined UI state used for displaying both the wallet list and total balance.
    val walletUiState: StateFlow<WalletDisplayState> =
        combine(allWallets, totalAllWalletBalance) { wallet, balance ->
            WalletDisplayState(wallet, balance)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            WalletDisplayState(emptyList(), 0F)
        )


    //add wallet
    fun addWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.addWallet(wallet)
        }
    }

    //for the Ui state , when user add or update the wallet
    private val _walletInputState = MutableStateFlow(WalletInputState())
    val walletInputState = _walletInputState.asStateFlow()

    fun updateWalletName(newWalletName: String) {
        _walletInputState.update {
            it.copy(
                walletName = newWalletName,
                isWalletNameValid = newWalletName.isNotBlank()
            )
        }
    }

    fun updateWalletType(walletType: TypeOfWallet) {
        _walletInputState.update {
            it.copy(
                selectType = walletType
            )
        }
    }

    fun updateWalletAmount(walletAmount: String) {
        _walletInputState.update {
            it.copy(
                isWalletAmountValid = walletAmount.isNotBlank(),
                walletAmount = walletAmount
            )
        }
    }

    fun setColorPickerVisibility(colorPicker: Boolean) {
        _walletInputState.update {
            it.copy(showColorPicker = colorPicker)
        }
    }

    fun selectWalletColor(selectedColor: Color) {
        _walletInputState.update {
            it.copy(selectedColors = selectedColor, showColorPicker = false)
        }
    }

    fun selectWalletIcon(selectedIcon: Int) {
        _walletInputState.update {
            it.copy(selectedIcon = selectedIcon)
        }
    }

    //when user go back or save/update the wallet reset all the field
    fun clearWalletInputState() {
        _walletInputState.update {
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
                isEditWalletClicked = false
            )
        }
    }


    fun saveNewWallet() {
        val newWallet = Wallet(
            walletId = 0,
            walletName = _walletInputState.value.walletName,
            walletType = _walletInputState.value.selectType,
            walletAmount = _walletInputState.value.walletAmount.toFloat(),
            walletIcon = _walletInputState.value.selectedIcon,
            walletIconDes = _walletInputState.value.walletIconName,
            walletColor = _walletInputState.value.selectedColors
        )
        addWallet(newWallet)
        clearWalletInputState()
    }

    //user inside add wallet screen and selecting the drop down
    fun updateDropDown(dropDownState: Boolean) {
        _walletInputState.update {
            it.copy(isWalletTypeExpanded = dropDownState)
        }

    }

    //in expense / income screen user selected wallet
    fun updateSelectedWallet(walletId: Int) {
        _walletInputState.update {
            it.copy(selectedFinanceWalletId = walletId)
        }
        walletRepository.selectWalletById(walletId)
    }

    //expense/income screen based on user selected wallet , show wallet amount
    val selectedWallet = _walletInputState.map {
        it.selectedFinanceWalletId
    }.distinctUntilChanged().flatMapLatest {
        walletRepository.getWalletDataById(it)
    }

    //this is for the wallet screen , in wallet screen , click on one wallet
    fun getSelectedWalletData(walletId: Int) {
        _walletInputState.update {
            it.copy(
                selectedwalletIdDetail = walletId
            )
        }
    }

    //based on select wallet from wallet screen , show the details of wallet
    val selectedwalletDetail = _walletInputState.map {
        it.selectedwalletIdDetail
    }.distinctUntilChanged().flatMapLatest {
        walletRepository.getWalletDataById(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    //total expense done by selected wallet - wallet detail screen
    val selectedWalletExpenseCount = _walletInputState.map {
        it.selectedwalletIdDetail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.getExpenseCountById(it, TransactionType.Expense)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //total income for selected wallet - wallet detail screen
    val selectedWalletIncomeCount = _walletInputState.map {
        it.selectedwalletIdDetail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.getIncomeCountById(it, TransactionType.Income)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //transaction from selected wallet - wallet detail screen
    val transactionsForSelectedWallet = _walletInputState.map {
        it.selectedwalletIdDetail
    }.distinctUntilChanged().flatMapLatest {
        transactionRepository.showTransactionByWallet(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    /**combine
    ** selected wallet , count of expense done by wallet ,
    ** income for selected wallet , transaction by category from selected wallet
    ** - detail wallet screen
    **/
    val walletDetailUiState: StateFlow<WalletDetailState> =
        combine(
            selectedwalletDetail,
            selectedWalletExpenseCount,
            selectedWalletIncomeCount,
            transactionsForSelectedWallet
        )
        { wallet, countExp, countInc, transaction ->
            WalletDetailState(wallet, countExp, countInc, transaction)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            WalletDetailState(null, 0, 0, emptyList())
        )

    //from wallet detail screen , clicked on edit icon, show collect all the detail of wallet
    fun populateWalletEditState() {
        _walletInputState.update {
            it.copy(
                walletId = selectedwalletDetail.value?.walletId ?: 0,
                walletName = selectedwalletDetail.value?.walletName.toString(),
                walletAmount = formatWalletAmount(selectedwalletDetail.value?.walletAmount.toString()),
                isWalletNameValid = true,
                isWalletAmountValid = true,
                showListOfColor = coloCodeToColor,
                selectedColors = selectedwalletDetail.value?.walletColor
                    ?: coloCodeToColor.getValue("sky_blue"),
                showColorPicker = false,
                walletIconName = selectedwalletDetail.value?.walletIconDes ?: R.string.bank,
                listOfIcon = listOfWalletIcon.iconData,
                selectedIcon = selectedwalletDetail.value?.walletIcon ?: R.drawable.account_wallet_ic,
                isEditWalletClicked = true
            )
        }
    }

    //from edit wallet screen user clicked back button
    fun clearEditMode() {
        _walletInputState.update {
            it.copy(
                isEditWalletClicked = false
            )
        }
    }

    //user want to edit the existing wallet
    fun editWallet() {
        clearEditMode()
        val wallet = Wallet(
            walletId = _walletInputState.value.walletId,
            walletName = _walletInputState.value.walletName,
            walletType = _walletInputState.value.selectType,
            walletAmount = _walletInputState.value.walletAmount.toFloat(),
            walletIcon = _walletInputState.value.selectedIcon,
            walletIconDes = _walletInputState.value.walletIconName,
            walletColor = _walletInputState.value.selectedColors
        )
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.editWallet(wallet)

        }
    }

    //update visibility of wallet amount
    fun updateVisibility(visibility: Boolean) {
        _walletInputState.update {
            it.copy(
                hideBalance = visibility
            )
        }
    }

    //in wallet detail screen on click on transaction
    private val _walletCategoryFilterState = MutableStateFlow(
        TransactionForSelectedWalletCategoryState()
    )
    fun selectedCategoryForWallet(categoryId: Int) {
        _walletCategoryFilterState.update {
            it.copy(selectedCategoryForWallet = categoryId)
        }
    }

    //selected category from the selected wallet transaction - wallet detail screen
    val transactionsForSelectedCategory =
        _walletCategoryFilterState.map {
            transactionRepository.getTransaction_selectedWallet_ByCat(it.selectedCategoryForWallet)
        }.distinctUntilChanged().flatMapLatest {
            it
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //apply date wise filter to show data in ui
    val transactionGroupByDate: StateFlow<List<TransactionByDateState>> =
        transactionsForSelectedCategory.map { transformByDate(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //show total balance for selected wallet and selected category - next screen of wallet detail screen
    val totalAmountForSelectedWalletAndCategory =
        _walletCategoryFilterState.map {
            it.selectedCategoryForWallet
            transactionRepository.getTotalAmountForCatByWallet(
                _walletInputState.value.selectedwalletIdDetail,
                _walletCategoryFilterState.value.selectedCategoryForWallet)
        }.distinctUntilChanged().flatMapLatest {
            it
        }

    val walletOverviewState = totalAmountForSelectedWalletAndCategory.map {
        OverViewDisplayState(
            total = it,
            isLoading = true,
            showAll = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), OverViewDisplayState())


}