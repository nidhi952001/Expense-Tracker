package com.example.expensetracker.uiScreen

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.expensetracker.ui.theme.AppColors.onSurface
import com.example.expensetracker.ui.theme.AppColors.secondary
import com.example.expensetracker.ui.theme.AppColors.secondaryContainer
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.DisplayUIState.WalletDisplayState
import com.example.expensetracker.utils.InputUIState.WalletInputState
import com.example.expensetracker.utils.ListOfColors

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
    Column( modifier = Modifier.background(color = inverseOnSurface).fillMaxSize().padding(top = 5.dp).background(color = surface).padding(start = 30.dp, end = 30.dp)
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
        Row {
            Column(Modifier.weight(1f)) {
                Label("Color")
               walletColor(
                   showColorPicker = walletUiState.showColorPicker,
                   listOfColors = walletUiState.showListOfColor,
                   selectedColor = walletUiState.selectedColors,
                   onClickColorPicker = onClickColorPicker,
                   onSelectedColor = onSelectedColor,
               )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Label("Icon")
                Image(
                    painter = painterResource(walletUiState.selectedIcon),
                    contentDescription = stringResource(walletUiState.walletIconName),
                    modifier = Modifier
                        .border(width = 1.dp, color = onSurface, shape = RectangleShape)
                        .width(80.dp)
                        .padding(15.dp)
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
    addWallet: () -> Unit
){
    LazyColumn(modifier) {
        item {
            Spacer(modifier = Modifier.height(5.dp))
            showBalance(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                totalBalance = walletDatabaseState.totalBalance
            )
            Spacer(Modifier.height(10.dp))
            showWallets(
                modifier = Modifier.fillMaxWidth()
                    .background(color = surface),
                listOfWallet = walletDatabaseState.savedWallets,
                addWallet = addWallet
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
fun showWallets(modifier: Modifier, listOfWallet: List<Wallet>, addWallet: () -> Unit){
    Column(modifier = modifier.padding(horizontal = 25.dp, vertical = 20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Wallet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
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
                modifier = Modifier.fillMaxWidth().height(((listOfWallet.size/2)*150).dp),
                userScrollEnabled = false
            ) {
                items(items = listOfWallet) { wallet->
                    wallet(wallet)
                }
                item {
                    addWallet(addWallet = addWallet)
                }
            }
    }
}

@Composable
fun wallet(wallet: Wallet) {
    println("wallet data "+wallet.walletIconDes)
        Card(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().background(color = wallet.walletColor).padding(vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly ,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Icon(painter = painterResource(wallet.walletIcon), contentDescription = null)
                Text(
                    text = wallet.walletName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.Center) {
                    Icon(
                        painter = painterResource(R.drawable.currency_rupee_ui),
                        contentDescription = stringResource(R.string.currency),
                        tint = inverseSurface,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(text = wallet.walletAmount.toString())
                }
            }
        }
}

@Composable
fun addWallet(addWallet: () -> Unit){
    Card(modifier = Modifier.fillMaxWidth().clickable(enabled = true, onClick = addWallet)
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
    Box(modifier = Modifier.padding(end = 20.dp)
        .clickable {
            isDropDownExpanded.value = true
        }
        .border(1.dp, inverseSurface , RectangleShape)
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
                contentDescription = "DropDown Icon"
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
            modifier = Modifier.widthIn(min = 250.dp , max = 300.dp)
        ) {
            listOfWallet.fastForEachIndexed { index, list ->
                DropdownMenuItem(
                    text = {
                        Text(text = list.name)
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
                    modifier = Modifier.size(50.dp).background(
                        color = if(walletUiState.selectedIcon.equals(it.icon))
                            inversePrimary
                                    else
                            inverseOnSurface,
                        shape = CircleShape
                    ).padding(10.dp)
                        .clickable(onClick = { onSelectedIcon(it.icon) }),
                    tint = inverseSurface,
                )
            }
        }
    }
}

@Composable
fun walletColor(showColorPicker:Boolean,onClickColorPicker:(Boolean)->Unit, listOfColors: List<ListOfColors>, selectedColor:Color , onSelectedColor: (Color) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = selectedColor, shape = MaterialTheme.shapes.large)
            .clickable(onClick = { onClickColorPicker(true) })
    )
    if(showColorPicker){
        colorPicker(
            listOfColors = listOfColors,
            onSelectedColor = onSelectedColor,
        )
    }
}

@Composable
fun colorPicker(listOfColors: List<ListOfColors>,onSelectedColor:(Color)->Unit) {
    Column(modifier = Modifier) {
        listOfColors.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(50.dp)
                    .background(color = it.colors, shape = MaterialTheme.shapes.large)
                    .clickable(onClick = {
                        onSelectedColor(it.colors)
                    })
            )
        }
    }
}
