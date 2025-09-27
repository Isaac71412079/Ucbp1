package com.example.ucbp1.features.login.data.repository

import com.example.ucbp1.features.login.domain.model.LoginResult
import com.example.ucbp1.features.login.domain.repository.ILoginRepository
import kotlinx.coroutines.delay

class LoginRepository : ILoginRepository {
    override suspend fun login(email: String, pass: String): LoginResult {
        delay(1000)

        return if (email == "isaac.rivero@ucb.edu.bo" && pass == "isaac") {
            LoginResult.Success
        } else {
            LoginResult.Failure.InvalidCredentials
        }
    }
}