package com.example.expensetracker.uiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.getScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithBackArrow(navHostController: NavHostController, currentRoute: String?, onActionClick: () -> Unit) {
    val currentScreenName = getScreenName(currentRoute!!)
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreenName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            Icon(Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier.clickable {
                    navHostController.navigateUp()
                })
        },
        actions = {
                topBarTrailingIcon(currentRoute, onActionClick = onActionClick)

        }
    )

}

@Composable
fun topBarTrailingIcon(currentRoute: String?, onActionClick:()->Unit){
    if(!currentRoute!!.equals(TopLevelDestination.pickWalletIcon.name)) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                painter = painterResource(R.drawable.save_ic),
                contentDescription = stringResource(R.string.save),
                modifier = Modifier.clickable(onClick = { onActionClick() })
            )
        }
}

@Composable
fun AppTopBar(
    userName: String, currentRoute: String, navHostController: NavHostController,
    selected: String, newSelectedRoute: (String) -> Unit,
    onActionClick: () -> Unit) {
    if (currentRoute.equals(TopLevelDestination.expense.name) ||
        currentRoute.equals(TopLevelDestination.addWallet.name) ||
        currentRoute.equals(TopLevelDestination.pickWalletIcon.name)) {
            Column {
                topBarWithBackArrow(
                    navHostController = navHostController,
                    currentRoute = currentRoute ,
                    onActionClick = onActionClick)
                if(currentRoute.equals(TopLevelDestination.expense.name)) {
                    categoryTopBar(
                        modifier = Modifier.fillMaxWidth(),
                        selected = selected,
                        newSelectedRoute = { newSelectedRoute(it) })
                }
            }
    }
    if (currentRoute.equals(TopLevelDestination.transaction.name) ||
        currentRoute.equals(TopLevelDestination.showWallet.name)
    ) {
        topBarWithoutBackArrow(userName, currentRoute)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithoutBackArrow(userName: String, currentRoute: String?) {
    Column {
        TopAppBar(
            title = { Text(text = userName, fontWeight = FontWeight.Bold) },
            modifier = Modifier
        )
        if (currentRoute.equals(TopLevelDestination.transaction.name)) {
            secondTopBar()
        }
    }
}

@Composable
fun secondTopBar() {
    Text(text = "another title for date with condition")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoryTopBar(
    modifier: Modifier,
    selected: String,
    newSelectedRoute: (String) -> Unit
){
    Surface(
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
    ) {
        Row(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.surface
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { newSelectedRoute(TopLevelDestination.income.name) },
                modifier = Modifier.weight(1f)
                    .background(
                        color =
                            if (selected.equals(TopLevelDestination.income.name)) Color.Blue.copy(
                                alpha = 0.5f
                            )
                            else Color.Unspecified
                    ).padding(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Unspecified,
                    contentColor = if (selected.equals(TopLevelDestination.income.name)) Color.White else Color.Black
                ),
                shape = RectangleShape,
            ) {
                Text(
                    text = stringResource(R.string.income),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Button(
                onClick = { newSelectedRoute(TopLevelDestination.expense.name) },
                modifier = Modifier.weight(1f)
                    .background(
                        color =
                            if (selected.equals(TopLevelDestination.expense.name)) Color.Red.copy(
                                alpha = 0.5f
                            )
                            else Color.Unspecified
                    ).padding(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Unspecified,
                    contentColor = if (selected.equals(TopLevelDestination.expense.name)) Color.White else Color.Black
                ),
                shape = RectangleShape
            ) {
                Text(
                    text = stringResource(R.string.expense),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

