package com.example.ucbp1.domain.repository

import com.example.ucbp1.domain.model.UserModel

interface IGithubRepository {
    fun findByNick( value: String ) : Result<UserModel>
}