package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.model.WalletModel
import com.example.testcryptoapp.repositories.WalletRepository
import kotlinx.coroutines.flow.Flow

class GetWalletUseCase(private val walletRepository: WalletRepository) {
    operator fun invoke(): Flow<WalletModel> {
        return walletRepository.getWallet()
    }
}