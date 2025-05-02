package com.example.expensetracker.uiScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetracker.R

data class BottomNavItem(
    val route:String,
    val painter: Int?,
    val icon: ImageVector?,
    val label:Int
)
object listOfBottomButton  {
    val BottomNavItems = listOf(
        BottomNavItem(
            route = TopLevelDestination.transaction.name,
            painter = R.drawable.transaction_ic,
            icon = null,
            label = R.string.transaction
        ),
        BottomNavItem(
            route = TopLevelDestination.expense.name,
            painter = null,
            icon = Icons.Rounded.AddCircle,
            label = R.string.add
        ),
        BottomNavItem(
            route = TopLevelDestination.wallet.name,
            painter = (R.drawable.wallet_ic),
            icon = null,
            label = R.string.wallet
        )

    )

}
@Composable
fun AppBottomBar(navHostController: NavHostController, currentRoute: String?){
    if(currentRoute.equals(TopLevelDestination.transaction.name)) {
        NavigationBar {
            listOfBottomButton.BottomNavItems.forEach{ items->

                NavigationBarItem(
                    selected = currentRoute==items.route,
                    onClick = {
                        navHostController.navigate(items.route)
                    },
                    icon = {
                        if(items.icon==null)
                            items.painter?.let { Icon(painter = painterResource(it) , contentDescription = stringResource( items.label)) }
                        else
                            Icon(imageVector = items.icon, contentDescription = stringResource( items.label))
                    },
                    label = {
                        Text(text = stringResource(items.label))
                    }
                )
            }
        }
    }


}

@Composable
fun BottomScreen(modifier: Modifier,onGetStarted: () -> Unit,onNext:()->Unit, page: Int){
        Row(modifier = modifier) {
                when (page) {
                    1 ->
                        Button(modifier = Modifier.fillMaxWidth(), onClick = { onGetStarted() }) {
                            Text(text = stringResource(R.string.get_started))
                        }

                    else ->
                        Button(modifier = Modifier.fillMaxWidth(), onClick = { onNext() }) {
                        Text(text = stringResource(R.string.next))
                    }

                }
        }

}