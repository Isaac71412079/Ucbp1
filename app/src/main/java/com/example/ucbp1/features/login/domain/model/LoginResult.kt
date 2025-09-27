package com.example.ucbp1.features.login.domain.model

sealed class LoginResult {
    object Success : LoginResult()
    sealed class Failure : LoginResult() {
        object InvalidCredentials : Failure()
        object EmptyFields : Failure()
    }
}