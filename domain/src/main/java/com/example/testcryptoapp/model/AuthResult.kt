package com.example.testcryptoapp.model

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : AuthResult<Nothing>()
    data object Loading : AuthResult<Nothing>()
}