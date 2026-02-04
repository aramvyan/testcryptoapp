package com.example.testcryptoapp.ui.components

sealed class MainScreenDestinations(val route: String){
    data object LoginDestination: MainScreenDestinations("login_screen")
    data object WalletDetailsDestination: MainScreenDestinations("wallet_details_screen")
    data object SendTransactionDestination: MainScreenDestinations("send_transaction_screen")
}