package com.example.testcryptoapp.repository

import com.dynamic.sdk.android.DynamicSDK
import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val dynamicSDK: DynamicSDK
) : AuthRepository {

    override suspend fun sendEmailOTP(email: String): AuthResult<Unit> {
        return try {
            dynamicSDK.auth.email.sendOTP(email = email)
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(
                message = "Failed to send OTP: ${e.message}",
                exception = e
            )
        }
    }

    override suspend fun verifyEmailOTP(email: String, code: String): AuthResult<String> {
        return try {
            val result = dynamicSDK.auth.email.verifyOTP(token = code)
            AuthResult.Success(result.toString())
        } catch (e: Exception) {
            AuthResult.Error(
                message = "Failed to verify OTP: ${e.message}",
                exception = e
            )
        }
    }

    override suspend fun resendEmailOTP(email: String): AuthResult<Unit> {
        return try {
            dynamicSDK.auth.email.sendOTP(email = email)
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(
                message = "Failed to resend OTP: ${e.message}",
                exception = e
            )
        }
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return dynamicSDK.auth.authenticatedUserChanges
            .map { user -> user != null }
            .distinctUntilChanged()
    }
}