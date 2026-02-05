package com.example.testcryptoapp.repository

import com.dynamic.sdk.android.DynamicSDK
import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AuthRepositoryImpl(
    private val dynamicSDK: DynamicSDK
) : AuthRepository {

    override suspend fun sendEmailOTP(email: String): AuthResult {
        return try {
            dynamicSDK.auth.email.sendOTP(email = email)
            AuthResult.Success
        } catch (e: Exception) {
            when {
                e.message?.contains("rate limit") == true -> {
                    AuthResult.Error(
                        message = "Too many attempts. Please try again later.",
                        exception = e
                    )
                }

                e.message?.contains("invalid email") == true -> {
                    AuthResult.Error(
                        message = "Invalid email address.",
                        exception = e
                    )
                }

                else -> {
                    AuthResult.Error(
                        message = "Failed to send OTP: ${e.message}",
                        exception = e
                    )
                }
            }
        }
    }

    override suspend fun verifyEmailOTP(email: String, code: String): AuthResult {
        return try {
            dynamicSDK.auth.email.verifyOTP(token = code)
            AuthResult.Success
        } catch (e: Exception) {
            when {
                e.message?.contains("rate limit") == true -> {
                    AuthResult.Error(
                        message = "Too many attempts. Please try again later.",
                        exception = e
                    )
                }

                e.message?.contains("invalid") == true -> {
                    AuthResult.Error(
                        message = "Invalid OTP code.",
                        exception = e
                    )
                }

                else -> {
                    AuthResult.Error(
                        message = "Failed to verify OTP: ${e.message}",
                        exception = e
                    )
                }
            }
        }
    }

    override suspend fun resendEmailOTP(email: String): AuthResult {
        return try {
            dynamicSDK.auth.email.resendOTP()
            AuthResult.Success
        } catch (e: Exception) {
            when {
                e.message?.contains("rate limit") == true -> {
                    AuthResult.Error(
                        message = "Too many attempts. Please try again later.",
                        exception = e
                    )
                }

                else -> {
                    AuthResult.Error(
                        message = "Failed to resend OTP: ${e.message}",
                        exception = e
                    )
                }
            }
        }
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return dynamicSDK.auth.authenticatedUserChanges
            .map { user -> user != null }
            .onStart {
                val current = dynamicSDK.auth.authenticatedUser != null
                emit(current)
            }
            .distinctUntilChanged()
    }
}