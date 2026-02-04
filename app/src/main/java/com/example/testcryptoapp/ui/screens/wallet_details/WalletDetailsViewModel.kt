package com.example.testcryptoapp.ui.screens.wallet_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcryptoapp.ui_models.WalletDetailsState
import com.example.testcryptoapp.ui_models.toWalletDetailsUi
import com.example.testcryptoapp.use_case.GetWalletUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletDetailsViewModel(
    private val getWalletUseCase: GetWalletUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WalletDetailsState())
    val state: StateFlow<WalletDetailsState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            try {
                val wallet = getWalletUseCase()
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        ui = wallet.map {wallet ->
                            wallet.toWalletDetailsUi()
                        }.first()
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        error = e.message ?: "Failed to load wallet"
                    )
                }
            }
        }
    }
}