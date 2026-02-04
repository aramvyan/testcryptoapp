package com.example.testcryptoapp.ui_models

import com.example.testcryptoapp.model.WalletModel

data class WalletDetailsState(
    val isRefreshing: Boolean = false,
    val ui: WalletDetailsUi? = null,
    val error: String? = null
)

data class WalletDetailsUi(
    val chainLabel: String,
    val address: String,
    val networkName: String,
    val chainId: String,
    val balanceEth: String
)

fun WalletModel.toWalletDetailsUi(): WalletDetailsUi {
    return WalletDetailsUi(
        chainLabel = "EVM",
        address = address,
        networkName = "$walletName - $chain",
        chainId = chain,
        balanceEth = "$balance ETH"
    )
}