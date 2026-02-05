package com.example.testcryptoapp.ui.screens.transaction_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcryptoapp.ui_models.SendTxUiState
import com.example.testcryptoapp.ui_models.TxStatusUi
import com.example.testcryptoapp.ui_models.toUi
import com.example.testcryptoapp.use_case.SendTransactionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SendTransactionViewModel(
    private val sendTxUseCase: SendTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SendTxUiState())
    val state = _state.asStateFlow()

    private var sendJob: Job? = null

    fun onRecipientChange(v: String) {
        _state.update { it.copy(recipient = v) }
    }

    fun onAmountChange(v: String) {
        _state.update { it.copy(amountEth = v) }
    }

    fun send() {
        sendJob?.cancel()

        val s = state.value
        sendJob = viewModelScope.launch {
            sendTxUseCase(
                recipient = s.recipient.trim(),
                amountEth = s.amountEth.trim()
            ).collect { status ->
                _state.update {
                    it.copy(status = status.toUi())
                }
            }
        }
    }

    fun clearStatus() {
        _state.update { it.copy(status = TxStatusUi.Idle) }
    }
}