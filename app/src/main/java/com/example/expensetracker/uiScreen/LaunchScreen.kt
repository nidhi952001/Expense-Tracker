package com.example.expensetracker.uiScreen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun homeScreen(modifier: Modifier, onGetStarted: () -> Unit) {
    var pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    Column(modifier = modifier , verticalArrangement = Arrangement.Center) {
        horizontalScroller(pagerState, modifier = Modifier.weight(1f))
        BottomScreen(
            modifier = Modifier.padding(bottom = 10.dp),
            onGetStarted = onGetStarted,
            onNext = {
                scope.launch {
                val nextPage = (pagerState.currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
                pagerState.scrollToPage(nextPage)
            }
                     },
            pagerState.currentPage
        )
    }

}
@Composable
fun horizontalScroller(pagerState: PagerState, modifier: Modifier) {
    /*LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            // viewModel.sendPageSelectedEvent(page)
            Log.d("Page change", "Page changed to $page")
        }
    }*/
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (page) {
                1 -> {
                    Image(
                        painterResource(R.drawable.moeny_img),
                        contentDescription = "Money",
                        modifier = Modifier.size(300.dp).padding(top = 50.dp)
                    )
                    Text(
                        text = stringResource(R.string.money_category),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.money_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                }

                else -> {
                    Image(
                        painterResource(R.drawable.finance_img),
                        contentDescription = "Finance",
                        modifier = Modifier.size(300.dp).padding(top = 50.dp)
                    )
                    Text(
                        text = stringResource(R.string.financial_category),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
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
                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.inverseOnSurface
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
fun addNameScreen(modifier: Modifier,saveUserName:(String)->Unit){
    Column(modifier = modifier.padding(top = 50.dp) , horizontalAlignment = Alignment.CenterHorizontally) {
        var userName by remember { mutableStateOf("") }
        Text(text = stringResource(R.string.add_account) , fontWeight = FontWeight.Bold , style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(5.dp))
        Text(text = stringResource(R.string.choose_name) , style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(5.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it },
            label = {Text(text = "Name")},
            singleLine = true,
            modifier = Modifier.border(BorderStroke(1.dp,Color.Unspecified))
        )
        Spacer(Modifier.height(5.dp))
            Button(onClick = { saveUserName(userName) } ,
                enabled = if(userName.isNotEmpty()) true else false,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxWidth()) {
                Text(text = stringResource(R.string.next))
        }

    }
}

@Composable
fun saveInitalMoneyScreen(modifier: Modifier,saveInitialMoney:(String)->Unit){
    Column(modifier = modifier.padding(top = 50.dp) , horizontalAlignment = Alignment.CenterHorizontally) {
        var initialMoney by remember { mutableStateOf("0") }
        Text(text = stringResource(R.string.initial_amount) , fontWeight = FontWeight.Bold , style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(5.dp))
        Text(text = stringResource(R.string.initial_amount_sub) , style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(5.dp))
        TextField(
            value = initialMoney,
            onValueChange = {
                initialMoney = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType =  KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {saveInitialMoney(initialMoney)})
        )
    }
}

