package com.example.testcryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import com.example.testcryptoapp.ui.screens.MainScreen
import com.example.testcryptoapp.ui.theme.TestCryptoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val props = ClientProps(
            environmentId = BuildConfig.DYNAMIC_ENVIRONMENT_ID,
            appName = "Test Crypto App",
            logLevel = LoggerLevel.DEBUG
        )
        DynamicSDK.initialize(props, applicationContext, this)
        setContent {
            TestCryptoAppTheme {
                MainScreen()
                DynamicUI()
            }
        }
    }
}