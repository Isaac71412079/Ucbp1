package com.example.ucbp1.features.github.domain.repository

import com.example.ucbp1.features.github.domain.model.UserModel

interface IGithubRepository {
    fun findByNick(value: String): Result<UserModel>
}