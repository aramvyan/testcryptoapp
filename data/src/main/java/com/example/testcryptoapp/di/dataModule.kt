package com.example.testcryptoapp.di

import com.dynamic.sdk.android.DynamicSDK
import com.example.testcryptoapp.repositories.AuthRepository
import com.example.testcryptoapp.repositories.WalletRepository
import com.example.testcryptoapp.repository.AuthRepositoryImpl
import com.example.testcryptoapp.repository.WalletRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single { DynamicSDK.getInstance() }

    single<AuthRepository> {
        AuthRepositoryImpl(
            dynamicSDK = get()
        )
    }

    single<WalletRepository> {
        WalletRepositoryImpl(
            dynamicSDK = get()
        )
    }
}