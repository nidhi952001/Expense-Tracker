package com.example.expensetracker.uiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.expensetracker.R
import com.example.expensetracker.entity.TypeOfWallet
import com.example.expensetracker.entity.Wallet
import com.example.expensetracker.utils.WalletStateData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addWallet(walletUiState: WalletStateData,
              onNameChanged:(String)->Unit,
              onValueChange:(Float)-> Unit,
              onSelectType: (String) -> Unit,
              saveAmount: (String) -> Unit){
    val scrollableState = rememberScrollState()
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableIntStateOf(0)
    }
    val listOfWallet = listOf<String>(TypeOfWallet.GENERAL.toString(),
        TypeOfWallet.CREDITCARD.toString()
    )
    Column( modifier = Modifier.fillMaxSize().padding(start = 30.dp, end = 30.dp)
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
        dropDown(isDropDownExpanded,itemPosition,listOfWallet, selectedWallet = {onSelectType(it)})
        Label("Amount")
        InputField(
            value = if(walletUiState.initialAmount.toString()=="0.0") "" else walletUiState.initialAmount.toString(),
            placeholder = "",
            onClick = {  } ,
            trailingIconNeeded = true ,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = true,
            leadingIcon = R.drawable.currency_rupee_ui,
            isReadOnly = false ,
            onValueChange = {
                onValueChange(it.toFloat())
            },
            isClickable = true,
            saveAmount = {saveAmount(it.toString())})
        Label("Icon")
       /* InputField(
            value = walletUiState.walletIcon,
            placeholder = "",
            onClick = enterAmount ,
            trailingIconNeeded = true ,
            trailingIcon = Icons.Default.ArrowDropDown,
            leadingIconNeeded = false,
            isReadOnly = true ,
            onValueChange = {},
            isClickable = true)*/
    }

}

@Composable
fun showWallet(modifier: Modifier, listOfWallet: List<Wallet>, addWallet: () -> Unit){
    val scroll = rememberScrollState()
    Column(modifier = modifier){
        ShowBalance(modifier = Modifier.fillMaxWidth().height(40.dp))
        Spacer(Modifier.height(10.dp))
        ShowWallet(modifier = Modifier.fillMaxWidth(),
            listOfWallet = listOfWallet,
            addWallet = addWallet)
    }
}

@Composable
fun ShowBalance(modifier: Modifier){
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Account Balance")
    }
}

@Composable
fun ShowWallet(modifier: Modifier,listOfWallet: List<Wallet>,addWallet: () -> Unit){
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Wallet")
            Text(text = "Manage (${listOfWallet.size})")
        }
        Spacer(modifier= Modifier.padding(vertical = 10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = listOfWallet) { wallet->
                    wallet(wallet)
                }
                item {
                    addwalletIcon(addWallet = addWallet)
                }
            }
    }
}

@Composable
fun wallet(wallet: Wallet) {
        Card(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            Column(
                modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.secondaryContainer),
                verticalArrangement = Arrangement.SpaceEvenly ,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Icon(painter = painterResource(R.drawable.account_wallet_ic), contentDescription = stringResource(wallet.iconDes))
                Text(text = wallet.walletName)
                Text(text = wallet.initialAmount.toString())
            }
        }
}

@Composable
fun addwalletIcon(addWallet: () -> Unit){
    Card(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable(enabled = true, onClick = addWallet)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.secondaryContainer),
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
    listOfWallet: List<String>,
    selectedWallet:(String)->Unit
) {
    Box(modifier = Modifier.padding(end = 20.dp)
        .clickable {
            isDropDownExpanded.value = true
        }
        .border(1.dp, MaterialTheme.colorScheme.inverseSurface , RectangleShape)
        .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = listOfWallet[itemPosition.value] )
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
                        Text(text = list)
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                        selectedWallet(listOfWallet[itemPosition.value])
                    }
                )
            }
        }
    }
}