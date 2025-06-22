package com.example.expensetracker.utils

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