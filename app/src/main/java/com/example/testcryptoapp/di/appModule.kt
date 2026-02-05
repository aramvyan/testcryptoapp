package com.example.testcryptoapp.di

import com.example.testcryptoapp.ui.screens.login_screen.AuthViewModel
import com.example.testcryptoapp.ui.screens.transaction_screen.SendTransactionViewModel
import com.example.testcryptoapp.ui.screens.wallet_details.WalletDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        AuthViewModel(
            sendEmailOTPUseCase = get(),
            verifyEmailOTPUseCase = get(),
            resendEmailOTPUseCase = get(),
            userCheckUseCase = get()
        )
    }

    viewModel {
        WalletDetailsViewModel(observeWalletUseCase = get())
    }
    viewModel { SendTransactionViewModel(sendTxUseCase = get()) }
}