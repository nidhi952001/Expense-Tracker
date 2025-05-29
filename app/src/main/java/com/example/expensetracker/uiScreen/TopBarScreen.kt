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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.expensetracker.utils.InputUIState.SelectedTopBar
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.TopLevelDestination
import com.example.expensetracker.utils.getScreenName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarWithBackArrow(
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit,
    localExp: ExpenseIncomeInputState,
    localCat: CategoryInputState,
    selectedExpInc: SelectedTopBar,
    onEditWallet: () -> Unit
) {
    val currentScreenName = getScreenName(currentRoute!!,selectedExpInc)
    CenterAlignedTopAppBar(
        modifier = Modifier.background(color = Color.Transparent),
        title = {
            if(currentRoute!=TopLevelDestination.showDetailOfWallet.name) {
                Text(
                    text = currentScreenName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
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
                selectedExpInc = selectedExpInc,
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
    localExp: ExpenseIncomeInputState,
    localCategory: CategoryInputState,
    selectedExpInc: SelectedTopBar,
    onEditWallet:()->Unit
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
    if(currentRoute== TopLevelDestination.selectCategory.name){
        Icon(
            painter = painterResource(R.drawable.setting_ic),
            contentDescription = stringResource(R.string.setting),
            tint = onSurface,
            modifier = Modifier.size(40.dp).padding(end = 15.dp)
        )
    }
    if(currentRoute== TopLevelDestination.expenseIncome.name &&
        (selectedExpInc.selectedExpInc == R.string.expense || selectedExpInc.selectedExpInc == R.string.income)){
        val enable = if(selectedExpInc.selectedExpInc == R.string.expense)
                (localExp.validExpIncAmount && localCategory.validExpCategory)
        else
                (localExp.validExpIncAmount && localCategory.validIncCategory)
        IconButton(
            onClick = { onActionClick() },
            enabled =  enable,
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
    if(currentRoute==TopLevelDestination.showDetailOfWallet.name)
    if(currentRoute == TopLevelDestination.showDetailOfWallet.name){
            Icon(
                painter = painterResource(R.drawable.edit_ic),
                contentDescription = stringResource(R.string.edit),
                tint = onSurface,
                modifier = Modifier.size(40.dp).padding(end = 15.dp).clickable(onClick = {onEditWallet()})
            )
        Icon(
            painter = painterResource(R.drawable.pie_chart_ic),
            contentDescription = stringResource(R.string.statistic),
            tint = onSurface,
            modifier = Modifier.size(40.dp).padding(end = 15.dp)
        )

    }

}

@Composable
fun AppTopBar(
    userName: String,
    currentRoute: String?,
    localWallet: WalletInputState,
    onActionClick: () -> Unit,
    onBackClick: () -> Unit,
    localExpIncState: ExpenseIncomeInputState,
    localCatState: CategoryInputState,
    selectedExpInc: SelectedTopBar,
    onSelectExpInc: (Int) -> Unit,
    onEditWallet:()->Unit
) {
    when (currentRoute) {
        TopLevelDestination.expenseIncome.name,
        TopLevelDestination.selectWallet.name,
        TopLevelDestination.selectCategory.name,
        TopLevelDestination.addWallet.name,
        TopLevelDestination.pickWalletIcon.name,
        TopLevelDestination.showDetailOfWallet.name-> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                shadowElevation = 5.dp,
                color = surface
            ) {
                Column {
                    topBarWithBackArrow(
                        currentRoute = currentRoute,
                        localWallet = localWallet,
                        localExp = localExpIncState,
                        localCat = localCatState,
                        selectedExpInc = selectedExpInc,
                        onActionClick = onActionClick,
                        onBackClick = onBackClick,
                        onEditWallet = onEditWallet
                    )
                    if (currentRoute == TopLevelDestination.expenseIncome.name
                    ) {
                        categoryTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            selectedExpInc = selectedExpInc,
                            onSelectExpInc = onSelectExpInc)
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
            selectMonthTopBar()
        }
    }
}

@Composable
fun selectMonthTopBar() {
    val formatMonthYear = android.icu.text.SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val date = Date()
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp) , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.previous)
        )
        Text(
            text = formatMonthYear.format(date),
            fontWeight = FontWeight.ExtraBold
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.next)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoryTopBar(
    modifier: Modifier,
    selectedExpInc: SelectedTopBar,
    onSelectExpInc:(Int)->Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onSelectExpInc(R.string.income) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedExpInc.selectedExpInc == R.string.income) primaryContainer
                else surface,
                contentColor = if (selectedExpInc.selectedExpInc == R.string.income) onPrimaryContainer else onSurface
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
            onClick = { onSelectExpInc(R.string.expense) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (selectedExpInc.selectedExpInc == R.string.expense) errorColor.copy(alpha = 0.8F)
                else surface,
                contentColor =
                if (selectedExpInc.selectedExpInc == R.string.expense) onError
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

