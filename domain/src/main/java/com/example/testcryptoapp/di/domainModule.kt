package com.example.testcryptoapp.di

import com.example.testcryptoapp.use_case.ObserveWalletUseCase
import com.example.testcryptoapp.use_case.ResendEmailOTPUseCase
import com.example.testcryptoapp.use_case.SendEmailOTPUseCase
import com.example.testcryptoapp.use_case.SendTransactionUseCase
import com.example.testcryptoapp.use_case.UserCheckUseCase
import com.example.testcryptoapp.use_case.VerifyEmailOTPUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SendEmailOTPUseCase(get()) }
    factory { VerifyEmailOTPUseCase(get()) }
    factory { ResendEmailOTPUseCase(get()) }
    factory { UserCheckUseCase(get()) }
    factory { ObserveWalletUseCase(get()) }
    factory { SendTransactionUseCase(get()) }
}