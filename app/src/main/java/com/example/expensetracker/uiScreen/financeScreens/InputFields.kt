package com.example.expensetracker.uiScreen.financeScreens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import java.util.Calendar

//shared UI across different screen
@Composable
fun label(label: String) {
    Text(
        text = label,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
        fontWeight = FontWeight.ExtraBold,
        color = AppColors.onSurface,
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
                color = AppColors.onSurface,
                fontWeight = AppColors.inputTextWeight,
                fontSize = AppColors.inputTextSize,
                fontStyle = AppColors.inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = AppColors.onSurface,
            fontWeight = AppColors.inputTextWeight,
            fontSize = AppColors.inputTextSize,
            fontStyle = AppColors.inputTextStyle
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = stringResource(R.string.currency),
                modifier = Modifier.size(15.dp),
                tint = AppColors.onBackground
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
        shape = AppColors.inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = AppColors.inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = AppColors.inputFieldBackgroundColors
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
                color = AppColors.onSurface,
                fontWeight = AppColors.inputTextWeight,
                fontSize = AppColors.inputTextSize,
                fontStyle = AppColors.inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = AppColors.onSurface,
            fontWeight = AppColors.inputTextWeight,
            fontSize = AppColors.inputTextSize,
            fontStyle = AppColors.inputTextStyle
        ),
        trailingIcon = {
            Image(
                imageVector = trailingIcon!!,
                contentDescription = stringResource(R.string.arrow_down),
                colorFilter = ColorFilter.tint(AppColors.onBackground)
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
        shape = AppColors.inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = AppColors.inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = AppColors.inputFieldBackgroundColors,
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
                color = AppColors.onSurface,
                fontWeight = AppColors.inputTextWeight,
                fontSize = AppColors.inputTextSize,
                fontStyle = AppColors.inputTextStyle
            )
        },
        textStyle = TextStyle.Default.copy(
            color = AppColors.onSurface,
            fontWeight = AppColors.inputTextWeight,
            fontSize = AppColors.inputTextSize,
            fontStyle = AppColors.inputTextStyle
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = AppColors.inputFieldShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = AppColors.inputFieldBackgroundColors,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = AppColors.inputFieldBackgroundColors
        ), // if value is false then only it is clickable
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
        )
    )
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

