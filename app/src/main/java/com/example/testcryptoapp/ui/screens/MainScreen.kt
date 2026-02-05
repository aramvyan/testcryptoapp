package com.example.testcryptoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testcryptoapp.ui.components.MainScreenDestinations
import com.example.testcryptoapp.ui.screens.login_screen.AuthScreen
import com.example.testcryptoapp.ui.screens.login_screen.AuthViewModel
import com.example.testcryptoapp.ui.screens.transaction_screen.SendTransactionScreen
import com.example.testcryptoapp.ui.screens.transaction_screen.SendTransactionViewModel
import com.example.testcryptoapp.ui.screens.wallet_details.WalletDetailsScreen
import com.example.testcryptoapp.ui.screens.wallet_details.WalletDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.background(Color.White),
        containerColor = Color.White) { paddingValues ->
        NavigationHost(paddingValues, navController)
    }
}

@Composable
fun NavigationHost(paddingValues: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainScreenDestinations.LoginDestination.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(MainScreenDestinations.LoginDestination.route) {
            val viewModel: AuthViewModel = koinViewModel()
            AuthScreen(
                viewModel = viewModel,
                onAuthSuccess = {
                    navController.navigate(MainScreenDestinations.WalletDetailsDestination.route)
                }
            )
        }
        composable(MainScreenDestinations.WalletDetailsDestination.route) {
            val viewModel: WalletDetailsViewModel = koinViewModel()
            WalletDetailsScreen(
                onBack = { },
                onSendTransaction = { navController.navigate(MainScreenDestinations.SendTransactionDestination.route) },
                onLogout = {  },
                viewModel = viewModel
            )
        }
        composable(MainScreenDestinations.SendTransactionDestination.route) {
            val viewModel: SendTransactionViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            SendTransactionScreen(
                state = state,
                onBack = {  },
                onRecipientChange = viewModel::onRecipientChange,
                onAmountChange = viewModel::onAmountChange,
                onSendClick = viewModel::send,
                onDismissStatus = viewModel::clearStatus
            )
        }
    }
}
