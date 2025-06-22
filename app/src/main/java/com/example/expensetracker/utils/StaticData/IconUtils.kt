package com.example.expensetracker.utils.StaticData

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.R

enum class TypeOfWallet {
    General, CreditCard
}

val listOfWallet = listOf(
    TypeOfWallet.General,
    TypeOfWallet.CreditCard
)


data class ListOfIcons(
    val icon:Int,
    val iconName:Int
)

object listOfWalletIcon{
    val iconData = listOf(
        ListOfIcons(R.drawable.account_wallet_ic,R.string.bank),
        ListOfIcons(R.drawable.credit_card_wallet_ic,R.string.credit_card),
        ListOfIcons(R.drawable.bitcoin_wallet_ic,R.string.bitcoin),
        ListOfIcons(R.drawable.share_wallet_ic,R.string.share_market),
        ListOfIcons(R.drawable.cash_wallet_ic,R.string.cash),
        ListOfIcons(R.drawable.money_bag_wallet_ic,R.string.money_bag),
        ListOfIcons(R.drawable.payment_wallet_ic,R.string.payment),
        ListOfIcons(R.drawable.piggy_bank_wallet_ic,R.string.bank),
        ListOfIcons(R.drawable.upi_wallet_ic,R.string.bank)
    )
}

object listOfWalletColor  {
    val coloCodeToColor = mapOf(
        "sky_blue" to Color(0xFF00BFFF),
        "doger_blue" to Color(0xFF1E90FF),
        "royal_blue" to Color(0xFF4169E1),
        "cubs_blue" to Color(0xFF0E3386),
        "teal" to Color(0xFF008080),
        "robin_egg_blue" to Color(0xFF00CCCC),
        "emerald" to Color(0xFF50C878),
        "sgbus_green" to Color(0xFF55DD33),
        "gold" to Color(0xFFFFD700),
        "macd_yellow" to Color(0xFFFFC72C),
        "real_madrid_gold" to Color(0xFFFEBE10),
        "purple" to Color(0XFF800080),
        "yahoo_purple" to Color(0XFF720e9e),
        "roku_purple" to Color(0XFF662d91),
        "plum" to Color(0XFFDDA0DD),
        "lyft_pink" to Color(0XFFFF00BF),
        "persin_rose" to Color(0xFFFE28A2),
        "crimson" to Color(0XFF841617),
        "feldguru" to Color(0XFF4D5D53),
        "light_gray" to Color(0XFF5d6d7e)

    )
}
