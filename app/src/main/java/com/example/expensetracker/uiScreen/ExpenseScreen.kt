package com.example.expensetracker.uiScreen

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
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
import androidx.core.text.buildSpannedString
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
import com.example.expensetracker.viewModel.ExpenseIncomeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseIncomeScreen(
    modifier: Modifier,
    showDateDialogUI: (Boolean) -> Unit,
    onDateSelected: (Long?) -> Unit,
    inputUiState: ExpenseIncomeInputState,
    onExpenseAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onClickListOfWallet: () -> Unit,
    selectedExpWallet: Wallet?,
    onClickListOfCategory: () -> Unit,
    selectedCategory: Int?,
    onBack: () -> Unit
) {

    BackHandler(onBack = onBack)
    val scrollableState = rememberScrollState()
    val expIncViewModel: ExpenseIncomeViewModel = hiltViewModel()


    val datePickerState = rememberDatePickerState()
    val currentTime = Calendar.getInstance()
    var formattedTime:String = ""

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
            expenseAmount(
                expenseAmount = inputUiState.expIncAmount,
                onExpenseValueChange = { onExpenseAmountChanged(it) })
            expenseDescription(
                description = inputUiState.expIncDescription,
                onDescriptionChanged = { onDescriptionChanged(it) })
            category(
                selectedCat = selectedCategory,
                onClickListOfCategory = onClickListOfCategory
            )
            expenseWallet(
                expSelectedWallet = selectedExpWallet,
                onClickListOfWallet = onClickListOfWallet)
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
        print("today's date ${System.currentTimeMillis()}")
    }
}

@Composable
private fun expenseAmount(expenseAmount: String, onExpenseValueChange: (String) -> Unit) {
    label("Amount")
    inputWithLeadingIcon(
        value = expenseAmount,
        placeholder = "0",
        leadingIcon = R.drawable.currency_rupee_ui,
        isReadOnly = false,
        onValueChange = { onExpenseValueChange(it) },
    )
}

@Composable
private fun expenseDescription(description: String, onDescriptionChanged: (String) -> Unit) {
    label("Description")
    inputWithNoIcon(
        value = description,
        placeholder = "Short Description",
        onValueChange = { onDescriptionChanged(it) },
        isReadOnly = false,
    )
}

@Composable
private fun category(selectedCat: Int?, onClickListOfCategory:()->Unit) {
    val value = if(selectedCat!=null)stringResource(selectedCat) else ""
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
private fun expenseWallet(expSelectedWallet: Wallet?,onClickListOfWallet:()->Unit) {
    val annotedString = buildAnnotatedString {
        append(expSelectedWallet?.walletName)
        append("\u0020")
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black,)
        ){
            append("•")
        }
        append("\u0020₹\u0020")
        append(formatAmount(expSelectedWallet?.walletAmount.toString()))
    }
    label("Wallet")
    inputWithTrailingIcon(
        value = annotedString.text,
        placeholder = "Wallet",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isEnabled = false,
        onClick =  onClickListOfWallet
    )
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDateDialog(state: DatePickerState, onDismiss: () -> Unit, onDateSelected: (Long) -> Unit) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(
                    state.selectedDateMillis ?: System.currentTimeMillis()
                )
                onDismiss()
            }) {
                Text("ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showTimeDialog(currentTime: Calendar, onTimeSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .background(color = Color.White)
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

    }
}

//shared UI across different screen
@Composable
fun label(label: String) {
    Text(
        text = label,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
        fontWeight = FontWeight.ExtraBold,
        color = onSurface,
        style = MaterialTheme.typography.titleSmall,
        fontSize = MaterialTheme.typography.titleSmall.fontSize
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun inputWithLeadingIcon(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: Int,
    isReadOnly: Boolean,
    saveAmount: (Float?) -> Unit = {}
) {
    /*val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()*/
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        readOnly = isReadOnly,
        placeholder = {
            Text(
                text = placeholder,
                color = onSurface,
                fontWeight = inputTextWeight,
                fontSize = inputTextSize,
                fontStyle = inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = onSurface,
            fontWeight = inputTextWeight,
            fontSize = inputTextSize,
            fontStyle = inputTextStyle
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = stringResource(R.string.currency),
                modifier = Modifier.size(15.dp),
                tint = onBackground
            )
        },
        modifier = Modifier
            /*.bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }*/
            .fillMaxWidth(),
        shape = inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = inputFieldBackgroundColors
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                saveAmount(value.toFloatOrNull())
            }
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun inputWithTrailingIcon(
    value: String,
    placeholder: String,
    trailingIcon: ImageVector? = null,
    isReadOnly: Boolean,
    isEnabled: Boolean,
    onClick:()->Unit
) {
   /* val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()*/
    TextField(
        value = value,
        onValueChange = {},  // No update needed as it's read-only
        readOnly = isReadOnly,
        placeholder = {
            Text(
                text = placeholder,
                color = onSurface,
                fontWeight = inputTextWeight,
                fontSize = inputTextSize,
                fontStyle = inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = onSurface,
            fontWeight = inputTextWeight,
            fontSize = inputTextSize,
            fontStyle = inputTextStyle
        ),
        trailingIcon = {
            Image(
                imageVector = trailingIcon!!,
                contentDescription = stringResource(R.string.arrow_down),
                colorFilter = ColorFilter.tint(onBackground)
            )
        },
        modifier = Modifier
            /*.bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }*/
            .fillMaxWidth().clickable(
                onClick = {
                    Log.d("click ","click is true")
                    onClick()
                }
            ),
        enabled = isEnabled,
        shape = inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = inputFieldBackgroundColors,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
        )
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun inputWithNoIcon(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isReadOnly: Boolean,
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        readOnly = isReadOnly,
        placeholder = {
            Text(
                text = placeholder,
                color = onSurface,
                fontWeight = inputTextWeight,
                fontSize = inputTextSize,
                fontStyle = inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = onSurface,
            fontWeight = inputTextWeight,
            fontSize = inputTextSize,
            fontStyle = inputTextStyle
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = inputFieldBackgroundColors
        ), // if value is false then only it is clickable
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
        )
    )
}

