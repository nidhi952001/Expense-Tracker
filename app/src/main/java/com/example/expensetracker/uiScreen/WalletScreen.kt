package com.example.expensetracker.uiScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.expensetracker.R
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.AppColors.inputFieldBackgroundColors
import com.example.expensetracker.ui.theme.AppColors.inputFieldShape
import com.example.expensetracker.ui.theme.AppColors.inputTextSize
import com.example.expensetracker.ui.theme.AppColors.inputTextStyle
import com.example.expensetracker.ui.theme.AppColors.inputTextWeight
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.inverseSurface
import com.example.expensetracker.ui.theme.AppColors.onPrimary
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.secondary
import com.example.expensetracker.ui.theme.AppColors.secondaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.DisplayUIState.WalletDetailState
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.DisplayUIState.transactionDetailByWallet
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.transactionensetracker.entity.TransactionType
import kotlin.math.floor
import kotlin.math.roundToInt

val listOfWallet = listOf(
    TypeOfWallet.General,
    TypeOfWallet.CreditCard
)

@Composable
fun viewWalletWithBalance(
    modifier: Modifier,
    walletDatabaseState: WalletDisplayState,
    addWallet: () -> Unit,
    onViewWalletDetail: (Int) -> Unit,
    onClickVisibility: (Boolean) -> Unit,
    amountVisibility: WalletInputState
) {
    LazyColumn(modifier) {
        item {
            Spacer(modifier = Modifier.height(15.dp))
            showBalance(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                totalBalance = walletDatabaseState.totalBalance,
                balanceVisibility = amountVisibility.hideBalance,
                onClickVisibility = onClickVisibility
            )
            Spacer(Modifier.height(15.dp))
            showWallets(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                listOfWallet = walletDatabaseState.savedWallets,
                addWallet = addWallet,
                onViewWalletDetail = onViewWalletDetail,
                amountVisibility = amountVisibility
            )
        }
    }
}

@Composable
fun showBalance(modifier: Modifier, totalBalance: Float, balanceVisibility: Boolean,onClickVisibility:(Boolean)->Unit) {
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
                text = if(balanceVisibility)
                    totalBalance.toString()
                else
                    "*".repeat(totalBalance.toString().length),
                style = MaterialTheme.typography.headlineMedium,
                color = inverseSurface
            )
            if(balanceVisibility) {
                Icon(
                    painter = painterResource(R.drawable.visibility_off_ic),
                    contentDescription = stringResource(R.string.show),
                    modifier = Modifier.clickable(onClick = {onClickVisibility(false)})
                )
            }
            else{
                Icon(
                    painter = painterResource(R.drawable.visibility_ic),
                    contentDescription = stringResource(R.string.hide),
                    modifier = Modifier.clickable(onClick = {onClickVisibility(true)})
                )
            }
        }
    }
}

fun formatAmount(totalBalance: Double): String {
    if((totalBalance%1).toInt() ==0)
     return totalBalance.roundToInt().toString()
    else
        return totalBalance.toString()
}

@Composable
fun showWallets(
    modifier: Modifier,
    listOfWallet: List<Wallet>,
    addWallet: () -> Unit,
    onViewWalletDetail: (Int) -> Unit,
    amountVisibility: WalletInputState
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
                text = "Manage (${listOfWallet.size})",
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
                if (listOfWallet.size <= 2)
                    (listOfWallet.size * 120).dp
                else
                    ((listOfWallet.size / 2) * 250).dp
            ),
            userScrollEnabled = false
        ) {
            items(items = listOfWallet) { wallet ->
                wallet(wallet = wallet, onViewWalletDetail = onViewWalletDetail,amountVisibility = amountVisibility)
            }
            item {
                addWalletUi(addWallet = addWallet)
            }
        }
    }
}

