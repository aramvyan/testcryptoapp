package com.example.testcryptoapp.use_case

import com.example.testcryptoapp.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow

class UserCheckUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Boolean> = authRepository.isAuthenticated()
}