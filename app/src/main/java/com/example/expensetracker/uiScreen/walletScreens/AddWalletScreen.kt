package com.example.expensetracker.uiScreen.walletScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.AppColors
import com.example.expensetracker.uiScreen.inputWithLeadingIcon
import com.example.expensetracker.uiScreen.inputWithNoIcon
import com.example.expensetracker.uiScreen.label
import com.example.expensetracker.uiScreen.uiState.WalletInputState
import com.example.expensetracker.utils.StaticData.TypeOfWallet
import com.example.expensetracker.utils.StaticData.listOfWallet
import com.example.expensetracker.viewModel.WalletViewModel

/**
 * Entry point for the Add Wallet screen. Collects UI state from the ViewModel
 * and triggers callbacks for user interactions like icon picking.
 */
@Composable
fun addWalletScreenEntry(
    onIconClick: () -> Unit,
    onBackClick: () -> Unit,
    walletViewModel: WalletViewModel
) {
    val scrollableState = rememberScrollState()
    val walletInputState by walletViewModel.walletInputState.collectAsState()

    BackHandler(enabled = true,
        onBack = {
            walletViewModel.clearWalletInputState()
            onBackClick()
        }
    )

    addWalletScreen(
        scrollableState = scrollableState,
        walletInputState = walletInputState,
        onWalletNameChanged = { walletViewModel.updateWalletName(it) },
        onDropdownExpandedChanged = { walletViewModel.updateDropDown(it) },
        onAmountChanged = { walletViewModel.updateWalletAmount(it) },
        setColorPickerExpanded = { walletViewModel.setColorPickerVisibility(it) },
        onSelectedColor = { walletViewModel.selectWalletColor(it) },
        onSelectType = { walletViewModel.updateWalletType(it) },
        onIconClick
    )

}

@Composable
private fun addWalletScreen(
    scrollableState: ScrollState,
    walletInputState: WalletInputState,
    onWalletNameChanged: (String) -> Unit,
    onDropdownExpandedChanged: (Boolean) -> Unit,
    onAmountChanged: (String) -> Unit,
    setColorPickerExpanded: (Boolean) -> Unit,
    onSelectedColor: (Color) -> Unit,
    onSelectType: (TypeOfWallet) -> Unit,
    onIconClick: () -> Unit
) {
    Column(
        modifier = Modifier.background(color = AppColors.inverseOnSurface).fillMaxSize().padding(top = 15.dp)
            .background(color = AppColors.surface).padding(start = 30.dp, end = 30.dp)
            .verticalScroll(scrollableState)
    ) {
        WalletName(walletInputState, onWalletNameChanged)
        if (!walletInputState.isEditWalletClicked)
            WalletType(listOfWallet, onDropdownExpandedChanged, walletInputState, onSelectType)
        WalletAmount(walletInputState, onAmountChanged)
        WalletColorWithIcon(walletInputState, setColorPickerExpanded, onSelectedColor, onIconClick)
    }
}

@Composable
private fun WalletName(
    walletInputState: WalletInputState,
    onNameChanged: (String) -> Unit
) {
    label("Name")
    inputWithNoIcon(
        value = walletInputState.walletName,
        placeholder = stringResource(R.string.wallet_name),
        onValueChange = { onNameChanged(it) },
        isReadOnly = false,
    )
}

@Composable
private fun WalletType(
    listOfWallet: List<TypeOfWallet>,
    onDropdownExpandedChanged: (Boolean) -> Unit,
    walletUiState: WalletInputState,
    onSelectType: (TypeOfWallet) -> Unit
) {
    label("Type")
    WalletTypeDropDown(isDropDownExpanded = walletUiState.isWalletTypeExpanded,
        onDropdownExpandedChanged = { onDropdownExpandedChanged(it) },
        listOfWallet = listOfWallet,
        selectedWallet = walletUiState.selectType,
        onSelectWallet = { onSelectType(it) })
}

/**
 * Dropdown menu for selecting the wallet type (e.g., Credit Card, General).
 */

