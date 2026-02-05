package com.example.testcryptoapp.model

data class WalletModel(
    val address: String,
    val chain: String,
    val walletProvider: String?,
    val walletName: String?,
    val balance: String
)