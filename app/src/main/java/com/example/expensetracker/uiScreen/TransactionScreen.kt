package com.example.expensetracker.uiScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun TransactionScreen(){
    val context = LocalContext.current
    val activity = context as Activity

    BackHandler(enabled = true){
        activity.finish()
    }
}