package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.repositories.AuthRepository

class VerifyEmailOTPUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): AuthResult<String> {
        return authRepository.verifyEmailOTP(email, code)
    }
}