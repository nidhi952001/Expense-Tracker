package com.example.expensetracker.uiScreen

import android.R.attr.enabled
import android.os.Build
import android.util.Log
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    expenseViewModel: ExpenseViewModel,
    onClick: () -> Unit
) {
    var selectedAmount by rememberSaveable { mutableStateOf("") }
    val scrollableState = rememberScrollState()
    val datePickerState = rememberDatePickerState()
    var showDateDialogUI by rememberSaveable { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var description by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 30.dp, end = 30.dp)
            .verticalScroll(scrollableState)
    ) {
        //Date
        Label("Date")
        Button(
            onClick = { showDateDialogUI = true },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text(text = convertMillisToDate(selectedDate ?: System.currentTimeMillis()))
            print("today's date ${System.currentTimeMillis()}")
        }
        if(showDateDialogUI){
            showDateDialog(datePickerState,
                onDateSelected = {selectedDate = it},
                onDismiss = {showDateDialogUI = false})
        }
        Label("Amount")
        InputField(
            value = selectedAmount,
            placeholder = "",
            onClick = onClick ,
            trailingIconNeeded = true ,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = true,
            leadingIcon = R.drawable.currency_rupee_ui,
            isReadOnly = true ,
            onValueChange = {},
            isClickable = true)
        Label("Description")
        InputField(
            value = description,
            placeholder = "Short Description",
            onValueChange = { description = it },
            trailingIconNeeded = false,
            leadingIconNeeded = false,
            isReadOnly = false,
            isClickable = false,
        )
        Label("Category")
        InputField(
            value = selectedAmount,
            placeholder = "Select Category",
            onClick = onClick,
            trailingIconNeeded = true,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = false,
            isReadOnly = false,
            onValueChange = {},
            isClickable = false,
        )
        Label("Wallet")
        InputField(
            value = selectedAmount,
            placeholder = "Wallet",
            onClick = onClick,
            trailingIconNeeded = true,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = false,
            isReadOnly = false,
            onValueChange = {},
            isClickable = false,
        )
    }

}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDateDialog(state: DatePickerState,onDismiss:()->Unit,onDateSelected:(Long?)->Unit) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(state.selectedDateMillis)
                onDismiss()
            } ) {
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

@Composable
fun Label(label: String) {
    Text(
        text = label,
        modifier = Modifier.padding(top = 20.dp),
        fontWeight = FontWeight.Black
    )
}

@Composable
fun showNumberKeyBoard() {
    Text(text = "hello")
}

@Composable
fun InputField(
    value: String,
    placeholder: String,
    onClick: () -> Unit = {},
    onValueChange:(String)-> Unit,
    trailingIconNeeded: Boolean,
    trailingIcon: ImageVector?=null,
    leadingIconNeeded: Boolean,
    leadingIcon: Int?=0,
    isReadOnly: Boolean,
    isClickable: Boolean,
    saveAmount:(Float?)->Unit={}
) {
    if(!trailingIconNeeded && !leadingIconNeeded) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = if (isReadOnly) true else false,
            placeholder = {
                Text(text = placeholder)
            },
            modifier = Modifier.clickable {
                Log.d("TAG", isClickable.toString())
                onClick()
            },
            enabled = !isClickable, // if value is false then only it is clickable
        )
    }
    else if(trailingIconNeeded && !leadingIconNeeded){
        OutlinedTextField(
            value = value,
            onValueChange = {},  // No update needed as it's read-only
            readOnly = if (isReadOnly) true else false,
            placeholder = {
                Text(text = placeholder)
            },
            trailingIcon = {
                if (trailingIconNeeded) {
                    Image(
                        imageVector = trailingIcon!!,
                        contentDescription = stringResource(R.string.currency)
                    )
                }
            },
            modifier = Modifier.clickable {
                Log.d("TAG", isClickable.toString())
                onClick()
            },
            enabled = !isClickable, // if value is false then only it is clickable
        )
    }
    else{
        OutlinedTextField(
            value = value,
            onValueChange = {onValueChange(it)},
            readOnly = false,
            placeholder = {
                Text(text = placeholder)
            },
            trailingIcon = {
                if (trailingIconNeeded) {
                    Image(
                        imageVector = trailingIcon!!,
                        contentDescription = stringResource(R.string.currency)
                    )
                }
            },
            leadingIcon = {
                if (leadingIconNeeded) {
                    Image(
                        painter = painterResource(leadingIcon!!),
                        contentDescription = stringResource(R.string.currency)
                    )
                }
            },
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
}