@Composable
fun WalletTypeDropDown(
    isDropDownExpanded: Boolean,
    onDropdownExpandedChanged: (Boolean) -> Unit,
    listOfWallet: List<TypeOfWallet>,
    selectedWallet: TypeOfWallet,
    onSelectWallet: (TypeOfWallet) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(enabled = true, onClick = { onDropdownExpandedChanged(true) })
            .clip(AppColors.inputFieldShape)
            .background(AppColors.inputFieldBackgroundColors)
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedWallet.name,
                fontWeight = AppColors.inputTextWeight,
                fontSize = AppColors.inputTextSize,
                fontStyle = AppColors.inputTextStyle
            )
            Image(
                painter = painterResource(id = R.drawable.arrow_drop_down_ic),
                contentDescription = stringResource(R.string.arrow_down),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { onDropdownExpandedChanged(false) },
            modifier = Modifier.widthIn(min = 250.dp, max = 300.dp).background(color = AppColors.surface)
        ) {
            listOfWallet.fastForEachIndexed { index, list ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = list.name,
                            color = AppColors.inverseSurface,
                            fontWeight = AppColors.inputTextWeight,
                            fontSize = AppColors.inputTextSize,
                            fontStyle = AppColors.inputTextStyle
                        )
                    },
                    onClick = {
                        onDropdownExpandedChanged(false)
                        onSelectWallet(listOfWallet[index])
                    }
                )
            }
        }
    }
}

@Composable
private fun WalletAmount(
    walletInputState: WalletInputState,
    onAmountChanged: (String) -> Unit
) {
    label("Amount")
    inputWithLeadingIcon(
        value = walletInputState.walletAmount,
        placeholder = "",
        leadingIcon = R.drawable.currency_rupee_ui,
        isReadOnly = false,
        onValueChange = {
            onAmountChanged(it)
        }
    )
}

fun formatWalletAmount(input: String): String {
    if (input != null && input.isNotEmpty()) {
        val num: Double = input.toDouble()
        if ((num % 1) == 0.0) {
            val integerNum = num.toInt()
            return integerNum.toString()
        } else {
            return input
        }
    } else
        return input
}

@Composable
private fun WalletColorWithIcon(
    walletInputState: WalletInputState,
    setColorPickerExpanded: (Boolean) -> Unit,
    onSelectedColor: (Color) -> Unit,
    onIconClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(Modifier.weight(1f)) {
            label("Color")
            WalletColor(
                showColorPicker = walletInputState.showColorPicker,
                listOfColors = walletInputState.showListOfColor.entries.toList(),
                selectedColor = walletInputState.selectedColors,
                setColorPickerExpanded = setColorPickerExpanded,
                onSelectedColor = onSelectedColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier) {
            label("Icon")
            Image(
                painter = painterResource(walletInputState.selectedIcon),
                contentDescription = stringResource(walletInputState.walletIconName),
                modifier = Modifier
                    .clip(AppColors.inputFieldShape)
                    .border(width = 1.dp, color = Color.Unspecified)
                    .background(color = AppColors.inputFieldBackgroundColors)
                    .padding(17.dp)
                    .clickable(onClick = { onIconClick() }),
                colorFilter = ColorFilter.tint(color = AppColors.onSurface)
            )
        }
    }
}

/**
 * UI element for displaying current selected color and triggering color picker.
 */

@Composable
fun WalletColor(
    showColorPicker: Boolean,
    setColorPickerExpanded: (Boolean) -> Unit,
    selectedColor: Color,
    onSelectedColor: (Color) -> Unit,
    listOfColors: List<Map.Entry<String, Color>>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(AppColors.inputFieldShape)
            .background(AppColors.inputFieldBackgroundColors)
            .clickable(onClick = { setColorPickerExpanded(true) })
            .padding(5.dp)
    ) {
        Button(
            enabled = true, onClick = { setColorPickerExpanded(true) },
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
            modifier = Modifier.clickable(onClick = { setColorPickerExpanded(true) })
        )
    }
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(0.5f).height(250.dp),
        expanded = showColorPicker,
        onDismissRequest = { setColorPickerExpanded(false) }) {
        ColorPicker(
            listOfColors = listOfColors,
            onSelectedColor = onSelectedColor,
        )
    }
}

@Composable
fun ColorPicker(onSelectedColor: (Color) -> Unit, listOfColors: List<Map.Entry<String, Color>>) {
    listOfColors.forEach {
        DropdownMenuItem(
            text = { it.key },
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
fun ShowIconScreen(onSelectedIconNavigate: () -> Unit, walletViewModel: WalletViewModel) {
    val walletUiState by walletViewModel.walletInputState.collectAsState()
    ShowIcon(modifier = Modifier.fillMaxSize(),
        walletUiState = walletUiState,
        onSelectedIcon =
        {
            walletViewModel.selectWalletIcon(it)
            onSelectedIconNavigate()
        }
    )

}

@Composable
fun ShowIcon(modifier: Modifier, walletUiState: WalletInputState, onSelectedIcon: (Int) -> Unit) {
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
                        AppColors.inverseOnSurface,
                    shape = CircleShape
                )
                    .padding(20.dp)
                    .clickable(onClick = { onSelectedIcon(it.icon) }),
                tint = if (walletUiState.selectedIcon == it.icon)
                    AppColors.onPrimary
                else
                    AppColors.inverseSurface
            )
        }
    }

}

