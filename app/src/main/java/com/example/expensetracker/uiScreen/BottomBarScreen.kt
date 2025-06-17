package com.example.expensetracker.uiScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.utils.TopLevelDestination

data class BottomNavItem(
    val route: String,
    val painter: Int,
    val label: Int
)

object ListOfBottomButton {
    val BottomNavItems = listOf(
        BottomNavItem(
            route = TopLevelDestination.transaction.name,
            painter = R.drawable.transaction_ic,
            label = R.string.transaction
        ),
        BottomNavItem(
            route = TopLevelDestination.Finance.name,
            painter = R.drawable.add_ic,
            label = R.string.add
        ),
        BottomNavItem(
            route = TopLevelDestination.showWallet.name,
            painter = (R.drawable.wallet_ic),
            label = R.string.showWallet
        )

    )

}

@Composable
fun appBottomBar(navHostController: NavHostController, currentRoute: String?) {
    if (currentRoute.equals(TopLevelDestination.transaction.name) ||
        currentRoute.equals(TopLevelDestination.showWallet.name)
    ) {
        BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, contentColor = onSurface, tonalElevation = 5.dp) {
            var selectedBottomBar by remember { mutableStateOf(currentRoute) }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ListOfBottomButton.BottomNavItems.forEach {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(
                            indication = null, enabled = true,
                            interactionSource = MutableInteractionSource()
                        ) {
                            selectedBottomBar = it.route
                            navHostController.navigate(it.route)
                        }.padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(it.painter),
                            contentDescription = stringResource(it.label),
                            /*tint =  if(selectedBottomBar == it.route) onSurface
                            else inverseOnSurface*/
                        )
                        if (it.painter != R.drawable.add_ic)
                            Text(
                                text = stringResource(it.label),
                                /*color = if(selectedBottomBar == it.route) onSurface
                                        else inverseOnSurface*/
                            )
                    }
                }
            }
        }
    }


}

@Composable
fun welcomeScreenBottom(
    modifier: Modifier,
    onGetStarted: () -> Unit,
    onNext: () -> Unit,
    page: Int
) {
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