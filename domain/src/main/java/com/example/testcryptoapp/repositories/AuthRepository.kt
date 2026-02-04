package com.example.testcryptoapp.repositories

import com.example.testcryptoapp.model.AuthResult
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun sendEmailOTP(email: String): AuthResult<Unit>
    suspend fun verifyEmailOTP(email: String, code: String): AuthResult<String>
    suspend fun resendEmailOTP(email: String): AuthResult<Unit>
    fun isAuthenticated(): Flow<Boolean>
}