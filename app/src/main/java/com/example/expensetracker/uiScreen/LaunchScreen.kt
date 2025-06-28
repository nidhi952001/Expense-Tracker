package com.example.expensetracker.uiScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.*
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.inverseSurface
import com.example.expensetracker.uiScreen.uiState.HomeStateData
import com.example.expensetracker.viewModel.HomeViewModel
import com.example.expensetracker.viewModel.WalletViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreenRoute(onGetStarted: () -> Unit) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    WelcomeScreen(
        modifier = modifier,
        pagerState = pagerState,
        onGetStarted = onGetStarted,
        scope = scope)

}

@Composable
private fun WelcomeScreen(
    modifier: Modifier,
    pagerState: PagerState,
    onGetStarted: () -> Unit,
    scope: CoroutineScope
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        horizontalScroller(pagerState =pagerState, modifier = Modifier.weight(1f))
        welcomeScreenBottom(
            modifier = Modifier.padding(bottom = 10.dp),
            onGetStarted = onGetStarted,
            onNext = {
                scope.launch {
                    val nextPage =
                        (pagerState.currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
                    pagerState.scrollToPage(nextPage)
                }
            },
            page = pagerState.currentPage
        )
    }
}

@Composable
fun horizontalScroller(pagerState: PagerState, modifier: Modifier) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            when (page) {
                1 -> {
                    pagerContent(
                        painter = painterResource(R.drawable.moeny_img),
                        imageDec = stringResource(R.string.money_dec),
                        modifier = Modifier.size(300.dp).padding(top = 50.dp),
                        titleText = stringResource(R.string.money_category),
                        titleFontWeight = FontWeight.Bold,
                        titleStyle = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.money_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    pagerContent(
                        painter = painterResource(R.drawable.finance_img),
                        imageDec = stringResource(R.string.finance_dec),
                        modifier = Modifier.size(300.dp).padding(top = 50.dp),
                        titleText = stringResource(R.string.financial_category),
                        titleFontWeight = FontWeight.Bold,
                        titleStyle = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.financial_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                Modifier.padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) inverseSurface else inverseOnSurface
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun pagerContent(
    painter: Painter,
    imageDec: String,
    modifier: Modifier,
    titleText: String,
    titleFontWeight: FontWeight,
    titleStyle: TextStyle,
    text: String,
    style: TextStyle,
    textAlign: TextAlign
) {
    Image(
        painter = painter,
        contentDescription = imageDec,
        modifier = modifier
    )
    Text(
        text = titleText,
        fontWeight = titleFontWeight,
        style = titleStyle
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
        text = text,
        style = style,
        textAlign = textAlign
    )
}


@Composable
fun userNameScreenRoute(
    onNavigate: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val homeUiState by homeViewModel.homeUiStateData.collectAsState()
    userNameScreen(
        modifier=  Modifier.fillMaxSize(),
        homeUiState = homeUiState,
        onUserNameChange = { homeViewModel.updateUserName(it) },
        saveUserName = {
            homeViewModel.saveUserName(it)
            onNavigate()
        })
}
@Composable
private fun userNameScreen(
    modifier: Modifier,
    homeUiState: HomeStateData,
    onUserNameChange:(String)->Unit,
    saveUserName: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.add_account),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.choose_name),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(5.dp))
        TextField(
            value = homeUiState.userName,
            onValueChange = { onUserNameChange(it) },
            placeholder = { Text(text = stringResource(R.string.name)) },
            singleLine = true,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Unspecified)),
            textStyle = TextStyle.Default.copy(
                color = AppColors.onSurface,
                fontWeight = AppColors.inputTextWeight,
                fontSize = AppColors.inputTextSize,
                fontStyle = AppColors.inputTextStyle
            ),
            shape = AppColors.inputFieldShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = AppColors.inputFieldBackgroundColors,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = AppColors.inputFieldBackgroundColors
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
            )
        )
        Spacer(Modifier.height(5.dp))
        Button(
            onClick = { saveUserName(homeUiState.userName) },
            enabled = homeUiState.userName.isNotEmpty(),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.next))
        }

    }
}

@Composable
fun initialMoneyScreenRoute(onNavigate:()->Unit,homeViewModel: HomeViewModel,walletViewModel: WalletViewModel)
{
    val homeUiState by homeViewModel.homeUiStateData.collectAsState()

    initialMoneyScreen(
        homeUiState = homeUiState,
        onInitialMoneyChange = { homeViewModel.updateInitalMoney(it) },
        saveInitialMoney = {
            walletViewModel.initializeWalletWithAmount(it)
            onNavigate()
        }
    )
}
@Composable
private fun initialMoneyScreen(
    homeUiState: HomeStateData,
    onInitialMoneyChange: (String) -> Unit,
    saveInitialMoney: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.initial_amount),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.initial_amount_sub),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(5.dp))
        TextField(
            placeholder = {
                Text(text = stringResource(R.string.zero))
            },
            value = homeUiState.initialMoney,
            onValueChange = {
                onInitialMoneyChange(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (homeUiState.initialMoney != "") {
                        saveInitialMoney(homeUiState.initialMoney)
                    }
                }
            )
        )
    }
}

