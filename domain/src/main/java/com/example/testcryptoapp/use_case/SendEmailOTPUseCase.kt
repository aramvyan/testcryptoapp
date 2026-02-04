package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.model.AuthResult
import com.example.testcryptoapp.repositories.AuthRepository

class SendEmailOTPUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult<Unit> {
        if (!isValidEmail(email)) {
            return AuthResult.Error("Invalid email format")
        }
        return authRepository.sendEmailOTP(email)
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
}