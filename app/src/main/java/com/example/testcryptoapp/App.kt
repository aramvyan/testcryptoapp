package com.example.testcryptoapp

import android.app.Application
import com.example.testcryptoapp.di.appModule
import com.example.testcryptoapp.di.dataModule
import com.example.testcryptoapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            AndroidLogger()
            androidContext(this@App)
            modules(
                appModule,
                domainModule,
                dataModule
            )
        }
    }
}