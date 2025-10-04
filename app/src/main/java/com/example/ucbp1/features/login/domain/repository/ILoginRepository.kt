package com.example.ucbp1.features.login.domain.repository

import com.example.ucbp1.features.login.domain.model.LoginResult
interface ILoginRepository {
    suspend fun login(email: String, pass: String): LoginResult
    suspend fun  saveSession(email: String)
}