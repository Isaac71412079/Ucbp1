package com.example.ucbp1.features.login.domain.usecase

import com.example.ucbp1.features.login.domain.model.LoginResult
import com.example.ucbp1.features.login.domain.repository.ILoginRepository

class LoginUseCase(
    private val repository: ILoginRepository
) {
    suspend fun invoke(email: String, pass: String): LoginResult {
        if (email.isBlank() || pass.isBlank()) {
            return LoginResult.Failure.EmptyFields
        }
        return repository.login(email, pass)
    }
}