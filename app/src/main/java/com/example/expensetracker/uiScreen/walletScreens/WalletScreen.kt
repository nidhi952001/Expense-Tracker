package com.example.expensetracker.uiScreen.walletScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.inverseSurface
import com.example.expensetracker.ui.theme.AppColors.secondary
import com.example.expensetracker.ui.theme.AppColors.secondaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.viewModel.WalletViewModel


@Composable
fun WalletScreenEntry(
    addWallet: () -> Unit,
    onWalletClick: () -> Unit,
    walletViewModel: WalletViewModel
) {
    val walletScrollableState = rememberScrollState()
    val walletUiState by walletViewModel.walletUiState.collectAsState()
    val balanceVisibilityState by walletViewModel.walletInputState.collectAsState()

    WalletScreenContent(
        modifier = Modifier.scrollable(
            state = walletScrollableState,
            orientation = Orientation.Vertical
        ).fillMaxSize().background(color = inverseOnSurface),
        walletUiState = walletUiState,
        visibilityState = balanceVisibilityState,
        onVisibilityClick = { walletViewModel.updateVisibility(it) },
        addWallet = addWallet,
        onViewWalletDetail = {
            walletViewModel.getSelectedWalletData(it)
            onWalletClick()
        }
    )
}

@Composable
fun WalletScreenContent(
    modifier: Modifier,
    walletUiState: WalletDisplayState,
    visibilityState: WalletInputState,
    onVisibilityClick: (Boolean) -> Unit,
    addWallet: () -> Unit,
    onViewWalletDetail: (Int) -> Unit
) {
    LazyColumn(modifier) {
        item {
            Spacer(modifier = Modifier.height(15.dp))
            AccountBalanceCard(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                totalWalletBalance = walletUiState.totalWalletBalance,
                isBalanceVisible = visibilityState.hideBalance,
                onVisibilityClick = onVisibilityClick
            )
            Spacer(Modifier.height(15.dp))
            WalletGridSection(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                userWallets = walletUiState.userWallets,
                addWallet = addWallet,
                onViewWalletDetail = onViewWalletDetail,
                visibilityState = visibilityState
            )
        }
    }
}

@Composable
fun AccountBalanceCard(
    modifier: Modifier,
    totalWalletBalance: Float,
    isBalanceVisible: Boolean,
    onVisibilityClick: (Boolean) -> Unit
) {
    Column(modifier = modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.account_balance),
            color = secondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.currency_rupee_ui),
                contentDescription = stringResource(R.string.currency),
                tint = inverseSurface
            )
            Text(
                text = if (isBalanceVisible)
                    formatWalletAmount(totalWalletBalance.toString())
                else
                    "*".repeat(formatWalletAmount(totalWalletBalance.toString()).length),
                style = MaterialTheme.typography.headlineMedium,
                color = inverseSurface
            )
            Icon(
                painter = painterResource(
                    if (isBalanceVisible) R.drawable.visibility_off_ic else R.drawable.visibility_ic
                ),
                contentDescription = stringResource(if (isBalanceVisible) R.string.hide else R.string.show),
                modifier = Modifier.clickable { onVisibilityClick(!isBalanceVisible) }
            )
        }
    }
}

@Composable
fun WalletGridSection(
    modifier: Modifier,
    userWallets: List<Wallet>,
    addWallet: () -> Unit,
    onViewWalletDetail: (Int) -> Unit,
    visibilityState: WalletInputState
) {
    Column(modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "WALLET",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = inverseSurface
            )
            Text(
                text = "Manage (${userWallets.size})",
                style = MaterialTheme.typography.labelLarge,
                color = inverseSurface
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().height(
                if (userWallets.size <= 2)
                    (userWallets.size * 120).dp
                else
                    ((userWallets.size / 2) * 250).dp
            ),
            userScrollEnabled = false
        ) {
            items(items = userWallets) { wallet ->
                WalletCard(
                    walletItem = wallet,
                    onViewWalletDetail = onViewWalletDetail,
                    visibilityState = visibilityState
                )
            }
            item {
                AddWalletCard(addWallet = addWallet)
            }
        }
    }
}

@Composable
fun WalletCard(
    walletItem: Wallet,
    onViewWalletDetail: (Int) -> Unit,
    visibilityState: WalletInputState
) {
    Card(
        modifier = Modifier.size(100.dp)
            .clickable(onClick = { onViewWalletDetail(walletItem.walletId) })
    ) {
        Column(
            modifier = Modifier.background(color = walletItem.walletColor).padding(5.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(walletItem.walletIcon),
                contentDescription = null,
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.2F),
                        shape = RoundedCornerShape(10.dp)
                    ).padding(8.dp),
                tint = Color.White
            )
            Text(
                text = walletItem.walletName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.7F)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.currency_rupee_ui),
                    contentDescription = stringResource(R.string.currency),
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = if (visibilityState.hideBalance)
                        formatWalletAmount(walletItem.walletAmount.toString())
                    else
                        "*".repeat(formatWalletAmount(walletItem.walletAmount.toString()).length),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AddWalletCard(addWallet: () -> Unit) {
    Card(
        modifier = Modifier.size(100.dp).clickable(enabled = true, onClick = addWallet)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = secondaryContainer)
                .padding(vertical = 35.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.add_ic),
                contentDescription = stringResource(R.string.addWallet)
            )
        }
    }
}



