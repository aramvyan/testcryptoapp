package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.model.TxStatus
import com.example.testcryptoapp.repositories.WalletRepository
import kotlinx.coroutines.flow.Flow

class SendTransactionUseCase(private val walletRepository: WalletRepository) {
    operator fun invoke(recipient: String, amountEth: String): Flow<TxStatus> {
        return walletRepository.sendTransaction(recipient, amountEth)
    }
}