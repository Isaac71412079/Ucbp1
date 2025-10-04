package com.example.ucbp1.features.login.data.repository

import androidx.datastore.dataStore
import com.example.ucbp1.features.login.data.LoginDataStore // <-- IMPORTAR
import com.example.ucbp1.features.login.domain.model.LoginResult
import com.example.ucbp1.features.login.domain.repository.ILoginRepository
import kotlinx.coroutines.delay

class LoginRepository(
    private val loginDataStore: LoginDataStore
) : ILoginRepository {
    override suspend fun login(email: String, pass: String): LoginResult {
        delay(1000)

        return if (email == "isaac.rivero@ucb.edu.bo" && pass == "dev") {
            LoginResult.Success
        } else {
            LoginResult.Failure.InvalidCredentials
        }
    }
    override suspend fun saveSession(email: String) {
        // Usa el nombre de la propiedad del constructor: 'loginDataStore'
        loginDataStore.saveUserEmail(email)
    }
}