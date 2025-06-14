package com.example.expensetracker.uiScreen.walletScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.viewModel.WalletViewModel

//show list of wallet for income and expense screen
@Composable
fun SelectWalletRoute(navigateUp: () -> Unit, walletViewModel: WalletViewModel) {
    val walletDatabaseState by walletViewModel.walletUiState.collectAsState()
    val selectedExpenseWallet by walletViewModel.selectedWallet.collectAsState(initial = null)

    SelectWallet(walletDatabaseState = walletDatabaseState,
        selectedExpenseWallet = selectedExpenseWallet,
        onSelectWallet = {
            walletViewModel.updateSelectedExpWallet(it)
            navigateUp()
        })
}

@Composable
private fun SelectWallet(
    walletDatabaseState: WalletDisplayState,
    selectedExpenseWallet: Wallet?,
    onSelectWallet: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(color = AppColors.inverseOnSurface).padding(top = 20.dp)
            .background(color = AppColors.surface)
    ) {
        items(walletDatabaseState.userWallets) { wallet ->
            showListOfWallet(wallet, selectedExpenseWallet, onSelectWallet)
        }
    }
}

@Composable
fun showListOfWallet(wallet: Wallet, selectedWallet: Wallet?, onSelectWallet: (Int) -> Unit) {

    val borderStroke =
        if (selectedWallet?.walletId != wallet.walletId) BorderStroke(1.dp, Color.Black)
        else BorderStroke(0.dp, Color.Transparent)
    Row(
        modifier = Modifier.padding(top = 10.dp).padding(horizontal = 30.dp).clickable(
            enabled = true, onClick = { onSelectWallet(wallet.walletId) }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    painter = painterResource(wallet.walletIcon),
                    contentDescription = stringResource(wallet.walletIconDes),
                    tint = Color.White,
                    modifier = Modifier.clip(CircleShape).background(wallet.walletColor)
                        .padding(10.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = wallet.walletName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.currency_rupee_ui),
                            contentDescription = stringResource(R.string.currency),
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = formatWalletAmount(wallet.walletAmount.toString()),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp)
                .clip(CircleShape)
                .background(
                    if (selectedWallet?.walletId == wallet.walletId) {
                        Color.Blue.copy(alpha = 0.6F)
                    } else {
                        Color.Unspecified
                    }
                )
                .border(borderStroke, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selectedWallet?.walletId == wallet.walletId) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }

    }

}


@Preview
@Composable
fun Check() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .padding(5.dp).clip(CircleShape)
            .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        /*Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.padding(3.dp))*/
    }
}