@Composable
fun wallet(wallet: Wallet, onViewWalletDetail: (Int) -> Unit, amountVisibility: WalletInputState) {
    Card(
        modifier = Modifier.size(100.dp)
            .clickable(onClick = { onViewWalletDetail(wallet.walletId) })
    ) {
        Column(
            modifier = Modifier.background(color = wallet.walletColor).padding(5.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(wallet.walletIcon),
                contentDescription = null,
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.2F),
                        shape = RoundedCornerShape(10.dp)
                    ).padding(8.dp),
                tint = Color.White
            )
            Text(
                text = wallet.walletName,
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
                    text = if(amountVisibility.hideBalance)
                    wallet.walletAmount.toString()
                    else
                        "*".repeat(wallet.walletAmount.toString().length),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun addWalletUi(addWallet: () -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addWallet(
    walletUiState: WalletInputState,
    onNameChanged: (String) -> Unit,
    onWalletAmountChanged: (String) -> Unit,
    onSelectType: (TypeOfWallet) -> Unit,
    expandDropDown: (Boolean) -> Unit,
    onIconClick: () -> Unit,
    onClickColorPicker: (Boolean) -> Unit,
    onSelectedColor: (Color) -> Unit,
    onBackClick: () -> Unit
) {

    BackHandler(enabled = true, onBack = onBackClick)
    val scrollableState = rememberScrollState()

    Column(
        modifier = Modifier.background(color = inverseOnSurface).fillMaxSize().padding(top = 15.dp)
            .background(color = surface).padding(start = 30.dp, end = 30.dp)
            .verticalScroll(scrollableState)
    ) {
        walletName(walletUiState, onNameChanged)
        if(!walletUiState.onEditWalletClick)
            walletType(listOfWallet, expandDropDown, walletUiState, onSelectType)
        walletAmount(walletUiState, onWalletAmountChanged)
        walletColorWithIcon(walletUiState, onClickColorPicker, onSelectedColor, onIconClick)
    }

}

@Composable
private fun walletName(
    walletUiState: WalletInputState,
    onNameChanged: (String) -> Unit
) {
    label("Name")
    inputWithNoIcon(
        value = walletUiState.walletName,
        placeholder = stringResource(R.string.wallet_name),
        onValueChange = { onNameChanged(it) },
        isReadOnly = false,
    )
}

@Composable
private fun walletType(
    listOfWallet: List<TypeOfWallet>,
    expandDropDown: (Boolean) -> Unit,
    walletUiState: WalletInputState,
    onSelectType: (TypeOfWallet) -> Unit
) {
    label("Type")
    walletTypeDropDown(isDropDownExpanded = walletUiState.isWalletTypeExpanded,
        expandDropDown = { expandDropDown(it) },
        listOfWallet = listOfWallet,
        selectedWallet = walletUiState.selectType,
        onSelectWallet = { onSelectType(it) })
}

@Composable
fun walletTypeDropDown(
    isDropDownExpanded: Boolean,
    expandDropDown: (Boolean) -> Unit,
    listOfWallet: List<TypeOfWallet>,
    selectedWallet: TypeOfWallet,
    onSelectWallet: (TypeOfWallet) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(enabled = true, onClick = { expandDropDown(true) })
            .clip(inputFieldShape)
            .background(inputFieldBackgroundColors)
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedWallet.name,
                fontWeight = inputTextWeight,
                fontSize = inputTextSize,
                fontStyle = inputTextStyle
            )
            Image(
                painter = painterResource(id = R.drawable.arrow_drop_down_ic),
                contentDescription = stringResource(R.string.arrow_down),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { expandDropDown(false) },
            modifier = Modifier.widthIn(min = 250.dp, max = 300.dp).background(color = surface)
        ) {
            listOfWallet.fastForEachIndexed { index, list ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = list.name,
                            color = inverseSurface,
                            fontWeight = inputTextWeight,
                            fontSize = inputTextSize,
                            fontStyle = inputTextStyle
                        )
                    },
                    onClick = {
                        expandDropDown(false)
                        onSelectWallet(listOfWallet[index])
                    }
                )
            }
        }
    }
}

