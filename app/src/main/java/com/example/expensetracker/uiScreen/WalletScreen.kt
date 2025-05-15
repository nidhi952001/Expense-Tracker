package com.example.expensetracker.uiScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.ui.theme.*
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.inversePrimary
import com.example.expensetracker.ui.theme.AppColors.inverseSurface
import com.example.expensetracker.ui.theme.AppColors.onPrimary
import com.example.expensetracker.ui.theme.AppColors.onPrimaryContainer
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.outlineVariant
import com.example.expensetracker.ui.theme.AppColors.secondary
import com.example.expensetracker.ui.theme.AppColors.secondaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.InputUIState.WalletInputState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addWallet(walletUiState: WalletInputState,
              onNameChanged:(String)->Unit,
              onValueChange:(String)-> Unit,
              onSelectType: (TypeOfWallet) -> Unit,
              onIconClick:()->Unit,
              onClickColorPicker:(Boolean)->Unit,
              onSelectedColor: (Color) -> Unit,
              onBackClick:()->Unit
              ){

    BackHandler(enabled = true, onBack = onBackClick)
    val scrollableState = rememberScrollState()
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableIntStateOf(0)
    }
    val listOfWallet = listOf(TypeOfWallet.GENERAL,
        TypeOfWallet.CREDITCARD
    ) //todo do state hoisting
    Column( modifier = Modifier.background(color = inverseOnSurface).
    fillMaxSize().padding(top = 15.dp).background(color = surface).padding(start = 30.dp, end = 30.dp)
        .verticalScroll(scrollableState)) {
        Label("Name")
        InputField(
            value = walletUiState.walletName,
            placeholder = stringResource(R.string.wallet_name),
            trailingIconNeeded = false ,
            leadingIconNeeded = false,
            onValueChange = {onNameChanged(it)},
            isReadOnly = false ,
            isClickable = false)
        Label("Type")
        dropDown(isDropDownExpanded = isDropDownExpanded,
            itemPosition = itemPosition,
            listOfWallet = listOfWallet,
            selectedWallet = walletUiState.selectType,
            onSelectWallet = {onSelectType(it)})
        Label("Amount")
        InputField(
            value = walletUiState.walletAmount,
            placeholder = "",
            onClick = {  } ,
            trailingIconNeeded = true ,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = true,
            leadingIcon = R.drawable.currency_rupee_ui,
            isReadOnly = false ,
            onValueChange = {
                onValueChange(it)
            },
            isClickable = true
        )
        Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()) {
            Column(Modifier.weight(1f)) {
                Label("Color")
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
                Label("Icon")
                Image(
                    painter = painterResource(walletUiState.selectedIcon),
                    contentDescription = stringResource(walletUiState.walletIconName),
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .border(width = 1.dp, color = Color.Unspecified)
                        .background(color = outlineVariant.copy(alpha = 0.4f))
                        .padding(17.dp)
                        .clickable(onClick = { onIconClick() }),
                    colorFilter = ColorFilter.tint(color = onSurface)
                )
            }
        }
    }

}

@Composable
fun viewWalletWithBalance(
    modifier: Modifier,
    walletDatabaseState: WalletDisplayState,
    addWallet: () -> Unit,
    onViewWalletDetail:(Int)->Unit
){
    LazyColumn(modifier) {
        item {
            Spacer(modifier = Modifier.height(15.dp))
            showBalance(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                totalBalance = walletDatabaseState.totalBalance
            )
            Spacer(Modifier.height(15.dp))
            showWallets(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                listOfWallet = walletDatabaseState.savedWallets,
                addWallet = addWallet,
                onViewWalletDetail = onViewWalletDetail
            )
        }
    }
}

@Composable
fun showBalance(modifier: Modifier,totalBalance:Float){
    Column(modifier = modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.account_balance),
            color = secondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp,Alignment.CenterHorizontally) ,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.currency_rupee_ui),
                contentDescription = stringResource(R.string.currency),
                tint = inverseSurface
            )
            Text(
                text = totalBalance.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = inverseSurface
            )
        }
    }
}

