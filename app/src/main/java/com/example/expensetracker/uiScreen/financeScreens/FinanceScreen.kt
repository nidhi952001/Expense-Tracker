package com.example.expensetracker.uiScreen.financeScreens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.AppColors.inputFieldBackgroundColors
import com.example.expensetracker.ui.theme.AppColors.inputFieldShape
import com.example.expensetracker.ui.theme.AppColors.inputTextSize
import com.example.expensetracker.ui.theme.AppColors.inputTextStyle
import com.example.expensetracker.ui.theme.AppColors.inputTextWeight
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.onBackground
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.uiScreen.uiState.FinanceInputState
import com.example.expensetracker.utils.formatWalletAmount
import com.example.expensetracker.viewModel.CategoryViewModel
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceScreenRoute(
    onClickListOfWallet: (Int) -> Unit,
    onClickListOfCategory: () -> Unit,
    homeViewModel: HomeViewModel,
    onBack: () -> Unit,
    financeViewModel: FinanceViewModel,
    walletViewModel: WalletViewModel,
    categoryViewModel: CategoryViewModel
) {
    val scrollableState = rememberScrollState()
    val inputUiState by financeViewModel.tempFinanceState.collectAsState()
    val selectedFinanceWallet by walletViewModel.selectedWallet.collectAsState(
        initial = null
    )
    val selectedToWallet by walletViewModel.selectedToWallet.collectAsState(initial = null)
    val selectedFinanceType by homeViewModel.selectedFinance.collectAsState()
    val selectedFinanceCategory by categoryViewModel.currentFinanceCategory.collectAsState(initial = null)
    val selectedIncCategory by categoryViewModel.currentIncCategory.collectAsState(initial = null)
    var selectedCategory =
        if (selectedFinanceType.selectedFinance == R.string.expense) selectedFinanceCategory
        else if (selectedFinanceType.selectedFinance == R.string.income) selectedIncCategory
        else 0  //because initially everything will be null

    BackHandler(onBack = {
        financeViewModel.resetUiState()
        categoryViewModel.resetIncCategory()
        categoryViewModel.resetExpCategory()
        onBack()
    })


    val datePickerState = rememberDatePickerState()
    val currentTime = Calendar.getInstance()
    var formattedTime: String = ""
    var modifier = Modifier.fillMaxSize()
    FinanceScreen(
        modifier = modifier,
        scrollableState = scrollableState,
        showDateDialogUI = { financeViewModel.updateDateDialogState(it) },
        inputUiState = inputUiState,
        datePickerState = datePickerState,
        onDateSelected = { financeViewModel.updateSelectedDate(it) },
        onAmountChanged = { financeViewModel.updateFinanceAmount(it) },
        onDescriptionChanged = { financeViewModel.updateFinanceDes(it) },
        selectedCategory = selectedCategory,
        onClickListOfCategory = onClickListOfCategory,
        selectedWallet = selectedFinanceWallet,
        selectedToWallet = selectedToWallet,
        onClickListOfWallet = { onClickListOfWallet(it) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceScreen(
    modifier: Modifier,
    scrollableState: ScrollState,
    showDateDialogUI: (Boolean) -> Unit,
    inputUiState: FinanceInputState,
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    selectedCategory: Int?,
    onClickListOfCategory: () -> Unit,
    selectedWallet: Wallet?,
    onClickListOfWallet: (Int) -> Unit,
    selectedToWallet: Wallet?
) {
    Box(
        modifier = modifier.imePadding().background(color = inverseOnSurface).padding(top = 20.dp)
            .verticalScroll(scrollableState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = surface)
                .padding(horizontal = 30.dp).padding(bottom = 20.dp)
        ) {
            //Date
            label("Date")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(inputFieldShape)
                    .background(color = inputFieldBackgroundColors)
            ) {
                dateUi(
                    showDateDialogUI = { showDateDialogUI(it) },
                    selectedDate = inputUiState.selectedDate
                )
                /*timeUi(
                    showTimeDialogUI = { showTimeDialogUI(it) },
                    selectedTime = expenseUiState.selectedTime
                )*/
            }
            if (inputUiState.showDateDialogUI) {
                showDateDialog(
                    state = datePickerState,
                    onDateSelected = { onDateSelected(it) },
                    onDismiss = { showDateDialogUI(false) })
            }
            financeAmount(
                financeAmount = inputUiState.financeAmount,
                onAmountValueChange = { onAmountChanged(it) })
            financeDescription(
                description = inputUiState.financeDescription,
                onDescriptionChanged = { onDescriptionChanged(it) })
            if (selectedCategory != 0) {  //for expense and income screen only
                category(
                    selectedCat = selectedCategory,
                    onClickListOfCategory = onClickListOfCategory
                )
                fromWallet(
                    selectedFromWallet = selectedWallet,
                    onClickListOfWallet = { onClickListOfWallet(it) }
                )
            } else {
                //for transfer screen
                fromWallet(
                    selectedFromWallet = selectedWallet,
                    onClickListOfWallet = { onClickListOfWallet(it) }
                )
                toWallet(
                    selectedToWallet = selectedToWallet,
                    onClickListOfWallet = { onClickListOfWallet(it) }
                )
            }
        }
    }
}

@Composable
private fun dateUi(showDateDialogUI: (Boolean) -> Unit, selectedDate: Long?) {
    Button(
        onClick = { showDateDialogUI(true) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = onSurface
        ),
        shape = inputFieldShape
    ) {
        Text(
            text = convertMillisToDate(selectedDate ?: System.currentTimeMillis()),
            color = onSurface,
            fontWeight = inputTextWeight,
            fontSize = MaterialTheme.typography.labelLarge.fontSize
        )
    }
}

@Composable
private fun financeAmount(financeAmount: String, onAmountValueChange: (String) -> Unit) {
    label("Amount")
    inputWithLeadingIcon(
        value = financeAmount,
        placeholder = "0",
        leadingIcon = R.drawable.currency_rupee_ui,
        isReadOnly = false,
        onValueChange = { onAmountValueChange(it) },
    )
}

@Composable
private fun financeDescription(description: String, onDescriptionChanged: (String) -> Unit) {
    label("Description")
    inputWithNoIcon(
        value = description,
        placeholder = "Short Description",
        onValueChange = { onDescriptionChanged(it) },
        isReadOnly = false,
    )
}

@Composable
private fun category(selectedCat: Int?, onClickListOfCategory: () -> Unit) {
    val value = if (selectedCat != null) stringResource(selectedCat) else ""
    label("Category")
    inputWithTrailingIcon(
        value = value,
        placeholder = "Select Category",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isEnabled = false,
        onClick = onClickListOfCategory
    )
}

@Composable
private fun fromWallet(selectedFromWallet: Wallet?, onClickListOfWallet: (Int) -> Unit) {
    val annotedString =
        if (selectedFromWallet != null) {
            buildAnnotatedString {
                append(selectedFromWallet?.walletName)
                append("\u0020")
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)
                ) {
                    append("•")
                }
                append("\u0020₹\u0020")
                if (selectedFromWallet != null) {
                    append(formatWalletAmount(selectedFromWallet.walletAmount.toString()))
                }
            }
        } else {
            buildAnnotatedString {
                append("")
            }
        }
    label("Wallet")
    inputWithTrailingIcon(
        value = annotedString.text,
        placeholder = "Select Wallet",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isEnabled = false,
        onClick = { onClickListOfWallet(R.string.fromWallet) }
    )
}
//for transfer screen

@Composable
private fun toWallet(selectedToWallet: Wallet?, onClickListOfWallet: (Int) -> Unit) {
    val annotedString =
        if (selectedToWallet != null) {
            buildAnnotatedString {
                append(selectedToWallet.walletName)
                append("\u0020")
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)
                ) {
                    append("•")
                }
                append("\u0020₹\u0020")
                append(formatWalletAmount(selectedToWallet.walletAmount.toString()))
            }
        } else {
            buildAnnotatedString {
                append("")
            }
        }
    label("Wallet")
    inputWithTrailingIcon(
        value = annotedString.text,
        placeholder = "Select Wallet",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isEnabled = false,
        onClick = { onClickListOfWallet(R.string.toWallet) }
    )
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