@Composable
private fun walletAmount(
    walletUiState: WalletInputState,
    onWalletAmountChanged: (String) -> Unit
) {
    label("Amount")
    inputWithLeadingIcon(
        value = walletUiState.walletAmount,
        placeholder = "",
        leadingIcon = R.drawable.currency_rupee_ui,
        isReadOnly = false,
        onValueChange = {
            onWalletAmountChanged(it)
        }
    )
}

@Composable
private fun walletColorWithIcon(
    walletUiState: WalletInputState,
    onClickColorPicker: (Boolean) -> Unit,
    onSelectedColor: (Color) -> Unit,
    onIconClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(Modifier.weight(1f)) {
            label("Color")
            walletColor(
                showColorPicker = walletUiState.showColorPicker,
                listOfColors = walletUiState.showListOfColor.entries.toList(),
                selectedColor = walletUiState.selectedColors,
                onClickColorPicker = onClickColorPicker,
                onSelectedColor = onSelectedColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier) {
            label("Icon")
            Image(
                painter = painterResource(walletUiState.selectedIcon),
                contentDescription = stringResource(walletUiState.walletIconName),
                modifier = Modifier
                    .clip(inputFieldShape)
                    .border(width = 1.dp, color = Color.Unspecified)
                    .background(color = inputFieldBackgroundColors)
                    .padding(17.dp)
                    .clickable(onClick = { onIconClick() }),
                colorFilter = ColorFilter.tint(color = onSurface)
            )
        }
    }
}

@Composable
fun walletColor(
    showColorPicker: Boolean,
    onClickColorPicker: (Boolean) -> Unit,
    selectedColor: Color,
    onSelectedColor: (Color) -> Unit,
    listOfColors: List<Map.Entry<String, Color>>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(inputFieldShape)
            .background(inputFieldBackgroundColors)
            .clickable(onClick = { onClickColorPicker(true) })
            .padding(5.dp)
    ) {
        Button(
            enabled = true, onClick = { onClickColorPicker(true) },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedColor
            ),
            modifier = Modifier.weight(1f)

        ) {}
        Spacer(modifier = Modifier.width(10.dp))
        Image(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = stringResource(R.string.arrow_down),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.clickable(onClick = { onClickColorPicker(true) })
        )
    }
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(0.5f).height(250.dp),
        expanded = showColorPicker,
        onDismissRequest = { onClickColorPicker(false) }) {
        colorPicker(
            listOfColors = listOfColors,
            onSelectedColor = onSelectedColor,
        )
    }
}

@Composable
fun colorPicker(onSelectedColor: (Color) -> Unit, listOfColors: List<Map.Entry<String, Color>>) {
    listOfColors.forEach {
        DropdownMenuItem(
            text = {},
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .height(30.dp)
                .background(color = it.value, shape = RoundedCornerShape(5.dp)),
            onClick = {
                onSelectedColor(it.value)
            }
        )
    }
}

@Composable
fun showIcon(walletUiState: WalletInputState, modifier: Modifier, onSelectedIcon: (Int) -> Unit) {
    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            items(walletUiState.listOfIcon) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = stringResource(it.iconName),
                    modifier = Modifier.background(
                        color = if (walletUiState.selectedIcon.equals(it.icon))
                            walletUiState.selectedColors
                        else
                            inverseOnSurface,
                        shape = CircleShape
                    )
                        .padding(20.dp)
                        .clickable(onClick = { onSelectedIcon(it.icon) }),
                    tint = if (walletUiState.selectedIcon.equals(it.icon))
                        onPrimary
                    else
                        inverseSurface
                )
            }
        }
    }
}


//show list of wallet for income and expense screen
@Composable
fun SelectWallet(walletDatabaseState: WalletDisplayState, selectedWallet: Wallet?,onSelectWallet:(Int)->Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(color = inverseOnSurface).padding(top = 20.dp).background(color = surface)) {
        items(walletDatabaseState.savedWallets) { wallet ->
            showListOfWallet(wallet,selectedWallet,onSelectWallet)
        }
    }
}

