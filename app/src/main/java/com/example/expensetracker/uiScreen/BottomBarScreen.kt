package com.example.expensetracker.uiScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
