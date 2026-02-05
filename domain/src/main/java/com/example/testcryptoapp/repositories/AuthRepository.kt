package com.example.testcryptoapp.repositories

import com.example.testcryptoapp.model.AuthResult
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun sendEmailOTP(email: String): AuthResult
    suspend fun verifyEmailOTP(email: String, code: String): AuthResult
    suspend fun resendEmailOTP(email: String): AuthResult
    fun isAuthenticated(): Flow<Boolean>
}