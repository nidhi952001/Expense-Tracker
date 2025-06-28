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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors.errorColor
import com.example.expensetracker.ui.theme.AppColors.onError
import com.example.expensetracker.ui.theme.AppColors.onPrimaryContainer
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.outlineVariant
import com.example.expensetracker.ui.theme.AppColors.primaryContainer
import com.example.expensetracker.ui.theme.AppColors.secondary
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.uiScreen.uiState.CategoryInputState
import com.example.expensetracker.uiScreen.uiState.FinanceInputState
import com.example.expensetracker.uiScreen.uiState.SelectedTopBar
import com.example.expensetracker.uiScreen.uiState.WalletInputState
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.getScreenName
import com.example.expensetracker.viewModel.FinanceViewModel
import com.example.expensetracker.viewModel.WalletViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithBackArrow(
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit,
    localFinance: FinanceInputState,
    localCat: CategoryInputState,
    selectedFinance: SelectedTopBar,
    onEditWallet: () -> Unit
) {
    val currentScreenName = getScreenName(currentRoute!!, selectedFinance)
    CenterAlignedTopAppBar(
        modifier = Modifier.background(color = Color.Transparent),
        title = {
            if (currentRoute != TopLevelDestination.showDetailOfWallet.name) {
                Text(
                    text = currentScreenName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        navigationIcon = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier.padding(15.dp).clickable(onClick = onBackClick)
            )
        },
        actions = {
            topBarTrailingIcon(
                currentRoute = currentRoute,
                localWallet = localWallet,
                localFinance = localFinance,
                localCategory = localCat,
                selectedFinance = selectedFinance,
                onActionClick = onActionClick,
                onEditWallet = onEditWallet
            )

        }
    )

}

@Composable
fun topBarTrailingIcon(
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    localFinance: FinanceInputState,
    localCategory: CategoryInputState,
    selectedFinance: SelectedTopBar,
    onEditWallet: () -> Unit
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
    if (currentRoute == TopLevelDestination.selectCategory.name || currentRoute.contains(TopLevelDestination.selectWallet.name)) {
        Icon(
            painter = painterResource(R.drawable.setting_ic),
            contentDescription = stringResource(R.string.setting),
            tint = onSurface,
            modifier = Modifier.size(40.dp).padding(end = 15.dp)
        )
    }
    if ((currentRoute == TopLevelDestination.Finance.name &&
        (selectedFinance.selectedFinance == R.string.expense || selectedFinance.selectedFinance == R.string.income
        || selectedFinance.selectedFinance == R.string.transfer)) && !currentRoute.contains(TopLevelDestination.selectWallet.name)
    ) {
        val enable = when (selectedFinance.selectedFinance) {
            R.string.expense -> {
                (localFinance.isFinanceAmountValid && localCategory.isExpenseCategoryValid && localFinance.isFromWalletValid)
            }

            R.string.income -> {
                (localFinance.isFinanceAmountValid && localCategory.isIncomeCategoryValid && localFinance.isFromWalletValid)
            }

            else -> {
                (localFinance.isFinanceAmountValid && localFinance.isToWalletValid && localFinance.isFromWalletValid)
            }
        }
        IconButton(
            onClick = { onActionClick() },
            enabled = enable,
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
        if (currentRoute == TopLevelDestination.showDetailOfWallet.name) {
            Icon(
                painter = painterResource(R.drawable.edit_ic),
                contentDescription = stringResource(R.string.edit),
                tint = onSurface,
                modifier = Modifier.size(40.dp).padding(end = 15.dp)
                    .clickable(onClick = { onEditWallet() })
            )
            Icon(
                painter = painterResource(R.drawable.pie_chart_ic),
                contentDescription = stringResource(R.string.statistic),
                tint = onSurface,
                modifier = Modifier.size(40.dp).padding(end = 15.dp)
            )

        }
        if(currentRoute == TopLevelDestination.Record.name){
            Icon(
                painter = painterResource(R.drawable.edit_ic),
                contentDescription = stringResource(R.string.edit),
                tint = onSurface,
                modifier = Modifier.size(40.dp).padding(end = 15.dp)
                    .clickable(onClick = { onEditWallet() })
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = onSurface,
                modifier = Modifier.size(40.dp).padding(end = 15.dp)
            )
        }

}

@Composable
fun AppTopBar(
    userName: String,
    currentRoute: String?,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit,
    localFinanceState: FinanceInputState,
    localCatState: CategoryInputState,
    selectedFinanceType: SelectedTopBar,
    onSelectFinance: (Int) -> Unit,
    onEditWallet: () -> Unit,
    walletViewModel: WalletViewModel
) {
    val walletInputState by walletViewModel.walletInputState.collectAsState()
    if (currentRoute == TopLevelDestination.Finance.name ||
        currentRoute?.startsWith(TopLevelDestination.selectWallet.name) == true ||
        currentRoute == TopLevelDestination.selectCategory.name ||
        currentRoute == TopLevelDestination.addWallet.name ||
        currentRoute == TopLevelDestination.pickWalletIcon.name ||
        currentRoute == TopLevelDestination.showDetailOfWallet.name ||
        currentRoute == TopLevelDestination.Record.name
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            shadowElevation = 5.dp,
            color = surface
        ) {
            Column {
                topBarWithBackArrow(
                    currentRoute = currentRoute,
                    localWallet = walletInputState,
                    localFinance = localFinanceState,
                    localCat = localCatState,
                    selectedFinance = selectedFinanceType,
                    onActionClick = onActionClick,
                    onBackClick = onBackClick,
                    onEditWallet = onEditWallet
                )
                if (currentRoute == TopLevelDestination.Finance.name
                ) {
                    categoryTopBar(
                        modifier = Modifier.fillMaxWidth(),
                        selectedTopBar = selectedFinanceType,
                        onSelectFinanceType = onSelectFinance
                    )
                }
            }
        }
    }
    if (currentRoute == TopLevelDestination.transaction.name ||
        currentRoute == TopLevelDestination.showWallet.name
    ) {
        topBarWithoutBackArrow(userName, currentRoute)

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
            selectMonthTopBar()
        }
    }
}

@Composable
fun selectMonthTopBar() {
    val financeViewModel: FinanceViewModel = hiltViewModel()
    val currentMonthYearState by financeViewModel.currentMonthYear.collectAsState()
    val formatMonthYear = android.icu.text.SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val currentMonthYear = formatMonthYear.format(currentMonthYearState.currentMonthYear.time)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.previous),
            modifier = Modifier.clickable(onClick = {
                financeViewModel.previousMonthYear(
                    currentMonthYearState.currentMonthYear
                )
            })
        )
        Text(
            text = currentMonthYear,
            fontWeight = FontWeight.ExtraBold
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.next),
            modifier = Modifier.clickable(onClick = {
                financeViewModel.nextMonthYear(
                    currentMonthYearState.currentMonthYear
                )
            })

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoryTopBar(
    modifier: Modifier,
    selectedTopBar: SelectedTopBar,
    onSelectFinanceType: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onSelectFinanceType(R.string.income) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTopBar.selectedFinance == R.string.income) primaryContainer
                else surface,
                contentColor = if (selectedTopBar.selectedFinance == R.string.income) onPrimaryContainer else onSurface
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
            onClick = { onSelectFinanceType(R.string.expense) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (selectedTopBar.selectedFinance == R.string.expense) errorColor.copy(alpha = 0.8F)
                else surface,
                contentColor =
                if (selectedTopBar.selectedFinance == R.string.expense) onError
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
        Button(
            onClick = { onSelectFinanceType(R.string.transfer) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (selectedTopBar.selectedFinance == R.string.transfer) secondary.copy(alpha = 0.8F)
                else surface,
                contentColor =
                if (selectedTopBar.selectedFinance == R.string.transfer) MaterialTheme.colorScheme.onSecondary
                else onSurface
            ),
            shape = RectangleShape
        ) {
            Text(
                text = stringResource(R.string.transfer),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

