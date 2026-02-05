package com.example.testcryptoapp.ui.screens.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.use_case.ResendEmailOTPUseCase
import com.example.testcryptoapp.use_case.SendEmailOTPUseCase
import com.example.testcryptoapp.use_case.UserCheckUseCase
import com.example.testcryptoapp.use_case.VerifyEmailOTPUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val sendEmailOTPUseCase: SendEmailOTPUseCase,
    private val verifyEmailOTPUseCase: VerifyEmailOTPUseCase,
    private val resendEmailOTPUseCase: ResendEmailOTPUseCase,
    userCheckUseCase: UserCheckUseCase
) : ViewModel() {


    val isAuthenticated: StateFlow<Boolean> =
        userCheckUseCase().stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun sendOTP(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an email") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = sendEmailOTPUseCase(email)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showOtpSheet = true,
                            successMessage = "OTP sent to $email"
                        )
                    }
                }

                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun verifyOTP(code: String) {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = verifyEmailOTPUseCase(uiState.value.email, code)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showOtpSheet = false,
                            isAuthenticated = true,
                            successMessage = "Authentication successful"
                        )
                    }
                }

                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun resendOTP() {
        val email = _uiState.value.email

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = resendEmailOTPUseCase(email)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "OTP resent to $email"
                        )
                    }
                }

                is AuthResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun dismissOtpSheet() {
        _uiState.update { it.copy(showOtpSheet = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
data class AuthUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val showOtpSheet: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false,
)