package com.example.testcryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.testcryptoapp.ui.screens.MainScreen
import com.example.testcryptoapp.ui.theme.TestCryptoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestCryptoAppTheme {
                MainScreen()
            }
        }
    }
}