package com.example.expensetracker.utils

import com.example.expensetracker.R

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
