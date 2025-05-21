package com.example.expensetracker.uiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.example.expensetracker.ui.theme.AppColors.errorColor
import com.example.expensetracker.ui.theme.AppColors.onError
import com.example.expensetracker.ui.theme.AppColors.onPrimaryContainer
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.outlineVariant
import com.example.expensetracker.ui.theme.AppColors.primaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.InputUIState.CategoryInputState
import com.example.expensetracker.utils.InputUIState.ExpenseIncomeInputState
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
    onBackClick: () -> Unit,
    localExp: ExpenseIncomeInputState,
    localCat: CategoryInputState,
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
                localExp = localExp,
                localCategory = localCat,
                onActionClick = onActionClick
            )

        }
    )

}

@Composable
fun topBarTrailingIcon(
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    localExp: ExpenseIncomeInputState,
    localCategory: CategoryInputState,
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
    if(currentRoute== TopLevelDestination.selectExpCategory.name){
        Icon(
            painter = painterResource(R.drawable.setting_ic),
            contentDescription = stringResource(R.string.setting),
            tint = onSurface,
            modifier = Modifier.size(30.dp).padding(15.dp)
        )
    }
    if(currentRoute == TopLevelDestination.expense.name || currentRoute==TopLevelDestination.income.name){
        val enable = if(currentRoute == TopLevelDestination.expense.name)
                (localExp.validExpIncAmount && localCategory.validExpCategory)
        else
                (localExp.validExpIncAmount && localCategory.validIncCategory)
        IconButton(
            onClick = { onActionClick() },
            enabled =  (localExp.validExpIncAmount && localCategory.validIncCategory),
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
    userName: String,
    currentRoute: String?,
    navHostController: NavHostController,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit,
    localExp: ExpenseIncomeInputState,
    localCat: CategoryInputState
) {
    when (currentRoute) {
        TopLevelDestination.expense.name,
        TopLevelDestination.selectWallet.name,
        TopLevelDestination.selectExpCategory.name,
        TopLevelDestination.addWallet.name,
        TopLevelDestination.pickWalletIcon.name,
        TopLevelDestination.income.name,
        TopLevelDestination.selectIncCategory.name-> {
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
                        localExp = localExp,
                        localCat = localCat,
                        onActionClick = onActionClick,
                        onBackClick = onBackClick
                    )
                    if (currentRoute == TopLevelDestination.expense.name ||
                        currentRoute == TopLevelDestination.income.name
                    ) {
                        categoryTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            navHostController = navHostController,
                            currentRoute = currentRoute)
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
    navHostController: NavHostController,
    currentRoute: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { navHostController.navigate(TopLevelDestination.income.name) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (currentRoute == TopLevelDestination.income.name) primaryContainer
                else surface,
                contentColor = if (currentRoute == TopLevelDestination.income.name) onPrimaryContainer else onSurface
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
            onClick = { navHostController.navigate(TopLevelDestination.expense.name) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (currentRoute == TopLevelDestination.expense.name) errorColor.copy(alpha = 0.8F)
                else surface,
                contentColor =
                if (currentRoute == TopLevelDestination.expense.name) onError
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

