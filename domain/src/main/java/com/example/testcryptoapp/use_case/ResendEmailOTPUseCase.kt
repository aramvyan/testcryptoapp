package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.repositories.AuthRepository

class ResendEmailOTPUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult<Unit> {
        return authRepository.resendEmailOTP(email)
    }
}