@Composable
fun showWallets(
    modifier: Modifier,
    listOfWallet: List<Wallet>,
    addWallet: () -> Unit,
    onViewWalletDetail:(Int)->Unit){
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
        Spacer(modifier= Modifier.padding(vertical = 10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().
                height(
                    if(listOfWallet.size<=2)
                        (listOfWallet.size*120).dp
                    else
                        ((listOfWallet.size/2)*250).dp
                ),
                userScrollEnabled = false
            ) {
                items(items = listOfWallet) { wallet->
                    wallet(wallet = wallet , onViewWalletDetail = onViewWalletDetail)
                }
                item {
                    addWallet(addWallet = addWallet)
                }
            }
    }
}

@Composable
fun wallet(wallet: Wallet,onViewWalletDetail:(Int)->Unit) {
    Card(modifier = Modifier.size(100.dp).clickable(onClick = {onViewWalletDetail(wallet.walletId)}) ) {
        Column(
            modifier = Modifier.background(color = wallet.walletColor).padding(5.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly ,
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
                tint = Color.White)
            Text(
                text = wallet.walletName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.7F)
            )
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.currency_rupee_ui),
                    contentDescription = stringResource(R.string.currency),
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = wallet.walletAmount.toString(),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun addWallet(addWallet: () -> Unit){
    Card(modifier = Modifier.size(100.dp).clickable(enabled = true, onClick = addWallet)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = secondaryContainer).padding(vertical = 35.dp),
            verticalArrangement = Arrangement.SpaceEvenly ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.add_ic),
                contentDescription = stringResource(R.string.addWallet))
        }
    }
}

@Composable
fun dropDown(
    isDropDownExpanded: MutableState<Boolean>,
    itemPosition: MutableState<Int>,
    listOfWallet: List<TypeOfWallet>,
    selectedWallet:TypeOfWallet,
    onSelectWallet:(TypeOfWallet)->Unit
) {
    Box(modifier = Modifier
        .clickable {
            isDropDownExpanded.value = true
        }
        .border(1.dp, Color.Unspecified , RoundedCornerShape(5.dp))
        .background(AppColors.outlineVariant.copy(alpha = 0.4f))
        .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = selectedWallet.name)
            Image(
                painter = painterResource(id = R.drawable.arrow_drop_down_ic),
                contentDescription = stringResource(R.string.arrow_down),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
            modifier = Modifier.widthIn(min = 250.dp , max = 300.dp).background(color = surface)
        ) {
            listOfWallet.fastForEachIndexed { index, list ->
                DropdownMenuItem(
                    text = {
                        Text(text = list.name, color = inverseSurface)
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                        onSelectWallet(listOfWallet[itemPosition.value])
                    }
                )
            }
        }
    }
}

@Composable
fun showIcon(walletUiState: WalletInputState, modifier: Modifier, onSelectedIcon:(Int)->Unit){
    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ){
            items(walletUiState.listOfIcon){
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = stringResource(it.iconName),
                    modifier = Modifier.background(
                        color = if(walletUiState.selectedIcon.equals(it.icon))
                            walletUiState.selectedColors
                                    else
                            inverseOnSurface,
                        shape = CircleShape
                    )
                        .padding(20.dp)
                        .clickable(onClick = { onSelectedIcon(it.icon) }),
                    tint = if(walletUiState.selectedIcon.equals(it.icon))
                        onPrimary
                    else
                        inverseSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun walletColor(
    showColorPicker: Boolean,
    onClickColorPicker: (Boolean) -> Unit,
    selectedColor: Color,
    onSelectedColor: (Color) -> Unit,
    listOfColors: List<Map.Entry<String, Color>>
){
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(outlineVariant.copy(alpha = 0.4f))
                .clickable(onClick = { onClickColorPicker(true) })
                .padding(5.dp)
        ) {
            Button(
                enabled = true, onClick = {onClickColorPicker(true)},
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
        onDismissRequest = {onClickColorPicker(false)}){
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
