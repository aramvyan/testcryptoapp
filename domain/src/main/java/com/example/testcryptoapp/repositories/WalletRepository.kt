package com.example.testcryptoapp.repositories

import com.example.testcryptoapp.model.TxStatus
import com.example.testcryptoapp.model.WalletModel
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun observeWallet(): Flow<WalletModel>
    fun sendTransaction(recipientAddress: String, amount: String): Flow<TxStatus>
}
