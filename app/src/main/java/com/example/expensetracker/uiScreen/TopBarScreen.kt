package com.example.expensetracker.uiScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors.errorColor
import com.example.expensetracker.ui.theme.AppColors.onError
import com.example.expensetracker.ui.theme.AppColors.onPrimaryContainer
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.outlineVariant
import com.example.expensetracker.ui.theme.AppColors.primaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.getScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithBackArrow(
    navHostController: NavHostController,
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    onBackClick:()->Unit,
) {
    val currentScreenName = getScreenName(currentRoute!!)
    CenterAlignedTopAppBar(
        modifier = Modifier.background(color = Color.Transparent),
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
                modifier = Modifier.padding(15.dp).clickable(onClick = onBackClick))
        },
        actions = {
            topBarTrailingIcon(
                currentRoute = currentRoute,
                localWallet = localWallet,
                onActionClick = onActionClick
            )

        }
    )

}

@Composable
fun topBarTrailingIcon(
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit
) {
    if (currentRoute!! == TopLevelDestination.addWallet.name) {
        IconButton(
            onClick = { onActionClick() },
            enabled = localWallet.isWalletNameValid && localWallet.isWalletAmountValid,
            colors = IconButtonDefaults.iconButtonColors(
                disabledContentColor = outlineVariant,
                contentColor = onSurface
            )
            ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AppTopBar(
    userName: String, currentRoute: String?,
    navHostController: NavHostController,
    selected: String,
    localWallet: WalletInputState,
    newSelectedRoute: (String) -> Unit,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    when (currentRoute) {
        TopLevelDestination.expense.name,
        TopLevelDestination.selectWallet.name,
        TopLevelDestination.addWallet.name,
        TopLevelDestination.pickWalletIcon.name,
        TopLevelDestination.income.name -> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                shadowElevation = 5.dp,
                color = surface
            ) {
                Column {
                    topBarWithBackArrow(
                        navHostController = navHostController,
                        currentRoute = currentRoute,
                        localWallet = localWallet,
                        onActionClick = onActionClick,
                        onBackClick = onBackClick
                    )
                    if (currentRoute == TopLevelDestination.expense.name ||
                        currentRoute == TopLevelDestination.income.name
                    ) {
                        categoryTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            selected = selected,
                            newSelectedRoute = { newSelectedRoute(it) })
                    }
                }
            }
        }

        TopLevelDestination.transaction.name,
        TopLevelDestination.showWallet.name -> {
            topBarWithoutBackArrow(userName, currentRoute)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithoutBackArrow(userName: String, currentRoute: String?) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
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
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { newSelectedRoute(TopLevelDestination.income.name) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected.equals(TopLevelDestination.income.name)) primaryContainer
                else surface,
                contentColor = if (selected.equals(TopLevelDestination.income.name)) onPrimaryContainer else onSurface
            ),
            shape = RectangleShape,
        ) {
            Text(
                text = stringResource(R.string.income),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { newSelectedRoute(TopLevelDestination.expense.name) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (selected == TopLevelDestination.expense.name) errorColor.copy(alpha = 0.8F)
                else surface,
                contentColor =
                if (selected == TopLevelDestination.expense.name) onError
                else onSurface
            ),
            shape = RectangleShape
        ) {
            Text(
                text = stringResource(R.string.expense),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

