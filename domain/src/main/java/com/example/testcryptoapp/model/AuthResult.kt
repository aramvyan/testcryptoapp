package com.example.testcryptoapp.model

sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String, val exception: Exception? = null) : AuthResult()
    data object Loading : AuthResult()
}