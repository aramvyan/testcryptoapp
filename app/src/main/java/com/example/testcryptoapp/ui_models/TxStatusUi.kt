package com.example.testcryptoapp.ui_models

import com.example.testcryptoapp.model.TxStatus

sealed interface TxStatusUi {
    data object Idle : TxStatusUi
    data object Loading : TxStatusUi
    data class Success(val txHash: String, val etherscanUrl: String) : TxStatusUi
    data class Error(val message: String) : TxStatusUi
}

data class SendTxUiState(
    val recipient: String = "",
    val amountEth: String = "",
    val status: TxStatusUi = TxStatusUi.Idle
)

fun TxStatus.toUi(): TxStatusUi = when (this) {
    TxStatus.Loading -> TxStatusUi.Loading
    is TxStatus.Success -> TxStatusUi.Success(
        txHash = txHash,
        etherscanUrl = "https://sepolia.etherscan.io/tx/$txHash"
    )
    is TxStatus.Error -> TxStatusUi.Error(message)
}