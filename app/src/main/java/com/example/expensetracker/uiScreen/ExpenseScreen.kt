package com.example.expensetracker.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors.inputFieldBackgroundColors
import com.example.expensetracker.ui.theme.AppColors.inputFieldShape
import com.example.expensetracker.ui.theme.AppColors.inputTextSize
import com.example.expensetracker.ui.theme.AppColors.inputTextStyle
import com.example.expensetracker.ui.theme.AppColors.inputTextWeight
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.onBackground
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.InputUIState.ExpenseInputState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    modifier: Modifier,
    showDateDialogUI: (Boolean) -> Unit,
    onDateSelected: (Long?) -> Unit,
    showTimeDialogUI: (Boolean) -> Unit,
    expenseUiState: ExpenseInputState,
    onExpenseAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {

    val scrollableState = rememberScrollState()
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = modifier.background(color = inverseOnSurface).padding(top = 20.dp)
            .verticalScroll(scrollableState)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(color = surface).padding(horizontal = 30.dp).padding(bottom = 20.dp)) {
            //Date
            label("Date")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(inputFieldShape)
                    .background(color = inputFieldBackgroundColors)
            ) {
                dateUi(showDateDialogUI = {showDateDialogUI(it)}, expenseUiState.selectedDate)
                timeUi(showTimeDialogUI = {showTimeDialogUI(it)})
            }
            if (expenseUiState.showDateDialogUI) {
                showDateDialog(datePickerState,
                    onDateSelected = { onDateSelected(it) },
                    onDismiss = { showDateDialogUI(false) })
            }
            if(expenseUiState.showTimeDialogUI){
                showTimeDialog()
            }
            expenseAmount(expenseAmount = expenseUiState.expAmount, onExpenseValueChange = {onExpenseAmountChanged(it)})
            expenseDescription(description = expenseUiState.expDescription, onDescriptionChanged = {onDescriptionChanged(it)})
            expenseCategory(expSelectedCat = expenseUiState.expSelectedCategory)
            expenseWallet(expSelectedWallet = "")
        }
    }

}
@Composable
private fun dateUi(showDateDialogUI: (Boolean)->Unit, selectedDate: Long?) {
    Button(
        onClick = { showDateDialogUI(true) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
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
private fun timeUi(showTimeDialogUI: (Boolean) -> Unit) {
    Button(
        onClick = { showTimeDialogUI(true)},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = inputFieldShape
    ) {
        Text(
            text = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                .toString() + "" + Calendar.getInstance().get(Calendar.MINUTE).toString(),
            color = onSurface,
            fontWeight = inputTextWeight,
            fontSize = MaterialTheme.typography.labelLarge.fontSize
        )
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
        onValueChange = {onExpenseValueChange(it)},
    )
}
@Composable
private fun expenseDescription(description: String,onDescriptionChanged:(String)->Unit) {
    label("Description")
    inputWithNoIcon(
        value = description,
        placeholder = "Short Description",
        onValueChange = { onDescriptionChanged(it) },
        isReadOnly = false,
    )
}
@Composable
private fun expenseCategory(expSelectedCat: String) {
    label("Category")
    inputWithTrailingIcon(
        value = expSelectedCat,
        placeholder = "Select Category",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isClickable = false,
    )
}
@Composable
private fun expenseWallet(expSelectedWallet: String) {
    label("Wallet")
    inputWithTrailingIcon(
        value = expSelectedWallet,
        placeholder = "Wallet",
        trailingIcon = Icons.Default.ArrowDropDown,
        isReadOnly = true,
        isClickable = true,
    )
}








fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
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
                    state.selectedDateMillis?:System.currentTimeMillis()
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
fun showTimeDialog(){
    Dialog(onDismissRequest = {  }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            TimeInput(
                state = rememberTimePickerState(0, 0, is24Hour = false),
            )
        }
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


@Composable
fun inputWithLeadingIcon(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: Int,
    isReadOnly: Boolean,
    saveAmount: (Float?) -> Unit = {}
){
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
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = inputFieldBackgroundColors
        )
        ,
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

@Composable
fun inputWithTrailingIcon(
    value: String,
    placeholder: String,
    trailingIcon: ImageVector? = null,
    isReadOnly: Boolean,
    isClickable: Boolean,
){
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
            .fillMaxWidth()
            .fillMaxHeight().clickable(enabled = isClickable, onClick = {}),
        shape = inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = inputFieldBackgroundColors
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
        )
    )
}
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
                .fillMaxWidth()
                .fillMaxHeight(),
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

