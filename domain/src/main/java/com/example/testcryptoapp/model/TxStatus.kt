package com.example.testcryptoapp.model

sealed interface TxStatus {
    data object Loading : TxStatus
    data class Success(val txHash: String) : TxStatus
    data class Error(val message: String, val cause: Throwable? = null) : TxStatus
}