package com.example.expensetracker.uiScreen

import android.R.attr.enabled
import android.os.Build
import android.util.Log
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    modifier: Modifier,
    expenseViewModel: ExpenseViewModel,
    onClick: () -> Unit
) {
    var selectedAmount by rememberSaveable { mutableStateOf("") }
    val scrollableState = rememberScrollState()
    val datePickerState = rememberDatePickerState()
    var showDateDialogUI by rememberSaveable { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var description by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = modifier.padding(top = 20.dp)
            .verticalScroll(scrollableState)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface).padding(horizontal = 30.dp).padding(bottom = 20.dp)) {
            //Date
            Label("Date")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Button(
                    onClick = { showDateDialogUI = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = convertMillisToDate(selectedDate ?: System.currentTimeMillis()),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize
                    )
                    print("today's date ${System.currentTimeMillis()}")
                }
                Button(onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(5.dp)){
                    Text(
                        text = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString() +""+Calendar.getInstance().get(Calendar.MINUTE).toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize
                    )
                }
            }
            if (showDateDialogUI) {
                showDateDialog(datePickerState,
                    onDateSelected = { selectedDate = it },
                    onDismiss = { showDateDialogUI = false })
            }
            Label("Amount")
            InputField(
                value = selectedAmount,
                placeholder = "0",
                onClick = onClick,
                trailingIconNeeded = false,
                leadingIconNeeded = true,
                leadingIcon = R.drawable.currency_rupee_ui,
                isReadOnly = false,
                onValueChange = {},
                isClickable = false
            )
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
                isReadOnly = true,
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
                isReadOnly = true,
                onValueChange = {},
                isClickable = false,
            )
        }
    }

}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDateDialog(state: DatePickerState, onDismiss: () -> Unit, onDateSelected: (Long?) -> Unit) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(state.selectedDateMillis)
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

@Composable
fun Label(label: String) {
    Text(
        text = label,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleSmall,
        fontSize = MaterialTheme.typography.titleSmall.fontSize
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
    onValueChange: (String) -> Unit,
    trailingIconNeeded: Boolean,
    trailingIcon: ImageVector? = null,
    leadingIconNeeded: Boolean,
    leadingIcon: Int? = 0,
    isReadOnly: Boolean,
    isClickable: Boolean,
    saveAmount: (Float?) -> Unit = {}
) {
    if (!trailingIconNeeded && !leadingIconNeeded) {
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = isReadOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontStyle = MaterialTheme.typography.titleSmall.fontStyle
                )
            },
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                Log.d("TAG", isClickable.toString())
                onClick()
            },
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            ),
            enabled = !isClickable, // if value is false then only it is clickable
        )
    } else if (trailingIconNeeded && !leadingIconNeeded) {
        OutlinedTextField(
            value = value,
            onValueChange = {},  // No update needed as it's read-only
            readOnly = isReadOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontStyle = MaterialTheme.typography.titleSmall.fontStyle
                )
            },
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle
            ),
            trailingIcon = {
                if (trailingIconNeeded) {
                    Image(
                        imageVector = trailingIcon!!,
                        contentDescription = stringResource(R.string.arrow_down),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                    Log.d("TAG", isClickable.toString())
                    onClick()
                },
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            ),
            enabled = !isClickable, // if value is false then only it is clickable
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            readOnly = isReadOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontStyle = MaterialTheme.typography.titleSmall.fontStyle
                )
            },
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle
            ),
            trailingIcon = {
                if (trailingIconNeeded) {
                    Image(
                        imageVector = trailingIcon!!,
                        contentDescription = stringResource(R.string.currency),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            },
            leadingIcon = {
                if (leadingIconNeeded) {
                    Image(
                        painter = painterResource(leadingIcon!!),
                        contentDescription = stringResource(R.string.arrow_down),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.height(14.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
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
}