@Composable
fun showListOfWallet(wallet: Wallet, selectedWallet: Wallet?, onSelectWallet: (Int) -> Unit) {

    val borderStroke =
        if(selectedWallet?.walletId!=wallet.walletId) BorderStroke(1.dp,Color.Black)
    else BorderStroke(0.dp,Color.Transparent)
    Row(
        modifier = Modifier.padding(top = 10.dp).padding(horizontal = 30.dp).clickable(
                enabled = true, onClick = {onSelectWallet(wallet.walletId)}
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
                            text = wallet.walletAmount.toString(),
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
                    if(selectedWallet?.walletId == wallet.walletId) {
                        Color.Blue.copy(alpha = 0.6F)
                    }else{
                        Color.Unspecified
                    }
                )
                .border(borderStroke, CircleShape)
            ,
            contentAlignment = Alignment.Center
        ) {
            if(selectedWallet?.walletId==wallet.walletId) {
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

//wallet detail screen
@Composable
fun showWalletDetail(
    walletData: WalletDetailState?,
    modifier: Modifier
) {
    val scrollableState = rememberScrollState()
    if (walletData?.selectedWallet_detail != null){
        Box(modifier = modifier.fillMaxSize().padding(top = 20.dp).scrollable(scrollableState,Orientation.Vertical)) {
            Column {
                walletDetail(walletData.selectedWallet_detail, walletData)
                if(walletData.transaction.isNotEmpty())
                    walletTransaction(walletData.transaction)
            }
        }
    }
}

@Composable
fun walletTransaction(transaction: List<transactionDetailByWallet>) {
    LazyColumn(modifier = Modifier.wrapContentHeight().padding(top = 20.dp).background(
        color = surface).padding(horizontal = 10.dp)) {
        item {
            Text(text = "Transaction list" ,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp))
        }
        items(transaction) {
            allWalletTransaction(it, modifier = Modifier)
        }
    }
}

@Composable
private fun walletDetail(
    selectedWallet_detail: Wallet,
    walletData: WalletDetailState
) {
    Column(
        modifier = Modifier.background(color = surface)
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(selectedWallet_detail.walletIcon),
                contentDescription = stringResource(selectedWallet_detail.walletIconDes),
                colorFilter = ColorFilter.tint(
                    color = Color.White
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = selectedWallet_detail.walletColor)
                    .padding(8.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedWallet_detail.walletName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.ruppes_icon) + selectedWallet_detail.walletAmount.toString(),
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.income))
            Text(
                text = walletData.countSelectedWallet_income.toString() + "  " +
                        stringResource(R.string.transactions),
                color = Color.Blue.copy(alpha = 0.5F)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.expense))
            Text(
                text = walletData.countSelectedWallet_expense.toString() + "  " +
                        stringResource(R.string.transactions),
                color = Color.Red.copy(alpha = 0.5F)
            )
        }
    }
}


@Composable
private fun allWalletTransaction(
    transaction: transactionDetailByWallet,
    modifier: Modifier.Companion
) {
    Row(modifier = modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp) , verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(transaction.categoryIcon),
                    contentDescription = stringResource(transaction.categoryName),
                    modifier = Modifier.size(35.dp)
                        .clip(CircleShape)
                        .background(color = transaction.categoryColor).padding(3.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text =
                        stringResource(transaction.categoryName),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(text = transaction.total_transaction.toString()+" "+ stringResource(R.string.transactions))
                }
            }
        }
        val annotedString = buildAnnotatedString {
            append(stringResource( R.string.ruppes_icon))
            append(" ")
            append(transaction.transaction_total_amount.toString())
        }
        val annotedString1 = buildAnnotatedString {
            append(stringResource(R.string.minus_icon))
            append(stringResource( R.string.ruppes_icon))
            append(" ")
            append(transaction.transaction_total_amount.toString())
        }
        Text(
            text =
            if(transaction.transaction_type == TransactionType.Income) annotedString
            else annotedString1,
            color =
            if(transaction.transaction_type == TransactionType.Income)
                Color.Blue.copy(alpha = 0.5F)
            else
                Color.Red.copy(alpha = 0.5F),
            textAlign = TextAlign.End
        )
    }
}


