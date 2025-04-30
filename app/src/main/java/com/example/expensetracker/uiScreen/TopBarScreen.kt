package com.example.expensetracker.uiScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackArrow(navHostController: NavHostController, currentRoute: String?) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = currentRoute!! , fontWeight = FontWeight.Bold , style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.go_back)
                , modifier = Modifier.clickable {
                    navHostController.navigateUp()
                })
        },
        actions = {
            Row {
                Text(text = stringResource(R.string.save) , style = MaterialTheme.typography.titleMedium)
                Icon(painter = painterResource(R.drawable.save_ic), contentDescription = stringResource(R.string.save))
            }
        }
    )
}

@Composable
fun AppTopBar(userName:String,currentRoute: String?,navHostController: NavHostController) {
    if(currentRoute.equals(TopLevelDestination.expense.name)) {
        TopBarWithBackArrow(navHostController,currentRoute)
    }
    if(currentRoute.equals(TopLevelDestination.transaction.name) ||
        currentRoute.equals(TopLevelDestination.wallet.name)) {
        TopBarWithoutbackArrow(userName,currentRoute)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithoutbackArrow(userName: String,currentRoute: String?){
    Column {
        TopAppBar(
            title = { Text(text = userName, fontWeight = FontWeight.Bold) },
            modifier = Modifier
        )
        if(currentRoute.equals(TopLevelDestination.transaction.name)) {
                AppSecondTopbar()
        }
    }
}
@Composable
fun AppSecondTopbar() {
    Text(text= "another title for date with condition")
}